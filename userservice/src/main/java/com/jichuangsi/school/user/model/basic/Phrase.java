package com.jichuangsi.school.user.model.basic;

public class Phrase {
    private String phraseId;
    private String phraseName;

    public Phrase(){}

    public Phrase(String phraseId, String phraseName){
        this.phraseId = phraseId;
        this.phraseName = phraseName;
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
