package com.jichuangsi.school.questionsrepository.importword.model;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by 窝里横 on 2019/4/3.
 */
public class TitleSourceInformation {
    private String text;
    private String pic;
    private List<String> options = new ArrayList<String>();

    public List<String> getOptions() {
        return options;
    }

    public void setOptions(List<String> options) {
        this.options = options;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getPic() {
        return pic;
    }

    public void setPic(String pic) {
        this.pic = pic;
    }
}
