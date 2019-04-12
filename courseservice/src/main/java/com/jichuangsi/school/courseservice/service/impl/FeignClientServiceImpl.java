package com.jichuangsi.school.courseservice.service.impl;

import com.jichuangsi.school.courseservice.Exception.FeignControllerException;
import com.jichuangsi.school.courseservice.Exception.StudentCourseServiceException;
import com.jichuangsi.school.courseservice.Exception.TeacherCourseServiceException;
import com.jichuangsi.school.courseservice.constant.Result;
import com.jichuangsi.school.courseservice.constant.ResultCode;
import com.jichuangsi.school.courseservice.constant.Status;
import com.jichuangsi.school.courseservice.entity.Course;
import com.jichuangsi.school.courseservice.entity.Question;
import com.jichuangsi.school.courseservice.entity.StudentAnswer;
import com.jichuangsi.school.courseservice.model.AnswerForStudent;
import com.jichuangsi.school.courseservice.model.CourseForStudent;
import com.jichuangsi.school.courseservice.model.Knowledge;
import com.jichuangsi.school.courseservice.model.feign.QuestionRateModel;
import com.jichuangsi.school.courseservice.model.feign.classType.*;
import com.jichuangsi.school.courseservice.model.feign.statistics.KnowledgeStatisticsModel;
import com.jichuangsi.school.courseservice.model.feign.statistics.ParentStatisticsModel;
import com.jichuangsi.school.courseservice.model.result.QuestionStatisticsRateModel;
import com.jichuangsi.school.courseservice.model.result.ResultKnowledgeModel;
import com.jichuangsi.school.courseservice.model.transfer.TransferKnowledge;
import com.jichuangsi.school.courseservice.model.transfer.TransferStudent;
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
                if (StringUtils.isEmpty(answerForStudent.getResult())) {
                    continue;
                }
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
        if (null == questionIds) {
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
            if (StringUtils.isEmpty(studentAnswer.getResult())) {
                continue;
            }
            if (Result.CORRECT.getName().equals(studentAnswer.getResult())) {
                count = count + 1;
            } else if (Result.PASS.getName().equals(studentAnswer.getResult())) {

            }
        }
        return count / studentAnswers.size();
    }

    @Override
    public List<ClassStatisticsModel> getClassStatisticsByClassIdsOnMonth(List<ClassDetailModel> classModels) throws FeignControllerException {
        if (null == classModels || !(classModels.size() > 0)) {
            throw new FeignControllerException(ResultCode.CLASSID_IS_NULL);
        }
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        calendar.add(Calendar.MONTH, -1);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        List<ClassStatisticsModel> classStatisticsModels = new ArrayList<ClassStatisticsModel>();
        for (ClassDetailModel classModel : classModels) {
            int wrong = 0;
            int weak = 0;
            List<ResultKnowledgeModel> resultKnowledgeModels = new ArrayList<ResultKnowledgeModel>();
            ClassStatisticsModel model = new ClassStatisticsModel();
            Map<String, QuestionStatisticsRateModel> questionCalendar = new HashMap<String, QuestionStatisticsRateModel>();
            List<Course> courses = courseRepository.findByClassIdAndStatusAndEndTimeGreaterThanOrderByCreateTime(classModel.getClassId(), Status.FINISH.getName(), calendar.getTimeInMillis());
            model.setClassId(classModel.getClassId());
            model.setClassName(classModel.getClassName());
            List<String> questionMonth = new ArrayList<String>();
            for (Course course : courses) {
                model.getCourseIds().add(course.getId());
                List<String> questionIds = course.getQuestionIds();
                for (String questionId : questionIds) {
                    try {
                        TransferKnowledge transferKnowledge = getKnowledgeOfParticularQuestion(questionId);
                        ResultKnowledgeModel resultKnowledgeModel = new ResultKnowledgeModel();
                        resultKnowledgeModel.setCourseId(course.getId());
                        resultKnowledgeModel.setQuestionId(questionId);
                        resultKnowledgeModel.setTransferKnowledge(transferKnowledge);
                        resultKnowledgeModels.add(resultKnowledgeModel);
                        questionMonth.add(questionId);
                        QuestionStatisticsRateModel statisticsRateModel = new QuestionStatisticsRateModel();
                        List<StudentAnswer> studentAnswers = studentAnswerRepository.findAllByQuestionId(questionId);
                        for (StudentAnswer studentAnswer : studentAnswers) {
                            if (Result.CORRECT.getName().equals(studentAnswer.getResult())) {
                                statisticsRateModel.setTrueNum(statisticsRateModel.getTrueNum() + 1);
                            } else if (Result.PASS.getName().equals(studentAnswer.getResult())) {
                                //todo 没全对计分
                            }
                        }
                        statisticsRateModel.setWrongNum(classModel.getStudentNum() - statisticsRateModel.getTrueNum());
                        statisticsRateModel.setQuestionId(questionId);
                        if (statisticsRateModel.getWrongNum() / classModel.getStudentNum() > 0.3) {
                            wrong++;
                            model.getWrongQuestionIds().add(questionId);
                        }
                        if (questionCalendar.containsKey(questionId)) {
                            statisticsRateModel.setTrueNum(statisticsRateModel.getTrueNum() + questionCalendar.get(questionId).getTrueNum());
                        }
                        questionCalendar.put(questionId, statisticsRateModel);
                    } catch (StudentCourseServiceException e) {
                        throw new FeignControllerException(e.getMessage());
                    }
                }
            }
            model.setWrongQuestionNum(wrong);
            Map<String, List<String>> questionMap = new HashMap<String, List<String>>();
            for (ResultKnowledgeModel knowledgeModel : resultKnowledgeModels) {
                for (Knowledge knowledge : knowledgeModel.getTransferKnowledge().getKnowledges()) {
                    if (StringUtils.isEmpty(knowledge.getKnowledge())) {
                        knowledge.setKnowledge("综合分类");
                    }
                    List<String> questionIds = new ArrayList<String>();
                    if (questionMap.containsKey(knowledge.getKnowledge())) {
                        questionIds.addAll(questionMap.get(knowledge.getKnowledge()));
                    }
                    questionIds.add(knowledgeModel.getQuestionId());
                    questionMap.put(knowledge.getKnowledge(), questionIds);
                }
            }
            for (String key : questionMap.keySet()) {
                List<String> qids = questionMap.get(key);
                KnowledgeResultModel resultModel = new KnowledgeResultModel();
                resultModel.setKnowledgeName(key);
                for (String qid : qids) {
                    QuestionStatisticsRateModel questionStatisticsRateModel = questionCalendar.get(qid);
                    resultModel.setTrueNum(resultModel.getTrueNum() + questionStatisticsRateModel.getTrueNum());
                    resultModel.setWrongNum(resultModel.getWrongNum() + questionStatisticsRateModel.getWrongNum());
                }
                int sumNum = resultModel.getTrueNum() + resultModel.getWrongNum();
                if (resultModel.getWrongNum() / sumNum > 0.3) {
                    weak++;
                }
                model.getKnowledgeResultModels().add(resultModel);
            }
            model.setWeakQuestionNum(weak);
            model.setMonthQuestionIds(questionMonth);
            classStatisticsModels.add(model);
        }
        return classStatisticsModels;
    }

    @Override
    public List<StudentKnowledgeModel> getStudentKnowledges(SearchStudentKnowledgeModel model) throws FeignControllerException {
        if (!(model.getQuestionIds().size() > 0) || !(model.getTransferStudents().size() > 0)) {
            throw new FeignControllerException(ResultCode.PARAM_MISS_MSG);
        }
        Map<String, List<String>> questionMap = new HashMap<String, List<String>>();
        try {
            List<ResultKnowledgeModel> resultKnowledgeModels = getQuestionKnowledges(model.getQuestionIds());
            for (ResultKnowledgeModel knowledgeModel : resultKnowledgeModels) {
                for (Knowledge knowledge : knowledgeModel.getTransferKnowledge().getKnowledges()) {
                    if (StringUtils.isEmpty(knowledge.getKnowledge())) {
                        knowledge.setKnowledge("综合分类");
                    }
                    List<String> questionIds = new ArrayList<String>();
                    if (questionMap.containsKey(knowledge.getKnowledge())) {
                        questionIds.addAll(questionMap.get(knowledge.getKnowledge()));
                    }
                    questionIds.add(knowledgeModel.getQuestionId());
                    questionMap.put(knowledge.getKnowledge(), questionIds);
                }
            }
        } catch (StudentCourseServiceException e) {
            throw new FeignControllerException(e.getMessage());
        }
        List<StudentKnowledgeModel> studentKnowledgeModels = new ArrayList<StudentKnowledgeModel>();
        for (TransferStudent student : model.getTransferStudents()) {
            StudentKnowledgeModel knowledgeModel = new StudentKnowledgeModel();
            knowledgeModel.setStudentId(student.getStudentId());
            knowledgeModel.setStudentName(student.getStudentName());
            Map<String, String> questionResult = new HashMap<String, String>();
            for (String questionId : model.getQuestionIds()) {
                StudentAnswer studentAnswer = studentAnswerRepository.findFirstByQuestionIdAndStudentId(questionId, student.getStudentId());
                String result = "";
                if (null == studentAnswer || Result.WRONG.getName().equals(studentAnswer.getResult())) {
                    result = Result.WRONG.getName();
                } else {
                    result = studentAnswer.getResult();
                }
                questionResult.put(questionId, result);
            }
            List<KnowledgeResultModel> resultModels = new ArrayList<KnowledgeResultModel>();
            for (String key : questionMap.keySet()) {
                KnowledgeResultModel resultModel = new KnowledgeResultModel();
                resultModel.setKnowledgeName(key);
                List<String> questionIds = questionMap.get(key);
                for (String questionId : questionIds) {
                    String result = questionResult.get(questionId);
                    if (Result.CORRECT.getName().equals(result)) {
                        resultModel.setTrueNum(resultModel.getTrueNum() + 1);
                    } else if (Result.PASS.getName().equals(result)) {

                    } else if (Result.WRONG.getName().equals(result)) {
                        resultModel.setWrongNum(resultModel.getWrongNum() + 1);
                    }
                }
                resultModels.add(resultModel);
            }
            knowledgeModel.setKnowledgeResultModels(resultModels);
            studentKnowledgeModels.add(knowledgeModel);
        }
        return studentKnowledgeModels;
    }

    @Override
    public List<KnowledgeStatisticsModel> getParentStatistics(ParentStatisticsModel model) throws FeignControllerException {
        if (StringUtils.isEmpty(model.getClassId()) || StringUtils.isEmpty(model.getStudentId()) || StringUtils.isEmpty(model.getSubjectName()) || 0 == model.getStudentNum()) {
            throw new FeignControllerException(ResultCode.PARAM_MISS_MSG);
        }
        List<Course> courses = new ArrayList<Course>();
        if (model.getBeignTime() > 0) {
            courses.addAll(courseRepository.findByClassIdAndStatusAndEndTimeGreaterThanAndSubjectNameLikeOrderByCreateTime(model.getClassId(), Status.FINISH.getName(), model.getBeignTime(), model.getSubjectName()));
        } else {
            if (!(model.getStatisticsTimes().size() > 0)) {
                throw new FeignControllerException(ResultCode.PARAM_MISS_MSG);
            }
            for (Long beignTime : model.getStatisticsTimes()) {
                if (null == beignTime) {
                    continue;
                }
                Calendar calendar = Calendar.getInstance();
                calendar.setTime(new Date(beignTime));
                calendar.set(Calendar.HOUR_OF_DAY, 23);
                calendar.set(Calendar.MINUTE, 59);
                calendar.set(Calendar.SECOND, 59);
                courses.addAll(courseRepository.findByClassIdAndStatusAndEndTimeGreaterThanAndEndTimeLessThanAndSubjectNameLike(model.getClassId(), Status.FINISH.getName(), beignTime, calendar.getTimeInMillis(), model.getSubjectName()));
            }
        }
        List<String> questionIds = new ArrayList<String>();
        for (Course course : courses) {
            questionIds.addAll(course.getQuestionIds());
        }
        Map<String,Integer> selfTrue = new HashMap<String, Integer>();
        Map<String,Integer> classTrue = new HashMap<String, Integer>();
        for (String qid : questionIds){
            int trueNum = 0;
            int selfNum = 0;
            List<StudentAnswer> answers = studentAnswerRepository.findAllByQuestionId(qid);
            for (StudentAnswer answer : answers){
                if (Result.CORRECT.getName().equals(answer.getResult())){
                    trueNum ++;
                }else if (Result.PASS.getName().equals(answer.getResult())){
                    //todo 主观题统计
                }
                if (model.getStudentId().equals(answer.getStudentId())){
                    selfNum = 1;
                }
            }
            selfTrue.put(qid,selfNum);
            classTrue.put(qid,trueNum);
        }
        try {
            List<ResultKnowledgeModel> resultKnowledgeModels = getQuestionKnowledges(questionIds);
            Map<String, List<String>> questionMap = new HashMap<String, List<String>>();
            for (ResultKnowledgeModel knowledgeModel : resultKnowledgeModels) {
                for (Knowledge knowledge : knowledgeModel.getTransferKnowledge().getKnowledges()) {
                    if (StringUtils.isEmpty(knowledge.getKnowledge())) {
                        knowledge.setKnowledge("综合分类");
                    }
                    List<String> qIds = new ArrayList<String>();
                    if (questionMap.containsKey(knowledge.getKnowledge())) {
                        qIds.addAll(questionMap.get(knowledge.getKnowledge()));
                    }
                    questionIds.add(knowledgeModel.getQuestionId());
                    questionMap.put(knowledge.getKnowledge(), qIds);
                }
            }
            int questionNum = 0;
            for (String key : questionMap.keySet()){
                questionNum = questionNum + questionMap.get(key).size();
            }
            List<KnowledgeStatisticsModel> models = new ArrayList<KnowledgeStatisticsModel>();
            for (String key : questionMap.keySet()){
                int trueNum = 0;
                int classNum = 0;
                for (String questionId: questionMap.get(key)){
                    trueNum = trueNum + selfTrue.get(questionId);
                    classNum = classNum + classTrue.get(questionId);
                }
                KnowledgeStatisticsModel statisticsModel = new KnowledgeStatisticsModel();
                statisticsModel.setClassRightAvgRate(classNum / (model.getStudentNum() * questionMap.get(key).size()));
                statisticsModel.setKnowledgeName(key);
                if (0 != questionNum) {
                    statisticsModel.setKnowledgeRate(questionMap.get(key).size() / questionNum);
                }
                statisticsModel.setStudentRightRate(trueNum / questionMap.get(key).size());
                models.add(statisticsModel);
            }
            return models;
        } catch (StudentCourseServiceException e) {
            throw new FeignControllerException(e.getMessage());
        }
    }
}
