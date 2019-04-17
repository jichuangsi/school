package com.jichuangsi.school.questionsrepository.importword.util;



import com.jichuangsi.school.questionsrepository.importword.model.MatcherIndex;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by 窝里横 on 2019/4/11.
 */
public class StringUtils {
    /**
     *      *获取所有的索引
     *      *
     *      * @param str
     *      * @param modelStr
     *      * @param count
     *      * @return
     *     
     */
    public static List<MatcherIndex> findAllIndex(String str, String modelStr) {
        List<MatcherIndex> result = new ArrayList<MatcherIndex>();
// 对子字符串进行匹配
        Matcher slashMatcher =    Pattern.compile(modelStr).matcher(str);
        while (slashMatcher.find()) {
            try {
                int start = slashMatcher.start();
                int end = slashMatcher.end();
                MatcherIndex matcherIndex = new MatcherIndex();
                matcherIndex.setStart(start);
                matcherIndex.setEnd(end);
                result.add(matcherIndex);
            } catch (Exception e) {
            }
        }
        return result;
    }

    /**
     *      * 子字符串modelStr在字符串str中第count次出现时的下标
     *      * @param str
     *      * @param modelStr
     *      * @param count
     *      * @return
     *     
     */
    public static MatcherIndex getIndex(String str, String modelStr, int count) {
        // 对子字符串进行匹配
        Matcher slashMatcher = Pattern.compile(modelStr).matcher(str);
        int index = 0;
        while (slashMatcher.find()) {
            if (index == count) {
                break;
            }
            index++;
        }
        try {
            int start = slashMatcher.start();
            int end = slashMatcher.end();
            MatcherIndex matcherIndex = new MatcherIndex();
            matcherIndex.setStart(start);
            matcherIndex.setEnd(end);
            return matcherIndex;
        } catch (Exception e) {
        }
        return null;
    }


}


