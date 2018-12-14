package com.jichuangsi.school.courseservice.controller;

import com.alibaba.fastjson.JSONObject;
import com.jichuangsi.microservice.common.model.ResponseModel;
import com.jichuangsi.microservice.common.model.UserInfoForToken;
import com.jichuangsi.school.courseservice.Exception.TeacherCourseServiceException;
import com.jichuangsi.school.courseservice.constant.ResultCode;
import com.jichuangsi.school.courseservice.model.*;
import com.jichuangsi.school.courseservice.service.ITeacherCourseService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/teacher")
@Api("TeacherCourseController相关的api")
public class TeacherCourseController {

    @Resource
    private ITeacherCourseService teacherCourseService;

    //获取老师课堂列表
    @ApiOperation(value = "根据用户id获取老师课堂列表信息", notes = "")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header", name = "accessToken", value = "用户token", required = true, dataType = "String")})
    @GetMapping("/getList")
    public ResponseModel<List<CourseForTeacher>> getList(@ModelAttribute UserInfoForToken userInfo) throws TeacherCourseServiceException{

        return ResponseModel.sucess("",  teacherCourseService.getCoursesList(userInfo));
    }

    //获取老师历史课堂列表
    @ApiOperation(value = "根据用户id获取老师历史课堂列表信息", notes = "")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header", name = "accessToken", value = "用户token", required = true, dataType = "String")})
    @PostMapping("/getHistory")
    public ResponseModel<PageHolder<CourseForTeacher>> getHistory(@ModelAttribute UserInfoForToken userInfo,
                                                            @RequestBody CourseForTeacher pageInform) throws TeacherCourseServiceException{

        return ResponseModel.sucess("",  teacherCourseService.getHistoryCoursesList(userInfo, pageInform));
    }

    //查询课堂列表
    @ApiOperation(value = "根据用户id和查询条件获取老师课堂列表信息", notes = "")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header", name = "accessToken", value = "用户token", required = true, dataType = "String")})
    @PostMapping("/query")
    public ResponseModel<List<CourseForTeacher>> query(@ModelAttribute UserInfoForToken userInfo,
                                                       @RequestBody CourseForTeacher courseForQuery) throws TeacherCourseServiceException{
        return ResponseModel.sucess("",  teacherCourseService.queryCoursesList(userInfo, courseForQuery));
    }

