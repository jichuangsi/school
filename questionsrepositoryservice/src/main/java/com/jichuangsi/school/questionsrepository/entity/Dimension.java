package com.jichuangsi.school.questionsrepository.entity;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.ArrayList;
import java.util.List;

@Document(collection = "school_question_Dimension")
public class Dimension {

    @Id
    private String id;//id1
    private String content;//题干1
    private List<String> options = new ArrayList<String>();//选项1
    private String answer;//答案0
    private String answerDetail;//？？0
    private String parse;//题目解析，描述
    private String type;//题型0
    private String difficulty;//难度0
    private String subjectId;//学科id1
    private List<Knowledge> knowledges = new ArrayList<Knowledge>();//知识点0
    private String questionIdMD52;//关联原题目0
    private long createTime;//0
    private long updateTime;//0

    public String getQuestionIdMD52() {
        return questionIdMD52;
    }

    public void setQuestionIdMD52(String questionIdMD52) {
        this.questionIdMD52 = questionIdMD52;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
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

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
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

    public List<Knowledge> getKnowledges() {
        return knowledges;
    }

    public void setKnowledges(List<Knowledge> knowledges) {
        this.knowledges = knowledges;
    }

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
}
