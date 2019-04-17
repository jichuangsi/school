package com.jichuangsi.school.questionsrepository.importword.util;

/**
 * Created by 窝里横 on 2019/4/9.
 */
public class StringTool {
    public static String replace(String original ,String find,String replace)
    {
        if (original==null || find==null ||replace==null)
        {
            return original;
        }
        int findLen = find.length();
        int originalLen = original.length();
        if (originalLen==0 || findLen==0)
        {
            return original;
        }
        StringBuffer sb = new StringBuffer();
        int begin = 0; //下次检索开始的位置
        int i = original.indexOf(find); //找到的子串位置
        while (i!=-1)
        {
            sb.append(original.substring(begin,i));
            sb.append(replace);
            begin = i + findLen;
            i = original.indexOf(find,begin);
        }
        if (begin<originalLen)
        {
            sb.append(original.substring(begin));
        }
        return sb.toString();
    }
}
