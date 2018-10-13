package com.jichuangsi.school.courseservice.util;

import com.jichuangsi.school.courseservice.entity.Course;
import com.jichuangsi.school.courseservice.model.message.CourseMessageModel;

import java.util.Date;

public final class MappingEntity2MessageConverter {

    private MappingEntity2MessageConverter(){}

    public static final CourseMessageModel ConvertCourse(Course course){
        CourseMessageModel courseMessageModel = new CourseMessageModel();
        courseMessageModel.setCourseId(course.getId());
        courseMessageModel.setClassId(course.getClassId());
        courseMessageModel.setCourseName(course.getName());
        courseMessageModel.setTimestamp(new Date().getTime());
        return courseMessageModel;
    }
}
