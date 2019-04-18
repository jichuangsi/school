package com.jichuangsi.school.user.init;

import com.jichuangsi.school.user.service.IAttachmentService;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;

@Component
public class InitComponent {

    @Resource
    private IAttachmentService attachmentService;

    @PostConstruct
    public void saveAttachment(){
        attachmentService.saveAttachments();//将resource/xml底下所有文件上传
    }
}
