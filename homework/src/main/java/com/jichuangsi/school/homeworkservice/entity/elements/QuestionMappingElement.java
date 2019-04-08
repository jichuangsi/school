package com.jichuangsi.school.homeworkservice.entity.elements;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.ArrayList;
import java.util.List;

@Document(collection = "school_question_mapping")
public class QuestionMappingElement {
    @Id
    private String id;
    private String type;
    private List<String> options = new ArrayList<String>();
    private List<AnswerMapping> answers = new ArrayList<AnswerMapping>();

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public List<String> getOptions() {
        return options;
    }

    public void setOptions(List<String> options) {
        this.options = options;
    }

    public List<AnswerMapping> getAnswers() {
        return answers;
    }

    public void setAnswers(List<AnswerMapping> answers) {
        this.answers = answers;
    }
}
