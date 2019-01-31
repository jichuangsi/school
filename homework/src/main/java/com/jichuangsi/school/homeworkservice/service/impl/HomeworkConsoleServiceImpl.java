package com.jichuangsi.school.homeworkservice.service.impl;

import com.jichuangsi.microservice.common.model.UserInfoForToken;
import com.jichuangsi.school.homeworkservice.constant.HomeworkSort;
import com.jichuangsi.school.homeworkservice.constant.ResultCode;
import com.jichuangsi.school.homeworkservice.constant.Status;
import com.jichuangsi.school.homeworkservice.entity.Homework;
import com.jichuangsi.school.homeworkservice.entity.Question;
import com.jichuangsi.school.homeworkservice.exception.TeacherHomeworkServiceException;
import com.jichuangsi.school.homeworkservice.model.HomeworkModelForTeacher;
import com.jichuangsi.school.homeworkservice.model.QuestionModel;
import com.jichuangsi.school.homeworkservice.model.QuestionModelForTeacher;
import com.jichuangsi.school.homeworkservice.model.SearchHomeworkModel;
import com.jichuangsi.school.homeworkservice.model.common.DeleteQueryModel;
import com.jichuangsi.school.homeworkservice.model.common.Elements;
import com.jichuangsi.school.homeworkservice.model.common.PageHolder;
import com.jichuangsi.school.homeworkservice.model.transfer.TransferTeacher;
import com.jichuangsi.school.homeworkservice.repository.HomeworkConsoleRepository;
import com.jichuangsi.school.homeworkservice.repository.QuestionRepository;
import com.jichuangsi.school.homeworkservice.service.IExamInfoService;
import com.jichuangsi.school.homeworkservice.service.IHomeworkConsoleService;
import com.jichuangsi.school.homeworkservice.service.IUserInfoService;
import com.jichuangsi.school.homeworkservice.utils.MappingEntity2ModelConverter;
import com.jichuangsi.school.homeworkservice.utils.MappingModel2EntityConverter;
import com.mongodb.client.result.DeleteResult;
import com.mongodb.client.result.UpdateResult;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.regex.Pattern;

@Service
public class HomeworkConsoleServiceImpl implements IHomeworkConsoleService {

    @Resource
    private IUserInfoService userInfoService;

    @Resource
    private IExamInfoService examInfoService;

    @Resource
    private MongoTemplate mongoTemplate;

    @Resource
    private HomeworkConsoleRepository homeworkConsoleRepository;

    @Resource
    private QuestionRepository questionRepository;

    @Override
    public void saveNewHomework(UserInfoForToken userInfo, HomeworkModelForTeacher homeworkModelForTeacher) throws TeacherHomeworkServiceException {
        if(StringUtils.isEmpty(userInfo.getUserId())) throw new TeacherHomeworkServiceException(ResultCode.PARAM_MISS_MSG);
        TransferTeacher transferTeacher = userInfoService.getUserForTeacherById(userInfo.getUserId());
        if(transferTeacher==null) throw new TeacherHomeworkServiceException(ResultCode.TEACHER_INFO_NOT_EXISTED);
        homeworkModelForTeacher.setSubjectId(transferTeacher.getSubjectId());
        homeworkModelForTeacher.setSubjectName(transferTeacher.getSubjectName());
        Homework homework = MappingModel2EntityConverter.ConvertTeacherHomework(userInfo,homeworkModelForTeacher);

        homework = homeworkConsoleRepository.save(homework);
        if(homework==null){
            throw new TeacherHomeworkServiceException(ResultCode.HOMEWORK_FAIL_SAVE);
        }
        if(homeworkModelForTeacher.getQuestions()!=null) {
            List<String> questionIds = new ArrayList<String>();
            Update update = new Update();
            for (QuestionModelForTeacher questionModelForTeacher : homeworkModelForTeacher.getQuestions()) {
                questionModelForTeacher.setGradeId(transferTeacher.getGradeId());
                questionModelForTeacher.setSubjectId(transferTeacher.getSubjectId());
                Question question = MappingModel2EntityConverter.ConvertTeacherQuestion(questionModelForTeacher);
                question = questionRepository.save(question);
                /*if (question == null) {
                    throw new TeacherCourseServiceException(ResultCode.QUESTION_FAIL_SAVE);
                }*/
                questionIds.add(question.getId());
            }
            //homework.setQuestionIds(questionIds);
            update.set("questionIds",questionIds);
            UpdateResult result = mongoTemplate.upsert(new Query(Criteria.where("_id").is(homework.getId())),update,Homework.class);
        }
    }

