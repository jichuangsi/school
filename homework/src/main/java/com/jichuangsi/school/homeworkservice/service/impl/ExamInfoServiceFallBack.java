package com.jichuangsi.school.homeworkservice.service.impl;

import com.jichuangsi.school.homeworkservice.model.transfer.TransferExam;
import com.jichuangsi.school.homeworkservice.service.IExamInfoService;
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
