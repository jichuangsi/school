package com.jichuangsi.school.eaxmservice.exception;

public class EaxmException extends Exception {
    private String msg;
    public EaxmException(String msg){
        this.msg = msg;
    }
}
