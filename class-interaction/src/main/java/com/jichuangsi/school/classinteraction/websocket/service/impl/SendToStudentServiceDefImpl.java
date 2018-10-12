/**
 * 
 */
package com.jichuangsi.school.classinteraction.websocket.service.impl;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONObject;
import com.jichuangsi.school.classinteraction.websocket.model.ClassInfoForStudent;
import com.jichuangsi.school.classinteraction.websocket.model.CourseStatistics;
import com.jichuangsi.school.classinteraction.websocket.model.ResponseModel;
import com.jichuangsi.school.classinteraction.websocket.service.ISendToStudentService;

/**
 * @author huangjiajun
 *
 */
@Service
public class SendToStudentServiceDefImpl implements ISendToStudentService {

	@Resource
	private SimpMessagingTemplate messagingTemplate;
	@Value("${custom.ws.sub.csChange}")
	private String csChangePre;
	@Value("${custom.ws.sub.classInfo}")
	private String classInfoPre;

	@Override
	public void sendClassInfo(ClassInfoForStudent classInfoForStudent) {
		messagingTemplate.convertAndSend(classInfoPre + classInfoForStudent.getClassId(),
				JSONObject.toJSONString(ResponseModel.sucess("", classInfoForStudent)));

	}

	@Override
	public void sendCourseStatisticsInfo(CourseStatistics courseStatistics) {
		messagingTemplate.convertAndSend(csChangePre + courseStatistics.getCourseId(),
				JSONObject.toJSONString(ResponseModel.sucess("", courseStatistics)));

	}

}
