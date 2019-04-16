package com.jichuangsi.school.user.service;

import com.jichuangsi.microservice.common.model.UserInfoForToken;
import com.jichuangsi.school.user.exception.BackUserException;
import com.jichuangsi.school.user.model.backstage.TimeTableModel;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

public interface IBackSchoolService {

    TimeTableModel findByClassId(String classId) throws BackUserException;

    @Transactional(rollbackFor = Exception.class)
    void saveTimeTableByExcel(MultipartFile file, String classId, UserInfoForToken userInfo) throws BackUserException;

    void delTimeTable(String tableId,String classId,UserInfoForToken userInfo) throws BackUserException;
}
