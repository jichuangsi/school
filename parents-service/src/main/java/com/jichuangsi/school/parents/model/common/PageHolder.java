package com.jichuangsi.school.parents.model.common;

import java.util.List;

public class PageHolder<T> {
    private int total;
    private int pageSize;
    private int pageNum;
    private int pageCount;
    private List<T> content;

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

    public int getPageNum() {
        return pageNum;
    }

    public void setPageNum(int pageNum) {
        this.pageNum = pageNum;
    }

    public List<T> getContent() {
        return content;
    }

    public void setContent(List<T> content) {
        this.content = content;
    }

    public int getPageCount() {
        return (this.total + this.pageSize - 1) / this.pageSize;
    }

    public void setPageCount(int pageCount) {
        this.pageCount = pageCount;
    }
}
