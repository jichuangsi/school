package com.jichuangsi.school.questionsrepository.model;

import java.util.List;

public class PHPResponseModel2<Q,P,D,Y,A> {

	private String errorCode;
	private List<Q> qtypes;
	private List<P> paperTypes;
	private List<D> diffTypes;
	private List<Y> years;
	private List<A> areas;

	public String getErrorCode() {
		return errorCode;
	}

	public void setErrorCode(String errorCode) {
		this.errorCode = errorCode;
	}

	public List<Q> getQtypes() {
		return qtypes;
	}

	public void setQtypes(List<Q> qtypes) {
		this.qtypes = qtypes;
	}

	public List<P> getPaperTypes() {
		return paperTypes;
	}

	public void setPaperTypes(List<P> paperTypes) {
		this.paperTypes = paperTypes;
	}

	public List<D> getDiffTypes() {
		return diffTypes;
	}

	public void setDiffTypes(List<D> diffTypes) {
		this.diffTypes = diffTypes;
	}

	public List<Y> getYears() {
		return years;
	}

	public void setYears(List<Y> years) {
		this.years = years;
	}

	public List<A> getAreas() {
		return areas;
	}

	public void setAreas(List<A> areas) {
		this.areas = areas;
	}
}
