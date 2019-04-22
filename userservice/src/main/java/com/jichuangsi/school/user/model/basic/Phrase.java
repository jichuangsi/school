package com.jichuangsi.school.user.model.basic;

public class Phrase {
    private String phraseId;
    private String phraseName;
    private String id;

    public Phrase(){}

    public Phrase(String phraseId, String phraseName,String id){
        this.phraseId = phraseId;
        this.phraseName = phraseName;
        this.id = id;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPhraseId() {
        return phraseId;
    }

    public void setPhraseId(String phraseId) {
        this.phraseId = phraseId;
    }

    public String getPhraseName() {
        return phraseName;
    }

    public void setPhraseName(String phraseName) {
        this.phraseName = phraseName;
    }
}
