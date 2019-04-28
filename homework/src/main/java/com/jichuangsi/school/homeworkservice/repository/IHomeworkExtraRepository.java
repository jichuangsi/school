package com.jichuangsi.school.homeworkservice.repository;

import com.jichuangsi.school.homeworkservice.entity.Homework;

import java.util.List;

public interface IHomeworkExtraRepository {

    List<Homework> findByClassIdAndStatusAndEndTimeGreaterThanAndEndTimeLessThanAndSubjectNameLike(String classId, String status, long beginTime, long endTime, String subjectName);
}
