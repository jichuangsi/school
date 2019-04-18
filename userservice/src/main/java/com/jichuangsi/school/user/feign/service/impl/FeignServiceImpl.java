package com.jichuangsi.school.user.feign.service.impl;

import com.jichuangsi.school.user.constant.ResultCode;
import com.jichuangsi.school.user.constant.Status;
import com.jichuangsi.school.user.entity.StudentInfo;
import com.jichuangsi.school.user.entity.TeacherInfo;
import com.jichuangsi.school.user.entity.UserInfo;
import com.jichuangsi.school.user.entity.backstage.SchoolNoticeInfo;
import com.jichuangsi.school.user.exception.*;
import com.jichuangsi.school.user.feign.model.ClassDetailModel;
import com.jichuangsi.school.user.feign.model.ClassTeacherInfoModel;
import com.jichuangsi.school.user.feign.model.NoticeModel;
import com.jichuangsi.school.user.feign.model.ParentStudentModel;
import com.jichuangsi.school.user.feign.service.IFeignService;
import com.jichuangsi.school.user.model.backstage.TimeTableModel;
import com.jichuangsi.school.user.model.school.SchoolModel;
import com.jichuangsi.school.user.model.transfer.TransferStudent;
import com.jichuangsi.school.user.repository.ISchoolNoticeInfoRepository;
import com.jichuangsi.school.user.repository.UserRepository;
import com.jichuangsi.school.user.service.IBackSchoolService;
import com.jichuangsi.school.user.service.ISchoolClassService;
import com.jichuangsi.school.user.service.UserInfoService;
import com.jichuangsi.school.user.util.MappingEntity2ModelConverter;
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
    @Resource
    private IBackSchoolService backSchoolService;
    @Resource
    private ISchoolNoticeInfoRepository schoolNoticeInfoRepository;

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
            throw new FeignControllerException(e.getMessage());
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
        if (null == studentIds || !(studentIds.size() > 0)) {
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

    @Override
    public ClassDetailModel getStudentClassDetail(String studentId) throws FeignControllerException {
        if (StringUtils.isEmpty(studentId)) {
            throw new FeignControllerException(ResultCode.PARAM_MISS_MSG);
        }
        UserInfo student = userRepository.findFirstById(studentId);
        if (null == student) {
            throw new FeignControllerException(ResultCode.USER_SELECT_NULL_MSG);
        }
        ClassDetailModel model = new ClassDetailModel();
        if (student.getRoleInfos().get(0) instanceof StudentInfo) {
            StudentInfo studentInfo = (StudentInfo) student.getRoleInfos().get(0);
            model = findClassDetailByClassId(studentInfo.getPrimaryClass().getClassId());
        }
        return model;
    }

    @Override
    public List<ClassTeacherInfoModel> getStudentTeachers(String studentId) throws FeignControllerException {
        if (StringUtils.isEmpty(studentId)) {
            throw new FeignControllerException(ResultCode.PARAM_MISS_MSG);
        }
        try {
            List<ClassTeacherInfoModel> userInfos = userInfoService.getStudentTeachers(studentId);
            return userInfos;
        } catch (UserServiceException e) {
            throw new FeignControllerException(e.getMessage());
        }
    }

    @Override
    public TransferStudent getStudentByAccount(String account) throws FeignControllerException {
        if (StringUtils.isEmpty(account)){
            throw new FeignControllerException(ResultCode.PARAM_MISS_MSG);
        }
        UserInfo userInfo = userRepository.findFirstByAccountAndStatus(account, Status.ACTIVATE.getName());
        if (null == userInfo){
            throw new FeignControllerException(ResultCode.USER_ISNOT_EXIST);
        }
        return MappingEntity2ModelConverter.TransferStudent(userInfo);
    }

    @Override
    public TimeTableModel getStudentTimeTable(String studentId) throws FeignControllerException {
        if (StringUtils.isEmpty(studentId)){
            throw new FeignControllerException(ResultCode.PARAM_MISS_MSG);
        }
        UserInfo student = userRepository.findFirstById(studentId);
        String classId = "";
        if (student.getRoleInfos().get(0) instanceof StudentInfo){
            StudentInfo studentInfo = (StudentInfo) student.getRoleInfos().get(0);
            classId = studentInfo.getPrimaryClass().getClassId();
        }
        try {
            return backSchoolService.findByClassId(classId);
        } catch (BackUserException e) {
            throw new FeignControllerException(e.getMessage());
        }
    }

    @Override
    public NoticeModel getNoticeDetailByNoticeId(String noticeId) throws FeignControllerException {
        if (StringUtils.isEmpty(noticeId)){
            throw new FeignControllerException(ResultCode.PARAM_MISS_MSG);
        }
        SchoolNoticeInfo noticeInfo = schoolNoticeInfoRepository.findFirstByIdAndDeleteFlag(noticeId,"0");
        if (null == noticeInfo){
            throw new FeignControllerException(ResultCode.SELECT_NULL_MSG);
        }
        return MappingEntity2ModelConverter.CONVERTERFROMSCHOOLNOTICEINFO(noticeInfo);
    }
}
