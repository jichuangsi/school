/**
 * 课堂交互老师端
 */
package com.jichuangsi.school.classinteraction.websocket.controller;

import javax.annotation.Resource;

import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.messaging.simp.annotation.SubscribeMapping;
import org.springframework.web.bind.annotation.RestController;

import com.jichuangsi.microservice.common.constant.ResultCode;
import com.jichuangsi.microservice.common.model.ResponseModel;

@RestController
public class ClassInteractionForTeacherController {

	@Resource
	private SimpMessagingTemplate template;

	// 订阅某堂课统计并数据更新的消息
//	@SubscribeMapping("${custom.ws.sub.teacher.csChangePre}{courseId}")
//	public ResponseModel<Object> csChange(@DestinationVariable String courseId) {
//		return ResponseModel.sucess(ResultCode.SUCESS_MSG, null);
//	}
	
	// 订阅某堂课题目统计更新的消息
	@SubscribeMapping("${custom.ws.sub.teacher.qcChangePre}{courseId}")
	public ResponseModel<Object> cqsChange(@DestinationVariable String courseId) {
		return ResponseModel.sucess(ResultCode.SUCESS_MSG, null);
	}
	
//	// 发布题目
//	@MessageMapping("/teacher/course/send")
//	@SendToUser(value = "/user/queue/return", broadcast = false)
//	public ResponseModel<Object> teacherSend(QuestionForPublish questionForPublish) {
//		System.out.println("==================teacherSend:" + Thread.currentThread().getId());
//		questionForPublish.setContent("以下哪项是对的：A B C D");
//		template.convertAndSend("/topic/course/student/" + questionForPublish.getCourseId(),
//				JSONObject.toJSONString(ResponseModel.sucess("", questionForPublish)));
//		return ResponseModel.sucess(ResultCode.SUCESS_MSG, null);
//	}



}
