package com.jichuangsi.school.testservice.service;

import com.jichuangsi.school.testservice.model.common.TestFile;

public interface IFileStoreService {

    TestFile uploadTestFile(TestFile file) throws Exception;

    TestFile downloadTestFile(String fileName) throws Exception;

    void deleteTestFile(String fileName) throws Exception;

}
