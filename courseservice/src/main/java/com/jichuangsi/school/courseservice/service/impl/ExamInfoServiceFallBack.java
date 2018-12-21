package com.jichuangsi.school.courseservice.service.impl;

import com.jichuangsi.school.courseservice.model.transfer.TransferExam;
import com.jichuangsi.school.courseservice.service.IExamInfoService;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class ExamInfoServiceFallBack implements IExamInfoService {

    @Override
    public List<TransferExam> getExamForTeacherById(String teacherId){
        return new ArrayList<>();
    }
}
