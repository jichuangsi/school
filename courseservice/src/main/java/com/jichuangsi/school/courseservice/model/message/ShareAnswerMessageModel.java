/**
 * 
 */
package com.jichuangsi.school.courseservice.model.message;

/**
 * @author huangjiajun
 *
 */
public class ShareAnswerMessageModel {
	private String courseId;
	private String questionId;
	private String picPath;

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

	public String getPicPath() {
		return picPath;
	}

	public void setPicPath(String picPath) {
		this.picPath = picPath;
	}

	@Override
	public String toString(){
		StringBuffer objectInfo = new StringBuffer();
		objectInfo.append("{courseId:" + courseId + ",questionId:" + questionId + ",picPath:" + picPath + "}");
		return objectInfo.toString();
	}
}
