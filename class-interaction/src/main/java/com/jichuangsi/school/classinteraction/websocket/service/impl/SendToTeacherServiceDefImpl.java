/**
 * 
 */
package com.jichuangsi.school.classinteraction.websocket.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.jichuangsi.microservice.common.model.ResponseModel;
import com.jichuangsi.school.classinteraction.model.StudentAnswerModel;
import com.jichuangsi.school.classinteraction.websocket.model.AbstractNotifyInfoForTeacher;
import com.jichuangsi.school.classinteraction.websocket.model.CourseStatistics;
import com.jichuangsi.school.classinteraction.websocket.model.QuestionStatistics;
import com.jichuangsi.school.classinteraction.websocket.service.ISendToTeacherService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

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
	@Value("${custom.ws.sub.teacher.questionAnswerPre}")
	private String questionAnswerPre;

	@Override
	public void sendCourseStatisticsInfo(CourseStatistics courseStatistics) {
		courseStatistics.setNotifyType(AbstractNotifyInfoForTeacher.NOTIFY_TYPE_CS);
		messagingTemplate.convertAndSend(csChangePre + courseStatistics.getCourseId(),
				JSONObject.toJSONString(ResponseModel.sucess("", courseStatistics)));

	}

	@Override
	public void sendQuestionStatisticsInfo(QuestionStatistics questionStatistics) {
		questionStatistics.setNotifyType(AbstractNotifyInfoForTeacher.NOTIFY_TYPE_QS);
		messagingTemplate.convertAndSend(qcChangePre + questionStatistics.getCourseId(),
				JSONObject.toJSONString(ResponseModel.sucess("", questionStatistics)));

	}

	@Override
	public void sendQuestionAnswerInfo(StudentAnswerModel studentAnswerModel) {
		studentAnswerModel.setNotifyType(AbstractNotifyInfoForTeacher.NOTIFY_TYPE_SA);
		messagingTemplate.convertAndSend(questionAnswerPre + studentAnswerModel.getCourseId(),
				JSONObject.toJSONString(ResponseModel.sucess("", studentAnswerModel)));

	}
}
