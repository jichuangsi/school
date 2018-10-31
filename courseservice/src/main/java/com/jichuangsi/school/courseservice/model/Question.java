package com.jichuangsi.school.courseservice.model;

import com.jichuangsi.school.courseservice.constant.Status;

import java.util.ArrayList;
import java.util.List;

public class Question {
    //题目基本信息
    String questionId;
    String questionContent;
    List<String> options = new ArrayList<String>();
    String answer;
    String answerDetail;
    String parse;
    String quesetionType;
    String difficulty;
    String subjectId;
    String gradeId;
    String knowledge;
    String questionIdMD52;
    Status questionStatus;

    int pageNum;
    int pageSize;

    public String getQuestionId() {
        return questionId;
    }

    public void setQuestionId(String questionId) {
        this.questionId = questionId;
    }

    public String getQuestionContent() {
        return questionContent;
    }

    public void setQuestionContent(String questionContent) {
        this.questionContent = questionContent;
    }

    public List<String> getOptions() {
        return options;
    }

    public void setOptions(List<String> options) {
        this.options = options;
    }

    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }

    public String getAnswerDetail() {
        return answerDetail;
    }

    public void setAnswerDetail(String answerDetail) {
        this.answerDetail = answerDetail;
    }

    public String getParse() {
        return parse;
    }

    public void setParse(String parse) {
        this.parse = parse;
    }

    public String getQuesetionType() {
        return quesetionType;
    }

    public void setQuesetionType(String quesetionType) {
        this.quesetionType = quesetionType;
    }

    public String getDifficulty() {
        return difficulty;
    }

    public void setDifficulty(String difficulty) {
        this.difficulty = difficulty;
    }

    public String getSubjectId() {
        return subjectId;
    }

    public void setSubjectId(String subjectId) {
        this.subjectId = subjectId;
    }

    public String getKnowledge() {
        return knowledge;
    }

    public void setKnowledge(String knowledge) {
        this.knowledge = knowledge;
    }

    public String getQuestionIdMD52() {
        return questionIdMD52;
    }

    public void setQuestionIdMD52(String questionIdMD52) {
        this.questionIdMD52 = questionIdMD52;
    }

    public String getGradeId() {
        return gradeId;
    }

    public void setGradeId(String gradeId) {
        this.gradeId = gradeId;
    }

    public Status getQuestionStatus() {
        return questionStatus;
    }

    public void setQuestionStatus(Status questionStatus) {
        this.questionStatus = questionStatus;
    }

    public int getPageNum() {
        return pageNum;
    }

    public void setPageNum(int pageNum) {
        this.pageNum = pageNum;
    }

    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }
}
