package com.jichuangsi.school.questionsrepository.model;

import java.util.ArrayList;
import java.util.List;

public class EditionTreeNode {
    String id;
    String name;
    String pid;
    String code;
    List<EditionTreeNode> child = new ArrayList<EditionTreeNode>();

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

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public List<EditionTreeNode> getChild() {
        return child;
    }

    public void setChild(List<EditionTreeNode> child) {
        this.child = child;
    }
}
