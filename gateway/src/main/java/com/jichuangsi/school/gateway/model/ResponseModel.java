package com.jichuangsi.school.gateway.model;

public class ResponseModel {

	private String code;
	private String msg;

	public ResponseModel(String code, String msg) {
		super();
		this.code = code;
		this.msg = msg;
	}

	public final String getCode() {
		return code;
	}

	public final void setCode(String code) {
		this.code = code;
	}

	public final String getMsg() {
		return msg;
	}

	public final void setMsg(String msg) {
		this.msg = msg;
	}

}
