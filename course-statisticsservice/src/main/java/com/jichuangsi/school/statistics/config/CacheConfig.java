package com.jichuangsi.school.statistics.config;

import com.jichuangsi.microservice.common.model.UserInfoForToken;
import com.jichuangsi.school.statistics.model.classType.SearchStudentKnowledgeModel;
import org.springframework.cache.interceptor.KeyGenerator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.StringUtils;


@Configuration
public class CacheConfig {

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
