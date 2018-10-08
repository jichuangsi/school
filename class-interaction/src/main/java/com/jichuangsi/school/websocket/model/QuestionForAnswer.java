package com.jichuangsi.school.websocket.model;

public class QuestionForAnswer {

	private String userId;
	private String token;
	private String questionId;
	private String courseId;
	private String quType;
	private String answer;

	public final String getUserId() {
		return userId;
	}

	public final void setUserId(String userId) {
		this.userId = userId;
	}

	public final String getToken() {
		return token;
	}

	public final void setToken(String token) {
		this.token = token;
	}

	public final String getQuestionId() {
		return questionId;
	}

	public final void setQuestionId(String questionId) {
		this.questionId = questionId;
	}

	public final String getQuType() {
		return quType;
	}

	public final void setQuType(String quType) {
		this.quType = quType;
	}

	public final String getAnswer() {
		return answer;
	}

	public final void setAnswer(String answer) {
		this.answer = answer;
	}

	public final String getCourseId() {
		return courseId;
	}

	public final void setCourseId(String courseId) {
		this.courseId = courseId;
	}

}
