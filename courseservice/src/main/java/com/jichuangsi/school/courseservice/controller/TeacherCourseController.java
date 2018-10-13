package com.jichuangsi.school.courseservice.controller;

import com.jichuangsi.microservice.common.model.ResponseModel;
import com.jichuangsi.microservice.common.model.UserInfoForToken;
import com.jichuangsi.school.courseservice.Exception.TeacherCourseServiceException;
import com.jichuangsi.school.courseservice.model.*;
import com.jichuangsi.school.courseservice.service.ITeacherCourseService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/teacher")
public class TeacherCourseController {

    @Resource
    private ITeacherCourseService teacherCourseService;

    //获取老师课堂列表
    @GetMapping("/getList")
    public ResponseModel<List<CourseForTeacher>> getList(@ModelAttribute UserInfoForToken userInfo) {
        return ResponseModel.sucess("",  new ArrayList<CourseForTeacher>());
    }

    //获取老师历史课堂列表
    @GetMapping("/getHistory")
    public ResponseModel<List<CourseForTeacher>> getHistory(@ModelAttribute UserInfoForToken userInfo) {
        return ResponseModel.sucess("",  new ArrayList<CourseForTeacher>());
    }

    //查询课堂列表
    @PostMapping("/query")
    public ResponseModel<List<CourseForTeacher>> query(@ModelAttribute UserInfoForToken userInfo,
                                                       @RequestBody CourseForTeacher courseForQuery) {
        return ResponseModel.sucess("",  new ArrayList<CourseForTeacher>());
    }

    //获取指定课堂
    @GetMapping("/getCourse/{courseId}")
    public ResponseModel<CourseForTeacher> getCourse(@ModelAttribute UserInfoForToken userInfo, @PathVariable String courseId) {
        return ResponseModel.sucess("",  new CourseForTeacher());
    }

    //获取指定课堂题目
    @GetMapping("/getQuestion/{courseId}/{questionId}")
    public ResponseModel<QuestionForTeacher> getQuestion(@ModelAttribute UserInfoForToken userInfo,
                                                         @PathVariable String courseId, @PathVariable String questionId) {
        return ResponseModel.sucess("",  new CourseForTeacher().getQuestions().get(0));
    }

    //获取指定学生答案
    @GetMapping("/getAnswer/{courseId}/{questionId}/{studentId}")
    public ResponseModel<AnswerForStudent> getAnswer(@ModelAttribute UserInfoForToken userInfo,
                                                     @PathVariable String courseId, @PathVariable String questionId,
                                                     @PathVariable String studentId) {
        return ResponseModel.sucess("",  new CourseForTeacher().getQuestions().get(0).getAnswerForStudent().get(0));
    }

    //课堂主观题图片存根
    @PostMapping("/sendSubjectPic")
    public ResponseModel<AnswerForTeacher> sendSubjectPic(@ModelAttribute UserInfoForToken userInfo, @RequestBody AnswerForTeacher subjectivePic){
        return ResponseModel.sucess("",  new AnswerForTeacher());
    }

    //发送老师批改
    @PostMapping("/sendAnswer/{courseId}/{questionId}")
    public ResponseModel<AnswerForTeacher> sendAnswer(@ModelAttribute UserInfoForToken userInfo, @PathVariable String courseId,
                                                      @PathVariable String questionId,  @RequestBody AnswerForTeacher revise){
        return ResponseModel.sucess("",  new AnswerForTeacher());
    }

    //更新课堂状态
    @PutMapping("/updateCourseStatus")
    public ResponseModel<CourseForTeacher> updateCourseStatus(@ModelAttribute UserInfoForToken userInfo, @RequestBody CourseForTeacher courseStatus){
        //mqService.send(courseStatus);
        return ResponseModel.sucess("",  new CourseForTeacher());
    }

    //开始课堂
    @PostMapping("/courseStart")
    public ResponseModel<CourseForTeacher> courseStart(@ModelAttribute UserInfoForToken userInfo, @RequestBody CourseForTeacher course) throws TeacherCourseServiceException{
        teacherCourseService.startCourse(course);
        return ResponseModel.sucess("", null);
    }

    //更新问题状态
    @PutMapping("/updateQuestionStatus/{courseId}")
    public ResponseModel<QuestionForTeacher> updateQuestionStatus(@ModelAttribute UserInfoForToken userInfo,
                                                                @PathVariable String courseId, @RequestBody QuestionForTeacher questionStatus){
        return ResponseModel.sucess("",  new QuestionForTeacher());
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
        return ResponseModel.sucess("",  null);
    }

    //编辑指定课堂的问题集
    @PostMapping("/editQuestions/{courseId}")
    public ResponseModel<List<QuestionForTeacher>> editQuestions(@ModelAttribute UserInfoForToken userInfo, @PathVariable String courseId,
                                                        @RequestBody List<QuestionForTeacher> questions) throws TeacherCourseServiceException{
        return ResponseModel.sucess("",  teacherCourseService.saveQuestions(courseId, questions));
    }
}
