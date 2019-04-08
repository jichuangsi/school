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
import com.jichuangsi.school.courseservice.repository.CourseConsoleRepository;
import com.jichuangsi.school.courseservice.repository.CourseRepository;
import com.jichuangsi.school.courseservice.repository.QuestionRepository;
import com.jichuangsi.school.courseservice.repository.StudentAnswerRepository;
import com.jichuangsi.school.courseservice.service.IFeignClientService;
import com.jichuangsi.school.courseservice.service.IStudentCourseService;
import com.jichuangsi.school.courseservice.service.ITeacherCourseService;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.math.BigDecimal;
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
    @Resource
    private CourseConsoleRepository courseConsoleRepository;

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
            List<Course> courses = courseRepository.findByClassIdAndStatusAndEndTimeGreaterThanAndTeacherIdAndSubjectNameLikeOrderByCreateTime
                    (classModel.getClassId(), Status.FINISH.getName(), calendar.getTimeInMillis(),classModel.getTeacherId(),classModel.getSubject());
          System.out.println(courses.size());
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
        Map<String, String> capabilityMap =new HashMap<String,  String>();//记录认知能力及问题ID
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
                    capabilityMap.put(knowledgeModel.getQuestionId(),knowledge.getCapabilityId());//记录认知能力
                }
            }
        } catch (StudentCourseServiceException e) {
            throw new FeignControllerException(e.getMessage());
        }
        //保留两位数字
        BigDecimal bg = null;
        double f1 = 0.0;

        //记录认知总数及正确数
        int memory=0,comprehend=0,analy=0,apply=0,syhthesize=0,mt=0,ct=0,ant=0,apt=0,st=0,other=0,ot=0;
        List<StudentKnowledgeModel> studentKnowledgeModels = new ArrayList<StudentKnowledgeModel>();
        for (TransferStudent student : model.getTransferStudents()) {//循环取单个学生对应数据
            StudentKnowledgeModel knowledgeModel = new StudentKnowledgeModel();
            knowledgeModel.setStudentId(student.getStudentId());
            knowledgeModel.setStudentName(student.getStudentName());
            Map<String, String> questionResult = new HashMap<String, String>();
            for (String questionId : model.getQuestionIds()) {
                StudentAnswer studentAnswer = studentAnswerRepository.findFirstByQuestionIdAndStudentId(questionId, student.getStudentId());
                String result = "";
                System.out.println(questionId);
              //  System.out.println(studentAnswer.getResult());
                if (null == studentAnswer || Result.WRONG.getName().equals(studentAnswer.getResult())) {
                    result = Result.WRONG.getName();
                } else {
                    result = studentAnswer.getResult();
                }
                questionResult.put(questionId, result);
            }
            List<KnowledgeResultModel> resultModels = new ArrayList<KnowledgeResultModel>();
            Map<String,Double> capability1=new HashMap<String, Double>();


                for (String key : questionMap.keySet()) {
                    KnowledgeResultModel resultModel = new KnowledgeResultModel();
                    resultModel.setKnowledgeName(key);
                    List<String> questionIds = questionMap.get(key);
                    //for (String questionId1 : questionIds1) {//循环认知

                        for (String questionId : questionIds) {//循环知识点问题Id
                            String result = questionResult.get(questionId);
                            if (Result.CORRECT.getName().equals(result)) {//正确
                                resultModel.setTrueNum(resultModel.getTrueNum() + 1);
                            } else if (Result.PASS.getName().equals(result)) {
                            } else if (Result.WRONG.getName().equals(result)) {
                                resultModel.setWrongNum(resultModel.getWrongNum() + 1);
                            }
                            if (result != null) {
                                for (String key1 : capabilityMap.keySet()) {//循环读取认知能力
                                    String capability = capabilityMap.get(key1);//
                                    System.out.println(key1);
                                    String questionIds1 = key1;
                                    System.out.println(questionId);
                                    if (questionIds1.equals(questionId)) {//如果ID相同
                                        if (capability.equals("1")) {//记忆
                                            memory++;
                                            if (Result.CORRECT.getName().equals(result)) {//正确
                                                mt++;
                                                resultModel.setTrueNum(resultModel.getTrueNum() + 1);
                                            } else if (Result.PASS.getName().equals(result)) {
                                            } else if (Result.WRONG.getName().equals(result)) {
                                                resultModel.setWrongNum(resultModel.getWrongNum() + 1);
                                            }
                                        }
                                        if (capability.equals("2")) {//记忆
                                            comprehend++;
                                            if (Result.CORRECT.getName().equals(result)) {//正确
                                                ct++;
                                                resultModel.setTrueNum(resultModel.getTrueNum() + 1);
                                            } else if (Result.PASS.getName().equals(result)) {
                                            } else if (Result.WRONG.getName().equals(result)) {
                                                resultModel.setWrongNum(resultModel.getWrongNum() + 1);
                                            }
                                        }
                                        if (capability.equals("3")) {//记忆
                                            analy++;
                                            if (Result.CORRECT.getName().equals(result)) {//正确
                                                ant++;
                                                resultModel.setTrueNum(resultModel.getTrueNum() + 1);
                                            } else if (Result.PASS.getName().equals(result)) {
                                            } else if (Result.WRONG.getName().equals(result)) {
                                                resultModel.setWrongNum(resultModel.getWrongNum() + 1);
                                            }
                                        }
                                        if (capability.equals("4")) {//记忆
                                            apply++;
                                            if (Result.CORRECT.getName().equals(result)) {//正确
                                                apt++;
                                                resultModel.setTrueNum(resultModel.getTrueNum() + 1);
                                            } else if (Result.PASS.getName().equals(result)) {
                                            } else if (Result.WRONG.getName().equals(result)) {
                                                resultModel.setWrongNum(resultModel.getWrongNum() + 1);
                                            }
                                        }
                                        if (capability.equals("5")) {//记忆
                                            syhthesize++;
                                            if (Result.CORRECT.getName().equals(result)) {//正确
                                                st++;
                                                resultModel.setTrueNum(resultModel.getTrueNum() + 1);
                                            } else if (Result.PASS.getName().equals(result)) {
                                            } else if (Result.WRONG.getName().equals(result)) {
                                                resultModel.setWrongNum(resultModel.getWrongNum() + 1);
                                            }
                                        }
                                        if (capability == "" || capability == null) {//记忆
                                            other++;
                                            if (Result.CORRECT.getName().equals(result)) {//正确
                                                ot++;
                                                resultModel.setTrueNum(resultModel.getTrueNum() + 1);
                                            } else if (Result.PASS.getName().equals(result)) {
                                            } else if (Result.WRONG.getName().equals(result)) {
                                                resultModel.setWrongNum(resultModel.getWrongNum() + 1);
                                            }
                                        }
                                    }
                                    // }
                                }
                                resultModels.add(resultModel);
                                double m = 0, c = 0, an = 0, ap = 0, s = 0, o = 0;
                                //double m =resultModel.getTrueNum()/memory;
                                if (mt != 0 && memory != 0) {
                                    f1 = (double) mt / memory;
                                    bg = new BigDecimal(f1);
                                    m = bg.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
                                } else if (mt == 0 && memory != 0) {
                                    m = 0;
                                }
                                if (ct != 0 && comprehend != 0) {
                                    f1 = (double) ct / comprehend;
                                    bg = new BigDecimal(f1);
                                    c = bg.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
                                } else if (ct == 0 && comprehend != 0) {
                                    c = 0;
                                }
                                if (ant != 0 && analy != 0) {
                                    f1 = (double) ant / analy;
                                    bg = new BigDecimal(f1);
                                    an = bg.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
                                } else if (ant == 0 && analy != 0) {
                                    an = 0;
                                }
                                if (apt != 0 && apply != 0) {
                                    f1 = (double) apt / apply;
                                    bg = new BigDecimal(f1);
                                    ap = bg.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
                                } else if (apt == 0 && apply != 0) {
                                    ap = 0;
                                }
                                if (st != 0 && syhthesize != 0) {
                                    f1= (double) st / syhthesize;
                                    bg = new BigDecimal(f1);
                                    s = bg.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
                                } else if (st == 0 && syhthesize != 0) {
                                    s = 0;
                                }
                               /* if (ot != 0 && other != 0) {
                                    f1 = (double) ot / other;
                                    bg = new BigDecimal(f1);
                                    o = bg.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
                                } else if (ot == 0 && other != 0) {
                                    o = 0;
                                }*/

                                capability1.put("记忆", m);
                                capability1.put("理解", c);
                                capability1.put("分析", an);
                                capability1.put("应用", ap);
                                capability1.put("综合", s);
                               // capability1.put("其他", o);
                            }
                        }
            }
            knowledgeModel.setCapability(capability1);
            knowledgeModel.setKnowledgeResultModels(resultModels);
            studentKnowledgeModels.add(knowledgeModel);


        }//单个循环完
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
                courses.addAll(courseConsoleRepository.findByClassIdAndStatusAndEndTimeGreaterThanAndEndTimeLessThanAndSubjectNameLike(model.getClassId(), Status.FINISH.getName(), beignTime, calendar.getTimeInMillis(), model.getSubjectName()));
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
                    qIds.add(knowledgeModel.getQuestionId());
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

                statisticsModel.setClassRightAvgRate((double) classNum / (model.getStudentNum() * questionMap.get(key).size()));
                statisticsModel.setKnowledgeName(key);
                if (0 != questionNum) {
                    double rate = (double)questionMap.get(key).size() /questionNum;
                    statisticsModel.setKnowledgeRate(rate);
                }
                statisticsModel.setStudentRightRate((double) trueNum / questionMap.get(key).size());
                models.add(statisticsModel);
            }
            return models;
        } catch (StudentCourseServiceException e) {
            throw new FeignControllerException(e.getMessage());
        }
    }
}
