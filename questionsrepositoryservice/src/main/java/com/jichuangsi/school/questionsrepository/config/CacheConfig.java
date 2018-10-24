package com.jichuangsi.school.questionsrepository.config;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import com.jichuangsi.school.questionsrepository.model.ChapterQueryModel;
import com.jichuangsi.school.questionsrepository.model.QuestionQueryModel;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.caffeine.CaffeineCache;
import org.springframework.cache.interceptor.KeyGenerator;
import org.springframework.cache.support.SimpleCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.util.StringUtils;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.concurrent.TimeUnit;

@EnableCaching
@Configuration
public class CacheConfig {

    @Bean("questionsKeyGenerator")
    public KeyGenerator questionsKeyGenerator() {
        return (o, method, objects) -> {
            StringBuilder stringBuilder = new StringBuilder();
            QuestionQueryModel params = (QuestionQueryModel)objects[1];
            stringBuilder.append(method.getName());
            if(!StringUtils.isEmpty(params.getKnowledgeId())){
                stringBuilder.append("-");
                stringBuilder.append(params.getKnowledgeId());
            }
            if(!StringUtils.isEmpty(params.getQtypeId())){
                stringBuilder.append("-");
                stringBuilder.append(params.getQtypeId());
            }
            if(!StringUtils.isEmpty(params.getPaperType())){
                stringBuilder.append("-");
                stringBuilder.append(params.getPaperType());
            }
            if(!StringUtils.isEmpty(params.getDiff())){
                stringBuilder.append("-");
                stringBuilder.append(params.getDiff());
            }
            if(!StringUtils.isEmpty(params.getYear())){
                stringBuilder.append("-");
                stringBuilder.append(params.getYear());
            }
            if(!StringUtils.isEmpty(params.getPage())){
                stringBuilder.append("-");
                stringBuilder.append(params.getPage());
            }

            return stringBuilder.toString();
        };
    }

    @Bean("chaptersKeyGenerator")
    public KeyGenerator chaptersKeyGenerator() {
        return (o, method, objects) -> {
            StringBuilder stringBuilder = new StringBuilder();
            ChapterQueryModel params = (ChapterQueryModel)objects[1];
            stringBuilder.append(method.getName());
            if(!StringUtils.isEmpty(params.getPharseId())){
                stringBuilder.append("-");
                stringBuilder.append(params.getPharseId());
            }
            if(!StringUtils.isEmpty(params.getGradeId())){
                stringBuilder.append("-");
                stringBuilder.append(params.getGradeId());
            }
            if(!StringUtils.isEmpty(params.getSubjectId())){
                stringBuilder.append("-");
                stringBuilder.append(params.getSubjectId());
            }
            if(!StringUtils.isEmpty(params.getEditionId())){
                stringBuilder.append("-");
                stringBuilder.append(params.getEditionId());
            }

            return stringBuilder.toString();
        };
    }

}
