package com.jichuangsi.school.questionsrepository.model;

import java.util.ArrayList;
import java.util.List;

public class KnowledgeTreeNode {
    String id;
    String name;
    String pid;
    String subjectId;
    String pharseId;
    String editionId;
    String gradeId;
    String sort;
    String oldId;
    String month;
    String level;
    List<KnowledgeTreeNode> child = new ArrayList<KnowledgeTreeNode>();

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

    public String getSort() {
        return sort;
    }

    public void setSort(String sort) {
        this.sort = sort;
    }

    public String getOldId() {
        return oldId;
    }

    public void setOldId(String oldId) {
        this.oldId = oldId;
    }

    public String getMonth() {
        return month;
    }

    public void setMonth(String month) {
        this.month = month;
    }

    public String getLevel() {
        return level;
    }

    public void setLevel(String level) {
        this.level = level;
    }

    public List<KnowledgeTreeNode> getChild() {
        return child;
    }

    public void setChild(List<KnowledgeTreeNode> child) {
        this.child = child;
    }
}
