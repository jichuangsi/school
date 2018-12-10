package com.jichuangsi.school.classinteraction.websocket.model;

public class QuestionForPublish extends AbstractQustionNotifyForStudentModel{

	private String quType;
	private String content;
	
	public QuestionForPublish() {
		this.wsType = AbstractQustionNotifyForStudentModel.WS_TYPE_QUESTION_PUBLISH;
	}

	public String getQuType() {
		return quType;
	}

	public void setQuType(String quType) {
		this.quType = quType;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

}
