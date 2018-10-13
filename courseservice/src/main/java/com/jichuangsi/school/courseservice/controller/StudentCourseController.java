package com.jichuangsi.school.courseservice.controller;

import com.jichuangsi.microservice.common.model.ResponseModel;
import com.jichuangsi.microservice.common.model.UserInfoForToken;
import com.jichuangsi.school.courseservice.model.AnswerForStudent;
import com.jichuangsi.school.courseservice.model.CourseForStudent;
import com.jichuangsi.school.courseservice.model.QuestionForStudent;
import com.jichuangsi.school.courseservice.service.ITokenService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/student")
public class StudentCourseController {

	@Resource
	private ITokenService tokenService;

	//获取学生课程列表
	@GetMapping("/getList")
	public ResponseModel<List<CourseForStudent>> getList(@ModelAttribute UserInfoForToken userInfo) {
		return ResponseModel.sucess("",  new ArrayList<CourseForStudent>());
	}

	//获取学生历史课程列表
	@GetMapping("/getHistory")
	public ResponseModel<List<CourseForStudent>> getHistory(@ModelAttribute UserInfoForToken userInfo) {
		return ResponseModel.sucess("",  new ArrayList<CourseForStudent>());
	}

	//获取指定课堂基本信息
	@GetMapping("/getInfo/{courseId}")
	public ResponseModel<String> getInfo(@ModelAttribute UserInfoForToken userInfo, @PathVariable String courseId) {
		return ResponseModel.sucess("",  new CourseForStudent().getCourseInfo());
	}

	//获取指定课堂历史题目列表
	@GetMapping("/getQuestions/{courseId}")
	public ResponseModel<List<QuestionForStudent>> getQuestions(@ModelAttribute UserInfoForToken userInfo, @PathVariable String courseId) {
		return ResponseModel.sucess("",  new CourseForStudent().getQuestions());
	}

	//课堂主观题图片存根
	@PostMapping("/sendSubjectPic")
	public ResponseModel<AnswerForStudent> sendSubjectPic(@ModelAttribute UserInfoForToken userInfo, @RequestBody AnswerForStudent subjectivePic){
		return ResponseModel.sucess("",  new AnswerForStudent());
	}

	//发送学生答案
	@PostMapping("/sendAnswer/{courseId}/{questionId}")
	public ResponseModel<AnswerForStudent> sendAnswer(@ModelAttribute UserInfoForToken userInfo, @PathVariable String courseId,
													  @PathVariable String questionId, @RequestBody AnswerForStudent answer){
		return ResponseModel.sucess("",  new AnswerForStudent());
	}
}
