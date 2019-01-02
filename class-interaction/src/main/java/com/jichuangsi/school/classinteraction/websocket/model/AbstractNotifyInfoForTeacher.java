/**
 * 
 */
package com.jichuangsi.school.classinteraction.websocket.model;

/**
 * @author huangjiajun
 *
 */
public abstract class AbstractNotifyInfoForTeacher {
	
	public static final String NOTIFY_TYPE_CS="CourseStatistics";//课堂统计
	public static final String NOTIFY_TYPE_QS="QuestionStatistics";//题目统计
	public static final String NOTIFY_TYPE_SA="StudentAnswer";//学生作答
	
	protected String notifyType;

	public String getNotifyType() {
		return notifyType;
	}

	public void setNotifyType(String notifyType) {
		this.notifyType = notifyType;
	}
	
	
}
