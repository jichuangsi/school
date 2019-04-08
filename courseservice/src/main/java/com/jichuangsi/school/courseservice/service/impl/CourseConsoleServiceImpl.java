package com.jichuangsi.school.courseservice.service.impl;

import com.jichuangsi.microservice.common.model.UserInfoForToken;
import com.jichuangsi.school.courseservice.Exception.TeacherCourseServiceException;
import com.jichuangsi.school.courseservice.constant.ResultCode;
import com.jichuangsi.school.courseservice.constant.Status;
import com.jichuangsi.school.courseservice.entity.Course;
import com.jichuangsi.school.courseservice.entity.Question;
import com.jichuangsi.school.courseservice.model.*;
import com.jichuangsi.school.courseservice.model.common.DeleteQueryModel;
import com.jichuangsi.school.courseservice.model.transfer.TransferTeacher;
import com.jichuangsi.school.courseservice.repository.CourseConsoleRepository;
import com.jichuangsi.school.courseservice.repository.CourseRepository;
import com.jichuangsi.school.courseservice.repository.QuestionRepository;
import com.jichuangsi.school.courseservice.service.*;
import com.jichuangsi.school.courseservice.util.DateFormateUtil;
import com.jichuangsi.school.courseservice.util.MappingEntity2ModelConverter;
import com.jichuangsi.school.courseservice.util.MappingModel2EntityConverter;
import com.mongodb.client.result.UpdateResult;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
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
    private CourseRepository courseRepository;

    @Resource
    private MongoTemplate mongoTemplate;

    @Resource
    private IFileStoreService fileStoreService;

    @Resource
    private IElementInfoService elementInfoService;

    @Resource
    private IMqService mqService;

    @Resource
    private QuestionRepository questionRepository;

    @Override
    public PageHolder<Course> getSortCoursesList(Course course, PageHolder<Course> page, String keyWord, Integer sortNum, Date nowDay) throws TeacherCourseServiceException {
        Long[] dates = null;
        if (nowDay != null) {
            Calendar calendar = new GregorianCalendar();
            calendar.setTime(nowDay);
            calendar.add(Calendar.DATE, 1);
            Date tomorrow = calendar.getTime();
            dates = new Long[]{nowDay.getTime(), tomorrow.getTime()};
        }
        List<Course> courses = courseConsoleRepository.findCourseByCourseAndKeyWord(course, sortNum, keyWord, page, dates);
        page.setContent(courses);
        page.setTotal((int) courseConsoleRepository.findCourseByCourseAndKeyWordCount(course, sortNum, keyWord, dates));
        return page;
    }

    //未写，添加course
    @Override
    public void saveNewCourse(UserInfoForToken userInfoForToken, CourseForTeacher courseForTeacher) throws TeacherCourseServiceException {
        DateFormateUtil dfu = new DateFormateUtil(defaultDateFormat1);
        TransferTeacher transferTeacher = userInfoService.getUserForTeacherById(userInfoForToken.getUserId());
        if (transferTeacher == null) throw new TeacherCourseServiceException(ResultCode.TEACHER_INFO_NOT_EXISTED);
        courseForTeacher.setSubjectId(transferTeacher.getSubjectId());
        courseForTeacher.setSubjectName(transferTeacher.getSubjectName());
        Course course = MappingModel2EntityConverter.ConvertTeacherCourse(userInfoForToken, courseForTeacher);
        if (course.getStartTime() > 0) course.setEndTime(dfu.getEndTime(course.getStartTime(), coursePeriod));
        //course.setStatus(Status.NOTSTART.getName());
        course = (Course) courseConsoleRepository.save(course);
        if (course == null) {
            throw new TeacherCourseServiceException(ResultCode.COURSE_FAIL_SAVE);
        }
        if (courseForTeacher.getQuestions() != null) {
            List<String> questionIds = new ArrayList<String>();
            Update update = new Update();
            for (QuestionForTeacher questionForTeacher : courseForTeacher.getQuestions()) {
                questionForTeacher.setGradeId(transferTeacher.getGradeId());
                questionForTeacher.setSubjectId(transferTeacher.getSubjectId());
                Question question = MappingModel2EntityConverter.ConvertTeacherQuestion(questionForTeacher);
                question = (Question) courseConsoleRepository.save(question);
                /*if (question == null) {
                    throw new TeacherCourseServiceException(ResultCode.QUESTION_FAIL_SAVE);
                }*/
                questionIds.add(question.getId());
            }
            //course.setQuestionIds(questionIds);
            update.set("questionIds", questionIds);
            UpdateResult result = mongoTemplate.upsert(new Query(Criteria.where("_id").is(course.getId())), update, Course.class);
        }
        //mongoTemplate.save(course);
    }

    @Override
    public long getCoursesListByTime(CourseForTeacher courseForTeacher) {
        long num = 0;
        courseForTeacher.setCourseEndTime(new DateFormateUtil(defaultDateFormat1).getEndTime(courseForTeacher.getCourseStartTime(), coursePeriod));
        if (courseForTeacher.getCourseStartTime() != 0 && courseForTeacher.getCourseEndTime() != 0 && courseForTeacher.getTeacherId() != null) {
            num = courseConsoleRepository.findCourseByStartTimeAndEndTime(courseForTeacher.getCourseStartTime(), courseForTeacher.getCourseEndTime(), courseForTeacher.getTeacherId());
        }
        return num;
    }

    @Override
    public List<Course> getNewCoursesList(String teacherId) {

        return courseConsoleRepository.findNewCourse(teacherId);
    }

    @Override
    public Course getCourseById(String id) {
        return mongoTemplate.findById(id, Course.class);
    }

    @Override
    public Boolean deleteCourseIsN(String courseId) {
        Course course = mongoTemplate.findById(courseId, Course.class);
        if (Status.NOTSTART.getName().equals(course.getStatus())) {
            List<Question> questions = mongoTemplate.findAllAndRemove(new Query(Criteria.where("id").in(course.getQuestionIds())), Question.class);
            mongoTemplate.remove(course);
        } else {
            return false;
        }
        return true;
    }

    @Override
    public void updateCourseIsN(UserInfoForToken userInfoForToken, CourseForTeacher courseForTeacher) throws TeacherCourseServiceException {
        Course storedCourse = mongoTemplate.findById(courseForTeacher.getCourseId(), Course.class);
        if (!Status.NOTSTART.getName().equalsIgnoreCase(storedCourse.getStatus()))
            throw new TeacherCourseServiceException(ResultCode.COURSE_ALREADY_BEGIN);
        DateFormateUtil dfu = new DateFormateUtil(defaultDateFormat1);
        //courseForTeacher = MappingEntity2ModelConverter.ConvertTeacherCourse(storedCourse);
        Course updatedCourse = MappingModel2EntityConverter.ConvertTeacherCourse(userInfoForToken, courseForTeacher);
        if (updatedCourse.getStartTime() > 0)
            updatedCourse.setEndTime(dfu.getEndTime(updatedCourse.getStartTime(), coursePeriod));
        courseConsoleRepository.updateCourseById(updatedCourse);
    }

    @Override
    public CourseForTeacher uploadIco(UserInfoForToken userInfo, CourseFile courseFile) throws TeacherCourseServiceException {
        try {
            fileStoreService.uploadCourseFile(courseFile);
        } catch (Exception e) {
            throw new TeacherCourseServiceException(ResultCode.FILE_UPLOAD_ERROR);
        }
        CourseForTeacher courseForTeacher = new CourseForTeacher();
        courseForTeacher.setTeacherId(userInfo.getUserId());
        courseForTeacher.setTeacherName(userInfo.getUserName());
        courseForTeacher.setCoursePic(courseFile.getStoredName());
        return courseForTeacher;
    }

    @Override
    public CourseFile downIco(UserInfoForToken userInfo, String pic) throws TeacherCourseServiceException {
        try {
            return fileStoreService.donwloadCourseFile(pic);
        } catch (Exception e) {
            throw new TeacherCourseServiceException(ResultCode.FILE_DOWNLOAD_ERROR);
        }
    }

    @Override
    public void updateIco(UserInfoForToken userInfo, CourseFile courseFile, String cid) throws TeacherCourseServiceException {
        Course course = mongoTemplate.findById(cid, Course.class);
        CourseForTeacher courseModle = new CourseForTeacher();
        courseModle.setCourseId(course.getId());
        courseModle.setCoursePic(uploadIco(userInfo, courseFile).getCoursePic());
        courseConsoleRepository.updateCourseById(MappingModel2EntityConverter.ConvertTeacherCourse(userInfo, courseModle));
        try {
            fileStoreService.deleteCourseFile(course.getPicAddress());
        } catch (Exception e) {
            throw new TeacherCourseServiceException(ResultCode.FILE_REMOVE_ERROR);
        }
    }

    @Override
    public AttachmentModel uploadAttachment(UserInfoForToken userInfo, CourseFile courseFile) throws TeacherCourseServiceException {
        try {
            fileStoreService.uploadCourseFile(courseFile);
        } catch (Exception e) {
            throw new TeacherCourseServiceException(ResultCode.FILE_UPLOAD_ERROR);
        }
        return new AttachmentModel(courseFile.getName(), courseFile.getStoredName(), courseFile.getContentType());
    }

    @Override
    public void removeCourseAttachment(UserInfoForToken userInfo, DeleteQueryModel deleteQueryModel) throws TeacherCourseServiceException {
        try {
            for (String d : deleteQueryModel.getIds()) {
                fileStoreService.deleteCourseFile(d);
            }
        } catch (Exception exp) {
            throw new TeacherCourseServiceException(exp.getMessage());
        }
    }

    @Override
    public List<Question> getQuestionList(List<String> qIds) {
        return mongoTemplate.find(new Query(Criteria.where("id").in(qIds)), Question.class);
    }

    @Override
    public void publishFileByTeacher(String fileName, String fileId, String courseId, UserInfoForToken userInfo) throws TeacherCourseServiceException {
        if (org.springframework.util.StringUtils.isEmpty(fileId) || org.springframework.util.StringUtils.isEmpty(fileName) || org.springframework.util.StringUtils.isEmpty(courseId) ||
                org.springframework.util.StringUtils.isEmpty(userInfo.getUserId()) || org.springframework.util.StringUtils.isEmpty(userInfo.getUserName())) {
            throw new TeacherCourseServiceException(ResultCode.PARAM_MISS_MSG);
        }
        Course course = courseRepository.findFirstByIdAndTeacherId(courseId, userInfo.getUserId());
        if (null == course) {
            throw new TeacherCourseServiceException(ResultCode.SELECT_NULL_MSG);
        }
        // 判断未开始的课不能发布
        if (course.getStatus().equals(Status.NOTSTART)) {
            throw new TeacherCourseServiceException(ResultCode.COURSE_NOTSTART);
        }
        // 将题目改为已发布
        List<com.jichuangsi.school.courseservice.entity.Attachment> attachments = course.getAttachments();
        attachments.forEach(attachment -> {
            if (fileId.equals(attachment.getSub())) {
                attachment.setPublishStatus("1");
            }
        });
        courseRepository.save(course);
        TeacherPublishFile publishFile = new TeacherPublishFile();
        publishFile.setCourseId(courseId);
        publishFile.setFileId(fileId);
        publishFile.setFileName(fileName);
        publishFile.setTeacherId(userInfo.getUserId());
        publishFile.setTeacherName(userInfo.getUserName());
        mqService.sendPublishFile(publishFile);
    }

    @Override
    public List<QuestionForTeacher> getWrongQuestions(List<String> questionIds) throws TeacherCourseServiceException {
        if (null == questionIds || !(questionIds.size() > 0)) {
            throw new TeacherCourseServiceException(ResultCode.PARAM_MISS_MSG);
        }
        List<Question> questions = questionRepository.findByIdIn(questionIds);
        List<QuestionForTeacher> questionForTeachers = new ArrayList<QuestionForTeacher>();
        questions.forEach(question -> {
            questionForTeachers.add(MappingEntity2ModelConverter.ConvertTeacherQuestion(question));
        });
        return questionForTeachers;
    }


}
