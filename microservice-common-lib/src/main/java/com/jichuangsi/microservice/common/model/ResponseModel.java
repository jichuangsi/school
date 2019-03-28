package com.jichuangsi.microservice.common.model;

import com.jichuangsi.microservice.common.constant.ResultCode;

public class ResponseModel<T> {

	private String tranId;
	private String code;
	private String msg;
	private T data;

	public ResponseModel() { }

	public ResponseModel(String tranId, String code, String msg, T data) {
		this.tranId = tranId;
		this.code = code;
		this.msg = msg;
		this.data = data;
	}

	public static <T> ResponseModel<T> sucessWithEmptyData(String tranId) {
		return sucess(tranId, null);
	}

	public static <T> ResponseModel<T> sucess(String tranId, T data) {
		return new ResponseModel<T>(tranId, ResultCode.SUCESS, ResultCode.SUCESS_MSG, data);
	}

	public static <T> ResponseModel<T> fail(String tranId) {
		return fail(tranId, ResultCode.SYS_ERROR_MSG);
	}

	public static <T> ResponseModel<T> fail(String tranId, String msg) {
		return new ResponseModel<T>(tranId, ResultCode.SYS_ERROR, msg, null);
	}

	public final String getTranId() {
		return tranId;
	}

	public final void setTranId(String tranId) {
		this.tranId = tranId;
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

	public final T getData() {
		return data;
	}

	public final void setData(T data) {
		this.data = data;
	}

}
