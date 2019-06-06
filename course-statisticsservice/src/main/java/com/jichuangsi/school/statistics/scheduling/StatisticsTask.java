package com.jichuangsi.school.statistics.scheduling;

import com.jichuangsi.microservice.common.model.UserInfoForToken;
import com.jichuangsi.school.statistics.service.IQuestionResultService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.util.List;

@Component
public class StatisticsTask {

    @Resource
    private CacheManager cacheManager;

    private static final String SPLITE = "-";

    private static final String HOTPOIN = "HP";

    private static final String methodForStudentOneWeekCourse = "getStudentSubjectResult";

    private Logger logger = LoggerFactory.getLogger(getClass());

    @Resource
    private IQuestionResultService questionResultService;

    //@Scheduled(cron = "0/30 * * * * *")
    @Scheduled(cron= "0 30 1,13 * * ?")
    public void statisticsStudentOneWeekCourse() {

        StringBuffer key = new StringBuffer().append(HOTPOIN).append(SPLITE).append(methodForStudentOneWeekCourse);
        Cache hotpointCache = cacheManager.getCache("hotpointCache");
        Cache statisticsCache = cacheManager.getCache("statisticsCache");
        Cache.ValueWrapper hpVals = hotpointCache.get(key.toString());
        logger.info("===I fetched hotpointCache/statisticsCache to refresh===");
        if(hpVals==null) return;

        List<String> vals = (List<String>)(hpVals.get());

        vals.stream().forEach(v->{
            String[] vs = v.split(SPLITE);
            UserInfoForToken userInfo = new UserInfoForToken(vs[0],null,null,vs[1],null);
            String statisticsKey = getStudentSubjectResultKeyGenerator(methodForStudentOneWeekCourse, userInfo, vs[2]);
            Cache.ValueWrapper statisticsVals = statisticsCache.get(statisticsKey);
            try {
                if(statisticsVals!=null){
                    statisticsCache.evict(statisticsKey);
                }
                List res = questionResultService.getStudentSubjectResult(userInfo, vs[2]);
                if(res.size()!=0){
                    statisticsCache.putIfAbsent(statisticsKey,res);
                }
            }catch (Exception e){

            }
        });
    }

    private String getStudentSubjectResultKeyGenerator(String method, UserInfoForToken userInfo, String subject){
        StringBuilder stringBuilder = new StringBuilder();
        String split = "-";
        stringBuilder.append(method);
        if (!StringUtils.isEmpty(userInfo.getClassId())){
            stringBuilder.append(split);
            stringBuilder.append(userInfo.getClassId());
        }
        if (!StringUtils.isEmpty(subject)){
            stringBuilder.append(split);
            stringBuilder.append(subject);
        }
        return stringBuilder.toString();
    }
}
