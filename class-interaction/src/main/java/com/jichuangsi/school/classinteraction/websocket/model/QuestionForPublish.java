package com.jichuangsi.school.classinteraction.websocket.model;

public class QuestionForPublish {
	private String questionId;
	private String courseId;
	private String quType;
	private String content;
	private String wsType = "QuestionForPublish";

	public final String getQuestionId() {
		return questionId;
	}

	public final void setQuestionId(String questionId) {
		this.questionId = questionId;
	}

	public final String getCourseId() {
		return courseId;
	}

	public final void setCourseId(String courseId) {
		this.courseId = courseId;
	}

	public final String getQuType() {
		return quType;
	}

	public final void setQuType(String quType) {
		this.quType = quType;
	}

	public final String getContent() {
		return content;
	}

	public final void setContent(String content) {
		this.content = content;
	}

	public final String getWsType() {
		return wsType;
	}

	public final void setWsType(String wsType) {
		this.wsType = wsType;
	}

}
