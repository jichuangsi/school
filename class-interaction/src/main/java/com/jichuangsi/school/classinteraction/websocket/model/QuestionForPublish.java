package com.jichuangsi.school.classinteraction.websocket.model;

import java.util.ArrayList;
import java.util.List;

public class QuestionForPublish extends AbstractQustionNotifyForStudentModel{

	private String quType;
	private String content;
    private List<String> student=new ArrayList<String>();
    public List<String> getStudent() {
        return student;
    }

    public void setStudent(List<String> student) {
        this.student = student;
    }
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
