package com.jichuangsi.school.homeworkservice.service;

import com.jichuangsi.school.homeworkservice.model.common.HomeworkFile;

public interface IFileStoreService {

    HomeworkFile uploadHomeworkFile(HomeworkFile file) throws Exception;

    HomeworkFile downloadHomeworkFile(String fileName) throws Exception;

    void deleteHomeworkFile(String fileName) throws Exception;

}
