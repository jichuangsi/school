package com.jichuangsi.school.courseservice.service.impl;

import com.jichuangsi.microservice.common.model.UserInfoForToken;
import com.jichuangsi.school.courseservice.Exception.StudentCourseServiceException;
import com.jichuangsi.school.courseservice.constant.QuestionType;
import com.jichuangsi.school.courseservice.constant.Result;
import com.jichuangsi.school.courseservice.constant.ResultCode;
import com.jichuangsi.school.courseservice.constant.Status;
import com.jichuangsi.school.courseservice.entity.*;
import com.jichuangsi.school.courseservice.entity.Course;
import com.jichuangsi.school.courseservice.entity.Question;
import com.jichuangsi.school.courseservice.model.*;
import com.jichuangsi.school.courseservice.model.Knowledge;
import com.jichuangsi.school.courseservice.model.common.CustomArrayList;
import com.jichuangsi.school.courseservice.model.repository.QuestionNode;
import com.jichuangsi.school.courseservice.model.repository.QuestionQueryModel;
import com.jichuangsi.school.courseservice.repository.CourseRepository;
import com.jichuangsi.school.courseservice.repository.QuestionRepository;
import com.jichuangsi.school.courseservice.repository.StudentAnswerRepository;
import com.jichuangsi.school.courseservice.repository.TeacherAnswerRepository;
import com.jichuangsi.school.courseservice.service.*;
import com.jichuangsi.school.courseservice.util.MappingEntity2MessageConverter;
import com.jichuangsi.school.courseservice.util.MappingEntity2ModelConverter;
import com.jichuangsi.school.courseservice.util.MappingModel2EntityConverter;
import com.jichuangsi.school.courseservice.util.MappingModel2ModelConverter;
import com.mongodb.client.result.UpdateResult;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
public class StudentCourseServiceImpl implements IStudentCourseService {

    @Value("${com.jichuangsi.school.result.page-size}")
    private int defaultPageSize;

    @Value("${com.jichuangsi.school.question.ai-push.number}")
    private int questionAIPushNumber;

    @Value("${com.jichuangsi.school.question.ai-push.size}")
    private String questionAIPushSize;

    @Resource
    private IMqService mqService;

    @Resource
    private CourseRepository courseRepository;

    @Resource
    private QuestionRepository questionRepository;

    @Resource
    private StudentAnswerRepository studentAnswerRepository;

    @Resource
    private TeacherAnswerRepository teacherAnswerRepository;

    @Resource
    private IFileStoreService fileStoreService;

    @Resource
    private IAutoVerifyAnswerService autoVerifyAnswerService;

    @Resource
    private IQuestionInfoService questionInfoService;

    @Resource
    private IElementInfoService elementInfoService;

    @Resource
    private MongoTemplate mongoTemplate;

    @Override
    public List<CourseForStudent> getCoursesList(UserInfoForToken userInfo) throws StudentCourseServiceException {
        if (StringUtils.isEmpty(userInfo.getClassId()))
            throw new StudentCourseServiceException(ResultCode.PARAM_MISS_MSG);
        List<Course> courses = courseRepository.findCourseByClassIdAndStatus(userInfo.getClassId());
        return convertCourseList(courses);
    }

    @Override
    public PageHolder<CourseForStudent> getHistoryCoursesListFeign(String classId, CourseForStudent pageInform) throws StudentCourseServiceException {
        if (StringUtils.isEmpty(classId)) {
            throw new StudentCourseServiceException(ResultCode.PARAM_MISS_MSG);
        }
        PageHolder<CourseForStudent> pageHolder = new PageHolder<CourseForStudent>();
        pageHolder.setTotal(courseRepository.findByClassIdAndStatus(classId, Status.FINISH.getName()).size());
        pageHolder.setPageNum(pageInform.getPageNum());
        pageHolder.setPageSize(StringUtils.isEmpty(pageInform.getPageSize()) || pageInform.getPageSize() == 0 ? defaultPageSize : pageInform.getPageSize());
        List<Course> courses = courseRepository.findHistoryCourseByClassIdAndStatus(classId, pageInform.getPageNum(),
                StringUtils.isEmpty(pageInform.getPageSize()) || pageInform.getPageSize() == 0 ? defaultPageSize : pageInform.getPageSize());
        pageHolder.setContent(convertCourseList(courses));
        return pageHolder;
    }

