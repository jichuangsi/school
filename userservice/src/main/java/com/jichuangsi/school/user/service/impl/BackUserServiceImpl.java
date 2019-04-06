package com.jichuangsi.school.user.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.jichuangsi.microservice.common.model.UserInfoForToken;
import com.jichuangsi.school.user.commons.Md5Util;
import com.jichuangsi.school.user.constant.ResultCode;
import com.jichuangsi.school.user.constant.Status;
import com.jichuangsi.school.user.entity.backstage.BackUserInfo;
import com.jichuangsi.school.user.exception.BackUserException;
import com.jichuangsi.school.user.exception.SchoolServiceException;
import com.jichuangsi.school.user.model.backstage.BackUserModel;
import com.jichuangsi.school.user.model.backstage.UpdatePwdModel;
import com.jichuangsi.school.user.model.school.SchoolModel;
import com.jichuangsi.school.user.repository.backstage.IBackUserInfoRepository;
import com.jichuangsi.school.user.service.IBackUserService;
import com.jichuangsi.school.user.service.ISchoolClassService;
import com.jichuangsi.school.user.service.BackTokenService;
import com.jichuangsi.school.user.util.MappingEntity2ModelConverter;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

@Service
public class BackUserServiceImpl implements IBackUserService {

    @Resource
    private IBackUserInfoRepository backUserInfoRepository;
    @Resource
    private ISchoolClassService schoolClassService;
    @Resource
    private BackTokenService tokenService;

    @Override
    public void registBackUser(BackUserModel model) throws BackUserException {
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
        backUserInfoRepository.save(backUserInfo);
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
}
