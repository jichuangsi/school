package com.jichuangsi.school.user.entity;

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "urlrelation")
@GenericGenerator(name = "jpa-uuid", strategy = "uuid")
public class UrlRelation {

    @Id
    @GeneratedValue(generator = "jpa-uuid")
    private String id;
    private  String rid;//角色ID
    private String uid;//urlID

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getRid() {
        return rid;
    }

    public void setRid(String rid) {
        this.rid = rid;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public UrlRelation(String rid, String uid) {
        this.rid = rid;
        this.uid = uid;
    }
}
