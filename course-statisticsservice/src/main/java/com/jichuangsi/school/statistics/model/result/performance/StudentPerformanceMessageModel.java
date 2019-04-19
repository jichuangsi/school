package com.jichuangsi.school.statistics.model.result.performance;

public abstract class StudentPerformanceMessageModel {
	protected String msgType;
	private String studentId;
	private String studentName;
	private String teacherId;
	private String teacherName;
	private int commend;
	private String remark;
	private String operId;// 操作用户ID
	private long timestamp;// 时间戳

	public final String getOperId() {
		return operId;
	}

	public final void setOperId(String operId) {
		this.operId = operId;
	}

	public final long getTimestamp() {
		return timestamp;
	}

	public final void setTimestamp(long timestamp) {
		this.timestamp = timestamp;
	}

	public String getStudentId() {
		return studentId;
	}

	public void setStudentId(String studentId) {
		this.studentId = studentId;
	}

	public String getTeacherId() {
		return teacherId;
	}

	public void setTeacherId(String teacherId) {
		this.teacherId = teacherId;
	}

	public String getTeacherName() {
		return teacherName;
	}

	public void setTeacherName(String teacherName) {
		this.teacherName = teacherName;
	}

	public int getCommend() {
		return commend;
	}

	public void setCommend(int commend) {
		this.commend = commend;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public String getStudentName() {
		return studentName;
	}

	public void setStudentName(String studentName) {
		this.studentName = studentName;
	}

	public String getMsgType() {
		return msgType;
	}

	public void setMsgType(String msgType) {
		this.msgType = msgType;
	}
}
