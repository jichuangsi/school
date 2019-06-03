package com.jichuangsi.school.user.service.impl;

import com.jichuangsi.microservice.common.model.UserInfoForToken;
import com.jichuangsi.school.user.constant.ResultCode;
import com.jichuangsi.school.user.constant.Status;
import com.jichuangsi.school.user.entity.backstage.BackRoleInfo;
import com.jichuangsi.school.user.entity.backstage.BackUserInfo;
import com.jichuangsi.school.user.entity.backstage.orz.PromisedInfo;
import com.jichuangsi.school.user.exception.BackUserException;
import com.jichuangsi.school.user.model.backstage.BackRoleModel;
import com.jichuangsi.school.user.model.backstage.orz.PromisedModel;
import com.jichuangsi.school.user.repository.backstage.IBackPromisedInfoRepository;
import com.jichuangsi.school.user.repository.backstage.IBackRoleInfoRepository;
import com.jichuangsi.school.user.repository.backstage.IBackUserInfoRepository;
import com.jichuangsi.school.user.service.IBackRoleService;
import com.jichuangsi.school.user.util.MappingEntity2ModelConverter;
import com.jichuangsi.school.user.util.MappingModel2EntityConverter;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@Service
public class BackRoleServiceImpl implements IBackRoleService {

    @Resource
    private IBackRoleInfoRepository backRoleInfoRepository;
    @Resource
    private IBackUserInfoRepository backUserInfoRepository;
    @Resource
    private IBackPromisedInfoRepository backPromisedInfoRepository;

    @Override
    public void insertRole(UserInfoForToken userInfo , BackRoleModel model) throws BackUserException {
        if (StringUtils.isEmpty(model.getRoleName())){
            throw new BackUserException(ResultCode.PARAM_MISS_MSG);
        }
        if (backRoleInfoRepository.countByRoleNameAndDeleteFlag(model.getRoleName(),"0") > 0){
            throw new BackUserException(ResultCode.ROLE_ISEXIST_MSG);
        }
        BackRoleInfo backRoleInfo = MappingModel2EntityConverter.CONVERTERFROMBACKROLEMODEL(model);
        backRoleInfo.setCreatorId(userInfo.getUserId());
        backRoleInfo.setCreatorName(userInfo.getUserName());
        backRoleInfo.setUpdatedId(userInfo.getUserId());
        backRoleInfo.setUpdatedName(userInfo.getUserName());
        backRoleInfoRepository.save(backRoleInfo);
    }

    @Override
    public void deleteRole(UserInfoForToken userInfo, String roleId) throws BackUserException {
        if (StringUtils.isEmpty(roleId)){
            throw new BackUserException(ResultCode.PARAM_MISS_MSG);
        }
        BackRoleInfo roleInfo = backRoleInfoRepository.findFirstByIdAndDeleteFlag(roleId,"0");
        if (null == roleInfo){
            throw new BackUserException(ResultCode.SELECT_NULL_MSG);
        }
        roleInfo.setDeleteFlag("1");
        roleInfo.setUpdatedName(userInfo.getUserName());
        roleInfo.setUpdatedId(userInfo.getUserId());
        roleInfo.setUpdatedTime(new Date().getTime());
        backRoleInfoRepository.save(roleInfo);
    }

    @Override
    public void bindRole(UserInfoForToken userInfo, String roleId, String userId) throws BackUserException {
        if (StringUtils.isEmpty(roleId) || StringUtils.isEmpty(userId)){
            throw new BackUserException(ResultCode.PARAM_MISS_MSG);
        }
        BackUserInfo backUserInfo = backUserInfoRepository.findFirstByIdAndStatus(userId, Status.INACTIVATE.getName());
        if (null == backUserInfo){
            throw new BackUserException(ResultCode.USER_ISNOT_EXIST);
        }
        BackRoleInfo backRoleInfo = backRoleInfoRepository.findFirstByIdAndDeleteFlag(roleId,"0");
        if (null == backRoleInfo){
            throw new BackUserException(ResultCode.ROLE_ISNOT_EXIST);
        }
        backUserInfo.setRoleId(backRoleInfo.getId());
        backUserInfo.setRoleName(backRoleInfo.getRoleName());
        backUserInfo.setUpdatedTime(new Date().getTime());
        backUserInfo.setUpdatedId(userInfo.getUserId());
        backUserInfo.setUpdatedName(userInfo.getUserName());
        backUserInfo.setStatus(Status.ACTIVATE.getName());
        backUserInfoRepository.save(backUserInfo);
    }

