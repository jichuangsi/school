/**
 * 
 */
package com.jichuangsi.school.classinteraction.websocket.service.impl;

import javax.annotation.Resource;

import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONObject;
import com.jichuangsi.school.classinteraction.websocket.model.ClassInfoForStudent;
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

	@Override
	public void sendClassInfo(ClassInfoForStudent classInfoForStudent) {
		messagingTemplate.convertAndSend("/topic/group/student/" + classInfoForStudent.getClassId(),
				JSONObject.toJSONString(ResponseModel.sucess("", classInfoForStudent)));

	}

}
