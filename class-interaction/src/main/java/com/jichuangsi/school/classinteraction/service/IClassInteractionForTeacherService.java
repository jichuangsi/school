package com.jichuangsi.school.classinteraction.service;
/**
 * @author huangjiajun
 *
 */
/**
 * 课堂互动非WS交互接口（老师）
 */
public interface IClassInteractionForTeacherService {
    /**
     * 老师发布抢答
     */
    void pubRaceQuestion(String courseId, String raceId);

}