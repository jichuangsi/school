package com.jichuangsi.school.gateway.feign.service.impl;

import com.jichuangsi.microservice.common.model.ResponseModel;
import com.jichuangsi.microservice.common.model.UserInfoForToken;
import com.jichuangsi.school.gateway.feign.model.UrlModel;
import com.jichuangsi.school.gateway.feign.model.UrlMapping;
import com.jichuangsi.school.gateway.feign.model.User;
import com.jichuangsi.school.gateway.feign.service.IUserFeignService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserFallBackServiceImpl implements IUserFeignService {
    private final String ERR_MSG="调用微服务失败";

    @Override
    public ResponseModel<User> findUser(String id) {
         return ResponseModel.fail("",ERR_MSG);
    }

    @Override
    public ResponseModel<List<UrlMapping>> getAllRole() {
        return ResponseModel.fail("",ERR_MSG);
    }

    @Override
    public ResponseModel<List<UrlModel>> getAllFreeUrl() {
        return ResponseModel.fail("",ERR_MSG);
    }
}
