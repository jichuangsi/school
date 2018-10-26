/**
 * 
 */
package com.jichuangsi.school.statistics.entity;

/**
 * @author huangjiajun
 *
 */

public class StudentAnswerEntity {

	private float score;// 题目得分
	private boolean isRight;// 正确与否
	private String quType;// 题目类型，主观题，客观题
	private String answer;// 答案内容
	private String studentId;// 学生ID

	public StudentAnswerEntity() {
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((studentId == null) ? 0 : studentId.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		StudentAnswerEntity other = (StudentAnswerEntity) obj;
		if (studentId == null) {
			if (other.studentId != null)
				return false;
		} else if (!studentId.equals(other.studentId))
			return false;
		return true;
	}

	public StudentAnswerEntity(float score, boolean isRight, String quType, String answer, String studentId) {
		super();
		this.score = score;
		this.isRight = isRight;
		this.quType = quType;
		this.answer = answer;
		this.studentId = studentId;
	}

	public final float getScore() {
		return score;
	}

	public final void setScore(float score) {
		this.score = score;
	}

	public final boolean getIsRight() {
		return isRight;
	}

	public final void setIsRight(boolean isRight) {
		this.isRight = isRight;
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

	public final String getStudentId() {
		return studentId;
	}

	public final void setStudentId(String studentId) {
		this.studentId = studentId;
	}
	
}
