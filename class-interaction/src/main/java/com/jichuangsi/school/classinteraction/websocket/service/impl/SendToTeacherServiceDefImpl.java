/**
 * 
 */
package com.jichuangsi.school.classinteraction.websocket.service.impl;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONObject;
import com.jichuangsi.microservice.common.model.ResponseModel;
import com.jichuangsi.school.classinteraction.websocket.model.CourseStatistics;
import com.jichuangsi.school.classinteraction.websocket.model.QuestionStatistics;
import com.jichuangsi.school.classinteraction.websocket.service.ISendToTeacherService;

/**
 * @author huangjiajun
 *
 */
@Service
public class SendToTeacherServiceDefImpl implements ISendToTeacherService {

	@Resource
	private SimpMessagingTemplate messagingTemplate;
	@Value("${custom.ws.sub.teacher.csChangePre}")
	private String csChangePre;
	@Value("${custom.ws.sub.teacher.qcChangePre}")
	private String qcChangePre;

	@Override
	public void sendCourseStatisticsInfo(CourseStatistics courseStatistics) {
		messagingTemplate.convertAndSend(csChangePre + courseStatistics.getCourseId(),
				JSONObject.toJSONString(ResponseModel.sucess("", courseStatistics)));

	}

	@Override
	public void sendQuestionStatisticsInfo(QuestionStatistics questionStatistics) {
		messagingTemplate.convertAndSend(qcChangePre + questionStatistics.getCourseId(),
				JSONObject.toJSONString(ResponseModel.sucess("", questionStatistics)));

	}

}