    @Override
    public PageHolder<CourseForStudent> getHistoryCoursesList(UserInfoForToken userInfo, CourseForStudent pageInform) throws StudentCourseServiceException {
        if (StringUtils.isEmpty(userInfo.getClassId()))
            throw new StudentCourseServiceException(ResultCode.PARAM_MISS_MSG);
        PageHolder<CourseForStudent> pageHolder = new PageHolder<CourseForStudent>();
        pageHolder.setTotal(courseRepository.findByClassIdAndStatus(userInfo.getClassId(), Status.FINISH.getName()).size());
        pageHolder.setPageNum(pageInform.getPageNum());
        pageHolder.setPageSize(StringUtils.isEmpty(pageInform.getPageSize()) || pageInform.getPageSize() == 0 ? defaultPageSize : pageInform.getPageSize());
        List<Course> courses = courseRepository.findHistoryCourseByClassIdAndStatus(userInfo.getClassId(), pageInform.getPageNum(),
                StringUtils.isEmpty(pageInform.getPageSize()) || pageInform.getPageSize() == 0 ? defaultPageSize : pageInform.getPageSize());
        pageHolder.setContent(convertCourseList(courses));
        return pageHolder;
    }

    @Override
    public CourseForStudent getParticularCourse(UserInfoForToken userInfo, String courseId) throws StudentCourseServiceException {
        if (StringUtils.isEmpty(courseId)) throw new StudentCourseServiceException(ResultCode.PARAM_MISS_MSG);
        Course course = courseRepository.findFirstByIdAndClassIdOrderByUpdateTimeDesc(courseId, userInfo.getClassId());
        if (course == null) throw new StudentCourseServiceException(ResultCode.COURSE_NOT_EXISTED);
        List<Question> questions = questionRepository.findQuestionsByClassIdAndCourseId(userInfo.getClassId(), courseId);
        CourseForStudent courseForStudent = MappingEntity2ModelConverter.ConvertStudentCourse(course);
        courseForStudent.getQuestions().addAll(convertQuestionList(questions));
        courseForStudent.setAttachments(converterfromEntity(course.getAttachments()));
        courseForStudent.getQuestions().forEach(question -> {
            question.setFavor(mongoTemplate.exists(new Query(Criteria.where("studentId").is(userInfo.getUserId()).and("questionIds").is(question.getQuestionId())), StudentFavorQuestion.class));
            Optional<StudentAnswer> result = Optional.ofNullable(studentAnswerRepository.findFirstByQuestionIdAndStudentIdOrderByUpdateTimeDesc(question.getQuestionId(), userInfo.getUserId()));
            if (result.isPresent()) {
                question.setAnswerForStudent(MappingEntity2ModelConverter.ConvertStudentAnswer(result.get()));
                if (QuestionType.SUBJECTIVE.getName().equalsIgnoreCase(question.getQuesetionType())) {
                    if (Result.PASS.getName().equalsIgnoreCase(result.get().getResult())) {
                        question.setAnswerForTeacher(MappingEntity2ModelConverter.ConvertTeacherAnswer(
                                teacherAnswerRepository.findFirstByTeacherIdAndQuestionIdAndStudentAnswerIdOrderByUpdateTimeDesc(course.getTeacherId(), question.getQuestionId(), result.get().getId())
                        ));
                    }
                }
            }
            if (question.getAnswerForTeacher() == null) {
                TeacherAnswer shareAnswer = teacherAnswerRepository.findFirstByTeacherIdAndQuestionIdAndIsShareOrderByShareTimeDesc(course.getTeacherId(), question.getQuestionId(), true);
                if (shareAnswer != null)
                    question.setAnswerForTeacher(MappingEntity2ModelConverter.ConvertTeacherAnswer(shareAnswer));
            }
        });
        return courseForStudent;
    }

