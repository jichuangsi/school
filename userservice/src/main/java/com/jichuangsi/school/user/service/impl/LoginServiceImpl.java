package com.jichuangsi.school.user.service.impl;

import com.alibaba.fastjson.JSON;
import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.jichuangsi.microservice.common.model.UserInfoForToken;
import com.jichuangsi.school.user.commons.MyResultCode;
import com.jichuangsi.school.user.exception.UserServiceException;
import com.jichuangsi.school.user.model.UserBaseInfo;
import com.jichuangsi.school.user.entity.UserInfo;
import com.jichuangsi.school.user.model.UserLoginModel;
import com.jichuangsi.school.user.repository.UserRepository;
import com.jichuangsi.school.user.service.LoginService;
import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;


@Service
@Transactional
public class LoginServiceImpl implements LoginService {
    @Autowired
    private UserRepository userRepository;
    @Value("${com.jichuangsi.school.token.userClaim}")
    private String userClaim;
    @Resource
    private Algorithm algorithm;
    @Override
    public UserLoginModel login(UserBaseInfo userBaseInfo)  throws UserServiceException {
        //根据页面传回来的用户Id查找数据库用户信息
        UserInfo userInfo=userRepository.findOneByUserId(userBaseInfo.getUserId());

        String md = DigestUtils.md5Hex(userBaseInfo.getPwd());
        //判断用户信息是否存在
        if (userInfo==null){
            throw new UserServiceException(MyResultCode.USER_UNEXITS);
            //用户信息存在了,就判断加密的密码是否匹配
        }else if (!md.equals(userInfo.getPwd())){
            //返回错误信息
            throw new UserServiceException(MyResultCode.USER_PASSWORD_CHECK);
        }else {
            //如果对了
            String token =this.getToken(userInfo);
            return UserLoginModel.successToLogin(token,userBaseInfo);
        }
    }
    /*
    * 这里只用到userInfo 页面注册信息 同时把密码进行加密
    * */
    @Override
    public boolean regist(UserInfo useInfo) {
        System.out.println(useInfo);
        //检查是否输入账号密码
        //进行Md5加密
        String s = DigestUtils.md5Hex(useInfo.getPwd());



        //检查是否输入信息
        if(useInfo.getUserId()==""||useInfo.getPwd()==""||useInfo.getUserId()==null||useInfo.getPwd()==null){
            return false;
        }
        //检查是否查到信息
        if (userRepository.findOneByUserId(useInfo.getUserId())!=null) {
            return false;
        }else {
            useInfo.setFlag("0");
            useInfo.setPwd(s);
            userRepository.save(useInfo);
            return true;
        }
    }

    private String getToken(UserInfo userInfo){
        String timeStamp = String.valueOf(System.currentTimeMillis());
        String userJson = JSON.toJSONString(new  UserInfoForToken(userInfo.getUserId(),userInfo.getUserNum(),userInfo.getUserName(),userInfo.getClassId(),timeStamp));
        return JWT.create().withClaim(userClaim, userJson).sign(algorithm);
    }
}