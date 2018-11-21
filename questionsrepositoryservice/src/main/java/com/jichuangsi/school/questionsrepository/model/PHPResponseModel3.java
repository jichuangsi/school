package com.jichuangsi.school.questionsrepository.model;

public class PHPResponseModel3<T> {

	private String errorCode;
	private T data;

	public String getErrorCode() {
		return errorCode;
	}

	public void setErrorCode(String errorCode) {
		this.errorCode = errorCode;
	}

	public T getData() {
		return data;
	}

	public void setData(T data) {
		this.data = data;
	}
}
