/**
 * 推送ws消息到学生端的服务接口
 */
package com.jichuangsi.school.classinteraction.websocket.service;

import com.jichuangsi.school.classinteraction.websocket.model.ClassInfoForStudent;
import com.jichuangsi.school.classinteraction.websocket.model.QuestionAnswerShare;
import com.jichuangsi.school.classinteraction.websocket.model.QuestionClose;
import com.jichuangsi.school.classinteraction.websocket.model.QuestionForPublish;
import com.jichuangsi.school.classinteraction.websocket.model.RaceQuestion;

/**
 * @author huangjiajun
 *
 */
public interface ISendToStudentService {
	
	/**
	 * 推送班级信息
	 */
	void sendClassInfo(ClassInfoForStudent classInfoForStudent);
	
	/**
	 * 推送发布题目信息
	 */
	void sendPubQuestionInfo(QuestionForPublish questionForPublish);
	
	/**
	 * 推送题目终止作答信息
	 */
	void sendQuestionCloseInfo(QuestionClose questionClose);
	
	/**
	 * 推送题目答案共享信息
	 */
	void sendQuestionAnswerShareInfo(QuestionAnswerShare answerShare);
	
	/**
	 * 推送开始抢答信息
	 */
	void sendRaceQuestionInfo(RaceQuestion raceQuestion);
	
}
