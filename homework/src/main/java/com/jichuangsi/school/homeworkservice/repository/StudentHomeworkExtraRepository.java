package com.jichuangsi.school.homeworkservice.repository;

import com.jichuangsi.school.homeworkservice.entity.Homework;

import java.util.List;

public interface StudentHomeworkExtraRepository {

    List<Homework> findProgressHomeworkByStudentId(String studentId);

    int countFinishedHomeworkByStudentId(String studentId);

    List<Homework> findFinishedHomeworkByStudentId(String studentId, int pageNum, int pageSize);

    List<Homework> findFinishedHomeWorkByStudentIdAndEndTime(String studentId,long endTime,String subject);


}
