package com.jichuangsi.school.examservice.Model;

import java.util.ArrayList;
import java.util.List;

public class DeleteQueryModel {
    private List<String> ids  = new ArrayList<String>();

    public List<String> getIds() {
        return ids;
    }

    public void setIds(List<String> ids) {
        this.ids = ids;
    }
}