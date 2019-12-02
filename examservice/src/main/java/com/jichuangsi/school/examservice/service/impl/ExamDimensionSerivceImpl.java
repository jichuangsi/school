package com.jichuangsi.school.examservice.service.impl;

import com.jichuangsi.microservice.common.model.UserInfoForToken;
import com.jichuangsi.school.examservice.Model.DeleteQueryModel;
import com.jichuangsi.school.examservice.Model.Dimension.*;
import com.jichuangsi.school.examservice.Model.ExamModel;
import com.jichuangsi.school.examservice.Model.PageHolder;
import com.jichuangsi.school.examservice.Model.QuestionModel;
import com.jichuangsi.school.examservice.Utils.MappingEntity2ModelConverter;
import com.jichuangsi.school.examservice.Utils.MappingModel2EntityConverter;
import com.jichuangsi.school.examservice.constant.ResultCode;
import com.jichuangsi.school.examservice.entity.*;
import com.jichuangsi.school.examservice.exception.ExamException;
import com.jichuangsi.school.examservice.repository.*;
import com.jichuangsi.school.examservice.service.ExamDimensionSerivce;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.util.*;
import java.util.regex.Pattern;

import static org.springframework.data.domain.Sort.Direction.DESC;

@Service
public class ExamDimensionSerivceImpl implements ExamDimensionSerivce {

    @Resource
    private IExamExtraRepository examRepository;
    @Resource
    private IQuestionExtraRepository questionRepository;

    @Value("${com.jichuangsi.school.sortType.difficult}")
    private String difficultType;
    @Value("${com.jichuangsi.school.sortType.question}")
    private String questionType;
    @Resource
    private MongoTemplate mongoTemplate;
    @Resource
    private PaperDimensRepository paperDimensRepository;
    @Resource
    private IExamDimensionRepository examDimensionRepository;
    @Resource
    private DimensionQuestionsRepository repository;

    @Override
    public void saveExam(UserInfoForToken userInfo, ExamModel examModel) {
        String pattern="[0-9]+年[a-zA-Z\\u4E00-\\u9FA5]+-[a-zA-Z\\u4E00-\\u9FA5]+科目[a-zA-Z\\u4E00-\\u9FA5]+";
        boolean isMatch = Pattern.matches(pattern, examModel.getExamName());
        if (isMatch==true) {
            List<String> qids = new ArrayList<String>();
            ExamDimension exam = MappingModel2EntityConverter.converterForDimensionModel(userInfo, examModel, qids);
            exam = (ExamDimension) examRepository.save(exam);//保存试卷
            List<DimensionQuestion> questions = changeDimensionQuestionList(examModel.getQuestionModels());
            if (questions.size() > 0) {
                qids = saveDimensionQuestions(questions, exam.getExamId());//保存试题
                exam.setQuestionIds(qids);
                examRepository.save(exam);//修改试卷
            }
            ExamDimension exam1 = mongoTemplate.findOne(new Query().with(new Sort(DESC, "createTime")), ExamDimension.class);
            if (exam1 == null) {
                return;
            }
            List<String> examId = new ArrayList<String>();//记录试卷id
            examId.add(exam1.getExamId());
            String dname="未知";
            String grade="未知";
            String subject="未知";
            String type="未知";

                Integer in = examModel.getExamName().indexOf("目");
               dname = examModel.getExamName().substring(0, examModel.getExamName().indexOf("年"));//获取年份
               grade = examModel.getExamName().substring(examModel.getExamName().indexOf("年") + 1, examModel.getExamName().indexOf("-"));//获取年级
               subject = examModel.getExamName().substring(examModel.getExamName().indexOf("-") + 1, examModel.getExamName().indexOf("科"));//获取科目
               type = examModel.getExamName().substring(in + 1, examModel.getExamName().length());//获取题目类型

            if (grade.contains("高")){
                grade="高中";
            }else if (grade.contains("初")){
                grade="初中";
            }
            //查询是否存在这一维度
            Criteria criteria = Criteria.where("year").is(dname).and("grade").is(grade).and("subject").is(subject).and("type").is(type);
            PaperDimension papers = MappingModel2EntityConverter.converterForExamDimension(examModel.getExamId(), dname, grade, subject, type, examId);
            List<PaperDimension> paper = mongoTemplate.find(new Query(criteria), PaperDimension.class);
            if (paper.size() > 0) {//有，直接添加试卷id
                papers.setId(paper.get(0).getId());
                papers.setPaper(examId);
                mongoTemplate.save(papers);
            } else {//无，添加维度并添加试卷id
                papers.setYear(dname);
                papers.setGrade(grade);
                papers.setSubject(subject);
                papers.setType(type);
                papers.setCreateTime(new Date().getTime());
                papers.setPaper(examId);
                mongoTemplate.save(papers);
            }
        }
    }

