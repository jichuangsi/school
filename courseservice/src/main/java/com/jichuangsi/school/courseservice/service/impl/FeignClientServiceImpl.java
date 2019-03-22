package com.jichuangsi.school.courseservice.service.impl;

import com.jichuangsi.school.courseservice.Exception.FeignControllerException;
import com.jichuangsi.school.courseservice.Exception.StudentCourseServiceException;
import com.jichuangsi.school.courseservice.Exception.TeacherCourseServiceException;
import com.jichuangsi.school.courseservice.constant.Result;
import com.jichuangsi.school.courseservice.constant.ResultCode;
import com.jichuangsi.school.courseservice.entity.Question;
import com.jichuangsi.school.courseservice.entity.StudentAnswer;
import com.jichuangsi.school.courseservice.model.AnswerForStudent;
import com.jichuangsi.school.courseservice.model.CourseForStudent;
import com.jichuangsi.school.courseservice.model.Knowledge;
import com.jichuangsi.school.courseservice.model.feign.QuestionRateModel;
import com.jichuangsi.school.courseservice.model.result.ResultKnowledgeModel;
import com.jichuangsi.school.courseservice.model.transfer.TransferKnowledge;
import com.jichuangsi.school.courseservice.repository.CourseRepository;
import com.jichuangsi.school.courseservice.repository.QuestionRepository;
import com.jichuangsi.school.courseservice.repository.StudentAnswerRepository;
import com.jichuangsi.school.courseservice.service.IFeignClientService;
import com.jichuangsi.school.courseservice.service.IStudentCourseService;
import com.jichuangsi.school.courseservice.service.ITeacherCourseService;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.util.*;

@Service
public class FeignClientServiceImpl implements IFeignClientService {
    @Resource
    private QuestionRepository questionRepository;
    @Resource
    private IStudentCourseService studentCourseService;
    @Resource
    private ITeacherCourseService teacherCourseService;
    @Resource
    private StudentAnswerRepository studentAnswerRepository;
    @Resource
    private CourseRepository courseRepository;

    @Override
    public TransferKnowledge getKnowledgeOfParticularQuestion(String questionId) throws StudentCourseServiceException {
        if (StringUtils.isEmpty(questionId)) throw new StudentCourseServiceException(ResultCode.PARAM_MISS_MSG);
        Optional<Question> result = questionRepository.findById(questionId);
        if (result.isPresent()) {
            TransferKnowledge transferKnowledge = new TransferKnowledge();
            result.get().getKnowledges().forEach(knowledge -> {
                transferKnowledge.getKnowledges().add(
                        new Knowledge(knowledge.getKnowledgeId(), knowledge.getKnowledge(),
                                knowledge.getCapabilityId(), knowledge.getCapability()));
            });
            return transferKnowledge;
        }
        throw new StudentCourseServiceException(ResultCode.QUESTION_NOT_EXISTED);
    }

    @Override
    public List<ResultKnowledgeModel> getStudentQuestionOnWeek(String classId, String subject) throws StudentCourseServiceException {
        List<CourseForStudent> courseForStudents = studentCourseService.getCourseOnWeek(classId);
        List<ResultKnowledgeModel> models = new ArrayList<ResultKnowledgeModel>();
        for (CourseForStudent course : courseForStudents) {
            if (subject.equals(course.getSubjectName())) {
                if (null == course.getQuestionIds()) {
                    continue;
                }
                for (String questionId : course.getQuestionIds()) {
                    ResultKnowledgeModel resultKnowledgeModel = new ResultKnowledgeModel();
                    resultKnowledgeModel.setQuestionId(questionId);
                    resultKnowledgeModel.setCourseId(course.getCourseId());
                    models.add(resultKnowledgeModel);
                }
            }
        }
        for (ResultKnowledgeModel model : models) {
            try {
                model.setTransferKnowledge(getKnowledgeOfParticularQuestion(model.getQuestionId()));
            } catch (StudentCourseServiceException e) {
                continue;
            }
        }
        return models;
    }

    @Override
    public double getStudentQuestionRate(QuestionRateModel model) throws FeignControllerException {
        List<String> questionIds = model.getQuestionIds();
        String studentId = model.getStudentId();
        if (null == questionIds || !(questionIds.size() > 0) || StringUtils.isEmpty(studentId)) {
            throw new FeignControllerException(ResultCode.PARAM_ERR_MSG);
        }
        try {
            double count = 0;
            List<AnswerForStudent> answerForStudents = teacherCourseService.getQuestionsResult(questionIds, studentId);
            if (!(answerForStudents.size() > 0)) {
                return 0;
            }
            for (AnswerForStudent answerForStudent : answerForStudents) {
                if (Result.CORRECT.getName().equals(answerForStudent.getResult().getName())) {
                    count = count + 1;
                } else if (Result.PASS.getName().equals(answerForStudent.getResult().getName())) {

                }
            }
            return count / answerForStudents.size();
        } catch (TeacherCourseServiceException e) {
            throw new FeignControllerException(e.getMessage());
        }
    }

    @Override
    public List<ResultKnowledgeModel> getQuestionKnowledges(List<String> questionIds) throws StudentCourseServiceException {
        if (null == questionIds || !(questionIds.size() > 0)) {
            throw new StudentCourseServiceException(ResultCode.SELECT_NULL_MSG);
        }
        List<ResultKnowledgeModel> resultKnowledgeModels = new ArrayList<ResultKnowledgeModel>();
        for (String questionId : questionIds) {
            ResultKnowledgeModel resultKnowledgeModel = new ResultKnowledgeModel();
            resultKnowledgeModel.setQuestionId(questionId);
            resultKnowledgeModel.setTransferKnowledge(getKnowledgeOfParticularQuestion(questionId));
            resultKnowledgeModels.add(resultKnowledgeModel);
        }
        return resultKnowledgeModels;
    }

    @Override
    public double getQuetsionIdsCrossByMD5(QuestionRateModel model) throws FeignControllerException {
        if (null == model || null == model.getQuestionIds() || !(model.getQuestionIds().size() > 0) || StringUtils.isEmpty(model.getGradeId())) {
            throw new FeignControllerException(ResultCode.PARAM_MISS_MSG);
        }
        List<Question> questions = questionRepository.findByIdIn(model.getQuestionIds());
        if (!(questions.size() > 0)) {
            throw new FeignControllerException(ResultCode.SELECT_NULL_MSG);
        }
        Set<String> MD52s = new HashSet<String>();
        for (Question question : questions) {
            MD52s.add(question.getIdMD52());
        }
        questions = questionRepository.findByGradeIdAndIdMD52In(model.getGradeId(), new ArrayList<String>(MD52s));
        List<String> questionIds = new ArrayList<String>();
        for (Question question : questions) {
            questionIds.add(question.getId());
        }
        List<StudentAnswer> studentAnswers = studentAnswerRepository.findByQuestionIdIn(questionIds);
        if (!(studentAnswers.size() > 0)) {
            return 0;
        }
        double count = 0;
        for (StudentAnswer studentAnswer : studentAnswers) {
            if (Result.CORRECT.getName().equals(studentAnswer.getResult())) {
                count = count + 1;
            } else if (Result.PASS.getName().equals(studentAnswer.getResult())) {

            }
        }
        return count / studentAnswers.size();
    }
}
