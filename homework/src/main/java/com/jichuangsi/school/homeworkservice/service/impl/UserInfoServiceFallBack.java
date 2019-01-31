package com.jichuangsi.school.homeworkservice.service.impl;

import com.jichuangsi.school.homeworkservice.model.transfer.TransferClass;
import com.jichuangsi.school.homeworkservice.model.transfer.TransferStudent;
import com.jichuangsi.school.homeworkservice.model.transfer.TransferTeacher;
import com.jichuangsi.school.homeworkservice.service.IUserInfoService;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class UserInfoServiceFallBack implements IUserInfoService {

    @Override
    public List<TransferClass> getClassForTeacherById(String teacherId){
        return new ArrayList<>();
    }

    @Override
    public TransferTeacher getUserForTeacherById(String teacherId){
        return null;
    }

    @Override
    public List<TransferStudent> getStudentsForClassById(String classId){
        return new ArrayList<>();
    }
}
