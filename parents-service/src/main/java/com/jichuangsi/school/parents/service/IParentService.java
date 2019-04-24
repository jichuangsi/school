package com.jichuangsi.school.parents.service;

import com.github.pagehelper.PageInfo;
import com.jichuangsi.microservice.common.model.UserInfoForToken;
import com.jichuangsi.school.parents.exception.ParentsException;
import com.jichuangsi.school.parents.model.*;
import com.jichuangsi.school.parents.model.http.HttpTokenModel;
import com.jichuangsi.school.parents.model.http.WxUserInfoModel;

import java.util.List;
import java.util.Map;

public interface IParentService {

    void sendParentMessage(UserInfoForToken userInfo, ParentMessageModel model) throws ParentsException;

    PageInfo<ParentMessageModel> getParentMessage(UserInfoForToken userInfo,String teacherId,int pageIndex,int pageSize) throws ParentsException;

    void insertMessageBoard(UserInfoForToken userInfo, MessageBoardModel model) throws ParentsException;

    void bindStudent(UserInfoForToken userInfo,String account) throws ParentsException;

    List<NoticeModel> parentGetNewNotices(UserInfoForToken userInfo) throws ParentsException;

    void deleteParentNotice(UserInfoForToken userInfo,String noticeId) throws ParentsException;

    String loginParentService(UserInfoForToken userInfo,String openId) throws ParentsException;

    String registParentService(ParentModel model) throws ParentsException;

    void setParentAccount(UserInfoForToken userInfo,ParentModel model) throws ParentsException;

    void setParentNewPwd(UserInfoForToken userInfo, UpdatePwdModel model) throws ParentsException;

    String loginParentServiceByAccount(UserInfoForToken userInfo,ParentModel model) throws ParentsException;

    HttpTokenModel findTokenByCode(String coed) throws ParentsException;

    WxUserInfoModel findWxUserInfo(String access_token,String openid,String code) throws ParentsException;

    void getBindStudentInfo(UserInfoForToken userInfo) throws ParentsException;

    NoticeModel findNoticeDetails(UserInfoForToken userInfo,String noticeId) throws ParentsException;

    void deleteBindStudent(UserInfoForToken userInfo , String studentId) throws ParentsException;

    Map<String,Boolean> getParentBindInfo(UserInfoForToken userInfo) throws ParentsException;

    void bindParentPhone(UserInfoForToken userInfo,ParentModel model) throws ParentsException;
}
