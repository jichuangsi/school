package com.jichuangsi.school.courseservice.constant;

public enum QuestionType {
    EMPTY(null, 0), OBJECTIVE("objective", 1), SUBJECTIVE("subjective", 2);
    private String name;
    private int index;

    // 构造方法
    private QuestionType(String name, int index) {
        this.name = name;
        this.index = index;
    }

    public static String getName(int index) {
        for (QuestionType c : QuestionType.values()) {
            if (c.getIndex() == index) {
                return c.name;
            }
        }
        return null;
    }

    public static QuestionType getResult(String name) {
        for (QuestionType c : QuestionType.values()) {
            if (c.getName().equalsIgnoreCase(name)) {
                return c;
            }
        }
        return null;
    }

    public static QuestionType getResult(int index) {
        for (QuestionType c : QuestionType.values()) {
            if (c.getIndex() == index) {
                return c;
            }
        }
        return null;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

}
