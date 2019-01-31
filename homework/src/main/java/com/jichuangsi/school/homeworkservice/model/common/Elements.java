package com.jichuangsi.school.homeworkservice.model.common;

import com.jichuangsi.school.homeworkservice.model.transfer.TransferClass;
import com.jichuangsi.school.homeworkservice.model.transfer.TransferExam;

import java.util.List;

public class Elements {
    private List<TransferExam> transferExams;
    private List<TransferClass> transferClasses;

    public List<TransferExam> getTransferExams() {
        return transferExams;
    }

    public void setTransferExams(List<TransferExam> transferExams) {
        this.transferExams = transferExams;
    }

    public List<TransferClass> getTransferClasses() {
        return transferClasses;
    }

    public void setTransferClasses(List<TransferClass> transferClasses) {
        this.transferClasses = transferClasses;
    }
}
