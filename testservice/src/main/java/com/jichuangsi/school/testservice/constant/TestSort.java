package com.jichuangsi.school.testservice.constant;

/**
 * course类排序枚举
 */
public enum TestSort {

    COMPREHENSIVE("P",1),TIME("T",2);

    private String name;
    private int index;

    // 构造方法
    private TestSort(String name, int index) {
        this.name = name;
        this.index = index;
    }

    public static String getName(int index) {
        for (TestSort c : TestSort.values()) {
            if (c.getIndex() == index) {
                return c.name;
            }
        }
        return null;
    }

    public static Status getCourseSort(String name) {
        for (Status c : Status.values()) {
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
