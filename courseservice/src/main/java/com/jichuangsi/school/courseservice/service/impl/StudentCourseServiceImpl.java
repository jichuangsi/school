package com.jichuangsi.school.courseservice.service.impl;

import com.jichuangsi.microservice.common.model.UserInfoForToken;
import com.jichuangsi.school.courseservice.Exception.StudentCourseServiceException;
import com.jichuangsi.school.courseservice.constant.ResultCode;
import com.jichuangsi.school.courseservice.entity.Course;
import com.jichuangsi.school.courseservice.entity.Question;
import com.jichuangsi.school.courseservice.entity.StudentAnswer;
import com.jichuangsi.school.courseservice.model.AnswerForStudent;
import com.jichuangsi.school.courseservice.model.CourseFile;
import com.jichuangsi.school.courseservice.model.CourseForStudent;
import com.jichuangsi.school.courseservice.model.QuestionForStudent;
import com.jichuangsi.school.courseservice.repository.CourseRepository;
import com.jichuangsi.school.courseservice.repository.QuestionRepository;
import com.jichuangsi.school.courseservice.repository.StudentAnswerRepository;
import com.jichuangsi.school.courseservice.service.IAutoVerifyAnswerService;
import com.jichuangsi.school.courseservice.service.IFileStoreService;
import com.jichuangsi.school.courseservice.service.IStudentCourseService;
import com.jichuangsi.school.courseservice.util.MappingEntity2ModelConverter;
import com.jichuangsi.school.courseservice.util.MappingModel2EntityConverter;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class StudentCourseServiceImpl implements IStudentCourseService{

    @Resource
    private CourseRepository courseRepository;

    @Resource
    private QuestionRepository questionRepository;

    @Resource
    private StudentAnswerRepository studentAnswerRepository;

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
    public List<CourseForStudent> getHistoryCoursesList(UserInfoForToken userInfo) throws StudentCourseServiceException {
        if(StringUtils.isEmpty(userInfo.getClassId())) throw new StudentCourseServiceException(ResultCode.PARAM_MISS_MSG);
        List<Course> courses = courseRepository.findHistoryCourseByClassIdAndStatus(userInfo.getClassId());
        return convertCourseList(courses);
    }

    @Override
    public CourseForStudent getParticularCourse(UserInfoForToken userInfo, String courseId) throws StudentCourseServiceException {
        if(StringUtils.isEmpty(courseId)) throw new StudentCourseServiceException(ResultCode.PARAM_MISS_MSG);
        Course course = courseRepository.findFirstByIdAndClassId(courseId, userInfo.getClassId());
        List<Question> questions = questionRepository.findHistoryQuestionsByClassIdAndCourseId(userInfo.getClassId(), courseId);
        CourseForStudent courseForStudent = MappingEntity2ModelConverter.ConvertStudentCourse(course);
        courseForStudent.getQuestions().addAll(convertQuestionList(questions));
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
    public void saveStudentAnswer(UserInfoForToken userInfo, String questionId, AnswerForStudent answer) throws StudentCourseServiceException {
        if(StringUtils.isEmpty(userInfo.getUserId())
                || StringUtils.isEmpty(questionId)
                || (StringUtils.isEmpty(answer.getAnswerForObjective()) && StringUtils.isEmpty(answer.getStubForSubjective())))
            throw new StudentCourseServiceException(ResultCode.PARAM_MISS_MSG);
        if(!StringUtils.isEmpty(answer.getAnswerForObjective())){
            Optional<Question> question = questionRepository.findById(questionId);
            if(!question.isPresent()) throw new StudentCourseServiceException(ResultCode.QUESTION_NOT_EXISTED);
            answer.setResult(autoVerifyAnswerService.verifyObjectiveAnswer(question.get(), answer.getAnswerForObjective()));
        }
        Optional<StudentAnswer> result = Optional.ofNullable(studentAnswerRepository.findFirstByQuestionIdAndStudentId(questionId, userInfo.getUserId()));
        if(result.isPresent()){
            StudentAnswer answer2Update = result.get();
            answer2Update.setSubjectivePic(answer.getPicForSubjective());
            answer2Update.setSubjectivePicStub(answer.getStubForSubjective());
            answer2Update.setObjectiveAnswer(answer.getAnswerForObjective());
            answer2Update.setResult(answer.getResult().getName());
            studentAnswerRepository.save(answer2Update);
        }else{
            studentAnswerRepository.save(MappingModel2EntityConverter.ConvertStudentAnswer(userInfo, questionId, answer));
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
