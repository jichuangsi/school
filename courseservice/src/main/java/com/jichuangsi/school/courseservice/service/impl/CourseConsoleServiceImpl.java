package com.jichuangsi.school.courseservice.service.impl;

import com.jichuangsi.microservice.common.model.UserInfoForToken;
import com.jichuangsi.school.courseservice.Exception.TeacherCourseServiceException;
import com.jichuangsi.school.courseservice.constant.ResultCode;
import com.jichuangsi.school.courseservice.constant.Status;
import com.jichuangsi.school.courseservice.entity.Course;
import com.jichuangsi.school.courseservice.entity.Question;
import com.jichuangsi.school.courseservice.model.CourseForTeacher;
import com.jichuangsi.school.courseservice.model.PageHolder;
import com.jichuangsi.school.courseservice.model.QuestionForTeacher;
import com.jichuangsi.school.courseservice.model.transfer.TransferTeacher;
import com.jichuangsi.school.courseservice.repository.CourseConsoleRepository;
import com.jichuangsi.school.courseservice.service.ICourseConsoleService;
import com.jichuangsi.school.courseservice.service.IUserInfoService;
import com.jichuangsi.school.courseservice.util.DateFormateUtil;
import com.jichuangsi.school.courseservice.util.MappingModel2EntityConverter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.*;

@Service
public class CourseConsoleServiceImpl implements ICourseConsoleService {


    @Value("${com.jichuangsi.school.result.date-format1}")
    private String defaultDateFormat1;

    @Value("${com.jichuangsi.school.result.course-period}")
    private int coursePeriod;

    @Resource
    private IUserInfoService userInfoService;

    @Resource
    private CourseConsoleRepository courseConsoleRepository;

    @Resource
    private MongoTemplate mongoTemplate;

    @Override
    public PageHolder<Course> getSortCoursesList(Course course, PageHolder<Course> page, String keyWord, Integer sortNum, Date nowDay) throws TeacherCourseServiceException {
        Long[] dates = null;
        if(nowDay!=null){
            Calendar calendar = new GregorianCalendar();
            calendar.setTime(nowDay);
            calendar.add(Calendar.DATE,1);
            Date tomorrow = calendar.getTime();
            dates = new Long[]{nowDay.getTime(),tomorrow.getTime()};
        }
        List<Course> courses = courseConsoleRepository.findCourseByCourseAndKeyWord(course,sortNum,keyWord,page,dates);
        page.setContent(courses);
        page.setTotal((int)courseConsoleRepository.findCourseByCourseAndKeyWordCount(course,sortNum,keyWord,dates));
        return page;
    }

    //未写，添加course
    @Override
    public void saveNewCourse(UserInfoForToken userInfoForToken, CourseForTeacher courseForTeacher) throws TeacherCourseServiceException {
        DateFormateUtil dfu  = new DateFormateUtil(defaultDateFormat1);
        Course course = MappingModel2EntityConverter.ConvertTeacherCourse(userInfoForToken,courseForTeacher);
        if(course.getStartTime() > 0) course.setEndTime(dfu.getEndTime(course.getStartTime(), coursePeriod));
        //course.setStatus(Status.NOTSTART.getName());
        course = (Course) courseConsoleRepository.save(course);
        if(course==null){
            throw new TeacherCourseServiceException(ResultCode.COURSE_FAIL_SAVE);
        }
        List<String> questionIds = new ArrayList<String>();
        for (QuestionForTeacher questionForTeacher: courseForTeacher.getQuestions() ) {
            Question question = MappingModel2EntityConverter.ConvertTeacherQuestion(questionForTeacher);
            question = (Question) courseConsoleRepository.save(question);
            if (question==null){
                throw new TeacherCourseServiceException(ResultCode.QUESTION_FAIL_SAVE);
            }
            questionIds.add(question.getId());
        }
        course.setQuestionIds(questionIds);
        mongoTemplate.save(course);
    }

    @Override
    public long getCoursesListByTime(CourseForTeacher courseForTeacher) {
        long num = 0;
        courseForTeacher.setCourseEndTime(new DateFormateUtil(defaultDateFormat1).getEndTime(courseForTeacher.getCourseStartTime(), coursePeriod));
        if(courseForTeacher.getCourseStartTime()!=0&&courseForTeacher.getCourseEndTime()!=0&&courseForTeacher.getTeacherId()!=null){
            num = courseConsoleRepository.findCourseByStartTimeAndEndTime(courseForTeacher.getCourseStartTime(),courseForTeacher.getCourseEndTime(),courseForTeacher.getTeacherId());
        }
        return num;
    }

    @Override
    public List<Course> getNewCoursesList(String teacherId) {

        return courseConsoleRepository.findNewCourse(teacherId);
    }

    @Override
    public Course getCourseById(String id) {
        return mongoTemplate.findById(id,Course.class);
    }

    @Override
    public Boolean deleteCourseIsN(String courseId) {
        Course course = mongoTemplate.findById(courseId,Course.class);
        if(Status.NOTSTART.getName().equals(course.getStatus())){
            List<Question> questions = mongoTemplate.findAllAndRemove(new Query(Criteria.where("id").in(course.getQuestionIds())), Question.class);
            mongoTemplate.remove(course);
        }else{
            return false;
        }
        return true;
    }

    @Override
    public void updateCourseIsN(UserInfoForToken userInfoForToken,CourseForTeacher courseForTeacher) throws TeacherCourseServiceException{
        Course storedCourse = mongoTemplate.findById(courseForTeacher.getCourseId(), Course.class);
        if(!Status.NOTSTART.getName().equalsIgnoreCase(storedCourse.getStatus())) throw new TeacherCourseServiceException(ResultCode.COURSE_ALREADY_BEGIN);
        DateFormateUtil dfu = new DateFormateUtil(defaultDateFormat1);
        Course course = MappingModel2EntityConverter.ConvertTeacherCourse(userInfoForToken,courseForTeacher);
        if(course.getStartTime() > 0)
            course.setEndTime(dfu.getEndTime(course.getStartTime(), coursePeriod));
        courseConsoleRepository.updateCourseById(course);
    }
}
