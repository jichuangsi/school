package com.jichuangsi.school.parents.service;

import com.github.pagehelper.PageInfo;
import com.jichuangsi.microservice.common.model.UserInfoForToken;
import com.jichuangsi.school.parents.exception.ParentsException;
import com.jichuangsi.school.parents.model.MessageBoardModel;
import com.jichuangsi.school.parents.model.NoticeModel;
import com.jichuangsi.school.parents.model.ParentMessageModel;

import java.util.List;

public interface IParentService {

    void sendParentMessage(UserInfoForToken userInfo, ParentMessageModel model) throws ParentsException;

    PageInfo<ParentMessageModel> getParentMessage(UserInfoForToken userInfo,String teacherId,int pageIndex,int pageSize) throws ParentsException;

    void insertMessageBoard(UserInfoForToken userInfo, MessageBoardModel model) throws ParentsException;

    void bindStudent(UserInfoForToken userInfo,String account) throws ParentsException;

    List<NoticeModel> parentGetNewNotices(UserInfoForToken userInfo) throws ParentsException;

    void deleteParentNotice(UserInfoForToken userInfo,String noticeId) throws ParentsException;
}
