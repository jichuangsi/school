/**
 * 课堂交互老师端
 */
package com.jichuangsi.school.websocket.controller;

import javax.annotation.Resource;

import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.messaging.simp.annotation.SendToUser;
import org.springframework.messaging.simp.annotation.SubscribeMapping;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSONObject;
import com.jichuangsi.school.websocket.constant.ResultCode;
import com.jichuangsi.school.websocket.model.QuestionForPublish;
import com.jichuangsi.school.websocket.model.ResponseModel;

@RestController
public class ClassInteractionForTeacherController {

	@Resource
	private SimpMessagingTemplate template;

	// 开始上课
	@MessageMapping("/teacher/course/start/{courseId}")
	@SendToUser(value = "/user/queue/return", broadcast = false)
	public ResponseModel<Object> teacherSend(@DestinationVariable String courseId) {
		//根据courseId找到对应的班级
		String classId = "";
		
		// 通知该班级的学生上课
		template.convertAndSend("/topic/course/student/" + classId,"");
		return ResponseModel.sucess(ResultCode.SUCESS_MSG, null);
	}

	// 订阅某堂课的信息
	@SubscribeMapping("/queue/course/teacher/{baz}")
	public ResponseModel<Object> subCourseForteacher(@DestinationVariable String baz) {
		System.out.println("======老师订阅某堂课的信息:" + baz + "-->Thread id:" + Thread.currentThread().getId());
		return ResponseModel.sucess(ResultCode.SUCESS_MSG, null);
	}

	// 发布题目
	@MessageMapping("/teacher/course/send")
	@SendToUser(value = "/user/queue/return", broadcast = false)
	public ResponseModel<Object> teacherSend(QuestionForPublish questionForPublish) {
		System.out.println("==================teacherSend:" + Thread.currentThread().getId());
		questionForPublish.setContent("以下哪项是对的：A B C D");
		template.convertAndSend("/topic/course/student/" + questionForPublish.getCourseId(),
				JSONObject.toJSONString(ResponseModel.sucess("", questionForPublish)));
		return ResponseModel.sucess(ResultCode.SUCESS_MSG, null);
	}
}