    @Override
    public Elements getElementsList(UserInfoForToken userInfo) throws TeacherHomeworkServiceException{
        Elements elements = new Elements();
        elements.setTransferExams(examInfoService.getExamForTeacherById(userInfo.getUserId()));
        elements.setTransferClasses(userInfoService.getClassForTeacherById(userInfo.getUserId()));

        return elements;
    }

    @Override
    public PageHolder<HomeworkModelForTeacher> getSortedHomeworksList(UserInfoForToken userInfo, SearchHomeworkModel searchHomeworkModel) throws TeacherHomeworkServiceException{
        if(StringUtils.isEmpty(userInfo.getUserId())) throw new TeacherHomeworkServiceException(ResultCode.PARAM_MISS_MSG);
        Criteria criteria = Criteria.where("teacherId").is(userInfo.getUserId());
        if(!StringUtils.isEmpty(searchHomeworkModel.getTime())){
            criteria.and("endTime").lte(Long.valueOf(searchHomeworkModel.getTime())+86400000l).gte(Long.valueOf(searchHomeworkModel.getTime()));
        }
        if(!StringUtils.isEmpty(searchHomeworkModel.getStatus())&&!Status.EMPTY.equals(searchHomeworkModel.getStatus())){
            criteria.and("status").is(searchHomeworkModel.getStatus().getName());
        }
        if (!StringUtils.isEmpty(searchHomeworkModel.getKeyWord())){
            Pattern pattern= Pattern.compile("^.*"+searchHomeworkModel.getKeyWord()+".*$", Pattern.CASE_INSENSITIVE);
            Criteria c1 = Criteria.where("info").regex(pattern);
            Criteria c2 = Criteria.where("name").regex(pattern);
            criteria.orOperator(c1,c2);
        }
        Query query = new Query(criteria);
        if(!StringUtils.isEmpty(searchHomeworkModel.getSortNum())){
            if (HomeworkSort.TIME.getName().equals(HomeworkSort.getName(searchHomeworkModel.getSortNum()))){
                query.with(new Sort(Sort.Direction.DESC,"endTime"));
            }else{
                query.with(new Sort(Sort.Direction.DESC,"createTime"));
            }
        }

        PageHolder<HomeworkModelForTeacher> pageHolder = new PageHolder<HomeworkModelForTeacher>();
        pageHolder.setTotal(mongoTemplate.find(query,Homework.class).size());
        pageHolder.setPageNum(searchHomeworkModel.getPageIndex());
        pageHolder.setPageSize(searchHomeworkModel.getPageSize());
        query.skip((searchHomeworkModel.getPageIndex()-1)*searchHomeworkModel.getPageSize());
        query.limit(searchHomeworkModel.getPageSize());
        List<Homework> homeworkList = mongoTemplate.find(query,Homework.class);
        List<HomeworkModelForTeacher> homeworkModelList = new ArrayList<HomeworkModelForTeacher>();
        homeworkList.forEach(homework -> {
            HomeworkModelForTeacher homeworkModelForTeacher =  MappingEntity2ModelConverter.ConvertTeacherHomework(homework);
            try{
                homeworkModelForTeacher.getQuestions().addAll(this.getQuestionList(homework.getQuestionIds()));
                homeworkModelForTeacher.getStudents().addAll(userInfoService.getStudentsForClassById(homework.getClassId()));
            }catch (Exception exp){}
            homeworkModelList.add(homeworkModelForTeacher);
        });
        pageHolder.setContent(homeworkModelList);

        return pageHolder;
    }

    @Override
    public List<QuestionModelForTeacher> getQuestionList(List<String> qIds) throws TeacherHomeworkServiceException{
        return convertQuestionList(mongoTemplate.find(new Query(Criteria.where("id").in(qIds)), Question.class));
    }

    @Override
    public void deleteHomeWorkIsNotStart(UserInfoForToken userInfo, DeleteQueryModel deleteQueryModel) throws TeacherHomeworkServiceException{
        if(StringUtils.isEmpty(userInfo.getUserId())) throw new TeacherHomeworkServiceException(ResultCode.PARAM_MISS_MSG);
        for(String id : deleteQueryModel.getIds()){
            Homework homework = mongoTemplate.findOne(new Query(Criteria.where("id").is(id).and("teacherId").is(userInfo.getUserId()).and("status").is(Status.NOTSTART.getName())),Homework.class);
            if(homework!=null){
                if(!homework.getQuestionIds().isEmpty()){
                    DeleteResult result = mongoTemplate.remove(new Query(Criteria.where("id").in(homework.getQuestionIds())), Question.class);
                    if(result.getDeletedCount()==0) throw new TeacherHomeworkServiceException(ResultCode.HOMEWORK_DELETE_FAIL);
                }
                mongoTemplate.remove(homework);
            }else{
                throw new TeacherHomeworkServiceException(ResultCode.HOMEWORK_DELETE_FAIL);
            }
        }
    }

