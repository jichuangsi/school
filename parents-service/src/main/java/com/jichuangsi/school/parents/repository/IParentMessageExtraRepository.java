package com.jichuangsi.school.parents.repository;

import com.jichuangsi.school.parents.entity.ParentMessage;

import java.util.List;

public interface IParentMessageExtraRepository {

    List<ParentMessage> findByParentIdAndTeacherIdAndSort(String parentId,String teacherId,int pageIndex,int pageSize);
}
