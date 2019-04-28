package com.jichuangsi.school.parents.config;

import com.jichuangsi.microservice.common.model.UserInfoForToken;
import com.jichuangsi.school.parents.model.statistics.ParentStatisticsModel;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.interceptor.KeyGenerator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.StringUtils;

@EnableCaching
@Configuration
public class CacheConfig {

    @Bean("getParentCourseStatisticsKeyGenerator")
    public KeyGenerator getParentCourseStatisticsKeyGenerator() {
        return (o, method, objects) -> {
            StringBuilder stringBuilder = new StringBuilder();
            String split = "-";
            UserInfoForToken userInfo = (UserInfoForToken) objects[0];
            ParentStatisticsModel model = (ParentStatisticsModel) objects[1];
            stringBuilder.append(method.getName());
            if (!StringUtils.isEmpty(userInfo.getUserId())) {
                stringBuilder.append(split);
                stringBuilder.append(userInfo.getUserId());
            }
            if (!StringUtils.isEmpty(model.getStudentId())) {
                stringBuilder.append(split);
                stringBuilder.append(model.getStudentId());
            }
            if (!StringUtils.isEmpty(model.getSubjectName())) {
                stringBuilder.append(split);
                stringBuilder.append(model.getSubjectName());
            }
            for (Long time : model.getStatisticsTimes()){
                if (null != time){
                    stringBuilder.append(split);
                    stringBuilder.append(time);
                }
            }
            return stringBuilder.toString();
        };
    }


    @Bean("getParentHomeworkStatisticsKeyGenerator")
    public KeyGenerator getParentHomeworkStatisticsKeyGenerator() {
        return (o, method, objects) -> {
            StringBuilder stringBuilder = new StringBuilder();
            String split = "-";
            UserInfoForToken userInfo = (UserInfoForToken) objects[0];
            ParentStatisticsModel model = (ParentStatisticsModel) objects[1];
            stringBuilder.append(method.getName());
            if (!StringUtils.isEmpty(userInfo.getUserId())) {
                stringBuilder.append(split);
                stringBuilder.append(userInfo.getUserId());
            }
            if (!StringUtils.isEmpty(model.getStudentId())) {
                stringBuilder.append(split);
                stringBuilder.append(model.getStudentId());
            }
            if (!StringUtils.isEmpty(model.getSubjectName())) {
                stringBuilder.append(split);
                stringBuilder.append(model.getSubjectName());
            }
            for (Long time : model.getStatisticsTimes()){
                if (null != time){
                    stringBuilder.append(split);
                    stringBuilder.append(time);
                }
            }
            return stringBuilder.toString();
        };
    }
}
