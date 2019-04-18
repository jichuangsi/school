package com.jichuangsi.school.courseservice.config;

import com.jichuangsi.school.courseservice.model.QuestionForTeacher;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.interceptor.KeyGenerator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.StringUtils;

@EnableCaching
@Configuration
public class CacheConfig {

    @Bean("getWrongQuestionsKeyGenerator")
    public KeyGenerator getWrongQuestionsKeyGenerator() {
        return (o, method, objects) -> {
            StringBuilder stringBuilder = new StringBuilder();
            String split = "-";
            QuestionForTeacher params = (QuestionForTeacher) objects[1];
            stringBuilder.append(method.getName());
            if (!StringUtils.isEmpty(params.getQuestionId())){
                stringBuilder.append(split);
                stringBuilder.append(params.getQuestionId());
            }
            if (!StringUtils.isEmpty(params.getQuesetionType())){
                stringBuilder.append(split);
                stringBuilder.append(params.getQuesetionType());
            }
            if (!StringUtils.isEmpty(params.getQuestionIdMD52())){
                stringBuilder.append(split);
                stringBuilder.append(params.getQuestionIdMD52());
            }
            if (!StringUtils.isEmpty(params.getQuestionTypeInCN())){
                stringBuilder.append(split);
                stringBuilder.append(params.getQuestionTypeInCN());
            }
            if (!StringUtils.isEmpty(params.getSubjectId())){
                stringBuilder.append(split);
                stringBuilder.append(params.getSubjectId());
            }
            if (!StringUtils.isEmpty(params.getGradeId())){
                stringBuilder.append(split);
                stringBuilder.append(params.getGradeId());
            }
            if (!StringUtils.isEmpty(params.getDifficulty())){
                stringBuilder.append(split);
                stringBuilder.append(params.getDifficulty());
            }
            return stringBuilder.toString();
        };
    }
}
