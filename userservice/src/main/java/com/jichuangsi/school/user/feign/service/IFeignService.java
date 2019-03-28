package com.jichuangsi.school.user.feign.service;

import com.jichuangsi.school.user.exception.FeignControllerException;
import com.jichuangsi.school.user.feign.model.ClassDetailModel;

public interface IFeignService {

    ClassDetailModel findClassDetailByClassId(String classId) throws FeignControllerException;
}
