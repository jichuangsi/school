package com.jichuangsi.school.eaxmservice.service.impl;

import com.jichuangsi.microservice.common.model.UserInfoForToken;
import com.jichuangsi.school.courseservice.model.transfer.TransferExam;
import com.jichuangsi.school.eaxmservice.Model.DeleteQueryModel;
import com.jichuangsi.school.eaxmservice.Model.EaxmModel;
import com.jichuangsi.school.eaxmservice.Model.QuestionModel;
import com.jichuangsi.school.eaxmservice.Utils.CommonUtils;
import com.jichuangsi.school.eaxmservice.Utils.MappingEntity2ModelConverter;
import com.jichuangsi.school.eaxmservice.Utils.MappingModel2EntityConverter;
import com.jichuangsi.school.eaxmservice.constant.ResultCode;
import com.jichuangsi.school.eaxmservice.entity.Eaxm;
import com.jichuangsi.school.eaxmservice.entity.Question;
import com.jichuangsi.school.eaxmservice.exception.EaxmException;
import com.jichuangsi.school.eaxmservice.repository.IEaxmRepository;
import com.jichuangsi.school.eaxmservice.repository.IQuestionRepository;
import com.jichuangsi.school.eaxmservice.service.IEaxmService;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@Service
public class EaxmServiceImpl implements IEaxmService {

    @Resource
    private IEaxmRepository eaxmRepository;
    @Resource
    private IQuestionRepository questionRepository;

    @Resource
    private MongoTemplate mongoTemplate;
    @Override
    public void saveEaxm(UserInfoForToken userInfo, EaxmModel eaxmModel) {
        List<String> qids = new ArrayList<String>();
        Eaxm eaxm = MappingModel2EntityConverter.converterForEaxmModel(userInfo,eaxmModel,qids);
        eaxm = eaxmRepository.save(eaxm);//保存试卷
        List<Question> questions = changeQuestionModelList(eaxmModel.getQuestionModels());
        if (questions.size()>0){
           qids = saveQuestions(questions,eaxm.getEaxmId());//保存试题
           eaxm.setQuestionIds(qids);
           eaxmRepository.save(eaxm);//修改试卷
        }
    }

    @Override
    public void deleteEaxms(DeleteQueryModel deleteQueryModel) {

       /* System.out.println(eaxmRepository.findCountByEaxmId("q"));*/
/*
        System.out.println(eaxmRepository.findByEaxmId(deleteQueryModel.getIds().get(0)).getEaxmId());
*/
         /* eaxmRepository.deleteEaxmById(deleteQueryModel.getIds());*/
        Criteria criteria = Criteria.where("eaxmId").in(deleteQueryModel.getIds());
        mongoTemplate.findAllAndRemove(new Query(criteria),Eaxm.class);
        deleteQueryModel.getIds().forEach(s -> {
            questionRepository.deleteQuestionByEaxmId(s);
        });
    }

    @Override
    public void updateEaxmQ(EaxmModel eaxmModel) throws EaxmException {
        Eaxm eaxm = eaxmRepository.findOneByEaxmId(eaxmModel.getEaxmId());//查询eaxm保证试卷存在
        if(eaxm!=null&& !StringUtils.isEmpty(eaxm.getEaxmId())) {
            questionRepository.deleteQuestionByEaxmId(eaxm.getEaxmId());//删除试卷相关原试题
            List<Question> questions = changeQuestionModelList(eaxmModel.getQuestionModels());
            List<String> nqids = saveQuestions(questions, eaxm.getEaxmId());//保存新试题
            if (CommonUtils.decideList(nqids)) {//保证List不为null
                nqids = new ArrayList<String>();
            }
            eaxm.setQuestionIds(nqids);
            eaxm.setUpdateTime(new Date().getTime());
            eaxmRepository.save(eaxm);//修改试卷
        }else {
            throw new EaxmException(ResultCode.EAXM_EXIST_ERR);
        }
    }

    @Override
    public List<TransferExam> getTransferExamByTeacherId(String teacherId) {
        List<Eaxm> eaxms = eaxmRepository.findByTeacherId(teacherId);
        return changeForEaxms(eaxms);
    }

    @Override
    public List<QuestionModel> getQuestions(String eaxmId) {
        List<Question> questions = questionRepository.findByEaxmId(eaxmId);
        return changQustionList(questions);
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
            question.setEaxmId(eid);
            question.setId(UUID.randomUUID().toString());
            question = questionRepository.save(question);
            if(question!=null){
                qids.add(question.getId());
            }
        }
        return qids;
    }

    private List<TransferExam> changeForEaxms(List<Eaxm> eaxms){
        List<TransferExam> transferExams = new ArrayList<TransferExam>();
        eaxms.forEach(eaxm -> {
            TransferExam transferExam = new TransferExam();
            transferExam.setEaxmId(eaxm.getEaxmId());
            transferExam.setEaxmName(eaxm.getEaxmName());
            transferExams.add(transferExam);
        });
        return transferExams;
    }
}
