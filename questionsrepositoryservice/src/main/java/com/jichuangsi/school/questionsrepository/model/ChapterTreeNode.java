package com.jichuangsi.school.questionsrepository.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class ChapterTreeNode implements Serializable {

    private String id;
    private String name;
    private String pid;
    private String subjectId;
    private String pharseId;
    private String editionId;
    private String gradeId;
    private int sort;
    private String oldId;
    private int month;
    private int level;
    private List<ChapterTreeNode> child = new ArrayList<ChapterTreeNode>();

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPid() {
        return pid;
    }

    public void setPid(String pid) {
        this.pid = pid;
    }

    public String getSubjectId() {
        return subjectId;
    }

    public void setSubjectId(String subjectId) {
        this.subjectId = subjectId;
    }

    public String getPharseId() {
        return pharseId;
    }

    public void setPharseId(String pharseId) {
        this.pharseId = pharseId;
    }

    public String getEditionId() {
        return editionId;
    }

    public void setEditionId(String editionId) {
        this.editionId = editionId;
    }

    public String getGradeId() {
        return gradeId;
    }

    public void setGradeId(String gradeId) {
        this.gradeId = gradeId;
    }

    public int getSort() {
        return sort;
    }

    public void setSort(int sort) {
        this.sort = sort;
    }

    public String getOldId() {
        return oldId;
    }

    public void setOldId(String oldId) {
        this.oldId = oldId;
    }

    public int getMonth() {
        return month;
    }

    public void setMonth(int month) {
        this.month = month;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public List<ChapterTreeNode> getChild() {
        return child;
    }

    public void setChild(List<ChapterTreeNode> child) {
        this.child = child;
    }
}