    @Override
    public void updateHomeWorkIsNotStart(UserInfoForToken userInfo, HomeworkModelForTeacher homeworkModelForTeacher) throws TeacherHomeworkServiceException{
        if(StringUtils.isEmpty(userInfo.getUserId())) throw new TeacherHomeworkServiceException(ResultCode.PARAM_MISS_MSG);
        Homework homework = mongoTemplate.findOne(new Query(Criteria.where("id").is(homeworkModelForTeacher.getHomeworkId()).and("teacherId").is(userInfo.getUserId()).and("status").is(Status.NOTSTART.getName())),Homework.class);
        if(homework!=null){
            Homework updatedHomework = MappingModel2EntityConverter.ConvertTeacherHomework(userInfo, homeworkModelForTeacher);
            Criteria criteria = Criteria.where("id").is(updatedHomework.getId());
            Update update = new Update();
            if(!StringUtils.isEmpty(updatedHomework.getName()))
                update.set("name",updatedHomework.getName());
            if(!StringUtils.isEmpty(updatedHomework.getClassId()))
                update.set("classId",updatedHomework.getClassId());
            if(!StringUtils.isEmpty(updatedHomework.getClassName()))
                update.set("className",updatedHomework.getClassName());
            if(updatedHomework.getEndTime()!=0)
                update.set("endTime",updatedHomework.getEndTime());
            if(!StringUtils.isEmpty(updatedHomework.getInfo()))
                update.set("info",updatedHomework.getInfo());
            update.set("updateTime",new Date().getTime());
            mongoTemplate.updateFirst(new Query(criteria),update,Homework.class);
        }else{
            throw new TeacherHomeworkServiceException(ResultCode.HOMEWORK_ALREADY_BEGIN);
        }
    }

    @Override
    public void updateHomeWork2NewStatus(UserInfoForToken userInfo, HomeworkModelForTeacher homeworkModelForTeacher) throws TeacherHomeworkServiceException{
        if(StringUtils.isEmpty(userInfo.getUserId()) || Status.EMPTY.equals(homeworkModelForTeacher.getHomeworkStatus())) throw new TeacherHomeworkServiceException(ResultCode.PARAM_MISS_MSG);
        Homework homework = mongoTemplate.findOne(new Query(Criteria.where("id").is(homeworkModelForTeacher.getHomeworkId()).and("teacherId").is(userInfo.getUserId())),Homework.class);
        if(homework!=null){
            Homework updatedHomework = MappingModel2EntityConverter.ConvertTeacherHomework(userInfo, homeworkModelForTeacher);
            Criteria criteria = Criteria.where("id").is(updatedHomework.getId());
            Update update = new Update();
            if(!StringUtils.isEmpty(updatedHomework.getStatus())){
                update.set("status",updatedHomework.getStatus());
                if(Status.PROGRESS.getName().equalsIgnoreCase(updatedHomework.getStatus())){
                    update.set("publishTime",new Date().getTime());
                }
            }
            update.set("updateTime",new Date().getTime());
            mongoTemplate.updateFirst(new Query(criteria),update,Homework.class);
        }else{
            throw new TeacherHomeworkServiceException(ResultCode.HOMEWORK_STATUS_ERROR);
        }
    }

    private List<HomeworkModelForTeacher> convertHomeworkList(List<Homework> homeworks){
        List<HomeworkModelForTeacher> homeworkModelForTeacher = new ArrayList<HomeworkModelForTeacher>();
        homeworks.forEach(homework -> {
            homeworkModelForTeacher.add(MappingEntity2ModelConverter.ConvertTeacherHomework(homework));
        });
        return homeworkModelForTeacher;
    }

    private List<QuestionModelForTeacher> convertQuestionList(List<Question> questions){
        List<QuestionModelForTeacher> questionForTeachers = new ArrayList<QuestionModelForTeacher>();
        questions.forEach(question -> {
            questionForTeachers.add(MappingEntity2ModelConverter.ConvertTeacherQuestion(question));
        });
        return questionForTeachers;
    }
}
