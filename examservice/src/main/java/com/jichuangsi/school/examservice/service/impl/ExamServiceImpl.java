package com.jichuangsi.school.examservice.service.impl;

import com.jichuangsi.microservice.common.model.UserInfoForToken;
import com.jichuangsi.school.courseservice.model.PageHolder;
import com.jichuangsi.school.courseservice.model.transfer.TransferExam;
import com.jichuangsi.school.examservice.Model.DeleteQueryModel;
import com.jichuangsi.school.examservice.Model.ExamModel;
import com.jichuangsi.school.examservice.Model.QuestionModel;
import com.jichuangsi.school.examservice.Utils.CommonUtils;
import com.jichuangsi.school.examservice.Utils.MappingEntity2ModelConverter;
import com.jichuangsi.school.examservice.Utils.MappingModel2EntityConverter;
import com.jichuangsi.school.examservice.constant.ResultCode;
import com.jichuangsi.school.examservice.entity.Exam;
import com.jichuangsi.school.examservice.entity.Question;
import com.jichuangsi.school.examservice.exception.ExamException;
import com.jichuangsi.school.examservice.repository.IExamRepository;
import com.jichuangsi.school.examservice.repository.IQuestionRepository;
import com.jichuangsi.school.examservice.service.IExamService;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class ExamServiceImpl implements IExamService{


    @Resource
    private IExamRepository examRepository;
    @Resource
    private IQuestionRepository questionRepository;

    @Resource
    private MongoTemplate mongoTemplate;
    @Override
    public void saveExam(UserInfoForToken userInfo, ExamModel examModel) {
        List<String> qids = new ArrayList<String>();
        Exam exam = MappingModel2EntityConverter.converterForExamModel(userInfo,examModel,qids);
        exam = (Exam) examRepository.save(exam);//保存试卷
        List<Question> questions = changeQuestionModelList(examModel.getQuestionModels());
        if (questions.size()>0){
            qids = saveQuestions(questions,exam.getExamId());//保存试题
            exam.setQuestionIds(qids);
            examRepository.save(exam);//修改试卷
        }
    }

    @Override
    public void deleteExams(DeleteQueryModel deleteQueryModel) {

        /* System.out.println(examRepository.findCountByExamId("q"));*/
/*
        System.out.println(examRepository.findByExamId(deleteQueryModel.getIds().get(0)).getExamId());
*/
        /* examRepository.deleteExamById(deleteQueryModel.getIds());*/
        /*Criteria criteria = Criteria.where("examId").in(deleteQueryModel.getIds());
        mongoTemplate.findAllAndRemove(new Query(criteria),Exam.class);*/
        examRepository.deleteExamsByExamIdIsIn(deleteQueryModel.getIds());
        deleteQueryModel.getIds().forEach(s -> {
            questionRepository.deleteQuestionByExamId(s);
        });
    }

    @Override
    public void updateExamQ(ExamModel examModel) throws ExamException {
        Exam exam = examRepository.findOneByExamId(examModel.getExamId());//查询exam保证试卷存在
        if(exam!=null&& !StringUtils.isEmpty(exam.getExamId())) {
            questionRepository.deleteQuestionByExamId(exam.getExamId());//删除试卷相关原试题
            List<Question> questions = changeQuestionModelList(examModel.getQuestionModels());
            List<String> nqids = saveQuestions(questions, exam.getExamId());//保存新试题
            if (CommonUtils.decideList(nqids)) {//保证List不为null
                nqids = new ArrayList<String>();
            }
            exam.setQuestionIds(nqids);
            exam.setUpdateTime(new Date().getTime());
            examRepository.save(exam);//修改试卷
        }else {
            throw new ExamException(ResultCode.EXAM_EXIST_ERR);
        }
    }

    @Override
    public List<TransferExam> getTransferExamByTeacherId(String teacherId) {
        List<Exam> exams = examRepository.findByTeacherId(teacherId);
        return changeForExams(exams);
    }

    @Override
    public List<QuestionModel> getQuestions(String examId) {
        List<Question> questions = questionRepository.findByExamId(examId);
        return changQustionList(questions);
    }

    @Override
    public PageHolder<TransferExam> getExamByExamName(ExamModel examModel) {
        PageHolder<TransferExam> page = new PageHolder<TransferExam>();
        List<Exam> exams = examRepository.findExamsByExamNameLike(examModel.getExamName(),examModel.getPageSize(),examModel.getPageIndex());
        page.setContent(changeForExams(exams));
        page.setPageSize(Integer.valueOf(examModel.getPageSize()));
        page.setPageNum(Integer.valueOf(examModel.getPageIndex()));
        page.setTotal((int)examRepository.countByExamNameLike(examModel.getExamName()));
        return page;
    }

    private List<QuestionModel> changQustionList(List<Question> questions){
        List<QuestionModel> questionModels = new ArrayList<QuestionModel>();
        questions.forEach(question -> {
            questionModels.add(MappingEntity2ModelConverter.ConverterForQuestion(question));
        });
        return questionModels;
    }
    private List<Question> changeQuestionModelList(List<QuestionModel> questionModels){
        List<Question> questions = new ArrayList<Question>();
        questionModels.forEach(questionModel -> {
            questions.add(MappingModel2EntityConverter.converterForQuestionModel(questionModel));
        });
        return questions;
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

    private List<TransferExam> changeForExams(List<Exam> exams){
        List<TransferExam> transferExams = new ArrayList<TransferExam>();
        exams.forEach(exam -> {
            TransferExam transferExam = new TransferExam();
            transferExam.setExamId(exam.getExamId());
            transferExam.setExamName(exam.getExamName());
            transferExams.add(transferExam);
        });
        return transferExams;
    }

}
