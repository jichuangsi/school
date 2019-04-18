package com.jichuangsi.school.user.service;

import com.github.pagehelper.PageInfo;
import com.jichuangsi.microservice.common.model.UserInfoForToken;
import com.jichuangsi.school.user.exception.BackUserException;
import com.jichuangsi.school.user.model.SchoolMessageModel;
import com.jichuangsi.school.user.model.backstage.SchoolNoticeModel;
import com.jichuangsi.school.user.model.backstage.TimeTableModel;
import com.jichuangsi.school.user.model.file.UserFile;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface IBackSchoolService {

    TimeTableModel findByClassId(String classId) throws BackUserException;

    @Transactional(rollbackFor = Exception.class)
    void saveTimeTableByExcel(MultipartFile file, String classId, UserInfoForToken userInfo) throws BackUserException;

    void delTimeTable(String tableId, String classId, UserInfoForToken userInfo) throws BackUserException;

    List<UserFile> getAttachments(UserInfoForToken userInfo) throws BackUserException;

    UserFile downAttachment(UserInfoForToken userInfo, String subName) throws BackUserException;

    void sendSchoolMessage(UserInfoForToken userInfo, String schoolId, SchoolMessageModel model) throws BackUserException;

    PageInfo<SchoolNoticeModel> getSchoolNotices(UserInfoForToken userInfo, String schoolId, int pageIndex, int pageSize) throws BackUserException;

    void deleteSchoolNotice(UserInfoForToken userInfo,String schoolId,String noticeId) throws BackUserException;
}