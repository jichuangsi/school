/**
 * 
 */
package com.jichuangsi.school.courseservice.model.message;

/**
 * @author huangjiajun
 *
 */
public class AnswerMessageModel {
	private String courseId;
	private String questionId;
	private double score;//题目得分
	private boolean isRight;//正确与否
	private String quType;// 题目类型，主观题，客观题
	private String answer;// 答案内容
	private String studentId;//老师批改

	public String getCourseId() {
		return courseId;
	}

	public void setCourseId(String courseId) {
		this.courseId = courseId;
	}

	public String getQuestionId() {
		return questionId;
	}

	public void setQuestionId(String questionId) {
		this.questionId = questionId;
	}

	public double getScore() {
		return score;
	}

	public void setScore(double score) {
		this.score = score;
	}

	public boolean isRight() {
		return isRight;
	}

	public void setRight(boolean right) {
		isRight = right;
	}

	public String getQuType() {
		return quType;
	}

	public void setQuType(String quType) {
		this.quType = quType;
	}

	public String getAnswer() {
		return answer;
	}

	public void setAnswer(String answer) {
		this.answer = answer;
	}

	public String getStudentId() {
		return studentId;
	}

	public void setStudentId(String studentId) {
		this.studentId = studentId;
	}

	@Override
	public String toString(){
		StringBuffer objectInfo = new StringBuffer();
		objectInfo.append("courseId:" + courseId + "questionId:" + questionId + ",quType:" + quType + ",score:" + score + ",isRight:" + isRight + ",answer:" + answer + ",studentId:" + studentId);
		return objectInfo.toString();
	}
}
