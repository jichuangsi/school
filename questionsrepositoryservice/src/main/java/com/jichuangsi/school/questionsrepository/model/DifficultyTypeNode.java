package com.jichuangsi.school.questionsrepository.model;

import java.io.Serializable;

public class DifficultyTypeNode  implements Serializable {

    private String id;
    private String difficulty;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDifficulty() {
        return difficulty;
    }

    public void setDifficulty(String difficulty) {
        this.difficulty = difficulty;
    }
}
