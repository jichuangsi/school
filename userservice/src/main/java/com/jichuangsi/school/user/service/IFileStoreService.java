package com.jichuangsi.school.user.service;

import com.jichuangsi.school.user.model.file.UserFile;

public interface IFileStoreService {

    void uploadFile(UserFile file) throws Exception;

    void deleteFile(String fileName) throws Exception;

    UserFile downFile(String fileName) throws Exception;
}
