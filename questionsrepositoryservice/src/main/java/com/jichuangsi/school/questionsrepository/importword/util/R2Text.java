package com.jichuangsi.school.questionsrepository.importword.util;

import java.util.regex.Pattern;

/**
 * Created by 窝里横 on 2019/4/12.
 */
public class R2Text {


    public static String r2Text(String text){
//        String s = text.replaceAll("\\.", "\\\\\\\\.").
//                replaceAll("\\(", "\\\\\\\\(").
//                replaceAll("\\)", "\\\\\\\\)").
//                replaceAll("\\*", "\\\\\\\\*");
        String s = text.replaceAll("\\.", "\\\\.").
                replaceAll("\\(", "\\\\(").
                replaceAll("\\)", "\\\\)").
                replaceAll("\\*", "\\\\*");

        return s;
    }
}
