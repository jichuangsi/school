/**
 * 课堂交互学生端
 */
package com.jichuangsi.school.classinteraction.websocket.controller;

import javax.annotation.Resource;

import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.messaging.simp.annotation.SubscribeMapping;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.web.bind.annotation.RestController;

import com.jichuangsi.microservice.common.constant.ResultCode;
import com.jichuangsi.microservice.common.model.ResponseModel;


/**
 * @author huangjiajun
 *
 */

@RestController
public class ClassInteractionForStudentController {
	@Resource
	private SimpMessagingTemplate template;

	// 订阅某个班级的信息
	@SubscribeMapping("${custom.ws.sub.student.classInfoPre}{classId}")
	public ResponseModel<Object> subClassInfo(@DestinationVariable String classId) {
		return ResponseModel.sucess(ResultCode.SUCESS_MSG, null);
	}

	// 订阅某堂课的信息（包含发布题目和终止作答，两者都属于课堂信息）
	@SubscribeMapping("${custom.ws.sub.student.courseIntercationPre}{courseId}")
	public ResponseModel<Object> subCourseForStudent(@DestinationVariable String courseId, StompHeaderAccessor sha) {
		return ResponseModel.sucess(ResultCode.SUCESS_MSG, null);
	}

	// 订阅某堂课新发布题目的信息
//	@SubscribeMapping("/topic/course/student/{courseId}/newqusetion")
//	public ResponseModel<Object> subCourseForStudent(@DestinationVariable String baz,
//			@Header Map<String, List<String>> nativeHeaders) {
//		System.out.println("======学生订阅某堂课的信息:" + baz + ":" + nativeHeaders.get("userId").get(0) + "-->Thread id:"
//				+ Thread.currentThread().getId());
//		template.convertAndSend("/queue/course/teacher/" + baz, JSONObject
//				.toJSONString(ResponseModel.sucess("", nativeHeaders.get("userId").get(0) + "加入了" + baz + "课堂")));
//		return ResponseModel.sucess("加入课堂成功", null);
//	}

	// 学生回答问题
//	@MessageMapping("/student/course/answer")
//	@SendToUser(value = "/user/queue/return", broadcast = false)
//	public ResponseModel<Object> studentAnswer(QuestionForAnswer courseQuestion) {
//		System.out.println("==================studentAnswer:" + Thread.currentThread().getId() + ";courseQuestion:"
//				+ courseQuestion.getAnswer());
//		template.convertAndSend("/queue/course/teacher/" + courseQuestion.getCourseId(), JSONObject.toJSONString(
//				ResponseModel.sucess("", courseQuestion.getUserId() + "回答了" + courseQuestion.getCourseId() + "课堂的题目")));
//
//		return ResponseModel.sucess("回答完成", null);
//	}

}