    @Override
    public AnswerForStudent uploadStudentSubjectPic(UserInfoForToken userInfo, CourseFile file) throws StudentCourseServiceException {
        try {
            fileStoreService.uploadCourseFile(file);
        } catch (Exception exp) {
            throw new StudentCourseServiceException(exp.getMessage());
        }
        AnswerForStudent answerForStudent = new AnswerForStudent();
        answerForStudent.setStudentId(userInfo.getUserId());
        answerForStudent.setStubForSubjective(file.getStoredName());
        return answerForStudent;
    }

    @Override
    public CourseFile downloadStudentSubjectPic(UserInfoForToken userInfo, String fileName) throws StudentCourseServiceException {
        try {
            return fileStoreService.donwloadCourseFile(fileName);
        } catch (Exception exp) {
            throw new StudentCourseServiceException(exp.getMessage());
        }
    }

    @Override
    public void deleteStudentSubjectPic(UserInfoForToken userInfo, String fileName) throws StudentCourseServiceException {
        try {
            fileStoreService.deleteCourseFile(fileName);
        } catch (Exception exp) {
            throw new StudentCourseServiceException(exp.getMessage());
        }
    }

    @Override
    public void saveStudentAnswer(UserInfoForToken userInfo, String courseId, String questionId, AnswerForStudent answer) throws StudentCourseServiceException {
        if (StringUtils.isEmpty(userInfo.getUserId())
                || StringUtils.isEmpty(courseId)
                || StringUtils.isEmpty(questionId)
                || (StringUtils.isEmpty(answer.getAnswerForObjective()) && StringUtils.isEmpty(answer.getStubForSubjective())))
            throw new StudentCourseServiceException(ResultCode.PARAM_MISS_MSG);
        String userId = userInfo.getUserId();
        synchronized (userId.intern()) {//需要实现分布式锁
            Optional<Question> question = questionRepository.findById(questionId);
            if (!question.isPresent()) throw new StudentCourseServiceException(ResultCode.QUESTION_NOT_EXISTED);
            if (Status.FINISH.getName().equalsIgnoreCase(question.get().getStatus()))
                throw new StudentCourseServiceException(ResultCode.QUESTION_COMPLETE);
            if (!StringUtils.isEmpty(answer.getAnswerForObjective())) {
                answer.setResult(autoVerifyAnswerService.verifyObjectiveAnswer(question.get(), answer.getAnswerForObjective()));
            }
            Optional<StudentAnswer> result = Optional.ofNullable(studentAnswerRepository.findFirstByQuestionIdAndStudentIdOrderByUpdateTimeDesc(questionId, userId));
            StudentAnswer answer2Return = null;
            if (result.isPresent()) {
                StudentAnswer answer2Update = result.get();
                //answer2Update.setSubjectivePic(answer.getReviseForSubjective());
                answer2Update.setSubjectivePicStub(answer.getStubForSubjective());
                answer2Update.setObjectiveAnswer(answer.getAnswerForObjective());
                answer2Update.setResult(StringUtils.isEmpty(answer.getResult()) ? null : answer.getResult().getName());
                answer2Update.setUpdateTime(new Date().getTime());
                answer2Return = studentAnswerRepository.save(answer2Update);
            } else {
                answer2Return = studentAnswerRepository.save(MappingModel2EntityConverter.ConvertStudentAnswer(userInfo, questionId, answer));
            }
            if (Optional.ofNullable(answer2Return).isPresent())
                mqService.sendMsg4SubmitAnswer(MappingEntity2MessageConverter.ConvertAnswer(courseId, answer2Return));
        }
    }

