package com.jichuangsi.school.parents.feign.model;


import com.jichuangsi.school.parents.feign.constant.Status;

public class SearchTestModel {

    private String keyWord;
    private String time;
    private Integer sortNum;
    private Status status;
    private int pageIndex;
    private int pageSize;

    public SearchTestModel() {
    }

    public SearchTestModel(String keyWord, String time, Integer sortNum, Status status, int pageIndex, int pageSize) {
        this.keyWord = keyWord;
        this.time = time;
        this.sortNum = sortNum;
        this.status = status;
        this.pageIndex = pageIndex;
        this.pageSize = pageSize;
    }

    public Integer getSortNum() {
        return sortNum;
    }

    public void setSortNum(Integer sortNum) {
        this.sortNum = sortNum;
    }


    public String getKeyWord() {
        return keyWord;
    }

    public void setKeyWord(String keyWord) {
        this.keyWord = keyWord;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public int getPageIndex() {
        return pageIndex;
    }

    public void setPageIndex(int pageIndex) {
        this.pageIndex = pageIndex;
    }

    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }
}
