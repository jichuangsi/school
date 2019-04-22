package com.jichuangsi.school.user.repository;

import com.jichuangsi.school.user.entity.org.ClassInfo;

import java.util.List;

public interface IClassInfoExtraRepository {


    List<ClassInfo> findByClassIdInAndDeleteFlagAndUpdateTimeGreaterThanAndUpdateTimeLessThanOrderByCreatedTime(List<String> classIds,String delete,long beign,long end,int pageIndex,int pageSize);


    int countByClassIdInAndDeleteFlagAndUpdateTimeGreaterThanAndUpdateTimeLessThanOrderByCreatedTime(List<String> classIds,String delete,long beign,long end);
}
