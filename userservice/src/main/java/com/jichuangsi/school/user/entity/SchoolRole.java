package com.jichuangsi.school.user.entity;

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "schoolrole")
@GenericGenerator(name = "jpa-uuid", strategy = "uuid")
public class SchoolRole {
    @Id
    @GeneratedValue(generator = "jpa-uuid")
    private String id;
    private String rname;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getrName() {
        return rname;
    }
    public void setrName(String rName) {
        this.rname = rName;
    }
};
