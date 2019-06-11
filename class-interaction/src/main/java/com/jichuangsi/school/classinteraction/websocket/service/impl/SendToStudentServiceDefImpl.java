/**
 * 
 */
package com.jichuangsi.school.classinteraction.websocket.service.impl;

import javax.annotation.Resource;

import com.jichuangsi.school.classinteraction.model.TeacherPublishFile;
import com.jichuangsi.school.classinteraction.websocket.model.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONObject;
import com.jichuangsi.microservice.common.model.ResponseModel;
import com.jichuangsi.school.classinteraction.websocket.service.ISendToStudentService;

/**
 * @author huangjiajun
 *
 */
@Service
public class SendToStudentServiceDefImpl implements ISendToStudentService {

	@Resource
	private SimpMessagingTemplate messagingTemplate;
	@Value("${custom.ws.sub.student.classInfoPre}")
	private String classInfoPre;
	@Value("${custom.ws.sub.student.courseIntercationPre}")
	private String courseIntercationPre;
	@Value("${custom.ws.sub.student.coursePublishFile}")
	private String coursePublishPre;

	@Override
	public void sendClassInfo(ClassInfoForStudent classInfoForStudent) {
		messagingTemplate.convertAndSend(classInfoPre + classInfoForStudent.getClassId(),
				JSONObject.toJSONString(ResponseModel.sucess("", classInfoForStudent)));
	}

	@Override
	public void sendPubQuestionInfo(QuestionForPublish questionForPublish) {
		messagingTemplate.convertAndSend(courseIntercationPre + questionForPublish.getCourseId(),
				JSONObject.toJSONString(ResponseModel.sucess("", questionForPublish)));
	}

	@Override
	public void sendQuestionCloseInfo(QuestionClose questionClose) {
		messagingTemplate.convertAndSend(courseIntercationPre + questionClose.getCourseId(),
				JSONObject.toJSONString(ResponseModel.sucess("", questionClose)));
	}

	@Override
	public void sendQuestionAnswerShareInfo(QuestionAnswerShare answerShare) {
		messagingTemplate.convertAndSend(courseIntercationPre + answerShare.getCourseId(),
				JSONObject.toJSONString(ResponseModel.sucess("", answerShare)));

	}

	@Override
	public void sendPublishFileInfo(TeacherPublishFile publishFile) {
		messagingTemplate.convertAndSend(coursePublishPre + publishFile.getCourseId(),
				JSONObject.toJSONString(ResponseModel.sucess("",publishFile.getFileId())));
	}

	@Override
	public void sendRaceQuestionInfo(RaceQuestion raceQuestion) {
		messagingTemplate.convertAndSend(courseIntercationPre + raceQuestion.getCourseId(),
				JSONObject.toJSONString(ResponseModel.sucess("", raceQuestion)));

	}
}
