package com.jichuangsi.school.questionsrepository.model;

import java.util.List;

public class PHPResponseModel2<T,S,E> {

	private String errorCode;
	private List<T> qtypes;
	private List<S> paperTypes;
	private List<E> diffTypes;

	public String getErrorCode() {
		return errorCode;
	}

	public void setErrorCode(String errorCode) {
		this.errorCode = errorCode;
	}

	public List<T> getQtypes() {
		return qtypes;
	}

	public void setQtypes(List<T> qtypes) {
		this.qtypes = qtypes;
	}

	public List<S> getPaperTypes() {
		return paperTypes;
	}

	public void setPaperTypes(List<S> paperTypes) {
		this.paperTypes = paperTypes;
	}

	public List<E> getDiffTypes() {
		return diffTypes;
	}

	public void setDiffTypes(List<E> diffTypes) {
		this.diffTypes = diffTypes;
	}
}
