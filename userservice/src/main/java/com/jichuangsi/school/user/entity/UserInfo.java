package com.jichuangsi.school.user.entity;

//import javax.persistence.Id;

import org.springframework.data.mongodb.core.mapping.Document;

//@Document(collection = "school-user-student")
public class UserInfo {
	//@Id
	private String userId;
	private String userNum;
	private String userName;
	private String classId;

	public final String getUserId() {
		return userId;
	}

	public final void setUserId(String userId) {
		this.userId = userId;
	}

	public final String getUserName() {
		return userName;
	}

	public final void setUserName(String userName) {
		this.userName = userName;
	}

	public final String getClassId() {
		return classId;
	}

	public final void setClassId(String classId) {
		this.classId = classId;
	}

	public final String getUserNum() {
		return userNum;
	}

	public final void setUserNum(String userNum) {
		this.userNum = userNum;
	}

}
