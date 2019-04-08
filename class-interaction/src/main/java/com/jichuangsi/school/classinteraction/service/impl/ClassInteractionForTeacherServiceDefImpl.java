package com.jichuangsi.school.classinteraction.service.impl;

import javax.annotation.Resource;

import com.jichuangsi.school.classinteraction.mq.producer.service.ISendService;
import com.jichuangsi.school.classinteraction.service.IClassInteractionForTeacherService;
import com.jichuangsi.school.classinteraction.websocket.model.RaceQuestion;
import com.jichuangsi.school.classinteraction.websocket.service.ISendToStudentService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service
public class ClassInteractionForTeacherServiceDefImpl implements IClassInteractionForTeacherService {

    @Resource
    private ISendService sendService;

    @Resource
    private ISendToStudentService sendToStudentService;

    @Override
    public void pubRaceQuestion(String courseId, String raceId) {
        RaceQuestion raceQuestion = new RaceQuestion();
        raceQuestion.setCourseId(courseId);
        raceQuestion.setRaceId(raceId);
        sendToStudentService.sendRaceQuestionInfo(raceQuestion);

    }
}
