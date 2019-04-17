package com.jichuangsi.school.questionsrepository.importword.model;

/**
 * Created by 窝里横 on 2019/4/11.
 */
public class MatcherIndex {
    int start = -1;
    int end = -1;

    public int getStart() {
        return start;
    }

    public void setStart(int start) {
        this.start = start;
    }

    public int getEnd() {
        return end;
    }

    public void setEnd(int end) {
        this.end = end;
    }

    @Override
    public String toString() {
        return "MatcherIndex [start=" + start + ", end=" + end + "]";
    }
}
