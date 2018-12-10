/**
 * 题目终止作答信息
 */
package com.jichuangsi.school.classinteraction.websocket.model;

/**
 * @author huangjiajun
 *
 */
public class QuestionClose extends AbstractQustionNotifyForStudentModel {

	public QuestionClose() {
		this.wsType = AbstractQustionNotifyForStudentModel.WS_TYPE_QUESTION_CLOSE;
	}
}
