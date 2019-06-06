package com.jichuangsi.school.user.service.impl;

import com.jichuangsi.school.user.entity.RoleInfo;
import com.jichuangsi.school.user.entity.StudentInfo;
import com.jichuangsi.school.user.entity.UserInfo;
import com.jichuangsi.school.user.repository.IStudentInfoRepository;
import com.jichuangsi.school.user.service.IStudentInfoService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;
@Service
@Transactional
public class StudentInfoServiceImpl implements IStudentInfoService{
    @Resource
    private IStudentInfoRepository studentInfoRepository;

    @Override
    public String findStudentClass(String id) {
        UserInfo infos = studentInfoRepository.findFirstById(id);
        if(!infos.equals(null)){
            StudentInfo studentInfo = (StudentInfo) infos.getRoleInfos().get(0);
            String classId = studentInfo.getPrimaryClass().getClassId();
            return classId;
        }
        return null;
    }
}