    //获取指定课堂
    @ApiOperation(value = "根据课堂id查询课堂信息", notes = "")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header", name = "accessToken", value = "用户token", required = true, dataType = "String"),
            @ApiImplicitParam(paramType = "path", name = "courseId", value = "课堂ID", required = true, dataType = "String") })
    @GetMapping("/getCourse/{courseId}")
    public ResponseModel<CourseForTeacher> getCourse(@ModelAttribute UserInfoForToken userInfo, @PathVariable String courseId) throws TeacherCourseServiceException{

        return ResponseModel.sucess("",  teacherCourseService.getParticularCourse(userInfo, courseId));
    }

    //获取指定课堂的题目列表
    @ApiOperation(value = "根据课堂id查询课堂题目列表", notes = "")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header", name = "accessToken", value = "用户token", required = true, dataType = "String"),
            @ApiImplicitParam(paramType = "path", name = "courseId", value = "课堂ID", required = true, dataType = "String") })
    @GetMapping("/getQuestions/{courseId}")
    public ResponseModel<List<QuestionForTeacher>> getQuestions(@ModelAttribute UserInfoForToken userInfo, @PathVariable String courseId) throws TeacherCourseServiceException{

        return ResponseModel.sucess("",  teacherCourseService.getQuestionsInParticularCourse(userInfo, courseId));
    }

    //获取指定课堂题目
    @ApiOperation(value = "根据问题id查询问题信息", notes = "")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header", name = "accessToken", value = "用户token", required = true, dataType = "String"),
            @ApiImplicitParam(paramType = "path", name = "questionId", value = "问题ID", required = true, dataType = "String") })
    @GetMapping("/getQuestion/{questionId}")
    public ResponseModel<QuestionForTeacher> getQuestion(@ModelAttribute UserInfoForToken userInfo, @PathVariable String questionId) throws TeacherCourseServiceException{

        return ResponseModel.sucess("",  teacherCourseService.getParticularQuestion(userInfo, questionId));
    }

    //获取指定题目的答案列表
    @ApiOperation(value = "根据问题id查询答案列表信息", notes = "")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header", name = "accessToken", value = "用户token", required = true, dataType = "String"),
            @ApiImplicitParam(paramType = "path", name = "questionId", value = "问题ID", required = true, dataType = "String") })
    @GetMapping("/getAnswers/{questionId}")
    public ResponseModel<List<AnswerForStudent>> getAnswers(@ModelAttribute UserInfoForToken userInfo, @PathVariable String questionId) throws TeacherCourseServiceException{

        return ResponseModel.sucess("", teacherCourseService.getAnswersInPaticularCourse(questionId));
    }

    //获取指定学生答案
    @ApiOperation(value = "根据问题id和学生id查询答案信息", notes = "")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header", name = "accessToken", value = "用户token", required = true, dataType = "String"),
            @ApiImplicitParam(paramType = "path", name = "questionId", value = "问题ID", required = true, dataType = "String"),
            @ApiImplicitParam(paramType = "path", name = "studentId", value = "学生ID", required = true, dataType = "String") })
    @GetMapping("/getAnswer/{questionId}/{studentId}")
    public ResponseModel<AnswerForStudent> getAnswer(@ModelAttribute UserInfoForToken userInfo, @PathVariable String questionId,
                                                     @PathVariable String studentId) throws TeacherCourseServiceException{

        return ResponseModel.sucess("",  teacherCourseService.getParticularAnswer(questionId, studentId));
    }

    //课堂主观题图片存根
    @ApiOperation(value = "根据老师id保存上传的文件", notes = "")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header", name = "accessToken", value = "用户token", required = true, dataType = "String")})
    @PostMapping("/sendSubjectPicByFile")
    public ResponseModel<AnswerForTeacher> sendSubjectPicByFile(@RequestParam MultipartFile file, @ModelAttribute UserInfoForToken userInfo) throws TeacherCourseServiceException{
        try{
            return ResponseModel.sucess("",  teacherCourseService.uploadTeacherSubjectPic(userInfo, new CourseFile(file.getOriginalFilename(), file.getContentType(), file.getBytes())));
        }catch (IOException ioExp){
            throw new TeacherCourseServiceException(ResultCode.FILE_UPLOAD_ERROR);
        }
    }

    @ApiOperation(value = "根据老师id保存上传的文件", notes = "")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header", name = "accessToken", value = "用户token", required = true, dataType = "String")})
    @PostMapping("/sendSubjectPicByString")
    public ResponseModel<AnswerForTeacher> sendSubjectPicByString(@ModelAttribute UserInfoForToken userInfo, @RequestBody Base64TransferFile file) throws TeacherCourseServiceException{
        return ResponseModel.sucess("",  teacherCourseService.uploadTeacherSubjectPic(userInfo, new CourseFile(file.getName(), file.getContentType(), file.getContent().getBytes())));
    }

    //获取指定文件名的手寫板輸入图片
    @ApiOperation(value = "根据老师id和文件名下载指定的手寫板輸入图片", notes = "")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header", name = "accessToken", value = "用户token", required = true, dataType = "String")})
    @PostMapping("/getSubjectPic")
    public ResponseModel<Base64TransferFile> getSubjectPic(@ModelAttribute UserInfoForToken userInfo, @RequestBody AnswerForTeacher revise) throws TeacherCourseServiceException{
        Base64TransferFile base64TransferFile = new Base64TransferFile();
        CourseFile courseFile =  teacherCourseService.downloadTeacherSubjectPic(userInfo, revise.getStubForSubjective());
        base64TransferFile.setName(courseFile.getName());
        base64TransferFile.setContentType(courseFile.getContentType());
        base64TransferFile.setContent(new String(courseFile.getContent()));
        return  ResponseModel.sucess("", base64TransferFile);
    }

    //获取指定文件名的上傳图片
    @ApiOperation(value = "根据老师id和文件名下载指定的上傳图片", notes = "")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header", name = "accessToken", value = "用户token", required = true, dataType = "String")})
    @PostMapping("/getQuestionPic")
    public ResponseModel<CourseFile> getQuestionPic(@ModelAttribute UserInfoForToken userInfo, @RequestBody AnswerForTeacher questionPic) throws TeacherCourseServiceException{
        return  ResponseModel.sucess("", teacherCourseService.downloadTeacherSubjectPic(userInfo, questionPic.getStubForSubjective()));
    }

    //删除指定文件名图片
    @ApiOperation(value = "根据老师id和文件名删除指定文件", notes = "")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header", name = "accessToken", value = "用户token", required = true, dataType = "String")})
    @DeleteMapping("/remoreSubjectPic")
    public ResponseModel<CourseFile> remoreSubjectPic(@ModelAttribute UserInfoForToken userInfo, @RequestBody AnswerForTeacher revise) throws TeacherCourseServiceException{
        teacherCourseService.deleteTeacherSubjectPic(userInfo, revise.getStubForSubjective());
        return ResponseModel.sucessWithEmptyData("");
    }

    //发送老师批改
    @ApiOperation(value = "根据老师id，问题id和学生答案id保存老师的批改", notes = "")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header", name = "accessToken", value = "用户token", required = true, dataType = "String"),
            @ApiImplicitParam(paramType = "path", name = "questionId", value = "问题ID", required = true, dataType = "String"),
            @ApiImplicitParam(paramType = "path", name = "studentAnswerId", value = "学生答案ID", required = true, dataType = "String") })
    @PostMapping("/sendAnswer/{questionId}/{studentAnswerId}")
    public ResponseModel<AnswerForTeacher> sendAnswer(@ModelAttribute UserInfoForToken userInfo,
                                                      @PathVariable String questionId,  @PathVariable String studentAnswerId,
                                                      @RequestBody AnswerForTeacher revise) throws TeacherCourseServiceException{
        teacherCourseService.saveTeacherAnswer(userInfo, questionId, studentAnswerId, revise);
        return ResponseModel.sucessWithEmptyData("");
    }

    //更新课堂状态
    @ApiOperation(value = "根据课堂id结束课堂", notes = "")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header", name = "accessToken", value = "用户token", required = true, dataType = "String")})
    @PostMapping("/courseEnd/{courseId}")
    public ResponseModel<CourseForTeacher> courseEnd(@ModelAttribute UserInfoForToken userInfo, @PathVariable String courseId) throws TeacherCourseServiceException{
        teacherCourseService.endCourse(courseId);
        return ResponseModel.sucessWithEmptyData("");
    }

    //开始课堂
    @ApiOperation(value = "根据课堂id开始上课", notes = "")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header", name = "accessToken", value = "用户token", required = true, dataType = "String")})
    @PostMapping("/courseStart/{courseId}")
    public ResponseModel<CourseForTeacher> courseStart(@ModelAttribute UserInfoForToken userInfo, @PathVariable String courseId) throws TeacherCourseServiceException{
        teacherCourseService.startCourse(courseId);
        return ResponseModel.sucessWithEmptyData("");
    }

    @ApiOperation(value = "根据id发布问题", notes = "")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header", name = "accessToken", value = "用户token", required = true, dataType = "String")})
    @PostMapping("/questionPublish/{courseId}/{questionId}")
    public ResponseModel<QuestionForTeacher> questionPublish(@ModelAttribute UserInfoForToken userInfo, @PathVariable String courseId, @PathVariable String questionId) throws TeacherCourseServiceException{
        teacherCourseService.publishQuestion(courseId, questionId);
        return ResponseModel.sucessWithEmptyData("");
    }

    @ApiOperation(value = "根据id终止问题", notes = "")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header", name = "accessToken", value = "用户token", required = true, dataType = "String")})
    @PostMapping("/questionTerminate/{courseId}/{questionId}")
    public ResponseModel<QuestionForTeacher> questionTerminate(@ModelAttribute UserInfoForToken userInfo, @PathVariable String courseId, @PathVariable String questionId) throws TeacherCourseServiceException{
        teacherCourseService.terminateQuestion(courseId, questionId);
        return ResponseModel.sucessWithEmptyData("");
    }

    //分享老师批改
    @ApiOperation(value = "根据老师id，问题id分享老师的批改", notes = "")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header", name = "accessToken", value = "用户token", required = true, dataType = "String"),
            @ApiImplicitParam(paramType = "path", name = "questionId", value = "问题ID", required = true, dataType = "String")})
    @PostMapping("/shareAnswer/{questionId}/{studentAnswerId}")
    public ResponseModel shareAnswer(@ModelAttribute UserInfoForToken userInfo,
                                                      @PathVariable String questionId,  @PathVariable String studentAnswerId,
                                                      @RequestBody AnswerForTeacher revise) throws TeacherCourseServiceException{
        teacherCourseService.shareTeacherAnswer(userInfo, questionId, studentAnswerId, revise);
        return ResponseModel.sucessWithEmptyData("");
    }

    //更新问题状态
    @ApiOperation(value = "根据问题id更新问题状态", notes = "")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header", name = "accessToken", value = "用户token", required = true, dataType = "String")})
    @PostMapping("/changeQuestionStatus")
    public ResponseModel<QuestionForTeacher> changeQuestionStatus(@ModelAttribute UserInfoForToken userInfo, @RequestBody QuestionForTeacher questionStatus) throws TeacherCourseServiceException{
        teacherCourseService.updateParticularQuestionStatus(questionStatus);
        return ResponseModel.sucessWithEmptyData("");
    }

    //编辑课堂信息
    @ApiOperation(value = "保存课堂信息", notes = "")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header", name = "accessToken", value = "用户token", required = true, dataType = "String")})
    @PostMapping("/editCourse")
    public ResponseModel<CourseForTeacher> editCourse(@ModelAttribute UserInfoForToken userInfo, @RequestBody CourseForTeacher course){

        return ResponseModel.sucess("",  teacherCourseService.saveCourse(userInfo, course));
    }

    //移除课堂
    @ApiOperation(value = "删除课堂信息", notes = "")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header", name = "accessToken", value = "用户token", required = true, dataType = "String")})
    @DeleteMapping("/removeCourse")
    public ResponseModel<CourseForTeacher> removeCourse(@ModelAttribute UserInfoForToken userInfo, @RequestBody CourseForTeacher course){
        teacherCourseService.deleteCourse(userInfo, course);
        return ResponseModel.sucessWithEmptyData("");
    }

    //编辑指定课堂的问题集
    @ApiOperation(value = "根据课堂id编辑的问题集", notes = "")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header", name = "accessToken", value = "用户token", required = true, dataType = "String"),
            @ApiImplicitParam(paramType = "path", name = "courseId", value = "课堂ID", required = true, dataType = "String")})
    @PostMapping("/editQuestions/{courseId}")
    public ResponseModel<List<QuestionForTeacher>> editQuestions(@ModelAttribute UserInfoForToken userInfo, @PathVariable String courseId,
                                                        @RequestBody List<QuestionForTeacher> questions) throws TeacherCourseServiceException{
        return ResponseModel.sucess("",  teacherCourseService.saveQuestions(courseId, questions));
    }
}
