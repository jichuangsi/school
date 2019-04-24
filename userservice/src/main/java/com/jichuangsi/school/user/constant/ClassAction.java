package com.jichuangsi.school.user.constant;

public enum ClassAction {
    NONE("", 0), DELETE("D", 1), GRADUATE("G", 2);
    private String name;
    private int index;

    // 构造方法
    private ClassAction(String name, int index) {
        this.name = name;
        this.index = index;
    }

    public static String getName(int index) {
        for (ClassAction c : ClassAction.values()) {
            if (c.getIndex() == index) {
                return c.name;
            }
        }
        return null;
    }

    public static ClassAction getClassAction(String name) {
        for (ClassAction c : ClassAction.values()) {
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
