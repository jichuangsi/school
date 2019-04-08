package com.jichuangsi.school.homeworkservice.service.impl;

import com.jichuangsi.microservice.common.model.UserInfoForToken;
import com.jichuangsi.school.homeworkservice.constant.ResultCode;
import com.jichuangsi.school.homeworkservice.exception.FileServiceException;
import com.jichuangsi.school.homeworkservice.model.Attachment;
import com.jichuangsi.school.homeworkservice.model.common.DeleteQueryModel;
import com.jichuangsi.school.homeworkservice.model.common.HomeworkFile;
import com.jichuangsi.school.homeworkservice.service.IFileStoreService;
import com.jichuangsi.school.homeworkservice.service.IFileTransferService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service
public class FileTransferServiceImpl implements IFileTransferService {

    @Resource
    private IFileStoreService fileStoreService;

    @Override
    public Attachment uploadAttachment(UserInfoForToken userInfo, HomeworkFile file) throws FileServiceException{
        try {
            fileStoreService.uploadHomeworkFile(file);
        } catch (Exception e) {
            throw new FileServiceException(ResultCode.FILE_UPLOAD_ERROR);
        }
        return new Attachment(file.getName(), file.getStoredName(), file.getContentType());
    }

    @Override
    public String uploadPic(UserInfoForToken userInfo, HomeworkFile file) throws FileServiceException{
        try{
            fileStoreService.uploadHomeworkFile(file);
        }catch (Exception exp){
            throw new FileServiceException(exp.getMessage());
        }
        return file.getStoredName();
    }

    @Override
    public void removeFile(UserInfoForToken userInfo, DeleteQueryModel deleteQueryModel) throws FileServiceException {
        try{
            for(String d : deleteQueryModel.getIds()){
                fileStoreService.deleteHomeworkFile(d);
            }
        }catch (Exception exp){
            throw new FileServiceException(exp.getMessage());
        }
    }

    @Override
    public HomeworkFile downloadFile(UserInfoForToken userInfo, String fileName) throws FileServiceException{
        try{
            return fileStoreService.downloadHomeworkFile(fileName);
        }catch (Exception exp){
            throw new FileServiceException(exp.getMessage());
        }
    }

}
