package com.jichuangsi.school.parents.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.PageInfo;
import com.jichuangsi.microservice.common.model.ResponseModel;
import com.jichuangsi.microservice.common.model.UserInfoForToken;
import com.jichuangsi.school.parents.commons.Md5Util;
import com.jichuangsi.school.parents.commons.ResultCode;
import com.jichuangsi.school.parents.entity.MessageBoard;
import com.jichuangsi.school.parents.entity.ParentInfo;
import com.jichuangsi.school.parents.entity.ParentMessage;
import com.jichuangsi.school.parents.entity.ParentNotice;
import com.jichuangsi.school.parents.exception.ParentHttpException;
import com.jichuangsi.school.parents.exception.ParentsException;
import com.jichuangsi.school.parents.feign.IUserFeignService;
//import com.jichuangsi.school.parents.feign.model.TransferStudent;
import com.jichuangsi.school.parents.model.*;
import com.jichuangsi.school.parents.model.http.HttpTokenModel;
import com.jichuangsi.school.parents.model.http.WxErrorModel;
import com.jichuangsi.school.parents.model.http.WxUserInfoModel;
import com.jichuangsi.school.parents.model.transfer.CourseSignModel;
import com.jichuangsi.school.parents.model.transfer.TransferNoticeToParent;
import com.jichuangsi.school.parents.model.transfer.TransferStudent;
import com.jichuangsi.school.parents.repository.*;
import com.jichuangsi.school.parents.service.IHttpService;
import com.jichuangsi.school.parents.service.IParentService;
import com.jichuangsi.school.parents.service.TokenService;
import com.jichuangsi.school.parents.util.MappingEntity2ModelConverter;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class ParentServiceImpl implements IParentService {

    @Resource
    private IParentMessageRepository parentMessageRepository;
    @Resource
    private IParentMessageExtraRepository parentMessageExtraRepository;
    @Resource
    private IMessageBoardRepository messageBoardRepository;
    @Resource
    private IParentInfoRepository parentInfoRepository;
    @Resource
    private IUserFeignService userFeignService;
    @Resource
    private IParentNoticeRepository parentNoticeRepository;
    @Resource
    private TokenService tokenService;
    @Resource
    private IHttpService httpService;

    @Override
    public void sendParentMessage(UserInfoForToken userInfo, ParentMessageModel model) throws ParentsException {
        if (StringUtils.isEmpty(model.getTeacherId()) || StringUtils.isEmpty(model.getContent()) || StringUtils.isEmpty(model.getTeacherName())){
            throw new ParentsException(ResultCode.PARAM_MISS_MSG);
        }
        ParentMessage message = new ParentMessage();
        message.setContent(model.getContent());
        message.setParentId(userInfo.getUserId());
        message.setParentName(userInfo.getUserName());
        message.setSendFrom(ParentMessage.PARENT_SEND);
        message.setTeacherId(model.getTeacherId());
        message.setTeacherName(model.getTeacherName());
        parentMessageRepository.save(message);
    }

    @Override
    public PageInfo<ParentMessageModel> getParentMessage(UserInfoForToken userInfo, String teacherId, int pageIndex, int pageSize) throws ParentsException {
        if (StringUtils.isEmpty(userInfo.getUserId()) || StringUtils.isEmpty(teacherId)){
            throw new ParentsException(ResultCode.PARAM_MISS_MSG);
        }
        PageInfo<ParentMessageModel> pageInfo = new PageInfo<ParentMessageModel>();
        pageInfo.setTotal(parentMessageRepository.countByParentIdAndTeacherId(userInfo.getUserId(),teacherId));
        pageInfo.setPageSize(pageSize);
        pageInfo.setPageNum(pageIndex);
        List<ParentMessage> parentMessages = parentMessageExtraRepository.findByParentIdAndTeacherIdAndSort(userInfo.getUserId(),teacherId,pageIndex,pageSize);
        List<ParentMessageModel> parentMessageModels = new ArrayList<ParentMessageModel>();
        parentMessages.forEach(parentMessage -> {
            parentMessageModels.add(MappingEntity2ModelConverter.CONVERTERFROMPARENTMESSAGE(parentMessage));
        });
        pageInfo.setList(parentMessageModels);
        return pageInfo;
    }

    @Override
    public void insertMessageBoard(UserInfoForToken userInfo, MessageBoardModel model) throws ParentsException {
        if (StringUtils.isEmpty(model.getContent())){
            throw new ParentsException(ResultCode.PARAM_MISS_MSG);
        }
        MessageBoard board = new MessageBoard();
        board.setContent(model.getContent());
        board.setParentId(userInfo.getUserId());
        board.setParentName(userInfo.getUserName());
        messageBoardRepository.save(board);
    }

    @Override
    public void bindStudent(UserInfoForToken userInfo, String account) throws ParentsException {
        if (StringUtils.isEmpty(account)){
            throw new ParentsException(ResultCode.PARAM_MISS_MSG);
        }
        ResponseModel<TransferStudent> responseModel = userFeignService.getStudentByAccount(account);
        if (!ResultCode.SUCESS.equals(responseModel.getCode())){
            throw new ParentsException(responseModel.getMsg());
        }
        TransferStudent student = responseModel.getData();
        ParentInfo parentInfo = parentInfoRepository.findFirstByIdAndDeleteFlag(userInfo.getUserId(),"0");
        if (StringUtils.isEmpty(parentInfo)){
            throw new ParentsException(ResultCode.PARENT_NOTFOUND_MSG);
        }
        parentInfo.getStudentIds().add(student.getStudentId());
        parentInfoRepository.save(parentInfo);
    }

    @Override
    public List<NoticeModel> parentGetNewNotices(UserInfoForToken userInfo) throws ParentsException {
        if (StringUtils.isEmpty(userInfo.getUserId())){
            throw new ParentsException(ResultCode.PARAM_MISS_MSG);
        }
        List<ParentNotice> parentNotices = parentNoticeRepository.findByParentIdAndDeleteFlag(userInfo.getUserId(),"0");
        List<NoticeModel> models = new ArrayList<NoticeModel>();
        parentNotices.forEach(parentNotice -> {
            models.add(MappingEntity2ModelConverter.CONVERTERFROMPARENTNOTICE(parentNotice));
        });
        return models;
    }

    @Override
    public void deleteParentNotice(UserInfoForToken userInfo, String noticeId) throws ParentsException {
        if (StringUtils.isEmpty(noticeId)){
            throw new ParentsException(ResultCode.PARAM_MISS_MSG);
        }
        ParentNotice notice = parentNoticeRepository.findFirstByIdAndDeleteFlag(noticeId,"0");
        if (null == notice){
            throw new ParentsException(ResultCode.SELECT_NULL_MSG);
        }
        notice.setDeleteFlag("1");
        parentNoticeRepository.save(notice);
    }

    @Override
    public String loginParentService(UserInfoForToken userInfo, String openId) throws ParentsException {
        if (StringUtils.isEmpty(openId)){
            throw new ParentsException(ResultCode.PARAM_MISS_MSG);
        }
        ParentInfo parentInfo = parentInfoRepository.findFirstByWeChatAndDeleteFlag(openId,"0");
        if (null == parentInfo){
            throw new ParentsException(ResultCode.PARENT_NOTFOUND_MSG);
        }
        userInfo = MappingEntity2ModelConverter.CONVERTERFROMPARENTINFO(parentInfo);
        try {
            return tokenService.createdToken(JSONObject.toJSONString(userInfo));
        } catch (UnsupportedEncodingException e) {
            throw new ParentsException(ResultCode.TOKEN_CHECK_ERR_MSG);
        }
    }

    @Override
    public String registParentService(ParentModel model) throws ParentsException {
        if (StringUtils.isEmpty(model.getOpenId())){
            throw new ParentsException(ResultCode.PARAM_MISS_MSG);
        }
        ParentInfo parentInfo = parentInfoRepository.findFirstByWeChatAndDeleteFlag(model.getOpenId(),"0");
        if (null == parentInfo){
            parentInfo = new ParentInfo();
            parentInfo.setPhone(model.getPhone());
            parentInfo.setUserName(model.getUserName());
            parentInfo.setWeChat(model.getOpenId());
            parentInfo.setHeadimgurl(model.getHeadimgurl());
            parentInfoRepository.save(parentInfo);
        }
        UserInfoForToken userInfo = MappingEntity2ModelConverter.CONVERTERFROMPARENTINFO(parentInfo);
        try {
            String userStr = JSONObject.toJSONString(userInfo);
            return tokenService.createdToken(userStr);
        } catch (UnsupportedEncodingException e) {
            throw new ParentsException(ResultCode.TOKEN_CHECK_ERR_MSG);
        }
    }

    @Override
    public void setParentAccount(UserInfoForToken userInfo, ParentModel model) throws ParentsException {
        if (StringUtils.isEmpty(userInfo.getUserId()) || StringUtils.isEmpty(model.getAccount()) || StringUtils.isEmpty(model.getPwd())){
            throw new ParentsException(ResultCode.PARAM_MISS_MSG);
        }
        ParentInfo parentInfo = parentInfoRepository.findFirstByIdAndDeleteFlag(userInfo.getUserId(),"0");
        if (null == parentInfo){
            throw new ParentsException(ResultCode.PARENT_NOTFOUND_MSG);
        }
        if (!StringUtils.isEmpty(parentInfo.getAccount())){
            throw new ParentsException(ResultCode.ACCOUNT_EXIST_MSG);
        }
        if (parentInfoRepository.countByAccountAndDeleteFlag(model.getAccount(),"0") > 0){
            throw new ParentsException(ResultCode.ACCOUNT_ISEXIST_MSG);
        }
        parentInfo.setAccount(model.getAccount());
        parentInfo.setPwd(Md5Util.encodeByMd5(model.getPwd()));
        parentInfoRepository.save(parentInfo);
    }

    @Override
    public void setParentNewPwd(UserInfoForToken userInfo, UpdatePwdModel model) throws ParentsException {
        if (StringUtils.isEmpty(model.getNewPwd()) ||StringUtils.isEmpty(model.getOldPwd())){
            throw new ParentsException(ResultCode.PARAM_MISS_MSG);
        }
        ParentInfo parentInfo = parentInfoRepository.findFirstByIdAndDeleteFlag(userInfo.getUserId(),"0");
        if (null == parentInfo){
            throw new ParentsException(ResultCode.PARENT_NOTFOUND_MSG);
        }
        if (StringUtils.isEmpty(parentInfo.getAccount())){
            throw new ParentsException(ResultCode.ACCOUNT_NOTBIND_MSG);
        }
        if (!Md5Util.encodeByMd5(model.getOldPwd()).equals(Md5Util.encodeByMd5(parentInfo.getPwd()))){
            throw new ParentsException(ResultCode.PWD_VALIDATE_ERR);
        }
        parentInfo.setPwd(Md5Util.encodeByMd5(model.getNewPwd()));
        parentInfoRepository.save(parentInfo);
    }

    @Override
    public String loginParentServiceByAccount(UserInfoForToken userInfo, ParentModel model) throws ParentsException {
        if (StringUtils.isEmpty(model.getPwd()) || StringUtils.isEmpty(model.getAccount())){
            throw new ParentsException(ResultCode.PARAM_MISS_MSG);
        }
        if (!(parentInfoRepository.countByAccountAndDeleteFlag(model.getAccount(),"0") > 0)){
            throw new ParentsException(ResultCode.ACCOUNT_REGIST_ERR);
        }
        ParentInfo parentInfo = parentInfoRepository.findFirstByAccountAndPwdAndDeleteFlag(model.getAccount(),Md5Util.encodeByMd5(model.getPwd()),"0");
        if (null == parentInfo){
            throw new ParentsException(ResultCode.PWD_VALIDATE_ERR);
        }
        userInfo = MappingEntity2ModelConverter.CONVERTERFROMPARENTINFO(parentInfo);
        try {
            return tokenService.createdToken(JSONObject.toJSONString(userInfo));
        } catch (UnsupportedEncodingException e) {
            throw new ParentsException(ResultCode.TOKEN_CHECK_ERR_MSG);
        }
    }

    @Override
    public HttpTokenModel findTokenByCode(String code) throws ParentsException {
        if (StringUtils.isEmpty(code)){
            throw new ParentsException(ResultCode.PARAM_MISS_MSG);
        }
        String result = "";
        try {
            result = httpService.findWxTokenModel(code);
        } catch (ParentHttpException e) {
            throw new ParentsException(e.getMessage());
        }
        HttpTokenModel tokenModel =  JSONObject.parseObject(result,HttpTokenModel.class);
        if (StringUtils.isEmpty(tokenModel.getAccess_token())){
            WxErrorModel model = new WxErrorModel();
            model = JSONObject.parseObject(result,WxErrorModel.class);
            throw new ParentsException(model.getErrmsg());
        }
        return tokenModel;
    }

/*    @Override
    public HttpTokenModel findTokenByCode2() throws ParentsException{
        String result = "";
        try {
            result = httpService.findWxTokenModel2();
        } catch (ParentHttpException e) {
            throw new ParentsException(e.getMessage());
        }
        HttpTokenModel tokenModel =  JSONObject.parseObject(result,HttpTokenModel.class);
        if (StringUtils.isEmpty(tokenModel.getAccess_token())){
            WxErrorModel model = new WxErrorModel();
            model = JSONObject.parseObject(result,WxErrorModel.class);
            throw new ParentsException(model.getErrmsg());
        }
        return tokenModel;
    }*/

    @Override
    public WxUserInfoModel findWxUserInfo(String access_token, String openid/*,String code*/) throws ParentsException{
        if (StringUtils.isEmpty(access_token) || StringUtils.isEmpty(openid)){
            throw new ParentsException(ResultCode.PARAM_MISS_MSG);
        }
        String result = "";
        try {
            result = httpService.findWxUserInfo(access_token,openid/*,code*/);
        } catch (ParentHttpException e) {
            throw new ParentsException(e.getMessage());
        }
        WxUserInfoModel wx =  JSONObject.parseObject(result,WxUserInfoModel.class);
        if (StringUtils.isEmpty(wx.getOpenid())){
            WxErrorModel model = JSONObject.parseObject(result,WxErrorModel.class);
            throw new ParentsException(model.getErrmsg());
        }
        return wx;
    }

  /*  @Override
    public WxUserInfoModel findWxUserInfo2(String access_token,String openid) throws ParentsException{
        if (StringUtils.isEmpty(access_token) || StringUtils.isEmpty(openid)){
            throw new ParentsException(ResultCode.PARAM_MISS_MSG);
        }
        String result = "";
        try {
            result = httpService.findWxUserInfo2(access_token,openid);
        } catch (ParentHttpException e) {
            throw new ParentsException(e.getMessage());
        }
        WxUserInfoModel wx =  JSONObject.parseObject(result,WxUserInfoModel.class);
        if (StringUtils.isEmpty(wx.getOpenid())){
            WxErrorModel model = JSONObject.parseObject(result,WxErrorModel.class);
            throw new ParentsException(model.getErrmsg());
        }
        return wx;
    }*/

    @Override
    public void getBindStudentInfo(UserInfoForToken userInfo) throws ParentsException {
        if (StringUtils.isEmpty(userInfo.getUserId())){
            throw new ParentsException(ResultCode.PARAM_MISS_MSG);
        }
        ParentInfo parentInfo = parentInfoRepository.findFirstByIdAndDeleteFlag(userInfo.getUserId(),"0");
        if (null == parentInfo){
            throw new ParentsException(ResultCode.PARENT_NOTFOUND_MSG);
        }
        if (!(parentInfo.getStudentIds().size() > 0)){
            throw new ParentsException(ResultCode.STUDNET_NOTBIND_MSG);
        }
    }

    @Override
    public NoticeModel findNoticeDetails(UserInfoForToken userInfo, String noticeId) throws ParentsException {
        if (StringUtils.isEmpty(userInfo.getUserId()) || StringUtils.isEmpty(noticeId)){
            throw new ParentsException(ResultCode.PARAM_MISS_MSG);
        }
        ParentNotice parentNotice = parentNoticeRepository.findFirstByIdAndParentId(noticeId,userInfo.getUserId());
        if (null == parentNotice){
            throw new ParentsException(ResultCode.SELECT_NULL_MSG);
        }
        parentNotice.setRead("1");
        parentNoticeRepository.save(parentNotice);
        ResponseModel<NoticeModel> responseModel = userFeignService.getNoticeDetails(parentNotice.getMessageId());
        if (!ResultCode.SUCESS.equals(responseModel.getCode())){
            throw new ParentsException(responseModel.getMsg());
        }
        return responseModel.getData();
    }

    @Override
    public void deleteBindStudent(UserInfoForToken userInfo, String studentId) throws ParentsException {
        if (StringUtils.isEmpty(userInfo.getUserId()) || StringUtils.isEmpty(studentId)){
            throw new ParentsException(ResultCode.PARAM_MISS_MSG);
        }
        ParentInfo parentInfo = parentInfoRepository.findFirstByIdAndDeleteFlag(userInfo.getUserId(),"0");
        if (null == parentInfo){
            throw new ParentsException(ResultCode.PARENT_NOTFOUND_MSG);
        }
        parentInfo.getStudentIds().remove(studentId);
        parentInfoRepository.save(parentInfo);
    }

    @Override
    public Map<String, Boolean> getParentBindInfo(UserInfoForToken userInfo) throws ParentsException {
        if (StringUtils.isEmpty(userInfo.getUserId())){
            throw new ParentsException(ResultCode.PARAM_MISS_MSG);
        }
        ParentInfo parentInfo = parentInfoRepository.findFirstByIdAndDeleteFlag(userInfo.getUserId(),"0");
        if(null == parentInfo){
            throw new ParentsException(ResultCode.PARENT_NOTFOUND_MSG);
        }
        Map<String,Boolean> map = new HashMap<String, Boolean>();
        map.put("phone",!StringUtils.isEmpty(parentInfo.getPhone()));
        map.put("account",!StringUtils.isEmpty(parentInfo.getAccount()));
        return map;
    }

    @Override
    public void bindParentPhone(UserInfoForToken userInfo, ParentModel model) throws ParentsException {
        if (StringUtils.isEmpty(userInfo.getUserId()) || StringUtils.isEmpty(model.getPhone())){
            throw new ParentsException(ResultCode.PARAM_MISS_MSG);
        }
        ParentInfo parentInfo = parentInfoRepository.findFirstByIdAndDeleteFlag(userInfo.getUserId(),"0");
        parentInfo.setPhone(model.getPhone());
        parentInfoRepository.save(parentInfo);
    }

    @Override
    public boolean addNoticeToParents(TransferNoticeToParent transferNoticeToParent)  throws ParentsException{

        List<ParentNotice> parentNotices = new ArrayList<ParentNotice>();
        for (String studentId : transferNoticeToParent.getStudentIds()) {
            List<ParentInfo> parentInfos = parentInfoRepository.findByStudentIdsContaining(studentId);
            for (ParentInfo parentInfo : parentInfos) {
                ParentNotice notice = new ParentNotice();
                notice.setMessageId(transferNoticeToParent.getMessageId());
                notice.setNoticeType(transferNoticeToParent.getMessageType());
                notice.setParentId(parentInfo.getId());
                notice.setParentName(StringUtils.isEmpty(parentInfo.getUserName()) ? "" : parentInfo.getUserName());
                notice.setTitle(transferNoticeToParent.getMessageTitle());
                parentNotices.add(notice);
                //parentNoticeRepository.save(notice);
            }
        }
        parentNoticeRepository.saveAll(parentNotices);

        return true;
    }

    @Override
    public boolean removeNoticeToParents(String messageId) throws ParentsException{
        List<ParentNotice> notices = parentNoticeRepository.findByMessageId(messageId);
        if (null != notices && notices.size() > 0){
            notices.stream().forEach(n->{
                parentNoticeRepository.deleteById(n.getId());
            });
        }
        return true;
    }

    @Override
    public boolean sendParentStudentMsg(CourseSignModel model) throws ParentsException {
        if (StringUtils.isEmpty(model.getCourseId()) || StringUtils.isEmpty(model.getCourseName())){
            throw new ParentsException(ResultCode.PARAM_MISS_MSG);
        }
        List<ParentNotice> parentNotices = new ArrayList<ParentNotice>();
        for (TransferStudent student : model.getStudents()) {
            try {
                ParentInfo parentInfo = parentInfoRepository.findFirstByStudentIdsContainingAndDeleteFlag(student.getStudentId(),"0");
                ParentNotice notice = new ParentNotice();
                notice.setTitle(model.getSubjectName() + "课堂：" + model.getCourseName());
                //notice.setCourse(model.getCourseId(), model.getCourseName(), model.getTeacherName(), model.getTeacherId(), model.getSubjectName(),model.getSubjectId());
                notice.setNoticeType(ParentNotice.SYSTEM_NOTICE);
                if ("0".equals(student.getSignFlag())){
                    notice.setContent(student.getStudentName()+"未签到课堂");
                }else{
                    notice.setContent(student.getStudentName()+"已完成课堂");
                }
                if(student.getCommendFlag()>0){
                    String commendTemp = "受到老师"+student.getCommendFlag()+"次表扬";
                    notice.setContent(StringUtils.isEmpty(notice.getContent())?student.getStudentName()+commendTemp:"，" + commendTemp);
                }
                notice.setParentName(StringUtils.isEmpty(parentInfo.getUserName())?"":parentInfo.getUserName());
                notice.setParentId(parentInfo.getId());
                parentNotices.add(notice);
            } catch (Exception e) {
                continue;
            }
        }
        parentNoticeRepository.saveAll(parentNotices);

        return true;
    }
}
