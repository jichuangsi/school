package com.jichuangsi.school.testservice.service;

import com.jichuangsi.microservice.common.model.UserInfoForToken;
import com.jichuangsi.school.testservice.exception.FileServiceException;
import com.jichuangsi.school.testservice.model.Attachment;
import com.jichuangsi.school.testservice.model.common.DeleteQueryModel;
import com.jichuangsi.school.testservice.model.common.TestFile;
import org.springframework.transaction.annotation.Transactional;

public interface IFileTransferService {

    @Transactional
    Attachment uploadAttachment(UserInfoForToken userInfo, TestFile file) throws FileServiceException;

    @Transactional
    String uploadPic(UserInfoForToken userInfo, TestFile file) throws FileServiceException;

    @Transactional
    void removeFile(UserInfoForToken userInfo, DeleteQueryModel deleteQueryModel) throws FileServiceException;

    @Transactional
    TestFile downloadFile(UserInfoForToken userInfo, String fileName) throws FileServiceException;

}
