package com.jichuangsi.school.courseservice.service.impl;

import com.jichuangsi.microservice.common.model.UserInfoForToken;
import com.jichuangsi.school.courseservice.Exception.StudentCourseServiceException;
import com.jichuangsi.school.courseservice.constant.Result;
import com.jichuangsi.school.courseservice.constant.ResultCode;
import com.jichuangsi.school.courseservice.constant.Status;
import com.jichuangsi.school.courseservice.entity.Course;
import com.jichuangsi.school.courseservice.entity.Question;
import com.jichuangsi.school.courseservice.entity.StudentAnswer;
import com.jichuangsi.school.courseservice.model.*;
import com.jichuangsi.school.courseservice.repository.CourseRepository;
import com.jichuangsi.school.courseservice.repository.QuestionRepository;
import com.jichuangsi.school.courseservice.repository.StudentAnswerRepository;
import com.jichuangsi.school.courseservice.repository.TeacherAnswerRepository;
import com.jichuangsi.school.courseservice.service.IAutoVerifyAnswerService;
import com.jichuangsi.school.courseservice.service.IFileStoreService;
import com.jichuangsi.school.courseservice.service.IMqService;
import com.jichuangsi.school.courseservice.service.IStudentCourseService;
import com.jichuangsi.school.courseservice.util.MappingEntity2MessageConverter;
import com.jichuangsi.school.courseservice.util.MappingEntity2ModelConverter;
import com.jichuangsi.school.courseservice.util.MappingModel2EntityConverter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class StudentCourseServiceImpl implements IStudentCourseService{

    @Value("${com.jichuangsi.school.result.page-size}")
    private int defaultPageSize;

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

    @Override
    public List<CourseForStudent> getCoursesList(UserInfoForToken userInfo) throws StudentCourseServiceException {
        if(StringUtils.isEmpty(userInfo.getClassId())) throw new StudentCourseServiceException(ResultCode.PARAM_MISS_MSG);
        List<Course> courses = courseRepository.findCourseByClassIdAndStatus(userInfo.getClassId());
        return convertCourseList(courses);
    }

    @Override
    public PageHolder<CourseForStudent> getHistoryCoursesList(UserInfoForToken userInfo, CourseForStudent pageInform) throws StudentCourseServiceException {
        if(StringUtils.isEmpty(userInfo.getClassId())) throw new StudentCourseServiceException(ResultCode.PARAM_MISS_MSG);
        PageHolder<CourseForStudent> pageHolder = new PageHolder<CourseForStudent>();
        pageHolder.setTotal(courseRepository.findByClassIdAndStatus(userInfo.getClassId(),Status.FINISH.getName()).size());
        pageHolder.setPageNum(pageInform.getPageNum());
        pageHolder.setPageSize(StringUtils.isEmpty(pageInform.getPageSize())||pageInform.getPageSize()==0?defaultPageSize:pageInform.getPageSize());
        List<Course> courses = courseRepository.findHistoryCourseByClassIdAndStatus(userInfo.getClassId(), pageInform.getPageNum(),
                StringUtils.isEmpty(pageInform.getPageSize())||pageInform.getPageSize()==0?defaultPageSize:pageInform.getPageSize());
        pageHolder.setContent(convertCourseList(courses));
        return pageHolder;
    }

    @Override
    public CourseForStudent getParticularCourse(UserInfoForToken userInfo, String courseId) throws StudentCourseServiceException {
        if(StringUtils.isEmpty(courseId)) throw new StudentCourseServiceException(ResultCode.PARAM_MISS_MSG);
        Course course = courseRepository.findFirstByIdAndClassIdOrderByUpdateTimeDesc(courseId, userInfo.getClassId());
        List<Question> questions = questionRepository.findQuestionsByClassIdAndCourseId(userInfo.getClassId(), courseId);
        CourseForStudent courseForStudent = MappingEntity2ModelConverter.ConvertStudentCourse(course);
        courseForStudent.getQuestions().addAll(convertQuestionList(questions));
        courseForStudent.getQuestions().forEach(question ->{
            Optional<StudentAnswer> result = Optional.ofNullable(studentAnswerRepository.findFirstByQuestionIdAndStudentIdOrderByUpdateTimeDesc(question.getQuestionId(), userInfo.getUserId()));
            if(result.isPresent()){
                question.setAnswerForStudent(MappingEntity2ModelConverter.ConvertStudentAnswer(result.get()));
                if(Result.PASS.getName().equalsIgnoreCase(result.get().getResult())){
                    question.setAnswerForTeacher(MappingEntity2ModelConverter.ConvertTeacherAnswer(
                            teacherAnswerRepository.findFirstByTeacherIdAndQuestionIdAndStudentAnswerIdOrderByUpdateTimeDesc(course.getTeacherId(), question.getQuestionId(), result.get().getId())
                    ));
                }
            }
        });
        return courseForStudent;
    }

    @Override
    public AnswerForStudent uploadStudentSubjectPic(UserInfoForToken userInfo, CourseFile file) throws StudentCourseServiceException {
        try{
            fileStoreService.uploadCourseFile(file);
        }catch (Exception exp){
            throw new StudentCourseServiceException(exp.getMessage());
        }
        AnswerForStudent answerForStudent = new AnswerForStudent();
        answerForStudent.setStudentId(userInfo.getUserId());
        answerForStudent.setStubForSubjective(file.getStoredName());
        return answerForStudent;
    }

    @Override
    public CourseFile downloadStudentSubjectPic(UserInfoForToken userInfo, String fileName) throws StudentCourseServiceException {
        try{
            return fileStoreService.donwloadCourseFile(fileName);
        }catch (Exception exp){
            throw new StudentCourseServiceException(exp.getMessage());
        }
    }

    @Override
    public void deleteStudentSubjectPic(UserInfoForToken userInfo, String fileName) throws StudentCourseServiceException {
        try{
            fileStoreService.deleteCourseFile(fileName);
        }catch (Exception exp){
            throw new StudentCourseServiceException(exp.getMessage());
        }
    }

    @Override
    public void saveStudentAnswer(UserInfoForToken userInfo, String courseId, String questionId, AnswerForStudent answer) throws StudentCourseServiceException {
        if(StringUtils.isEmpty(userInfo.getUserId())
                || StringUtils.isEmpty(courseId)
                || StringUtils.isEmpty(questionId)
                || (StringUtils.isEmpty(answer.getAnswerForObjective()) && StringUtils.isEmpty(answer.getStubForSubjective())))
            throw new StudentCourseServiceException(ResultCode.PARAM_MISS_MSG);
        synchronized (userInfo.getUserId().intern()){//需要实现分布式锁
            Optional<Question> question = questionRepository.findById(questionId);
            if(!question.isPresent()) throw new StudentCourseServiceException(ResultCode.QUESTION_NOT_EXISTED);
            if(Status.FINISH.getName().equalsIgnoreCase(question.get().getStatus())) throw new StudentCourseServiceException(ResultCode.QUESTION_COMPLETE);
            if(!StringUtils.isEmpty(answer.getAnswerForObjective())){
                answer.setResult(autoVerifyAnswerService.verifyObjectiveAnswer(question.get(), answer.getAnswerForObjective()));
            }
            Optional<StudentAnswer> result = Optional.ofNullable(studentAnswerRepository.findFirstByQuestionIdAndStudentIdOrderByUpdateTimeDesc(questionId, userInfo.getUserId()));
            StudentAnswer answer2Return = null;
            if(result.isPresent()){
                StudentAnswer answer2Update = result.get();
                //answer2Update.setSubjectivePic(answer.getReviseForSubjective());
                answer2Update.setSubjectivePicStub(answer.getStubForSubjective());
                answer2Update.setObjectiveAnswer(answer.getAnswerForObjective());
                answer2Update.setResult(StringUtils.isEmpty(answer.getResult())?null:answer.getResult().getName());
                answer2Update.setUpdateTime(new Date().getTime());
                answer2Return = studentAnswerRepository.save(answer2Update);
            }else{
                answer2Return = studentAnswerRepository.save(MappingModel2EntityConverter.ConvertStudentAnswer(userInfo, questionId, answer));
            }
            if(Optional.ofNullable(answer2Return).isPresent()) mqService.sendMsg4SubmitAnswer(MappingEntity2MessageConverter.ConvertAnswer(courseId, answer2Return));
        }
    }

    private List<CourseForStudent> convertCourseList(List<Course> courses){
        List<CourseForStudent> courseForTeachers = new ArrayList<CourseForStudent>();
        courses.forEach(course -> {
            courseForTeachers.add(MappingEntity2ModelConverter.ConvertStudentCourse(course));
        });
        return courseForTeachers;
    }

    private List<QuestionForStudent> convertQuestionList(List<Question> questions){
        List<QuestionForStudent> questionForStudents = new ArrayList<QuestionForStudent>();
        questions.forEach(question -> {
            questionForStudents.add(MappingEntity2ModelConverter.ConvertStudentQuestion(question));
        });
        return questionForStudents;
    }
}