    @Override
    public QuestionForStudent getParticularQuestion(UserInfoForToken userInfo, String questionId) throws StudentCourseServiceException {
        if (StringUtils.isEmpty(userInfo.getUserId()) || StringUtils.isEmpty(questionId))
            throw new StudentCourseServiceException(ResultCode.PARAM_MISS_MSG);
        Optional<Question> result = questionRepository.findById(questionId);
        if (result.isPresent()) {
            QuestionForStudent questionForStudent = MappingEntity2ModelConverter.ConvertStudentQuestion(result.get());
            questionForStudent.setFavor(mongoTemplate.exists(new Query(Criteria.where("questionIds").is(questionId)), StudentFavorQuestion.class));
            StudentAnswer studentAnswer = studentAnswerRepository.findFirstByQuestionIdAndStudentIdOrderByUpdateTimeDesc(questionId, userInfo.getUserId());
            if (studentAnswer != null) {
                questionForStudent.setAnswerForStudent(MappingEntity2ModelConverter.ConvertStudentAnswer(studentAnswer));
                TeacherAnswer teacherAnswer = teacherAnswerRepository.findFirstByQuestionIdAndStudentAnswerIdOrderByUpdateTimeDesc(questionId, studentAnswer.getId());
                if (teacherAnswer != null)
                    questionForStudent.setAnswerForTeacher(MappingEntity2ModelConverter.ConvertTeacherAnswer(teacherAnswer));
            }
            return questionForStudent;
        }
        throw new StudentCourseServiceException(ResultCode.QUESTION_NOT_EXISTED);
    }

    @Override
    public void addParticularQuestionInFavor(UserInfoForToken userInfo, String questionId) throws StudentCourseServiceException {
        if (StringUtils.isEmpty(userInfo.getUserId()) || StringUtils.isEmpty(questionId))
            throw new StudentCourseServiceException(ResultCode.PARAM_MISS_MSG);
        synchronized (questionId.intern()) {//需要实现分布式锁
            Optional<Question> result = questionRepository.findById(questionId);
            if (result.isPresent()) {
                Query query = new Query();
                query.addCriteria(Criteria.where("studentId").is(userInfo.getUserId()));
                Update update = new Update();
                update.set("studentId", userInfo.getUserId());
                update.set("studentName", userInfo.getUserName());
                // addToSet存在则不加，不存在则加,push不管是否存在都加，这里用addToSet
                update.addToSet("questionIds", questionId);
                UpdateResult ur = mongoTemplate.upsert(query, update, StudentFavorQuestion.class);
                if (ur.isModifiedCountAvailable())
                    return;
                else
                    throw new StudentCourseServiceException(ResultCode.STUDENT_ADD_FAVOR_QUESTION_FAIL);
            }
        }

        throw new StudentCourseServiceException(ResultCode.QUESTION_NOT_EXISTED);
    }

    @Override
    public void removeParticularQuestionInFavor(UserInfoForToken userInfo, String questionId) throws StudentCourseServiceException {
        if (StringUtils.isEmpty(userInfo.getUserId()) || StringUtils.isEmpty(questionId))
            throw new StudentCourseServiceException(ResultCode.PARAM_MISS_MSG);
        synchronized (questionId.intern()) {//需要实现分布式锁
            Optional<Question> result = questionRepository.findById(questionId);
            if (result.isPresent()) {
                Query query = new Query();
                query.addCriteria(Criteria.where("studentId").is(userInfo.getUserId()));
                Update update = new Update();
                update.pull("questionIds", questionId);
                UpdateResult ur = mongoTemplate.updateFirst(query, update, StudentFavorQuestion.class);
                if (ur.isModifiedCountAvailable())
                    return;
                else
                    throw new StudentCourseServiceException(ResultCode.STUDENT_REMOVE_FAVOR_QUESTION_FAIL);
            }
        }

        throw new StudentCourseServiceException(ResultCode.QUESTION_NOT_EXISTED);
    }

    @Override
    public List<QuestionForStudent> getFavorQuestionsList(UserInfoForToken userInfo) throws StudentCourseServiceException {
        if (StringUtils.isEmpty(userInfo.getUserId()))
            throw new StudentCourseServiceException(ResultCode.PARAM_MISS_MSG);
        StudentFavorQuestion studentFavorQuestion = mongoTemplate.findOne(
                new Query(Criteria.where("studentId").is(userInfo.getUserId())), StudentFavorQuestion.class);
        List<QuestionForStudent> questions = new ArrayList<QuestionForStudent>();
        studentFavorQuestion.getQuestionIds().forEach(q -> {
            try {
                questions.add(getParticularQuestion(userInfo, q));
            } catch (StudentCourseServiceException scsExp) {

            }
        });
        return questions;
    }

