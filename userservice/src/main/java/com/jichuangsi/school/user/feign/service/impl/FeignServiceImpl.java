package com.jichuangsi.school.user.feign.service.impl;

import com.jichuangsi.school.user.exception.ClassServiceException;
import com.jichuangsi.school.user.exception.FeignControllerException;
import com.jichuangsi.school.user.feign.model.ClassDetailModel;
import com.jichuangsi.school.user.feign.service.IFeignService;
import com.jichuangsi.school.user.service.ISchoolClassService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service
public class FeignServiceImpl implements IFeignService {
    @Resource
    private ISchoolClassService schoolClassService;

    @Override
    public ClassDetailModel findClassDetailByClassId(String classId) throws FeignControllerException {
        try {
            return schoolClassService.getClassDetail(classId);
        } catch (ClassServiceException e) {
            throw new FeignControllerException(e.getMessage());
        }
    }
}