    @Override
    public void deleteExams(DeleteQueryModel deleteQueryModel) {
        if (deleteQueryModel.getIds().size()>0){
            for (int s=0;s<deleteQueryModel.getIds().size();s++) {
                questionRepository.deleteDimensionQuestionByExamId(deleteQueryModel.getIds().get(s));//删除试卷内的所有题目
                //删除维度表里的试卷
                Criteria criteria = Criteria.where("paper").is(deleteQueryModel.getIds().get(s));
                Query mongoQuery = new Query(criteria);
                Update update = new Update();
                update.pull("paper", deleteQueryModel.getIds().get(s));
                mongoTemplate.updateMulti(mongoQuery, update, PaperDimension.class);

            }
        }
        examRepository.deleteExamDimensionByExamIdIsIn(deleteQueryModel.getIds());//删除试卷
    }

    @Override
    public PageHolder<ExamModel> getExamByExamName(UserInfoForToken userInfo, ExamModel examModel) {
        PageHolder<ExamModel> page = new PageHolder<ExamModel>();
        List<ExamDimension> exams = examRepository.findExamDimensionByExamNameAndConditions(userInfo.getUserId(), examModel.getExamName(), examModel.getPageSize(), examModel.getPageIndex());

        page.setContent(changeExamModelForExam(exams));
        page.setPageSize(examModel.getPageSize());
        page.setPageNum(examModel.getPageIndex());
        page.setTotal((int) examRepository.countByExamDimensionNameLike(userInfo.getUserId(), examModel.getExamName()));
        return page;
    }

    @Override
    public PageHolder<QuestionModel> getQuestions(ExamModel examModel) {
        PageHolder<QuestionModel> page = new PageHolder<QuestionModel>();
        List<DimensionQuestion> questions = questionRepository.findPageDimensionQuestion(examModel);
        page.setContent(changQustionList(questions));
        page.setPageNum(examModel.getPageIndex());
        page.setPageSize(examModel.getPageSize());
        page.setTotal((int) questionRepository.findCountByDimensionExamId(examModel.getExamId()));
        return page;
    }

    @Override
    public List<QuestionModel> getQuestions(String examId) {
        return changQustionList(questionRepository.findDimensionQuestionByExamId(examId));
    }

    @Override
    public List<Map<String, Object>> getQuestionTypegroup(String eid) {
        return examRepository.getGroupCounts(questionType, eid);
    }

    @Override
    public List<Map<String, Object>> getQuestionDifficultgroup(String eid) {
        return examRepository.getGroupCounts(difficultType, eid);
    }

    @Override
    public List<DimensionModel> getExamDimensionModel(UserInfoForToken userInfo) {
        DimensionModel dimensionModel=null;
        GradesModel gradesModel=null;
        SubjectModel subjectModel=null;
        TypeModel typeModel=null;

        List<DimensionModel> alllist=new ArrayList<DimensionModel>();
        List<GradesModel> glist=new ArrayList<GradesModel>();
        List<SubjectModel> slist=new ArrayList<SubjectModel>();
        List<TypeModel> tlist=new ArrayList<TypeModel>();
        //记录年份
        Set<String> year=new HashSet<String>();//记录年份
        Set<String> grade=new HashSet<String>();//记录年级
        Set<String> subject=new HashSet<String>();//记录科目
       // Set<String> type=new HashSet<String>();//记录类型
        Map<String,String> type=new HashMap<String,String>();

        List<PaperDimension> all=paperDimensRepository.findAll();//查询所有
        List<PaperDimension> gradelist=null;//年级
        List<PaperDimension> subjectlist=null;//科目
        List<PaperDimension> typelist=null;//科目
        if (all.size()>0){
            for (PaperDimension p:all){
                year.add(p.getYear());//去重记录年份
            }
           for (String y:year){
                grade=new HashSet<String>();
               gradelist=paperDimensRepository.findByYear(y);
                if (gradelist.size()>0){
                   for (PaperDimension g:gradelist){
                       grade.add(g.getGrade());
                   }
                   for (String g1:grade){
                       subject=new HashSet<String>();
                       subjectlist=paperDimensRepository.findByGradeAndYear(g1,y);
                       if (subjectlist.size()>0){
                           for (PaperDimension su:subjectlist){
                              subject.add(su.getSubject());
                           }
                           for (String s:subject){
                               type=new HashMap<String,String>();
                               typelist= paperDimensRepository.findBySubjectAndGradeAndYear(s,g1,y);
                               if (typelist.size()>0){
                                   for (PaperDimension t:typelist){
                                      type.put(t.getType(),t.getId());
                                   }
                                   for (String tt:type.keySet()){
                                       typeModel=new TypeModel(type.get(tt),tt,null);
                                       tlist.add(typeModel);
                                   }
                               }
                               subjectModel=new SubjectModel(s,tlist);
                               slist.add(subjectModel);
                               tlist=new ArrayList<TypeModel>();
                           }
                       }
                       gradesModel=new GradesModel(g1,slist);
                       glist.add(gradesModel);
                       slist=new ArrayList<SubjectModel>();
                   }
                }
               dimensionModel=new DimensionModel(y,glist);
               alllist.add(dimensionModel);
               glist=new ArrayList<GradesModel>();
           }
        }
        return alllist;
    }

