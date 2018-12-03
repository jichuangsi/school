package com.jichuangsi.school.user.constant;

public enum Sex {
    FEMALE("F", 1), MALE("M", 2);
    private String name;
    private int index;

    // 构造方法
    private Sex(String name, int index) {
        this.name = name;
        this.index = index;
    }

    public static String getName(int index) {
        for (Sex c : Sex.values()) {
            if (c.getIndex() == index) {
                return c.name;
            }
        }
        return null;
    }

    public static Sex getSex(String name) {
        for (Sex c : Sex.values()) {
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