    @Override
    public List<IncorrectQuestionReturnModel> getIncorrectQuestionList(UserInfoForToken userInfo, IncorrectQuestionQueryModel incorrectQuestionQueryModel) throws StudentCourseServiceException {
        if (StringUtils.isEmpty(userInfo.getUserId()))
            throw new StudentCourseServiceException(ResultCode.PARAM_MISS_MSG);
        if (StringUtils.isEmpty(incorrectQuestionQueryModel.getSubjectId()) && StringUtils.isEmpty(incorrectQuestionQueryModel.getKnowledgeId()))
            throw new StudentCourseServiceException(ResultCode.PARAM_MISS_MSG);
        List<Question> questions = studentAnswerRepository.findAllBySubjectIdAndKnowledgeIdAndStudentIdAndResult(
                incorrectQuestionQueryModel.getSubjectId(),
                incorrectQuestionQueryModel.getKnowledgeId(),
                userInfo.getUserId(), Result.WRONG.getName());
        List<QuestionForStudent> questionForStudents = convertQuestionList(questions);
        questionForStudents.forEach(questionForStudent -> {
            questionForStudent.setFavor(mongoTemplate.exists(new Query(Criteria.where("questionIds").is(questionForStudent.getQuestionId())), StudentFavorQuestion.class));
            StudentAnswer studentAnswer = studentAnswerRepository.findFirstByQuestionIdAndStudentIdOrderByUpdateTimeDesc(questionForStudent.getQuestionId(), userInfo.getUserId());
            if (studentAnswer != null) {
                questionForStudent.setAnswerForStudent(MappingEntity2ModelConverter.ConvertStudentAnswer(studentAnswer));
                TeacherAnswer teacherAnswer = teacherAnswerRepository.findFirstByQuestionIdAndStudentAnswerIdOrderByUpdateTimeDesc(questionForStudent.getQuestionId(), studentAnswer.getId());
                if (teacherAnswer != null)
                    questionForStudent.setAnswerForTeacher(MappingEntity2ModelConverter.ConvertTeacherAnswer(teacherAnswer));
            }
        });

        List<IncorrectQuestionReturnModel> incorrectQuestionReturnModelList = new ArrayList<IncorrectQuestionReturnModel>();
        Function<QuestionForStudent, CustomArrayList<Knowledge>> f1 = QuestionForStudent::getKnowledges;
        Function<CustomArrayList<Knowledge>, Knowledge> f2 = CustomArrayList::getFirst;
        Function<Knowledge, String> f3 = Knowledge::getKnowledgeId;
        questionForStudents.stream().collect(Collectors.groupingBy(f1.andThen(f2).andThen(f3), Collectors.toList()))
                .forEach((knowledgeId, questionListByKnowledgeId) -> {
                    IncorrectQuestionReturnModel incorrectQuestionReturnModel = new IncorrectQuestionReturnModel();
                    incorrectQuestionReturnModel.setKnowledgeId(knowledgeId);
                    if (questionListByKnowledgeId.size() > 0)
                        incorrectQuestionReturnModel.setKnowledge(questionListByKnowledgeId.get(0).getKnowledges().getFirst().getKnowledge());
                    incorrectQuestionReturnModel.setCount(questionListByKnowledgeId.size());
                    incorrectQuestionReturnModel.setQuestions(questionListByKnowledgeId);
                    incorrectQuestionReturnModelList.add(incorrectQuestionReturnModel);
                });

        /*questionForStudents.stream()
                .collect(Collectors.groupingBy(QuestionForStudent::getKnowledges, Collectors.toList()))
                .forEach((knowledgeId,questionListByKnowledgeId)->{
                    IncorrectQuestionReturnModel incorrectQuestionReturnModel = new IncorrectQuestionReturnModel();
                    incorrectQuestionReturnModel.setKnowledgeId(knowledgeId);
                    if(questionListByKnowledgeId.size()>0) incorrectQuestionReturnModel.setKnowledge(questionListByKnowledgeId.get(0).getKnowledge());
                    incorrectQuestionReturnModel.setCount(questionListByKnowledgeId.size());
                    incorrectQuestionReturnModel.setQuestions(questionListByKnowledgeId);
                    incorrectQuestionReturnModelList.add(incorrectQuestionReturnModel);
                });*/
        return incorrectQuestionReturnModelList;
    }

