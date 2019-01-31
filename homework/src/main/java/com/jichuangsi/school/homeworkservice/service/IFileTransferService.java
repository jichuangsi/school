package com.jichuangsi.school.homeworkservice.service;

import com.jichuangsi.microservice.common.model.UserInfoForToken;
import com.jichuangsi.school.homeworkservice.exception.FileServiceException;
import com.jichuangsi.school.homeworkservice.model.Attachment;
import com.jichuangsi.school.homeworkservice.model.common.DeleteQueryModel;
import com.jichuangsi.school.homeworkservice.model.common.HomeworkFile;
import org.springframework.transaction.annotation.Transactional;

public interface IFileTransferService {

    @Transactional
    Attachment uploadAttachment(UserInfoForToken userInfo, HomeworkFile file) throws FileServiceException;

    @Transactional
    String uploadPic(UserInfoForToken userInfo, HomeworkFile file) throws FileServiceException;

    @Transactional
    void removeFile(UserInfoForToken userInfo, DeleteQueryModel deleteQueryModel) throws FileServiceException;

    @Transactional
    HomeworkFile downloadFile(UserInfoForToken userInfo, String fileName) throws FileServiceException;

}
