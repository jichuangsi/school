/**
 * 
 */
package com.jichuangsi.school.classinteraction.service.impl;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.jichuangsi.school.classinteraction.service.IClassInteractionForTeacherService;
import com.jichuangsi.school.classinteraction.websocket.model.RaceQuestion;
import com.jichuangsi.school.classinteraction.websocket.service.ISendToStudentService;

/**
 * @author huangjiajun
 *
 */
@Service
public class ClassInteractionForTeacherServiceDefImpl implements IClassInteractionForTeacherService {
	@Resource
	private ISendToStudentService sendToStudentService;

	@Override
	public void pubRaceQuestion(String courseId, String raceId) {
		RaceQuestion raceQuestion = new RaceQuestion();
		raceQuestion.setCourseId(courseId);
		raceQuestion.setRaceId(raceId);
		sendToStudentService.sendRaceQuestionInfo(raceQuestion);
		
	}

}
