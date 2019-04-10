package com.jichuangsi.school.user.repository;

import com.jichuangsi.school.user.entity.UserInfo;

import java.util.List;

public interface IUserExtraRepository {

    List<UserInfo> findByRoleInfos(String classId);

    List<UserInfo> findBySchoolId(String schoolId);
}
