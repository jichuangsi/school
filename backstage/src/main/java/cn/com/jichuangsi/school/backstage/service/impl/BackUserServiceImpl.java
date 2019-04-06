package cn.com.jichuangsi.school.backstage.service.impl;

import cn.com.jichuangsi.school.backstage.commons.Md5Util;
import cn.com.jichuangsi.school.backstage.commons.ResultCode;
import cn.com.jichuangsi.school.backstage.constant.Status;
import cn.com.jichuangsi.school.backstage.entity.BackUserInfo;
import cn.com.jichuangsi.school.backstage.exception.BackUserException;
import cn.com.jichuangsi.school.backstage.feign.model.SchoolModel;
import cn.com.jichuangsi.school.backstage.feign.service.IUserFeignService;
import cn.com.jichuangsi.school.backstage.model.BackUserModel;
import cn.com.jichuangsi.school.backstage.model.UpdatePwdModel;
import cn.com.jichuangsi.school.backstage.repository.IBackUserInfoRepository;
import cn.com.jichuangsi.school.backstage.service.IBackUserService;
import cn.com.jichuangsi.school.backstage.service.TokenService;
import cn.com.jichuangsi.school.backstage.util.MappingEntity2ModelConverter;
import com.alibaba.fastjson.JSONObject;
import com.jichuangsi.microservice.common.model.ResponseModel;
import com.jichuangsi.microservice.common.model.UserInfoForToken;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.io.UnsupportedEncodingException;
import java.util.List;

@Service
public class BackUserServiceImpl implements IBackUserService {

    @Resource
    private IBackUserInfoRepository backUserInfoRepository;
    @Resource
    private IUserFeignService userFeignService;
    @Resource
    private TokenService tokenService;

    @Override
    public void registBackUser(BackUserModel model) throws BackUserException {
        if (StringUtils.isEmpty(model.getAccount()) || StringUtils.isEmpty(model.getPwd())
                || StringUtils.isEmpty(model.getUserName()) || StringUtils.isEmpty(model.getSchoolId())){
            throw new BackUserException(ResultCode.PARAM_MISS_MSG);
        }
        if (backUserInfoRepository.countByAccount(model.getAccount()) > 0){
            throw new BackUserException(ResultCode.ACCOUNT_ISEXIST_MSG);
        }
        ResponseModel<SchoolModel> response = userFeignService.getSchoolBySchoolId(model.getSchoolId());
        if (!ResultCode.SUCESS.equals(response.getCode())){
            throw new BackUserException(response.getMsg());
        }
        BackUserInfo backUserInfo = new BackUserInfo();
        backUserInfo.setAccount(model.getAccount());
        backUserInfo.setSchoolId(response.getData().getSchoolId());
        backUserInfo.setSchoolName(response.getData().getSchoolName());
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
        if (!userInfo.getSchoolId().equals(model.getSchoolId())){
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
        ResponseModel<List<SchoolModel>> responseModel = userFeignService.getBackSchools();
        if (!ResultCode.SUCESS.equals(responseModel.getCode())){
            throw new BackUserException(responseModel.getMsg());
        }
        return responseModel.getData();
    }
}
