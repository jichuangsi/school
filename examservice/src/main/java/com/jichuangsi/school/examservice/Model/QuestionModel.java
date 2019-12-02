package com.jichuangsi.school.examservice.Model;

import com.jichuangsi.school.examservice.constant.Status;

import java.util.ArrayList;
import java.util.List;

public class QuestionModel {
    //题目基本信息
    private String questionId;
    private String questionContent;
    private List<String> options = new ArrayList<String>();
    private String answer;
    private String answerDetail;
    private String parse;
    private String quesetionType;
    private String difficulty;
    private String subjectId;
    private String gradeId;
    private List<Knowledge> knowledges = new ArrayList<Knowledge>();
    private String questionIdMD52;
    private Status questionStatus;
    private String questionPic;

    private long createTime;
    private long updateTime;

    public long getCreateTime() {
        return createTime;
    }

    public void setCreateTime(long createTime) {
        this.createTime = createTime;
    }

    public long getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(long updateTime) {
        this.updateTime = updateTime;
    }

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

    public String getQuestionPic() {
        return questionPic;
    }

    public void setQuestionPic(String questionPic) {
        this.questionPic = questionPic;
    }

    public List<Knowledge> getKnowledges() {
        return knowledges;
    }

    public void setKnowledges(List<Knowledge> knowledges) {
        this.knowledges = knowledges;
    }

    public QuestionModel(String questionContent,
                         List<String> options, String answer, String answerDetail,
                         String parse, String quesetionType, String difficulty,
                         String subjectId, String gradeId, List<Knowledge> knowledges,
                         String questionIdMD52, Status questionStatus, String questionPic,
                         long createTime, long updateTime) {
        this.questionContent = questionContent;
        this.options = options;
        this.answer = answer;
        this.answerDetail = answerDetail;
        this.parse = parse;
        this.quesetionType = quesetionType;
        this.difficulty = difficulty;
        this.subjectId = subjectId;
        this.gradeId = gradeId;
        this.knowledges = knowledges;
        this.questionIdMD52 = questionIdMD52;
        this.questionStatus = questionStatus;
        this.questionPic = questionPic;
        this.createTime = createTime;
        this.updateTime = updateTime;
    }

    public QuestionModel() {
    }
}
