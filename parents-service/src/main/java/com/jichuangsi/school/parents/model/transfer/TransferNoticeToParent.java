package com.jichuangsi.school.parents.model.transfer;

import java.util.ArrayList;
import java.util.List;

public class TransferNoticeToParent {

    private List<String> studentIds = new ArrayList<>();
    private String messageId;
    private String messageTitle;
    private String messageType;

    public List<String> getStudentIds() {
        return studentIds;
    }

    public void setStudentIds(List<String> studentIds) {
        this.studentIds.addAll(studentIds);
    }

    public String getMessageId() {
        return messageId;
    }

    public void setMessageId(String messageId) {
        this.messageId = messageId;
    }

    public String getMessageTitle() {
        return messageTitle;
    }

    public void setMessageTitle(String messageTitle) {
        this.messageTitle = messageTitle;
    }

    public String getMessageType() {
        return messageType;
    }

    public void setMessageType(String messageType) {
        this.messageType = messageType;
    }
}
