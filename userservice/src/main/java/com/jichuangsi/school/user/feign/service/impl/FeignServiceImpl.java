package com.jichuangsi.school.user.feign.service.impl;

import com.jichuangsi.microservice.common.constant.ResultCode;
import com.jichuangsi.school.user.entity.StudentInfo;
import com.jichuangsi.school.user.entity.TeacherInfo;
import com.jichuangsi.school.user.entity.UserInfo;
import com.jichuangsi.school.user.exception.ClassServiceException;
import com.jichuangsi.school.user.exception.FeignControllerException;
import com.jichuangsi.school.user.exception.SchoolServiceException;
import com.jichuangsi.school.user.feign.model.ClassDetailModel;
import com.jichuangsi.school.user.feign.model.ParentStudentModel;
import com.jichuangsi.school.user.feign.service.IFeignService;
import com.jichuangsi.school.user.model.school.SchoolModel;
import com.jichuangsi.school.user.model.transfer.TransferStudent;
import com.jichuangsi.school.user.repository.UserRepository;
import com.jichuangsi.school.user.service.ISchoolClassService;
import com.jichuangsi.school.user.service.UserInfoService;
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
    @Resource
    private UserInfoService userInfoService;

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
        TeacherInfo teacherInfo = (TeacherInfo) userInfo.getRoleInfos().get(0);
        if ("Teacher".equals(teacherInfo.getRoleName())) {
            if (null != teacherInfo.getSecondaryClasses()) {
                teacherInfo.getSecondaryClasses().forEach(classInfo -> {
                    classIds.add(classInfo.getClassId());
                });
            }
            if (null != teacherInfo.getPrimaryClass()) {
                classIds.add(teacherInfo.getPrimaryClass().getClassId());
            }
        } else {
            throw new FeignControllerException(ResultCode.ROLE_NOT_RIGHT);
        }
        return classIds;
    }

    @Override
    public List<ClassDetailModel> findClassDetailByClassIds(List<String> classIds) throws FeignControllerException {
        List<ClassDetailModel> classDetailModels = new ArrayList<ClassDetailModel>();
        for (String classId : classIds) {
            classDetailModels.add(findClassDetailByClassId(classId));
        }
        return classDetailModels;
    }

    @Override
    public List<TransferStudent> findStudentsByClassId(String classId) throws FeignControllerException {
        return userInfoService.getStudentsByClassId(classId);
    }

    @Override
    public SchoolModel findSchoolBySchoolId(String schoolId) throws FeignControllerException {
        try {
            return schoolClassService.getSchoolBySchoolId(schoolId);
        } catch (SchoolServiceException e) {
            throw  new FeignControllerException(e.getMessage());
        }
    }

    @Override
    public List<SchoolModel> findBackSchools() throws FeignControllerException {
        try {
            return schoolClassService.getBackSchools();
        } catch (SchoolServiceException e) {
            throw new FeignControllerException(e.getMessage());
        }
    }

    @Override
    public List<ParentStudentModel> getParentStudent(List<String> studentIds) throws FeignControllerException {
        if (null == studentIds || !(studentIds.size() > 0)){
            throw new FeignControllerException(ResultCode.PARAM_MISS_MSG);
        }
        List<ParentStudentModel> parentStudentModels = new ArrayList<ParentStudentModel>();
        for (String studentId : studentIds) {
            UserInfo student = userRepository.findFirstById(studentId);
            if (null == student) {
                throw new FeignControllerException(ResultCode.SELECT_NULL_MSG);
            }
            ParentStudentModel studentModel = new ParentStudentModel();
            studentModel.setStudentId(studentId);
            studentModel.setStudentName(student.getName());
            if (student.getRoleInfos().get(0) instanceof StudentInfo) {
                StudentInfo studentInfo = (StudentInfo) student.getRoleInfos().get(0);
                studentModel.setClassId(studentInfo.getPrimaryClass().getClassId());
                studentModel.setClassName(studentInfo.getPrimaryClass().getClassName());
                studentModel.setSchoolId(studentInfo.getSchool().getSchoolId());
                studentModel.setSchoolName(studentInfo.getSchool().getSchoolName());
            }
            parentStudentModels.add(studentModel);
        }
        return parentStudentModels;
    }
}
