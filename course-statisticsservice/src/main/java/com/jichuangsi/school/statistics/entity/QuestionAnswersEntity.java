/**
 * 题目所有的回答实体
 */
package com.jichuangsi.school.statistics.entity;

import java.util.Set;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 * @author huangjiajun
 *
 */
@Document(collection = "course-question-answers")
public class QuestionAnswersEntity {
	@Id
	private String uid;
	private String courseId;
	private String questionId;
	private Set<StudentAnswerEntity> studentAnswers;

	public final String getUid() {
		return uid;
	}

	public final void setUid(String uid) {
		this.uid = uid;
	}

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

	public final Set<StudentAnswerEntity> getStudentAnswers() {
		return studentAnswers;
	}

	public final void setStudentAnswers(Set<StudentAnswerEntity> studentAnswers) {
		this.studentAnswers = studentAnswers;
	}

}
