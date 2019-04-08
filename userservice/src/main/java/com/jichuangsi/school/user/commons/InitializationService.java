package com.jichuangsi.school.user.commons;

import com.jichuangsi.school.user.exception.BackUserException;
import com.jichuangsi.school.user.service.IBackUserService;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;

@Component
public class InitializationService {
    @Resource
    private IBackUserService backUserService;

    @PostConstruct
    public void insertSuperMan(){
        try {
            backUserService.insertSuperMan();
        } catch (Exception e) {
        }
    }
}
