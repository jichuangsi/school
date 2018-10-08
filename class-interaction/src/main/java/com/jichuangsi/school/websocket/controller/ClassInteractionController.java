package com.jichuangsi.school.websocket.controller;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.messaging.simp.annotation.SendToUser;
import org.springframework.messaging.simp.annotation.SubscribeMapping;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSONObject;
import com.jichuangsi.school.websocket.model.QuestionForAnswer;
import com.jichuangsi.school.websocket.model.QuestionForPublish;
import com.jichuangsi.school.websocket.model.ResponseModel;

@RestController
public class ClassInteractionController {

	@Resource
	private SimpMessagingTemplate template;

	// 订阅某个班的信息——学生
	@SubscribeMapping("/topic/group/student/{baz}")
	public ResponseModel<Object> subGroupForStudent(@DestinationVariable String baz) {
		System.out.println("======学生订阅某个班的信息:" + baz + "-->Thread id:" + Thread.currentThread().getId());
		return ResponseModel.sucess("已订阅班级信息", null);
	}

	// 订阅某堂课的信息——学生
	@SubscribeMapping("/topic/course/student/{baz}")
	public ResponseModel<Object> subCourseForStudent(@DestinationVariable String baz,
			@Header Map<String, List<String>> nativeHeaders) {
		System.out.println("======学生订阅某堂课的信息:" + baz + ":" + nativeHeaders.get("userId").get(0) + "-->Thread id:"
				+ Thread.currentThread().getId());
		template.convertAndSend("/queue/course/teacher/" + baz, JSONObject
				.toJSONString(ResponseModel.sucess("", nativeHeaders.get("userId").get(0) + "加入了" + baz + "课堂")));
		return ResponseModel.sucess("加入课堂成功", null);
	}

	// 学生回答问题
	@MessageMapping("/student/course/answer")
	@SendToUser(value = "/user/queue/return", broadcast = false)
	public ResponseModel<Object> studentAnswer(QuestionForAnswer courseQuestion) {
		System.out.println("==================studentAnswer:" + Thread.currentThread().getId() + ";courseQuestion:"
				+ courseQuestion.getAnswer());
		template.convertAndSend("/queue/course/teacher/" + courseQuestion.getCourseId(), JSONObject.toJSONString(
				ResponseModel.sucess("", courseQuestion.getUserId() + "回答了" + courseQuestion.getCourseId() + "课堂的题目")));

		return ResponseModel.sucess("回答完成", null);
	}

	// 订阅某堂课的信息——老师
	@SubscribeMapping("/queue/course/teacher/{baz}")
	public ResponseModel<Object> subCourseForteacher(@DestinationVariable String baz) {
		System.out.println("======老师订阅某堂课的信息:" + baz + "-->Thread id:" + Thread.currentThread().getId());
		return ResponseModel.sucess("", null);
	}

	// 老师发布题目
	@MessageMapping("/teacher/course/send")
	@SendToUser(value = "/user/queue/return", broadcast = false)
	public ResponseModel<Object> teacherSend(QuestionForPublish questionForPublish) {
		System.out.println("==================teacherSend:" + Thread.currentThread().getId());
		questionForPublish.setContent("以下哪项是对的：A B C D");
		template.convertAndSend("/topic/course/student/" + questionForPublish.getCourseId(),
				JSONObject.toJSONString(ResponseModel.sucess("", questionForPublish)));
		return ResponseModel.sucess("发布完成", null);
	}
}
