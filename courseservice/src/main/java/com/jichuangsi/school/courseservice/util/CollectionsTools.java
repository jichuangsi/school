package com.jichuangsi.school.courseservice.util;

import java.util.*;

public final class CollectionsTools {

    /**
     * 求并集
     *
     * @param m
     * @param n
     * @return
     */
    public static Object[] getB(Object[] m, Object[] n)
    {
        // 将数组转换为set集合
        Set<Object> set1 = new HashSet<Object>(Arrays.asList(m));
        Set<Object> set2 = new HashSet<Object>(Arrays.asList(n));

        // 合并两个集合
        set1.addAll(set2);

        Object[] arr = {};
        return set1.toArray(arr);
    }

    /**
     * 求交集
     *
     * @param m
     * @param n
     * @return
     */
    public static Object[] getJ(Object[] m, Object[] n)
    {
        List<Object> rs = new ArrayList<Object>();

        // 将较长的数组转换为set
        Set<Object> set = new HashSet<Object>(Arrays.asList(m.length > n.length ? m : n));

        // 遍历较短的数组，实现最少循环
        for (Object i : m.length > n.length ? n : m)
        {
            if (set.contains(i))
            {
                rs.add(i);
            }
        }

        Object[] arr = {};
        return rs.toArray(arr);
    }

    /**
     * 求差集
     *
     * @param m
     * @param n
     * @return
     */
    public static Object[] getC(Object[] m, Object[] n)
    {
        // 将较长的数组转换为set
        Set<Object> set = new HashSet<Object>(Arrays.asList(m.length > n.length ? m : n));

        // 遍历较短的数组，实现最少循环
        for (Object i : m.length > n.length ? n : m)
        {
            // 如果集合里有相同的就删掉，如果没有就将值添加到集合
            if (set.contains(i))
            {
                set.remove(i);
            } else
            {
                set.add(i);
            }
        }

        Object[] arr = {};
        return set.toArray(arr);
    }

}
