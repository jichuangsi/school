package com.jichuangsi.school.courseservice.service.impl;

import com.jichuangsi.school.courseservice.Exception.TeacherCourseServiceException;
import com.jichuangsi.school.courseservice.constant.ResultCode;
import com.jichuangsi.school.courseservice.constant.Status;
import com.jichuangsi.school.courseservice.entity.Course;
import com.jichuangsi.school.courseservice.entity.Question;
import com.jichuangsi.school.courseservice.model.CourseForTeacher;
import com.jichuangsi.school.courseservice.model.QuestionForTeacher;
import com.jichuangsi.school.courseservice.repository.CourseRepository;
import com.jichuangsi.school.courseservice.repository.QuestionRepository;
import com.jichuangsi.school.courseservice.service.IMqService;
import com.jichuangsi.school.courseservice.service.ITeacherCourseService;
import com.jichuangsi.school.courseservice.util.MappingEntity2MessageConverter;
import com.jichuangsi.school.courseservice.util.MappingEntity2ModelConverter;
import com.jichuangsi.school.courseservice.util.MappingModel2EntityConverter;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class TeacherCourseServiceImpl implements ITeacherCourseService {

    @Resource
    private IMqService mqService;

    @Resource
    private CourseRepository courseRepository;

    @Resource
    private QuestionRepository questionRepository;

    @Override
    public CourseForTeacher saveCourse(CourseForTeacher course){
       return MappingEntity2ModelConverter.ConvertCourse(
               courseRepository.save(MappingModel2EntityConverter.ConvertTeacherCourse(course)));

    }

    @Override
    public void deleteCourse(CourseForTeacher course){
        courseRepository.delete(MappingModel2EntityConverter.ConvertTeacherCourse(course));
    }

    @Override
    public List<QuestionForTeacher> saveQuestions(String courseId, List<QuestionForTeacher> questions) throws TeacherCourseServiceException{
        Optional<Course> result = courseRepository.findById(courseId);
        if(result.isPresent()){
            Course course = result.get();
            List<Question> questionsList2Store = new ArrayList<Question>();
            questions.forEach(question -> {
                questionsList2Store.add(MappingModel2EntityConverter.ConvertTeacherQuestion(question));
            });
            if(questionsList2Store.size()>0){
                List<Question> questionsList2Trans = questionRepository.saveAll(questionsList2Store);
                List<String> oldQuestionIds = new ArrayList<String>(course.getQuestionIds());
                course.getQuestionIds().removeAll(course.getQuestionIds());
                questionsList2Trans.forEach(question -> {
                    course.getQuestionIds().add(question.getId());
                });
                courseRepository.save(course);
                oldQuestionIds.forEach(questionId -> {
                    questionRepository.deleteById(questionId);
                });
                List<QuestionForTeacher> questionsList2Return = new ArrayList<QuestionForTeacher>();
                questionsList2Trans.forEach(question -> {
                    questionsList2Return.add(MappingEntity2ModelConverter.ConvertQuestion(question));
                });
                return questionsList2Return;
            }
            throw new TeacherCourseServiceException(ResultCode.QUESTIONS_TO_SAVE_IS_EMPTY);
        }
        throw new TeacherCourseServiceException(ResultCode.COURSE_NOT_EXISTED);
    }

    @Override
    public void startCourse(CourseForTeacher course)throws TeacherCourseServiceException{
        String courseId = course.getCourseId();
        if(StringUtils.isEmpty(courseId)) throw new TeacherCourseServiceException(ResultCode.PARAM_MISS_MSG);
        Optional<Course> result = courseRepository.findById(courseId);
        if(result.isPresent()){
            Course course2Update = result.get();
            course2Update.setStatus(Status.PROGRESS.getName());
            course2Update = courseRepository.save(course2Update);
            mqService.sendMsg4StartCourse(MappingEntity2MessageConverter.ConvertCourse(course2Update));
        }else{
            throw new TeacherCourseServiceException(ResultCode.COURSE_NOT_EXISTED);
        }
    }
}
