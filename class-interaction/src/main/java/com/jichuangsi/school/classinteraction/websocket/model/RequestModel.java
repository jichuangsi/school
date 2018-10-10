package com.jichuangsi.school.classinteraction.websocket.model;

public class RequestModel<T> {
	
	private String tranId;
	private String token;
	private T requestParams;

	public final String getTranId() {
		return tranId;
	}

	public final void setTranId(String tranId) {
		this.tranId = tranId;
	}

	public final String getToken() {
		return token;
	}

	public final void setToken(String token) {
		this.token = token;
	}

	public final T getRequestParams() {
		return requestParams;
	}

	public final void setRequestParams(T requestParams) {
		this.requestParams = requestParams;
	}

}