    @Override
    public void insertPromised(UserInfoForToken userInfo, PromisedModel model) throws BackUserException {
        PromisedInfo info = changePromisedModel(model);
        backPromisedInfoRepository.save(info);
    }

    private PromisedInfo changePromisedModel(PromisedModel model){
        PromisedInfo promisedInfo = new PromisedInfo();
        promisedInfo.setName(model.getName());
        promisedInfo.setId(UUID.randomUUID().toString().replace("-",""));
        List<PromisedInfo> promisedInfos = new ArrayList<PromisedInfo>();
        for(PromisedModel promisedModel : model.getPromisedModels()){
            promisedInfos.add(changePromisedModel(promisedModel));
        }
        promisedInfo.setPromised(promisedInfos);
        return promisedInfo;
    }

    @Override
    public List<BackRoleModel> getRoles(UserInfoForToken userInfo) throws BackUserException {
        List<BackRoleInfo> backRoleInfos = backRoleInfoRepository.findByDeleteFlagOrderByCreatedTime("0");
        List<BackRoleModel> backRoleModels = new ArrayList<BackRoleModel>();
        if (!(backRoleInfos.size() > 0)){
            throw new BackUserException(ResultCode.SELECT_NULL_MSG);
        }
        backRoleInfos.forEach(backRoleInfo -> {
            backRoleModels.add(MappingEntity2ModelConverter.CONVERTERFROMBACKROLEINFO(backRoleInfo));
        });
        return backRoleModels;
    }
    @Override
    public List<BackRoleModel> getRoles() throws BackUserException {
        List<BackRoleInfo> backRoleInfos = backRoleInfoRepository.findByDeleteFlagOrderByCreatedTime("0");
        List<BackRoleModel> backRoleModels = new ArrayList<BackRoleModel>();
        if (!(backRoleInfos.size() > 0)){
            throw new BackUserException(ResultCode.SELECT_NULL_MSG);
        }
        backRoleInfos.forEach(backRoleInfo -> {
            backRoleModels.add(MappingEntity2ModelConverter.CONVERTERFROMBACKROLEINFO(backRoleInfo));
        });
        return backRoleModels;
    }

    @Override
    public void bindPromised(UserInfoForToken userInfo, String roleId, List<PromisedModel> models) throws BackUserException {
        if (null == models || StringUtils.isEmpty(roleId) || !(models.size() > 0)){
            throw new BackUserException(ResultCode.PARAM_MISS_MSG);
        }
        BackRoleInfo roleInfo = backRoleInfoRepository.findFirstByIdAndDeleteFlag(roleId,"0");
        if (null == roleInfo){
            throw new BackUserException(ResultCode.ROLE_ISNOT_EXIST);
        }
        List<PromisedInfo> promisedInfos = new ArrayList<PromisedInfo>();
        models.forEach(model -> {
            promisedInfos.add(changePromisedModel(model));
        });
        roleInfo.setPromisedInfos(promisedInfos);
        roleInfo.setUpdatedId(userInfo.getUserId());
        roleInfo.setUpdatedName(userInfo.getUserName());
        roleInfo.setUpdatedTime(new Date().getTime());
        backRoleInfoRepository.save(roleInfo);
    }

    @Override
    public List<PromisedModel> getPromised(UserInfoForToken userInfo) throws BackUserException {
        List<PromisedInfo> promisedInfos = backPromisedInfoRepository.findAll();
        List<PromisedModel> promisedModels = new ArrayList<PromisedModel>();
        promisedInfos.forEach(promisedInfo -> {
            promisedModels.add(changePromisedInfo(promisedInfo));
        });
        return promisedModels;
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
    public void deletePromised(UserInfoForToken userInfo, String promisedId) throws BackUserException {
        if (StringUtils.isEmpty(promisedId)){
            throw new BackUserException(ResultCode.PARAM_MISS_MSG);
        }
        PromisedInfo promisedInfo = backPromisedInfoRepository.findFirstById(promisedId);
        if (null == promisedInfo){
            throw new BackUserException(ResultCode.SELECT_NULL_MSG);
        }
        backPromisedInfoRepository.delete(promisedInfo);
    }
}
