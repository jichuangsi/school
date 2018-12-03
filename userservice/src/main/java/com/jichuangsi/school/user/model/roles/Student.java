package com.jichuangsi.school.user.model.roles;

public class Student extends SchoolUser {

    public boolean equals(Object obj) {
        if (obj instanceof Student) {
            return true;
        }
        return super.equals(obj);
    }
}