    @Override
    public PageHolder<ExamModel> getExamDimensionById(UserInfoForToken userInfo, ExamModel examModel) throws ExamException {
        PageHolder<ExamModel> page = new PageHolder<ExamModel>();
        PaperDimension paperDimension=null;
        if (StringUtils.isEmpty(examModel.getExamId())){

        }
        paperDimension =mongoTemplate.findById(examModel.getExamId(),PaperDimension.class);
        if (paperDimension==null){new ExamException(ResultCode.SELECT_NULL_MSG);}
        List<ExamDimension> list=examRepository.findExamDimensionByExamIdIsInAndConditions(
            paperDimension.getPaper(),examModel.getPageSize(),examModel.getPageIndex());//examDimensionRepository.findByExamIdIsIn(paperDimension.getPaper());
        page.setContent(changeExamModelForExam(list));
        page.setPageSize(examModel.getPageSize());
        page.setPageNum(examModel.getPageIndex());
        page.setTotal((int)examRepository.countByExamDimensionExamId(paperDimension.getPaper()));
        return page;
    }


    @Override
    public List<Question> getQuestionById(UserInfoForToken userInfo, String examId){

        return null;
    }

    @Override
    public void saveGroupQuestions(UserInfoForToken userInfo, String examId) {
        List<String> qids = new ArrayList<String>();
        List<QuestionModel> q=new ArrayList<QuestionModel>();
        QuestionModel qq=new QuestionModel();
        Exam exam=new Exam();
        if (!StringUtils.isEmpty(examId)){
            ExamDimension examD=mongoTemplate.findById(examId,ExamDimension.class);
            if (examD==null){return;}

            exam.setExamId( UUID.randomUUID().toString().replace("-",""));
            exam.setExamName(examD.getExamName());
            exam.setExamSecondName(examD.getExamSecondName());
            exam.setTeacherId(userInfo.getUserId());
            exam.setTeacherName(userInfo.getUserName());
            exam.setUpdateTime(new Date().getTime());
            exam.setCreateTime(new Date().getTime());
            exam = (Exam) examRepository.save(exam);//保存试卷

            if (examD.getQuestionIds().size()<1){return;}
             for (String id:examD.getQuestionIds()){
              DimensionQuestion di=mongoTemplate.findById(id,DimensionQuestion.class);
                if (di==null){return;}
                 qq=MappingModel2EntityConverter.converterForExam(userInfo,di);
                 q.add(qq);
            }
            List<Question> questions = changeQuestionModelList(q);

            if (questions.size()>0){
                qids = saveQuestions(questions,exam.getExamId());//保存试题
                exam.setQuestionIds(qids);
                examRepository.save(exam);//修改试卷
            }
        }


    }

    private List<DimensionQuestion> changeDimensionQuestionList(List<QuestionModel> questionModels){
       List<DimensionQuestion> questions = new ArrayList<DimensionQuestion>();
       questionModels.forEach(questionModel -> {
           questions.add(MappingModel2EntityConverter.converterForDimensionQuestion(questionModel));
       });
       return questions;
   }


    private List<String> saveDimensionQuestions(List<DimensionQuestion> questions, String eid){
        List<String> qids = new ArrayList<String>();
        for (DimensionQuestion question:questions) {
            question.setExamId(eid);
            question = repository.save(question);
            if(question!=null){
                qids.add(question.getId());
            }
        }
        return qids;
    }
    private List<String> saveQuestions(List<Question> questions,String eid){
        List<String> qids = new ArrayList<String>();
        for (Question question:questions) {
            question.setExamId(eid);
            question = questionRepository.save(question);
            if(question!=null){
                qids.add(question.getId());
            }
        }
        return qids;
    }

    private List<Question> changeQuestionModelList(List<QuestionModel> questionModels){
        List<Question> questions = new ArrayList<Question>();
        questionModels.forEach(questionModel -> {
            questions.add(MappingModel2EntityConverter.converterForQuestionModel(questionModel));
        });
        return questions;
    }


    private List<ExamModel> changeExamModelForExam(List<ExamDimension> exams){
        List<ExamModel> examModels = new ArrayList<ExamModel>();
        exams.forEach(exam -> {
            examModels.add(MappingEntity2ModelConverter.ConverterForExam(exam));
        });

        return examModels;
    }


    private List<QuestionModel> changQustionList(List<DimensionQuestion> questions){
        List<QuestionModel> questionModels = new ArrayList<QuestionModel>();
        questions.forEach(question -> {
            questionModels.add(MappingEntity2ModelConverter.ConverterForDimensionQuestion(question));
        });
        return questionModels;
    }


}
