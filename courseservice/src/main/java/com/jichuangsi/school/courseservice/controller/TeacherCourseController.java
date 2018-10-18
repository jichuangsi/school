package com.jichuangsi.school.courseservice.controller;

import com.jichuangsi.microservice.common.model.ResponseModel;
import com.jichuangsi.microservice.common.model.UserInfoForToken;
import com.jichuangsi.school.courseservice.Exception.TeacherCourseServiceException;
import com.jichuangsi.school.courseservice.constant.ResultCode;
import com.jichuangsi.school.courseservice.model.*;
import com.jichuangsi.school.courseservice.service.ITeacherCourseService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/teacher")
public class TeacherCourseController {

    @Resource
    private ITeacherCourseService teacherCourseService;

    //获取老师课堂列表
    @GetMapping("/getList")
    public ResponseModel<List<CourseForTeacher>> getList(@ModelAttribute UserInfoForToken userInfo) throws TeacherCourseServiceException{

        return ResponseModel.sucess("",  teacherCourseService.getCoursesList(userInfo.getUserId()));
    }

    //获取老师历史课堂列表
    @GetMapping("/getHistory")
    public ResponseModel<List<CourseForTeacher>> getHistory(@ModelAttribute UserInfoForToken userInfo) throws TeacherCourseServiceException{

        return ResponseModel.sucess("",  teacherCourseService.getHistoryCoursesList(userInfo.getUserId()));
    }

    //查询课堂列表
    @PostMapping("/query")
    public ResponseModel<List<CourseForTeacher>> query(@ModelAttribute UserInfoForToken userInfo,
                                                       @RequestBody CourseForTeacher courseForQuery) throws TeacherCourseServiceException{
        return ResponseModel.sucess("",  teacherCourseService.queryCoursesList(userInfo.getUserId(), courseForQuery));
    }

    //获取指定课堂
    @GetMapping("/getCourse/{courseId}")
    public ResponseModel<CourseForTeacher> getCourse(@ModelAttribute UserInfoForToken userInfo, @PathVariable String courseId) throws TeacherCourseServiceException{

        return ResponseModel.sucess("",  teacherCourseService.getParticularCourse(userInfo.getUserId(), courseId));
    }

    //获取指定课堂的题目列表
    @GetMapping("/getQuestions/{courseId}")
    public ResponseModel<List<QuestionForTeacher>> getQuestions(@ModelAttribute UserInfoForToken userInfo, @PathVariable String courseId) throws TeacherCourseServiceException{

        return ResponseModel.sucess("",  teacherCourseService.getQuestionsInParticularCourse(userInfo.getUserId(), courseId));
    }

    //获取指定课堂题目
    @GetMapping("/getQuestion/{questionId}")
    public ResponseModel<QuestionForTeacher> getQuestion(@ModelAttribute UserInfoForToken userInfo, @PathVariable String questionId) throws TeacherCourseServiceException{

        return ResponseModel.sucess("",  teacherCourseService.getParticularQuestion(questionId));
    }

    //获取指定题目的答案列表
    @GetMapping("/getAnswers/{questionId}")
    public ResponseModel<List<AnswerForStudent>> getAnswers(@ModelAttribute UserInfoForToken userInfo, @PathVariable String questionId) throws TeacherCourseServiceException{

        return ResponseModel.sucess("", teacherCourseService.getAnswersInPaticularCourse(questionId));
    }

    //获取指定学生答案
    @GetMapping("/getAnswer/{questionId}/{studentId}")
    public ResponseModel<AnswerForStudent> getAnswer(@ModelAttribute UserInfoForToken userInfo, @PathVariable String questionId,
                                                     @PathVariable String studentId) throws TeacherCourseServiceException{

        return ResponseModel.sucess("",  teacherCourseService.getParticularAnswer(questionId, studentId));
    }

