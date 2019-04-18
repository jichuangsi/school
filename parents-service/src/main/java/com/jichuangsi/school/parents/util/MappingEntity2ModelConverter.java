package com.jichuangsi.school.parents.util;

import com.jichuangsi.microservice.common.model.UserInfoForToken;
import com.jichuangsi.school.parents.entity.GrowthDay;
import com.jichuangsi.school.parents.entity.ParentInfo;
import com.jichuangsi.school.parents.entity.ParentMessage;
import com.jichuangsi.school.parents.entity.ParentNotice;
import com.jichuangsi.school.parents.model.GrowthModel;
import com.jichuangsi.school.parents.model.NoticeModel;
import com.jichuangsi.school.parents.model.ParentMessageModel;
import com.jichuangsi.school.parents.model.file.ParentFile;

public class MappingEntity2ModelConverter {

    private MappingEntity2ModelConverter(){}

    public final static GrowthModel CONVERTERFROMGROWTHDAYANDPARENTFILE(GrowthDay growthDay, ParentFile file){
        GrowthModel model = new GrowthModel();
        model.setId(growthDay.getId());
        model.setCreatedTime(growthDay.getCreatedTime());
        model.setStudentId(growthDay.getStudentId());
        model.setTitle(growthDay.getTitle());
        model.setParentFile(file);
        return model;
    }

    public static final ParentMessageModel CONVERTERFROMPARENTMESSAGE(ParentMessage message){
        ParentMessageModel model = new ParentMessageModel();
        model.setContent(message.getContent());
        model.setCreatedTime(message.getCreatedTime());
        model.setId(message.getId());
        model.setParentId(message.getParentId());
        model.setParentName(message.getParentName());
        model.setSendFrom(message.getSendFrom());
        model.setTeacherId(message.getTeacherId());
        model.setTeacherName(message.getTeacherName());
        return model;
    }

    public static final NoticeModel CONVERTERFROMPARENTNOTICE(ParentNotice notice){
        NoticeModel model = new NoticeModel();
        model.setContent(notice.getContent());
        model.setCreatedTime(notice.getCreatedTime());
        model.setId(notice.getId());
        model.setNoticeType(notice.getNoticeType());
        model.setParentId(notice.getParentId());
        model.setParentName(notice.getParentName());
        model.setTitle(notice.getTitle());
        model.setNoticeId(notice.getMessageId());
        model.setRead(notice.getRead());
        return model;
    }

    public static final UserInfoForToken CONVERTERFROMPARENTINFO(ParentInfo parentInfo){
        UserInfoForToken userInfo = new UserInfoForToken();
        userInfo.setUserNum(parentInfo.getAccount());
        userInfo.setUserId(parentInfo.getId());
        userInfo.setUserName(parentInfo.getUserName());
        userInfo.setRoleName("P");
        return userInfo;
    }
}
