package com.jichuangsi.school.statistics.model;

import com.jichuangsi.school.statistics.feign.model.Knowledge;

import java.util.ArrayList;
import java.util.List;
//用来查询认知能力以及题目的正确错误
public class SearchCapabilityModel {
   /* private List<String> questionIds = new ArrayList<String>();
    private String subjectId;*/
  private String questionId;
    private String id;
   private String status;
   private List<Knowledge> knowledges=new ArrayList<Knowledge>();

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
public String getQuestionId() {
        return questionId;
    }

    public void setQuestionId(String questionId) {
        this.questionId = questionId;
    }

    public List<Knowledge> getKnowledges() {
        return knowledges;
    }

    public String getStatus() {
        return status;
    }

    public void setKnowledges(List<Knowledge> knowledges) {
        this.knowledges = knowledges;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
