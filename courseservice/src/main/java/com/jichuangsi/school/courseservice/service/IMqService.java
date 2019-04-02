package com.jichuangsi.school.courseservice.service;

import com.jichuangsi.school.courseservice.model.Course;
import com.jichuangsi.school.courseservice.model.TeacherPublishFile;
import com.jichuangsi.school.courseservice.model.message.AnswerMessageModel;
import com.jichuangsi.school.courseservice.model.message.CourseMessageModel;
import com.jichuangsi.school.courseservice.model.message.QuestionMessageModel;
import com.jichuangsi.school.courseservice.model.message.ShareAnswerMessageModel;

public interface IMqService {
    void send(Course course);

    void sendMsg4StartCourse(CourseMessageModel courseMsg);

    void sendMsg4PublishQuestion(QuestionMessageModel questionMsg);

    void sendMsg4TermQuestion(QuestionMessageModel questionMsg);

    void sendMsg4SubmitAnswer(AnswerMessageModel answerMsg);

    void sendMsg4ShareAnswer(ShareAnswerMessageModel shareAnswerMsg);

    //发送发布附件消息
    void sendPublishFile(TeacherPublishFile teacherPublishFile);
}
