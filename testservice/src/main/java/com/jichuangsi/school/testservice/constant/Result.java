package com.jichuangsi.school.testservice.constant;

public enum Result {
    CORRECT("C", 1), PASS("P", 2), WRONG("W", 3);
    private String name;
    private int index;

    // 构造方法
    private Result(String name, int index) {
        this.name = name;
        this.index = index;
    }

    public static String getName(int index) {
        for (Result c : Result.values()) {
            if (c.getIndex() == index) {
                return c.name;
            }
        }
        return null;
    }

    public static Result getResult(String name) {
        for (Result c : Result.values()) {
            if (c.getName().equalsIgnoreCase(name)) {
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
