package com.jichuangsi.school.user.constant;

public enum NoticeType {
    COLLEGE("C", 1), SYSTEM("S", 2);
    private String name;
    private int index;

    // 构造方法
    private NoticeType(String name, int index) {
        this.name = name;
        this.index = index;
    }

    public static String getName(int index) {
        for (NoticeType c : NoticeType.values()) {
            if (c.getIndex() == index) {
                return c.name;
            }
        }
        return null;
    }

    public static NoticeType getNoticeType(String name) {
        for (NoticeType c : NoticeType.values()) {
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
