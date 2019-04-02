package com.jichuangsi.school.classinteraction.service.impl;

import com.jichuangsi.school.classinteraction.model.TeacherPublishFile;
import com.jichuangsi.school.classinteraction.mq.producer.service.ISendService;
import com.jichuangsi.school.classinteraction.service.IClassInteractionForTeacherService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service
public class ClassInteractionForTeacherServiceImpl implements IClassInteractionForTeacherService {

    @Resource
    private ISendService sendService;



}
