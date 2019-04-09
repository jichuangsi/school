package com.jichuangsi.school.user.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.jichuangsi.microservice.common.model.UserInfoForToken;
import com.jichuangsi.school.user.commons.Md5Util;
import com.jichuangsi.school.user.constant.ResultCode;
import com.jichuangsi.school.user.constant.Status;
import com.jichuangsi.school.user.entity.backstage.BackRoleInfo;
import com.jichuangsi.school.user.entity.backstage.BackUserInfo;
import com.jichuangsi.school.user.entity.backstage.orz.PromisedInfo;
import com.jichuangsi.school.user.exception.BackUserException;
import com.jichuangsi.school.user.exception.SchoolServiceException;
import com.jichuangsi.school.user.model.backstage.BackUserModel;
import com.jichuangsi.school.user.model.backstage.UpdatePwdModel;
import com.jichuangsi.school.user.model.backstage.orz.PromisedModel;
import com.jichuangsi.school.user.model.school.SchoolModel;
import com.jichuangsi.school.user.repository.backstage.IBackRoleInfoRepository;
import com.jichuangsi.school.user.repository.backstage.IBackUserInfoRepository;
import com.jichuangsi.school.user.service.BackTokenService;
import com.jichuangsi.school.user.service.IBackUserService;
import com.jichuangsi.school.user.service.ISchoolClassService;
import com.jichuangsi.school.user.util.MappingEntity2ModelConverter;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class BackUserServiceImpl implements IBackUserService {

    @Resource
    private IBackUserInfoRepository backUserInfoRepository;
    @Resource
    private ISchoolClassService schoolClassService;
    @Resource
    private BackTokenService tokenService;
    @Resource
    private IBackRoleInfoRepository backRoleInfoRepository;

    @Override
    public String registBackUser(BackUserModel model) throws BackUserException {
        if (StringUtils.isEmpty(model.getAccount()) || StringUtils.isEmpty(model.getPwd())
                || StringUtils.isEmpty(model.getUserName()) || StringUtils.isEmpty(model.getSchoolId())){
            throw new BackUserException(ResultCode.PARAM_MISS_MSG);
        }
        if (backUserInfoRepository.countByAccount(model.getAccount()) > 0){
            throw new BackUserException(ResultCode.ACCOUNT_ISEXIST_MSG);
        }
        SchoolModel schoolModel = new SchoolModel();
        try {
            schoolModel = schoolClassService.getSchoolBySchoolId(model.getSchoolId());
        } catch (SchoolServiceException e) {
            throw new BackUserException(e.getMessage());
        }
        if (null == schoolModel){
            throw new BackUserException(ResultCode.SELECT_NULL_MSG);
        }
        BackUserInfo backUserInfo = new BackUserInfo();
        backUserInfo.setAccount(model.getAccount());
        backUserInfo.setSchoolId(schoolModel.getSchoolId());
        backUserInfo.setSchoolName(schoolModel.getSchoolName());
        backUserInfo.setPwd(Md5Util.encodeByMd5(model.getPwd()));
        backUserInfo.setUserName(model.getUserName());
        backUserInfo.setStatus(Status.INACTIVATE.getName());
        backUserInfo = backUserInfoRepository.save(backUserInfo);
        return backUserInfo.getId();
    }

    @Override
    public String loginBackUser(BackUserModel model) throws BackUserException {
        if (StringUtils.isEmpty(model.getAccount()) || StringUtils.isEmpty(model.getPwd())){
            throw new BackUserException(ResultCode.PARAM_MISS_MSG);
        }
        BackUserInfo backUserInfo = backUserInfoRepository.findFirstByAccountAndPwdAndStatus(model.getAccount(),Md5Util.encodeByMd5(model.getPwd()),Status.ACTIVATE.getName());
        if (null == backUserInfo){
            throw new BackUserException(ResultCode.ACCOUNT_NOTEXIST_MSG);
        }
        String user = JSONObject.toJSONString(MappingEntity2ModelConverter.CONVERTERFROMBACKUSERINFO(backUserInfo));
        try {
           return tokenService.createdToken(user);
        } catch (UnsupportedEncodingException e) {
            throw new BackUserException(e.getMessage());
        }
    }

    @Override
    public void deleteBackUser(UserInfoForToken userInfo, BackUserModel model) throws BackUserException {
        if (StringUtils.isEmpty(model.getId())){
            throw new BackUserException(ResultCode.PARAM_MISS_MSG);
        }
        BackUserInfo backUserInfo = backUserInfoRepository.findFirstByIdAndStatus(model.getId(),Status.ACTIVATE.getName());
        if (null == backUserInfo){
            throw new BackUserException(ResultCode.ACCOUNT_NOTEXIST_MSG);
        }
        if (!userInfo.getSchoolId().equals(backUserInfo.getSchoolId())){
            throw new BackUserException(ResultCode.ACCOUNT_DELETEPOWER_MSG);
        }
        backUserInfo.setStatus(Status.DELETE.getName());
        backUserInfoRepository.save(backUserInfo);
    }

    @Override
    public void updateBackUserPwd(UserInfoForToken userInfo, UpdatePwdModel model) throws BackUserException {
        if (StringUtils.isEmpty(model.getNewPwd()) || StringUtils.isEmpty(model.getOldPwd())){
            throw new BackUserException(ResultCode.PARAM_MISS_MSG);
        }
        BackUserInfo backUserInfo = backUserInfoRepository.findFirstByAccountAndPwdAndStatus(userInfo.getUserNum(),Md5Util.encodeByMd5(model.getOldPwd()),Status.ACTIVATE.getName());
        if(null == backUserInfo){
            throw new BackUserException(ResultCode.PWD_NOT_MSG);
        }
        backUserInfo.setPwd(Md5Util.encodeByMd5(model.getNewPwd()));
        backUserInfoRepository.save(backUserInfo);
    }

    @Override
    public List<SchoolModel> getBackSchools() throws BackUserException {
        List<SchoolModel> schoolModels = new ArrayList<SchoolModel>();
        try {
            schoolModels = schoolClassService.getBackSchools();
        } catch (SchoolServiceException e) {
            throw new BackUserException(e.getMessage());
        }
        if (null == schoolModels || !(schoolModels.size() > 0 )){
            throw new BackUserException(ResultCode.SELECT_NULL_MSG);
        }
        return schoolModels;
    }

    @Override
    public List<BackUserModel> getSchoolUserInfo(UserInfoForToken userInfo) throws BackUserException {
        if (StringUtils.isEmpty(userInfo.getSchoolId()) && !"M".equals(userInfo.getRoleName())){
            throw new BackUserException(ResultCode.PARAM_MISS_MSG);
        }
        List<BackUserInfo> backUserInfos = new ArrayList<BackUserInfo>();
        if (!"M".equals(userInfo.getRoleName())) {
            backUserInfos = backUserInfoRepository.findBySchoolIdAndStatusNot(userInfo.getSchoolId(), Status.DELETE.getName());
        }else {
            backUserInfos = backUserInfoRepository.findByStatusNot(Status.DELETE.getName());
        }
        if (null == backUserInfos || !(backUserInfos.size() > 0)){
            throw new BackUserException(ResultCode.USER_ISNOT_EXIST);
        }
        List<BackUserModel> backUserModels = new ArrayList<BackUserModel>();
        backUserInfos.forEach(backUserInfo -> {
            backUserModels.add(MappingEntity2ModelConverter.CONVERTERFROMBACKUSERINFOTOMODEL(backUserInfo));
        });
        return backUserModels;
    }

    @Override
    public BackUserModel getUserInfoAndPromised(UserInfoForToken userInfo) throws BackUserException {
        if (StringUtils.isEmpty(userInfo.getUserId())){
            throw new BackUserException(ResultCode.PARAM_MISS_MSG);
        }
        BackUserInfo backUserInfo = backUserInfoRepository.findFirstByIdAndStatus(userInfo.getUserId(),Status.ACTIVATE.getName());
        if (null == backUserInfo){
            throw new BackUserException(ResultCode.USER_ISNOT_EXIST);
        }
        BackUserModel model = MappingEntity2ModelConverter.CONVERTERFROMBACKUSERINFOTOMODEL(backUserInfo);
        if (!StringUtils.isEmpty(model.getRoleId()) && !"M".equals(model.getRoleName())){
            BackRoleInfo roleInfo = backRoleInfoRepository.findFirstByIdAndDeleteFlag(model.getRoleId(),"0");
            if (null == roleInfo){
                throw new BackUserException(ResultCode.ROLE_ISNOT_EXIST);
            }
            List<PromisedModel> promisedModels = new ArrayList<PromisedModel>();
            for (PromisedInfo promisedInfo : roleInfo.getPromisedInfos()){
                promisedModels.add(changePromisedInfo(promisedInfo));
            }
            model.setPromisedModels(promisedModels);
        }
        return model;
    }

    private PromisedModel changePromisedInfo(PromisedInfo info){
        PromisedModel model = new PromisedModel();
        model.setId(info.getId());
        model.setName(info.getName());
        List<PromisedModel> promisedModels = new ArrayList<PromisedModel>();
        for (PromisedInfo promisedInfo : info.getPromised() ){
            promisedModels.add(changePromisedInfo(promisedInfo));
        }
        model.setPromisedModels(promisedModels);
        return model;
    }

    @Override
    public void insertSuperMan() throws BackUserException {
        if (backUserInfoRepository.countByAccount("Admin") > 0){
            throw new BackUserException(ResultCode.ACCOUNT_ISEXIST_MSG);
        }
        BackUserInfo userInfo = new BackUserInfo();
        userInfo.setStatus(Status.ACTIVATE.getName());
        userInfo.setRoleId("123456");
        userInfo.setRoleName("M");
        userInfo.setAccount("Admin");
        userInfo.setUserName("admin");
        userInfo.setPwd(Md5Util.encodeByMd5("admin"));
        userInfo.setCreatedTime(new Date().getTime());
        backUserInfoRepository.save(userInfo);
    }

    @Override
    public void updateOtherPwd(UserInfoForToken userInfo, UpdatePwdModel model,String userId) throws BackUserException {
        if (StringUtils.isEmpty(userId) || StringUtils.isEmpty(model.getNewPwd())){
            throw new BackUserException(ResultCode.PARAM_MISS_MSG);
        }
        BackUserInfo backUserInfo = backUserInfoRepository.findFirstByIdAndStatus(userId,Status.ACTIVATE.getName());
        if(null == backUserInfo){
            throw new BackUserException(ResultCode.USER_ISNOT_EXIST);
        }
        backUserInfo.setPwd(Md5Util.encodeByMd5(model.getNewPwd()));
        backUserInfo.setUserName(userInfo.getUserName());
        backUserInfo.setUpdatedId(userInfo.getUserId());
        backUserInfo.setUpdatedTime(new Date().getTime());
        backUserInfoRepository.save(backUserInfo);
    }
}
