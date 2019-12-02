/**
 * 
 */
package com.jichuangsi.school.courseservice.model.message;

import java.util.ArrayList;
import java.util.List;

/**
 * @author huangjiajun
 *
 */
public class QuestionMessageModel {
	private String questionId;// 班级ID
	private String courseId;// 课程ID
	private String quType;//题目类型
	private String content;//题目内容
    private List<String> student=new ArrayList<String>();//学生id


    public List<String> getStudent() {
        return student;
    }

    public void setStudent(List<String> student) {
        this.student = student;
    }

    public String getQuestionId() {
		return questionId;
	}

	public void setQuestionId(String questionId) {
		this.questionId = questionId;
	}

	public String getCourseId() {
		return courseId;
	}

	public void setCourseId(String courseId) {
		this.courseId = courseId;
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

	@Override
	public String toString(){
		StringBuffer objectInfo = new StringBuffer();
		objectInfo.append("{courseId:" + courseId + ",questionId:" + questionId + ",quType:" + quType + ",content:" + content+"，student:"+student+"}");
		return objectInfo.toString();
	}
}
