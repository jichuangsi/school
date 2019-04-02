package com.jichuangsi.school.user.feign.service.impl;

import com.jichuangsi.microservice.common.constant.ResultCode;
import com.jichuangsi.school.user.entity.TeacherInfo;
import com.jichuangsi.school.user.entity.UserInfo;
import com.jichuangsi.school.user.exception.ClassServiceException;
import com.jichuangsi.school.user.exception.FeignControllerException;
import com.jichuangsi.school.user.feign.model.ClassDetailModel;
import com.jichuangsi.school.user.feign.service.IFeignService;
import com.jichuangsi.school.user.repository.UserRepository;
import com.jichuangsi.school.user.service.ISchoolClassService;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

@Service
public class FeignServiceImpl implements IFeignService {
    @Resource
    private ISchoolClassService schoolClassService;
    @Resource
    private UserRepository userRepository;

    @Override
    public ClassDetailModel findClassDetailByClassId(String classId) throws FeignControllerException {
        try {
            return schoolClassService.getClassDetail(classId);
        } catch (ClassServiceException e) {
            throw new FeignControllerException(e.getMessage());
        }
    }

    @Override
    public List<String> getClassIdsByTeacherId(String teacherId) throws FeignControllerException {
        if (StringUtils.isEmpty(teacherId)) {
            throw new FeignControllerException(ResultCode.PARAM_MISS_MSG);
        }
        List<String> classIds = new ArrayList<String>();
        UserInfo userInfo = userRepository.findFirstById(teacherId);
        if (userInfo.getRoleInfos() instanceof TeacherInfo) {
            TeacherInfo teacherInfo = (TeacherInfo) userInfo.getRoleInfos();
            if ("Teacher".equals(teacherInfo.getRoleName())) {
                teacherInfo.getSecondaryClasses().forEach(classInfo -> {
                    classIds.add(classInfo.getClassId());
                });
                classIds.add(teacherInfo.getPrimaryClass().getClassId());
            } else {
                throw new FeignControllerException(ResultCode.ROLE_NOT_RIGHT);
            }
        }
        return classIds;
    }
}
