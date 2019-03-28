package com.jichuangsi.school.testservice.service.impl;

import com.jichuangsi.microservice.common.model.UserInfoForToken;
import com.jichuangsi.school.testservice.constant.ResultCode;
import com.jichuangsi.school.testservice.exception.FileServiceException;
import com.jichuangsi.school.testservice.model.Attachment;
import com.jichuangsi.school.testservice.model.common.DeleteQueryModel;
import com.jichuangsi.school.testservice.model.common.TestFile;
import com.jichuangsi.school.testservice.service.IFileStoreService;
import com.jichuangsi.school.testservice.service.IFileTransferService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service
public class FileTransferServiceImpl implements IFileTransferService {

    @Resource
    private IFileStoreService fileStoreService;

    @Override
    public Attachment uploadAttachment(UserInfoForToken userInfo, TestFile file) throws FileServiceException{
        try {
            fileStoreService.uploadTestFile(file);
        } catch (Exception e) {
            throw new FileServiceException(ResultCode.FILE_UPLOAD_ERROR);
        }
        return new Attachment(file.getName(), file.getStoredName(), file.getContentType());
    }

    @Override
    public String uploadPic(UserInfoForToken userInfo, TestFile file) throws FileServiceException{
        try{
            fileStoreService.uploadTestFile(file);
        }catch (Exception exp){
            throw new FileServiceException(exp.getMessage());
        }
        return file.getStoredName();
    }

    @Override
    public void removeFile(UserInfoForToken userInfo, DeleteQueryModel deleteQueryModel) throws FileServiceException {
        try{
            for(String d : deleteQueryModel.getIds()){
                fileStoreService.deleteTestFile(d);
            }
        }catch (Exception exp){
            throw new FileServiceException(exp.getMessage());
        }
    }

    @Override
    public TestFile downloadFile(UserInfoForToken userInfo, String fileName) throws FileServiceException{
        try{
            return fileStoreService.downloadTestFile(fileName);
        }catch (Exception exp){
            throw new FileServiceException(exp.getMessage());
        }
    }

}
