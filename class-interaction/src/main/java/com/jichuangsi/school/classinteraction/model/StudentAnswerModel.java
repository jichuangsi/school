/**
 * 
 */
package com.jichuangsi.school.classinteraction.model;

import com.jichuangsi.school.classinteraction.websocket.model.AbstractNotifyInfoForTeacher;

/**
 * @author huangjiajun
 *
 */
public class StudentAnswerModel extends AbstractNotifyInfoForTeacher {

	public static final String QUTYPE_OBJECTIVE = "objective";
	public static final String QUTYPE_SUBJECTIVE = "subjective";/*
<<<<<<< HEAD

=======
	
>>>>>>> a16781a631ec752b0ebb71e288ded947b01cb98c*/
	public static final String QUTYPE_RACE = "race";

	private String courseId;
	private String questionId;
	private float score;// 题目得分
	private boolean right;// 正确与否
	private String quType;// 题目类型，主观题，客观题
	private String answer;// 答案内容
	private String studentId;// 学生ID

	public final String getCourseId() {
		return courseId;
	}

	public final void setCourseId(String courseId) {
		this.courseId = courseId;
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

	public final float getScore() {
		return score;
	}

	public final void setScore(float score) {
		this.score = score;
	}

	public boolean getRight() {
		return right;
	}

	public void setRight(boolean right) {
		this.right = right;
	}

	public final String getStudentId() {
		return studentId;
	}

	public final void setStudentId(String studentId) {
		this.studentId = studentId;
	}

}
