package com.jichuangsi.school.user.service.impl;

import com.jichuangsi.microservice.common.model.UserInfoForToken;
import com.jichuangsi.school.user.constant.ResultCode;
import com.jichuangsi.school.user.entity.backstage.BackRoleInfo;
import com.jichuangsi.school.user.exception.BackUserException;
import com.jichuangsi.school.user.model.backstage.BackRoleModel;
import com.jichuangsi.school.user.repository.backstage.IBackRoleInfoRepository;
import com.jichuangsi.school.user.service.IBackRoleService;
import com.jichuangsi.school.user.util.MappingModel2EntityConverter;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.util.Date;

@Service
public class BackRoleServiceImpl implements IBackRoleService {

    @Resource
    private IBackRoleInfoRepository backRoleInfoRepository;

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
}
