package com.jichuangsi.school.user.service.impl;

import com.alibaba.fastjson.JSON;
import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.jichuangsi.microservice.common.model.UserInfoForToken;
import com.jichuangsi.school.user.constant.MyResultCode;
import com.jichuangsi.school.user.constant.Status;
import com.jichuangsi.school.user.entity.RoleInfo;
import com.jichuangsi.school.user.entity.StudentInfo;
import com.jichuangsi.school.user.exception.UserServiceException;
import com.jichuangsi.school.user.model.System.User;
import com.jichuangsi.school.user.entity.UserInfo;
import com.jichuangsi.school.user.model.UserLoginModel;
import com.jichuangsi.school.user.repository.UserRepository;
import com.jichuangsi.school.user.service.LoginService;
import com.jichuangsi.school.user.util.MappingEntity2ModelConverter;
import com.jichuangsi.school.user.util.MappingModel2EntityConverter;
import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;

@Service
@Transactional
public class LoginServiceImpl implements LoginService {
    @Resource
    private UserRepository userRepository;
    @Value("${com.jichuangsi.school.token.userClaim}")
    private String userClaim;
    @Resource
    private Algorithm algorithm;
    @Override
    public UserLoginModel login(User user)  throws UserServiceException {
        //根据页面传回来的用户Id查找数据库用户信息
        UserInfo userInfo=userRepository.findOneByAccount(user.getUserAccount());

        String md = DigestUtils.md5Hex(user.getUserPwd());
        //判断用户信息是否存在
        if (userInfo==null
                || Status.DELETE.getName().equalsIgnoreCase(userInfo.getStatus())
                || Status.INACTIVATE.getName().equalsIgnoreCase(userInfo.getStatus())){
            throw new UserServiceException(MyResultCode.USER_UNEXITS);
            //用户信息存在了,就判断加密的密码是否匹配
        }else if (!md.equals(userInfo.getPwd())){
            //返回错误信息
            throw new UserServiceException(MyResultCode.USER_PASSWORD_CHECK);
        }else {
            //如果对了
            String token = this.getToken(userInfo);
            if(StringUtils.isEmpty(token)) throw new UserServiceException(MyResultCode.TOKEN_ERROR);
            return UserLoginModel.successToLogin(token, MappingEntity2ModelConverter.ConvertUser(userInfo));
        }
    }
    /*
    * 这里只用到userInfo 页面注册信息 同时把密码进行加密
    * */
    @Override
    public UserInfo register(User user)  throws UserServiceException{
        if(StringUtils.isEmpty(user.getUserAccount()) || StringUtils.isEmpty(user.getUserPwd())){
            throw new UserServiceException(MyResultCode.PARAM_MISS_MSG);
        }
        //检查是否查到信息
        if (userRepository.findOneByAccount(user.getUserAccount())!=null) {
            throw new UserServiceException(MyResultCode.USER_EXISTED);
        }else {
            user.setUserStatus(Status.ACTIVATE);
            user.setUserPwd(DigestUtils.md5Hex(user.getUserPwd()));
            return userRepository.save(MappingModel2EntityConverter.ConvertUser(user));
        }
    }

    private String getToken(UserInfo userInfo){
        String timeStamp = String.valueOf(System.currentTimeMillis());

        String userJson = null;

        if(userInfo.getRoleInfos().contains(new StudentInfo())){
            for(RoleInfo s : userInfo.getRoleInfos()){
                if(s instanceof StudentInfo){
                    userJson = JSON.toJSONString(new  UserInfoForToken(userInfo.getId(),null,userInfo.getName(),((StudentInfo) s).getPrimaryClass().getClassId(),timeStamp));
                    break;
                }
            }
        }else{
            userJson = JSON.toJSONString(new  UserInfoForToken(userInfo.getId(),null,userInfo.getName(),null,timeStamp));
        }


        return StringUtils.isEmpty(userJson)?null:JWT.create().withClaim(userClaim, userJson).sign(algorithm);
    }
}