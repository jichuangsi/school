package com.jichuangsi.school.courseservice.model;

import com.jichuangsi.school.courseservice.model.transfer.TransferClass;
import com.jichuangsi.school.courseservice.model.transfer.TransferExam;;

import java.io.Serializable;
import java.util.List;

public class EaxmAndClass implements Serializable{
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
