package com.jichuangsi.school.questionsrepository.model;

import com.jichuangsi.microservice.common.constant.ResultCode;

import java.util.List;

public class PHPResponseModel<T> {

	private String errorCode;
	private List<T> data;

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
