package com.jichuangsi.school.statistics.config;

import com.github.benmanes.caffeine.cache.Caffeine;
import com.jichuangsi.microservice.common.model.UserInfoForToken;
import com.jichuangsi.school.statistics.model.classType.SearchStudentKnowledgeModel;
import org.springframework.cache.CacheManager;
import org.springframework.cache.caffeine.CaffeineCache;
import org.springframework.cache.interceptor.KeyGenerator;
import org.springframework.cache.support.SimpleCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.concurrent.TimeUnit;


@Configuration
public class CacheConfig {

    private static final int DEFAULT_MAXSIZE = 10000;
    private static final int DEFAULT_TTL = 600;

    public enum Caches{
        hotpointCache(604800,1000),          //有效期一周 , 最大容量1000
        statisticsCache(43200,1000);        //有效期半天 , 最大容量1000

        Caches() {
        }
        Caches(int ttl) {
            this.ttl = ttl;
        }
        Caches(int ttl, int maxSize) {
            this.ttl = ttl;
            this.maxSize = maxSize;
        }
        private int maxSize=DEFAULT_MAXSIZE;    //最大數量
        private int ttl=DEFAULT_TTL;        //过期时间（秒）
        public int getMaxSize() {
            return maxSize;
        }
        public int getTtl() {
            return ttl;
        }
    }

    @Bean
    @Primary
    public CacheManager caffeineCacheManager() {
        SimpleCacheManager cacheManager = new SimpleCacheManager();
        ArrayList<CaffeineCache> caches = new ArrayList<CaffeineCache>();
        for(Caches c : Caches.values()){
            caches.add(new CaffeineCache(c.name(),
                    Caffeine.newBuilder().recordStats()
                            .expireAfterWrite(c.getTtl(), TimeUnit.SECONDS)
                            .maximumSize(c.getMaxSize())
                            .build())
            );
        }
        cacheManager.setCaches(caches);
        return cacheManager;
    }

    @Bean(value = "classStatisticsKeyGenerator")
    public KeyGenerator classStatisticsKeyGenerator() {
        return (o, method, objects) -> {
            StringBuilder stringBuilder = new StringBuilder();
            String split = "-";
            UserInfoForToken params = (UserInfoForToken) objects[0];
            String subject = (String)objects[1];
            stringBuilder.append(method.getName());
            if (!StringUtils.isEmpty(params.getClassId())){
                stringBuilder.append(split);
                stringBuilder.append(params.getClassId());
            }
            if (!StringUtils.isEmpty(params.getUserId())){
                stringBuilder.append(split);
                stringBuilder.append(params.getUserId());
            }
            if (!StringUtils.isEmpty(params.getUserName())){
                stringBuilder.append(split);
                stringBuilder.append(params.getUserName());
            }
            if (!StringUtils.isEmpty(params.getSchoolId())){
                stringBuilder.append(split);
                stringBuilder.append(params.getSchoolId());
            }
            if (!StringUtils.isEmpty(subject)){
                stringBuilder.append(split);
                stringBuilder.append(subject);
            }
            return stringBuilder.toString();
        };
    }

    @Bean("classStudentKnowledgesKeyGenerator")
    public KeyGenerator classStudentKnowledgesKeyGenerator() {
        return (o, method, objects) -> {
            StringBuilder stringBuilder = new StringBuilder();
            String split = "-";
            SearchStudentKnowledgeModel params = (SearchStudentKnowledgeModel) objects[1];
            stringBuilder.append(method.getName());
            if (!StringUtils.isEmpty(params.getClassId())){
                stringBuilder.append(split);
                stringBuilder.append(params.getClassId());
            }
            return stringBuilder.toString();
        };
    }

    @Bean("getCourseSignKeyGenerator")
    public KeyGenerator getCourseSignKeyGenerator() {
        return (o, method, objects) -> {
            StringBuilder stringBuilder = new StringBuilder();
            String split = "-";
            String courseId = (String) objects[1];
            String classId = (String) objects[2];
            stringBuilder.append(method.getName());
            if (!StringUtils.isEmpty(courseId)){
                stringBuilder.append(split);
                stringBuilder.append(courseId);
            }
            if (!StringUtils.isEmpty(classId)){
                stringBuilder.append(split);
                stringBuilder.append(classId);
            }
            return stringBuilder.toString();
        };
    }

    @Bean("getStudentSubjectResultKeyGenerator")
    public KeyGenerator getStudentSubjectResultKeyGenerator() {
        return (o, method, objects) -> {
            StringBuilder stringBuilder = new StringBuilder();
            String split = "-";
            UserInfoForToken params = (UserInfoForToken) objects[0];
            String subject = (String) objects[1];
            stringBuilder.append(method.getName());
            if (!StringUtils.isEmpty(params.getClassId())){
                stringBuilder.append(split);
                stringBuilder.append(params.getClassId());
            }
            if (!StringUtils.isEmpty(subject)){
                stringBuilder.append(split);
                stringBuilder.append(subject);
            }
            return stringBuilder.toString();
        };
    }

    @Bean("getSubjectQuestionKeyGenerator")
    public KeyGenerator getSubjectQuestionKeyGenerator() {
        return (o, method, objects) -> {
            StringBuilder stringBuilder = new StringBuilder();
            String split = "-";
            UserInfoForToken params = (UserInfoForToken) objects[0];
            String classId = (String) objects[1];
            stringBuilder.append(method.getName());
            if (!StringUtils.isEmpty(params.getUserId())){
                stringBuilder.append(split);
                stringBuilder.append(params.getUserId());
            }
            if (!StringUtils.isEmpty(params.getClassId())){
                stringBuilder.append(split);
                stringBuilder.append(params.getClassId());
            }
            if (!StringUtils.isEmpty(classId)){
                stringBuilder.append(split);
                stringBuilder.append(classId);
            }
            return stringBuilder.toString();
        };
    }
}
