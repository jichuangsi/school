package com.jichuangsi.school.questionsrepository.service.impl;

import com.jichuangsi.school.questionsrepository.model.transfer.TransferSchool;
import com.jichuangsi.school.questionsrepository.model.transfer.TransferTeacher;
import com.jichuangsi.school.questionsrepository.service.IUserInfoService;
import org.springframework.stereotype.Component;

import java.util.ArrayList;

@Component
public class UserInfoServiceFallBack implements IUserInfoService {

    @Override
    public TransferTeacher getUserForTeacherById(String teacherId){
        return null;
    }

    @Override
    public TransferSchool getSchoolInfoById(String userId){
        return null;
    }
}
