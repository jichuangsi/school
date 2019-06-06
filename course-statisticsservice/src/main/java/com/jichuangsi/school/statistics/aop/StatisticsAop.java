package com.jichuangsi.school.statistics.aop;

import com.jichuangsi.microservice.common.model.UserInfoForToken;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

@Aspect
@Component
public class StatisticsAop {

    @Resource
    private CacheManager cacheManager;

    private static final String SPLITE = "-";

    private static final String HOTPOIN = "HP";

    private Logger logger = LoggerFactory.getLogger(getClass());

    @Before("execution(* com.jichuangsi.school.statistics.controller.QuestionResultController.get*SubjectResult(..)) && args(userInfo, subject)")
    public void transferQuestions(JoinPoint jp , UserInfoForToken userInfo, String subject){
        if(userInfo==null || StringUtils.isEmpty(userInfo.getUserId()) || StringUtils.isEmpty(userInfo.getClassId())) return;

        StringBuffer key = new StringBuffer().append(HOTPOIN).append(SPLITE).append(jp.getSignature().getName());
        StringBuffer val = new StringBuffer().append(userInfo.getUserId()).append(SPLITE).append(userInfo.getClassId()).append(SPLITE).append(subject);
        Cache.ValueWrapper cache = cacheManager.getCache("hotpointCache").get(key.toString());
        logger.info("===I fired hotpointCache, Key:"+key.toString()+",Val:"+val.toString()+"===");
        if(cache==null){
            List<String> hotpoints = new ArrayList<String>();
            hotpoints.add(val.toString());
            cacheManager.getCache("hotpointCache").put(key.toString(),hotpoints);
        }else{
            List<String> vals = (List<String>)(cache.get());
            if(!vals.contains(val.toString())){
                vals.add(val.toString());
            }
        }
    }
}
