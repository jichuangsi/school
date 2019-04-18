package com.jichuangsi.school.user.repository;

import com.jichuangsi.school.user.entity.backstage.SchoolNoticeInfo;

import java.util.List;

public interface ISchoolNoticeInfoExtraRepository {

    List<SchoolNoticeInfo> pageBySchoolIdAndDeleteFlagOrderByCreatedTimeDesc(String schoolId,String delete,int pageIndex,int pageSize);
}
