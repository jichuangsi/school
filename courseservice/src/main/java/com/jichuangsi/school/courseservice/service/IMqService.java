package com.jichuangsi.school.courseservice.service;

import com.jichuangsi.school.courseservice.model.AnswerForStudent;
import com.jichuangsi.school.courseservice.model.Course;
import com.jichuangsi.school.courseservice.model.Question;
import com.jichuangsi.school.courseservice.model.message.CourseMessageModel;

public interface IMqService {
    void send(Course course);
    void sendMsg4StartCourse(CourseMessageModel courseMsg);
    void sendMsg4PublishQuestion(String courseId, String questionId);
    void sendMsg4SubmitAnswer(String courseId, String questionId, AnswerForStudent answer);
}
