package com.jichuangsi.school.user.model.backstage.orz;

import java.util.ArrayList;
import java.util.List;

public class PromisedModel {

    private String id;
    private String name;
    private List<PromisedModel> promisedModels = new ArrayList<PromisedModel>();

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

    public List<PromisedModel> getPromisedModels() {
        return promisedModels;
    }

    public void setPromisedModels(List<PromisedModel> promisedModels) {
        this.promisedModels = promisedModels;
    }
}
