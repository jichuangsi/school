package com.jichuangsi.school.courseservice.config;

import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.interceptor.KeyGenerator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.StringUtils;

import java.util.List;

@EnableCaching
@Configuration
public class CacheConfig {

    @Bean("getWrongQuestionsKeyGenerator")
    public KeyGenerator getWrongQuestionsKeyGenerator() {
        return (o, method, objects) -> {
            StringBuilder stringBuilder = new StringBuilder();
            String split = "-";
            List<String> params = (List<String>) objects[0];
            if (params.size() > 0) {
                stringBuilder.append(method.getName());
                if (!StringUtils.isEmpty(params.get(0))) {
                    stringBuilder.append(split);
                    stringBuilder.append(params.get(0));
                }
                if (!StringUtils.isEmpty(params.size())) {
                    stringBuilder.append(split);
                    stringBuilder.append(params.size());
                }
                if (!StringUtils.isEmpty(params.get(params.size()-1))) {
                    stringBuilder.append(split);
                    stringBuilder.append(params.get(params.size()-1));
                }
                return stringBuilder.toString();
            }else {
                return "WrongQuestionsKey";
            }
        };
    }
}
