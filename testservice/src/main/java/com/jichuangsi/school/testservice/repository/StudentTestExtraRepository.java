package com.jichuangsi.school.testservice.repository;

import com.jichuangsi.school.testservice.entity.Test;

import java.util.List;

public interface StudentTestExtraRepository {

    List<Test> findProgressTestByStudentId(String studentId);

    int countFinishedTestByStudentId(String studentId);

    List<Test> findFinishedTestByStudentId(String studentId, int pageNum, int pageSize);
}