    //课堂主观题图片存根
    @PostMapping("/sendSubjectPic")
    public ResponseModel<AnswerForTeacher> sendSubjectPic(@RequestParam MultipartFile file, @ModelAttribute UserInfoForToken userInfo) throws TeacherCourseServiceException{
        try{
            return ResponseModel.sucess("",  teacherCourseService.uploadTeacherSubjectPic(userInfo.getUserId(), new CourseFile(file.getOriginalFilename(), file.getContentType(), file.getBytes())));
        }catch (IOException ioExp){
            throw new TeacherCourseServiceException(ResultCode.FILE_UPLOAD_ERROR);
        }
    }

    @PostMapping("/getSubjectPic")
    public ResponseModel<CourseFile> getSubjectPic(@ModelAttribute UserInfoForToken userInfo, @RequestBody AnswerForTeacher revise) throws TeacherCourseServiceException{

        return  ResponseModel.sucess("", teacherCourseService.downloadTeacherSubjectPic(userInfo.getUserId(), revise.getStubForSubjective()));
    }

    @PutMapping("/remoreSubjectPic")
    public ResponseModel<CourseFile> remoreSubjectPic(@ModelAttribute UserInfoForToken userInfo, @RequestBody AnswerForTeacher revise) throws TeacherCourseServiceException{
        teacherCourseService.deleteTeacherSubjectPic(userInfo.getUserId(), revise.getStubForSubjective());
        return ResponseModel.sucessWithEmptyData("");
    }

    //发送老师批改
    @PostMapping("/sendAnswer/{questionId}/{studentAnswerId}")
    public ResponseModel<AnswerForTeacher> sendAnswer(@ModelAttribute UserInfoForToken userInfo,
                                                      @PathVariable String questionId,  @PathVariable String studentAnswerId,
                                                      @RequestBody AnswerForTeacher revise) throws TeacherCourseServiceException{
        teacherCourseService.saveTeacherAnswer(userInfo.getUserId(), questionId, studentAnswerId, revise);
        return ResponseModel.sucessWithEmptyData("");
    }

    //更新课堂状态
    @PutMapping("/changeCourseStatus")
    public ResponseModel<CourseForTeacher> changeCourseStatus(@ModelAttribute UserInfoForToken userInfo, @RequestBody CourseForTeacher courseStatus) throws TeacherCourseServiceException{
        teacherCourseService.updateParticularCourseStatus(courseStatus);
        return ResponseModel.sucessWithEmptyData("");
    }

    //开始课堂
    @PostMapping("/courseStart")
    public ResponseModel<CourseForTeacher> courseStart(@ModelAttribute UserInfoForToken userInfo, @RequestBody CourseForTeacher course) throws TeacherCourseServiceException{
        teacherCourseService.startCourse(course);
        return ResponseModel.sucessWithEmptyData("");
    }

    //更新问题状态
    @PutMapping("/changeQuestionStatus")
    public ResponseModel<QuestionForTeacher> changeQuestionStatus(@ModelAttribute UserInfoForToken userInfo, @RequestBody QuestionForTeacher questionStatus) throws TeacherCourseServiceException{
        teacherCourseService.updateParticularQuestionStatus(questionStatus);
        return ResponseModel.sucessWithEmptyData("");
    }

    //编辑课堂信息
    @PostMapping("/editCourse")
    public ResponseModel<CourseForTeacher> editCourse(@ModelAttribute UserInfoForToken userInfo, @RequestBody CourseForTeacher course){

        return ResponseModel.sucess("",  teacherCourseService.saveCourse(course));
    }

    //移除课堂
    @PutMapping("/removeCourse")
    public ResponseModel<CourseForTeacher> removeCourse(@ModelAttribute UserInfoForToken userInfo, @RequestBody CourseForTeacher course){
        teacherCourseService.deleteCourse(course);
        return ResponseModel.sucessWithEmptyData("");
    }

    //编辑指定课堂的问题集
    @PostMapping("/editQuestions/{courseId}")
    public ResponseModel<List<QuestionForTeacher>> editQuestions(@ModelAttribute UserInfoForToken userInfo, @PathVariable String courseId,
                                                        @RequestBody List<QuestionForTeacher> questions) throws TeacherCourseServiceException{
        return ResponseModel.sucess("",  teacherCourseService.saveQuestions(courseId, questions));
    }
}
