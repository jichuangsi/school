package com.jichuangsi.school.user.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.jichuangsi.school.user.entity.UrlRelation;

import java.util.ArrayList;
import java.util.List;
public class UrlModel {

    private List<UrlRelation> urlRelations=new ArrayList<UrlRelation>();

    public List<UrlRelation> getUrlRelations() {
        return urlRelations;
    }

    public void setUrlRelations(List<UrlRelation> urlRelations) {
        this.urlRelations = urlRelations;
    }
}