    @Override
    public List<QuestionForStudent> findSimilarQuestionsList(UserInfoForToken userInfo, QuestionQueryModel questionQueryModel) throws StudentCourseServiceException {
        if (StringUtils.isEmpty(userInfo.getUserId()))
            throw new StudentCourseServiceException(ResultCode.PARAM_MISS_MSG);
        questionQueryModel.setPage("1");
        questionQueryModel.setPageSize(questionAIPushSize);
        PageHolder<QuestionNode> list = questionInfoService.getQuestionsByKnowledge(questionQueryModel);
        if (list == null || list.getContent().size() == 0)
            throw new StudentCourseServiceException(ResultCode.AI_PUSH_QUESTION_FAIL);
        List<QuestionNode> questionNodes = new ArrayList<>(questionAIPushNumber);
        if (list.getContent().size() <= questionAIPushNumber) {
            questionNodes.addAll(list.getContent());
        } else {
            Random rand = new Random();
            for (int i = 0; i < questionAIPushNumber; i++) {
                questionNodes.add(list.getContent().get(rand.nextInt(list.getContent().size())));
            }
        }

        return convertQuestionList2(questionNodes);
    }

    private List<CourseForStudent> convertCourseList(List<Course> courses) {
        List<CourseForStudent> courseForTeachers = new ArrayList<CourseForStudent>();
        courses.forEach(course -> {
            courseForTeachers.add(MappingEntity2ModelConverter.ConvertStudentCourse(course));
        });
        return courseForTeachers;
    }

    private List<QuestionForStudent> convertQuestionList(List<Question> questions) {
        List<QuestionForStudent> questionForStudents = new ArrayList<QuestionForStudent>();
        questions.forEach(question -> {
            questionForStudents.add(MappingEntity2ModelConverter.ConvertStudentQuestion(question));
        });
        return questionForStudents;
    }

    private List<QuestionForStudent> convertQuestionList2(List<QuestionNode> questions) {
        List<QuestionForStudent> questionForStudents = new ArrayList<QuestionForStudent>();
        questions.forEach(question -> {
            QuestionForStudent questionForStudent = MappingModel2ModelConverter.ConvertQuestionNode(question);
            String qt = elementInfoService.fetchQuestionType(questionForStudent.getQuesetionType()).getName();
            questionForStudent.setQuesetionType(StringUtils.isEmpty(qt) ? QuestionType.SUBJECTIVE.getName() : qt);
            questionForStudents.add(questionForStudent);
        });
        return questionForStudents;
    }

    @Override
    public List<CourseForStudent> getCourseOnWeek(String classId) throws StudentCourseServiceException {
        Calendar c = Calendar.getInstance();
        c.setTime(new Date());
        c.add(Calendar.WEEK_OF_MONTH, -1);
        c.set(Calendar.HOUR_OF_DAY, 0);
        c.set(Calendar.MINUTE, 0);
        c.set(Calendar.SECOND, 0);
        if (StringUtils.isEmpty(classId)) throw new StudentCourseServiceException(ResultCode.PARAM_MISS_MSG);
        List<Course> courses = courseRepository.findOneWeekCourseByClassIdAndStatusAndEndTime(classId, c.getTimeInMillis());
        return convertCourseList(courses);
    }

    private List<AttachmentModel> converterfromEntity(List<Attachment> attachments) {
        List<AttachmentModel> newAttachments = new ArrayList<AttachmentModel>();
        for (Attachment attachment : attachments) {
            newAttachments.add(MappingEntity2ModelConverter.CONVERTERFROMATTACHMENT(attachment));
        }
        return newAttachments;
    }

    @Override
    public CourseFile downloadStudentAttachment(UserInfoForToken userInfo, String fileName) throws StudentCourseServiceException {
        try {
            return fileStoreService.donwloadCourseFile(fileName);
        } catch (Exception exp) {
            throw new StudentCourseServiceException(exp.getMessage());
        }
    }
}
