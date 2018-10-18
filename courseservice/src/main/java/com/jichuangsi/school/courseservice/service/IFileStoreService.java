package com.jichuangsi.school.courseservice.service;

import com.jichuangsi.school.courseservice.model.CourseFile;

public interface IFileStoreService {

    CourseFile uploadCourseFile(CourseFile file) throws Exception;

    CourseFile donwloadCourseFile(String fileName) throws Exception;

    void deleteCourseFile(String fileName) throws Exception;

}
