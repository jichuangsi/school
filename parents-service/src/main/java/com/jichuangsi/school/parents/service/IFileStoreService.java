package com.jichuangsi.school.parents.service;

import com.jichuangsi.school.parents.model.file.ParentFile;

public interface IFileStoreService {

    void uploadFile(ParentFile file) throws Exception;

    void deleteFile(String fileName) throws Exception;

    ParentFile downFile(String fileName) throws Exception;
}
