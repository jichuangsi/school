package cn.com.jichuangsi.school.backstage.service.impl;

import cn.com.jichuangsi.school.backstage.commons.ResultCode;
import cn.com.jichuangsi.school.backstage.entity.BackRoleInfo;
import cn.com.jichuangsi.school.backstage.exception.BackUserException;
import cn.com.jichuangsi.school.backstage.model.BackRoleModel;
import cn.com.jichuangsi.school.backstage.repository.IBackRoleInfoRepository;
import cn.com.jichuangsi.school.backstage.service.IBackRoleService;
import cn.com.jichuangsi.school.backstage.util.MappingModel2EntityConverter;
import com.jichuangsi.microservice.common.model.UserInfoForToken;
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
