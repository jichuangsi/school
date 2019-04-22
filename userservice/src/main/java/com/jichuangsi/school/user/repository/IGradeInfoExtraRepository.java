package com.jichuangsi.school.user.repository;

import com.jichuangsi.school.user.entity.org.GradeInfo;

import java.util.List;

public interface IGradeInfoExtraRepository {

    List<GradeInfo> findByDeleteFlagAndIdInAndUpdateTimeGreaterThanAndUpdateTimeLessThanOrderByCreateTime(String delete,List<String> ids,long beign,long end,int pageIndex,int pageSize);

    int countByDeleteFlagAndIdInAndUpdateTimeGreaterThanAndUpdateTimeLessThanOrderByCreateTime(String delete,List<String> ids,long beign,long end);
}
