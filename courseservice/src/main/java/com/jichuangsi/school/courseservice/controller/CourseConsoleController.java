package com.jichuangsi.school.courseservice.controller;

import com.jichuangsi.microservice.common.model.ResponseModel;
import com.jichuangsi.microservice.common.model.UserInfoForToken;
import com.jichuangsi.school.courseservice.Exception.TeacherCourseServiceException;
import com.jichuangsi.school.courseservice.constant.ResultCode;
import com.jichuangsi.school.courseservice.entity.Course;
import com.jichuangsi.school.courseservice.entity.Question;
import com.jichuangsi.school.courseservice.model.*;
import com.jichuangsi.school.courseservice.model.common.DeleteQueryModel;
import com.jichuangsi.school.courseservice.service.ICourseConsoleService;
import com.jichuangsi.school.courseservice.service.IExamInfoService;
import com.jichuangsi.school.courseservice.service.IUserInfoService;
import com.jichuangsi.school.courseservice.util.DateFormateUtil;
import com.jichuangsi.school.courseservice.util.MappingEntity2ModelConverter;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@RequestMapping("/console")
@RestController
@Api("CourseConsoleController测试开发相关的api")
public class CourseConsoleController {

    @Resource
    private ICourseConsoleService courseConsoleService;

    @Resource
    private IUserInfoService userInfoService;

    @Resource
    private IExamInfoService examInfoService;

    @Value("${com.jichuangsi.school.result.date-format1}")
    private String defaultDateFormat1;

    @Value("${com.jichuangsi.school.result.date-format2}")
    private String defaultDateFormat2;

    @Value("${com.jichuangsi.school.result.page-size}")
    private int defaultPageSize;

