package com.jichuangsi.school.parents.model;

import java.util.List;

public class PHPResponseModel<T> {

	private String errorstatus;
	private String errorCode;
	private List<T> data;

	public String getErrorstatus() {
		return errorstatus;
	}

	public void setErrorstatus(String errorstatus) {
		this.errorstatus = errorstatus;
	}

	public String getErrorCode() {
		return errorCode;
	}

	public void setErrorCode(String errorCode) {
		this.errorCode = errorCode;
	}

	public List<T> getData() {
		return data;
	}

	public void setData(List<T> data) {
		this.data = data;
	}
}
