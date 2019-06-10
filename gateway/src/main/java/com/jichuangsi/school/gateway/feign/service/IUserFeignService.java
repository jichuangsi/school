package com.jichuangsi.school.gateway.feign.service;

import com.jichuangsi.microservice.common.model.ResponseModel;
import com.jichuangsi.school.gateway.feign.model.UrlMapping;
import com.jichuangsi.school.gateway.feign.model.User;
import com.jichuangsi.school.gateway.feign.service.impl.UserFallBackServiceImpl;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@FeignClient(value = "userservice" ,fallback = UserFallBackServiceImpl.class)
public interface IUserFeignService {
    @GetMapping("/findUser")
    ResponseModel<User> findUser(@RequestParam("id") String id);

    @GetMapping("/getAllRole")
    ResponseModel<List<UrlMapping>> getAllRole();

    @GetMapping("/getAllFreeUrl")
    ResponseModel<List<UrlModel>> getAllFreeUrl();
}
