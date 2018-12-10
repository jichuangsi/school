/**
 * 
 */
package com.jichuangsi.school.classinteraction.websocket.model;

/**
 * @author huangjiajun
 *
 */
public class QuestionAnswerShare extends AbstractQustionNotifyForStudentModel {

	private String picPath;

	public QuestionAnswerShare() {
		this.wsType = AbstractQustionNotifyForStudentModel.WS_TYPE_QUESTION_ANSWER_SHARE;
	}

	public String getPicPath() {
		return picPath;
	}

	public void setPicPath(String picPath) {
		this.picPath = picPath;
	}


}
