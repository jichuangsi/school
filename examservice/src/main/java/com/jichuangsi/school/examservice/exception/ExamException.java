package com.jichuangsi.school.examservice.exception;

public class ExamException extends Exception {
    private String msg;
    public ExamException(String msg){
        this.msg = msg;
    }
}