    /**
     * {
     * "nanos":0,
     * "time":1371721834000,
     * "minutes":50,
     * "seconds":34,
     * "hours":17,
     * "month":5,
     * "year":113,
     * "timezoneOffset":-480,
     * "day":4,
     * "date":20
     * }
     *
     * @param userInfo
     * @return
     * @throws TeacherCourseServiceException
     */
    //获取老师条件查询课堂列表
    @ApiOperation(value = "根据course.status,course.startTime,sortNum,keyword,page获取老师课堂列表信息", notes = "")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header", name = "accessToken", value = "用户token", required = true, dataType = "String")
    })
    @PostMapping(value = "/getSortList", consumes = "application/json")
    public ResponseModel<PageHolder<ReturnCourse>> getSortList(@ModelAttribute UserInfoForToken userInfo, @RequestBody(required = false) SearchCourseModel searchCourseModel) throws TeacherCourseServiceException {
        /*        searchCourseModel = new SearchCourseModel("","2018-11-23",1,"N",1,3);*/
        if (userInfo == null || searchCourseModel == null) {
            throw new TeacherCourseServiceException(ResultCode.COURSE_QUERY_IS_EMPTY);
        }
        Course course = new Course();
        PageHolder<Course> page = new PageHolder<Course>();
        course.setTeacherId(userInfo.getUserId());
        if (!StringUtils.isEmpty(searchCourseModel.getStatus())) {
            course.setStatus(searchCourseModel.getStatus().getName());
        }
        page.setPageSize(searchCourseModel.getPageSize() > 0 ? searchCourseModel.getPageSize() : defaultPageSize);
        page.setPageNum(searchCourseModel.getPageIndex());
        DateFormateUtil dfu = new DateFormateUtil(defaultDateFormat1);
        page = courseConsoleService.getSortCoursesList(course, page, searchCourseModel.getKeyWord(), searchCourseModel.getSortNum(), dfu.getDateTime(searchCourseModel.getTime()));
        PageHolder<ReturnCourse> pageHolder = new PageHolder<ReturnCourse>();
        pageHolder.setContent(changeReturnCourse(page.getContent()));
        pageHolder.setPageCount(page.getPageCount());
        pageHolder.setPageSize(page.getPageSize());
        pageHolder.setTotal(page.getTotal());
        pageHolder.setPageNum(page.getPageNum());
        return ResponseModel.sucess("", pageHolder);
    }

    //新增备课
    @ApiOperation(value = "教师新增备课", notes = "")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header", name = "accessToken", value = "用户token", required = true, dataType = "String")
    })
    @PostMapping(value = "/saveCourse", consumes = "application/json")
    public ResponseModel<CourseForTeacher> saveCourse(@ModelAttribute UserInfoForToken userInfo, @RequestBody CourseForTeacher courseForTeacher) throws TeacherCourseServiceException {
        if (userInfo == null || courseForTeacher == null) {
            throw new TeacherCourseServiceException(ResultCode.COURSE_QUERY_IS_EMPTY);
        }
        if (courseForTeacher.getCourseId() != null) {
            throw new TeacherCourseServiceException(ResultCode.COURSE_ALREADY_EXISTED);
        }
        courseConsoleService.saveNewCourse(userInfo, courseForTeacher);
        return ResponseModel.sucessWithEmptyData("");
    }

    //判断当前时间是否存在课
    @ApiOperation(value = "新增备课时，判断是否有时间重叠，返回的是string类型", notes = "")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header", name = "accessToken", value = "用户token", required = true, dataType = "String")
    })
    @PostMapping(value = "/varifyClassTime", consumes = "application/json")
    public ResponseModel<IncludeInfo> varifyClassTime(@ModelAttribute UserInfoForToken userInfo, @RequestBody CourseForTeacher courseForTeacher) throws TeacherCourseServiceException {
        if (userInfo == null || courseForTeacher == null) {
            throw new TeacherCourseServiceException(ResultCode.COURSE_QUERY_IS_EMPTY);
        }
        IncludeInfo info = new IncludeInfo();
        courseForTeacher.setTeacherId(userInfo.getUserId());
        info.setResult("none");
        if (courseConsoleService.getCoursesListByTime(courseForTeacher) > 0) {
            info.setResult("include");
        }
        return ResponseModel.sucess("", info);
    }

    //获取添加course页面数据全部班级
    @ApiOperation(value = "新增备课时，添加班级的数据", notes = "")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header", name = "accessToken", value = "用户token", required = true, dataType = "String")
    })
    @GetMapping(value = "/getClass")
    public ResponseModel<EaxmAndClass> getClassList(@ModelAttribute UserInfoForToken userInfo) throws TeacherCourseServiceException {
        if (userInfo == null) {
            throw new TeacherCourseServiceException(ResultCode.COURSE_QUERY_IS_EMPTY);
        }
        EaxmAndClass examAndClass = new EaxmAndClass();
        /*Teacher teacher = teacherCourseService.getTeacher(String teacherId);*///暂时不获取teacher

        /*eaxmAndClass.setsTransferClasses( teacherCourseService.getTeachClass(userInfo.getUserId()) );//暂时全班级数据*/
        examAndClass.setTransferExams(examInfoService.getExamForTeacherById(userInfo.getUserId()));
        examAndClass.setTransferClasses(userInfoService.getClassForTeacherById(userInfo.getUserId()));
        return ResponseModel.sucess("", examAndClass);
    }

    //新建课堂页面的最新创建未开课的新课程
    @ApiOperation(value = "新建课堂页面的最新创建未开课的新课程", notes = "")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header", name = "accessToken", value = "用户token", required = true, dataType = "String")
    })
    @GetMapping(value = "/getNewCourse", consumes = "application/json")
    public ResponseModel<List<ReturnCourse>> getNewCreateCourse(@ModelAttribute UserInfoForToken userInfo) throws TeacherCourseServiceException {
        if (userInfo == null) {
            throw new TeacherCourseServiceException(ResultCode.COURSE_QUERY_IS_EMPTY);
        }
        List<Course> courses = courseConsoleService.getNewCoursesList(userInfo.getUserId());
        return ResponseModel.sucess("", changeReturnCourse(courses));
    }

    //删除新建没有开始的课程
    @ApiOperation(value = "删除新建没有开始的课程", notes = "")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header", name = "accessToken", value = "用户token", required = true, dataType = "String")
    })
    @DeleteMapping(value = "/deleteNewCourse", consumes = "application/json")
    public ResponseModel deleteCourse(@ModelAttribute UserInfoForToken userInfo, @RequestBody CourseForTeacher courseForTeacher) throws TeacherCourseServiceException {
        if (userInfo == null || courseForTeacher == null || StringUtils.isEmpty(courseForTeacher.getCourseId())) {
            throw new TeacherCourseServiceException(ResultCode.COURSE_QUERY_IS_EMPTY);
        }
        if (courseConsoleService.getCourseById(courseForTeacher.getCourseId()) == null) {
            throw new TeacherCourseServiceException(ResultCode.COURSE_NOT_EXISTED);
        }
        Boolean result = courseConsoleService.deleteCourseIsN(courseForTeacher.getCourseId());
        if (!result) {
            throw new TeacherCourseServiceException(ResultCode.COURSE_DELETE_FAIL);
        }
        return ResponseModel.sucessWithEmptyData("");
    }

    //修改新建没有开始的课程
    @ApiOperation(value = "修改新建没有开始的课程", notes = "")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header", name = "accessToken", value = "用户token", required = true, dataType = "String")
    })
    @PostMapping("/updateCourse")
    public ResponseModel updateCourse(@ModelAttribute UserInfoForToken userInfo, @RequestBody CourseForTeacher courseForTeacher) throws TeacherCourseServiceException {
        if (userInfo == null || courseForTeacher == null || StringUtils.isEmpty(courseForTeacher.getCourseId())) {
            throw new TeacherCourseServiceException(ResultCode.PARAM_MISS_MSG);
        }
        courseConsoleService.updateCourseIsN(userInfo, courseForTeacher);
        return ResponseModel.sucessWithEmptyData("");
    }

    @ApiOperation(value = "上传course的ico图标", notes = "")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header", name = "accessToken", value = "用户token", required = true, dataType = "String")
    })
    @PostMapping("/saveCourseIco")
    public ResponseModel<CourseForTeacher> saveCourseIco(@RequestParam MultipartFile file, @ModelAttribute UserInfoForToken userInfo) {
        try {
            return ResponseModel.sucess("", courseConsoleService.uploadIco(userInfo, new CourseFile(file.getOriginalFilename(), file.getContentType(), file.getBytes())));
        } catch (TeacherCourseServiceException e) {
            return ResponseModel.fail("", e.getMessage());
        } catch (IOException e) {
            return ResponseModel.fail("", ResultCode.FILE_UPLOAD_ERROR);
        }
    }

    @ApiOperation(value = "查看course的ico图标", notes = "")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header", name = "accessToken", value = "用户token", required = true, dataType = "String")
    })
    @PostMapping("/getCourseIco")
    public ResponseModel<CourseFile> getCourseIco(@ModelAttribute UserInfoForToken userInfo, @RequestBody CourseForTeacher courseForTeacher) {

        try {
            /*Base64TransferFile base64TransferFile = new Base64TransferFile();
            CourseFile courseFile = courseConsoleService.downIco(userInfo, courseForTeacher.getCoursePic());
            base64TransferFile.setName(courseFile.getName());
            base64TransferFile.setContentType(courseFile.getContentType());
            base64TransferFile.setContent(new String(courseFile.getContent()));*/
            return ResponseModel.sucess("",courseConsoleService.downIco(userInfo, courseForTeacher.getCoursePic()));
        } catch (TeacherCourseServiceException e) {
            return ResponseModel.fail("", e.getMessage());
        }
    }

    @ApiOperation(value = "修改course的ico图标", notes = "")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header", name = "accessToken", value = "用户token", required = true, dataType = "String")
    })
    @PostMapping("/updateCourseIco")
    public ResponseModel updateCourseIco(@RequestParam MultipartFile file,@ModelAttribute UserInfoForToken userInfo,@RequestParam String courseId){
        try {
            courseConsoleService.updateIco(userInfo, new CourseFile(file.getOriginalFilename(), file.getContentType(), file.getBytes()),courseId);
        } catch (TeacherCourseServiceException e) {
            return ResponseModel.fail("",e.getMessage());
        } catch (IOException e) {
            return ResponseModel.fail("",ResultCode.FILE_UPLOAD_ERROR);
        }
        return ResponseModel.sucessWithEmptyData("");
    }

    @ApiOperation(value = "上传course的附件", notes = "")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header", name = "accessToken", value = "用户token", required = true, dataType = "String")
    })
    @PostMapping("/saveCourseAttachment")
    public ResponseModel<Attachment> saveCourseAttachment(@RequestParam MultipartFile file, @ModelAttribute UserInfoForToken userInfo) {
        try {
            return ResponseModel.sucess("", courseConsoleService.uploadAttachment(userInfo, new CourseFile(file.getOriginalFilename(), file.getContentType(), file.getBytes())));
        } catch (TeacherCourseServiceException e) {
            return ResponseModel.fail("", e.getMessage());
        } catch (IOException e) {
            return ResponseModel.fail("", ResultCode.FILE_UPLOAD_ERROR);
        }
    }

    @ApiOperation(value = "删除course的附件", notes = "")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header", name = "accessToken", value = "用户token", required = true, dataType = "String")
    })
    @DeleteMapping("/removeCourseAttachment")
    public ResponseModel removeCourseAttachment(@ModelAttribute UserInfoForToken userInfo, @RequestBody DeleteQueryModel deleteQueryModel) {
        try {
            courseConsoleService.removeCourseAttachment(userInfo, deleteQueryModel);
            return ResponseModel.sucessWithEmptyData("");
        } catch (TeacherCourseServiceException e) {
            return ResponseModel.fail("", e.getMessage());
        }
    }

    private List<QuestionForTeacher> convertQuestionList(List<Question> questions){
        List<QuestionForTeacher> questionForTeachers = new ArrayList<QuestionForTeacher>();
        questions.forEach(question -> {
            questionForTeachers.add(MappingEntity2ModelConverter.ConvertTeacherQuestion(question));
        });
        return questionForTeachers;
    }


    //使course数据带时间
    private List<ReturnCourse> changeReturnCourse(List<Course> olds){
        List<ReturnCourse> rcs = new ArrayList<ReturnCourse>();
        olds.forEach(course -> {
            CourseForTeacher courseForTeacher = MappingEntity2ModelConverter.ConvertTeacherCourse(course);
            courseForTeacher.getQuestions().addAll(convertQuestionList(courseConsoleService.getQuestionList(course.getQuestionIds())));
            courseForTeacher.getStudents().addAll(userInfoService.getStudentsForClassById(course.getClassId()));
            ReturnCourse rc = new ReturnCourse();
            rc.setCourseForTeacher(courseForTeacher);
            rc.setBeginTime(new SimpleDateFormat(defaultDateFormat2).format(new Date(course.getStartTime())));
            rc.setGameOver(new SimpleDateFormat(defaultDateFormat2).format(new Date(course.getEndTime())));
            rc.setDistanceTime((new Date().getTime()<courseForTeacher.getCourseStartTime()?
                    courseForTeacher.getCourseStartTime()-new Date().getTime():0)/1000);
            rcs.add(rc);
        });
        return rcs;
    }
}
