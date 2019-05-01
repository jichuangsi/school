package com.jichuangsi.school.parents.entity;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;

@Document(collection = "parent_notice")
public class ParentNotice {

    public final static String COLLEGE_NOTICE ="C";
    public final static String SYSTEM_NOTICE = "S";


    @Id
    private String id;
    private String parentId;
    private String parentName;
    private String title;
    private String content;
    private String messageId;
    private long createdTime = new Date().getTime();
    private String deleteFlag = "0";
    private String noticeType;
    private String read = "0";
    /*private Course course;

    public class Course{
        private String courseId;
        private String courseName;
        private String teacherName;
        private String teacherId;
        private String subjectName;
        private String subjectId;


        public Course(String courseId, String courseName, String teacherName, String teacherId, String subjectName,String subjectId) {
            this.courseId = courseId;
            this.courseName = courseName;
            this.teacherName = teacherName;
            this.teacherId = teacherId;
            this.subjectName = subjectName;
            this.subjectId = subjectId;
        }

        public String getSubjectId() {
            return subjectId;
        }

        public void setSubjectId(String subjectId) {
            this.subjectId = subjectId;
        }

        public String getCourseId() {
            return courseId;
        }

        public void setCourseId(String courseId) {
            this.courseId = courseId;
        }

        public String getCourseName() {
            return courseName;
        }

        public void setCourseName(String courseName) {
            this.courseName = courseName;
        }

        public String getTeacherName() {
            return teacherName;
        }

        public void setTeacherName(String teacherName) {
            this.teacherName = teacherName;
        }

        public String getTeacherId() {
            return teacherId;
        }

        public void setTeacherId(String teacherId) {
            this.teacherId = teacherId;
        }

        public String getSubjectName() {
            return subjectName;
        }

        public void setSubjectName(String subjectName) {
            this.subjectName = subjectName;
        }
    }

    public Course getCourse() {
        return course;
    }

    public void setCourse(String courseId, String courseName, String teacherName, String teacherId, String subjectName,String subjectId) {
        this.course = new Course(courseId, courseName, teacherName, teacherId, subjectName,subjectId);
    }*/

    public String getRead() {
        return read;
    }

    public void setRead(String read) {
        this.read = read;
    }

    public String getMessageId() {
        return messageId;
    }

    public void setMessageId(String messageId) {
        this.messageId = messageId;
    }

    public String getNoticeType() {
        return noticeType;
    }

    public void setNoticeType(String noticeType) {
        this.noticeType = noticeType;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getParentId() {
        return parentId;
    }

    public void setParentId(String parentId) {
        this.parentId = parentId;
    }

    public String getParentName() {
        return parentName;
    }

    public void setParentName(String parentName) {
        this.parentName = parentName;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public long getCreatedTime() {
        return createdTime;
    }

    public void setCreatedTime(long createdTime) {
        this.createdTime = createdTime;
    }

    public String getDeleteFlag() {
        return deleteFlag;
    }

    public void setDeleteFlag(String deleteFlag) {
        this.deleteFlag = deleteFlag;
    }
}
