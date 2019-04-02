package com.jichuangsi.school.classinteraction.mq.consumer.student;

import com.alibaba.fastjson.JSONObject;
import com.jichuangsi.school.classinteraction.model.TeacherPublishFile;
import com.jichuangsi.school.classinteraction.mq.consumer.AbstractReceiver;
import com.jichuangsi.school.classinteraction.websocket.service.ISendToStudentService;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

@Component
public class CoursePublishFilleReceiver extends AbstractReceiver {

    @Resource
    private ISendToStudentService sendToStudentService;


    @Override
    public void process(String jsonData) {
        TeacherPublishFile publishFile = JSONObject.parseObject(jsonData,TeacherPublishFile.class);
        sendToStudentService.sendPublishFileInfo(publishFile);
    }

    @Override
    @RabbitListener(queuesToDeclare = { @Queue(value = "${custom.mq.consumer.queue-name.file-publish}") })
    public void processWithLog(String jsonData) {
        super.processWithLog(jsonData);
    }
}
