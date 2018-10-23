package com.jichuangsi.school.courseservice.service;

import com.jichuangsi.school.courseservice.model.AnswerForStudent;
import com.jichuangsi.school.courseservice.model.Course;
import com.jichuangsi.school.courseservice.model.Question;
import com.jichuangsi.school.courseservice.model.message.AnswerMessageModel;
import com.jichuangsi.school.courseservice.model.message.CourseMessageModel;
import com.jichuangsi.school.courseservice.model.message.QuestionMessageModel;

public interface IMqService {
    void send(Course course);
    void sendMsg4StartCourse(CourseMessageModel courseMsg);
    void sendMsg4PublishQuestion(QuestionMessageModel questionMsg);
    void sendMsg4TermQuestion(QuestionMessageModel questionMsg);
    void sendMsg4SubmitAnswer(AnswerMessageModel answerMsg);
}
