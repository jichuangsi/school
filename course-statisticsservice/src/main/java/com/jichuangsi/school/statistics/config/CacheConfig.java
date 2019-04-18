package com.jichuangsi.school.statistics.config;

import com.jichuangsi.school.statistics.feign.model.TransferStudent;
import com.jichuangsi.school.statistics.model.classType.ClassStatisticsModel;
import com.jichuangsi.school.statistics.model.classType.StudentKnowledgeModel;
import com.jichuangsi.school.statistics.model.homework.HomeworkModelForTeacher;
import com.jichuangsi.school.statistics.model.result.StudentResultModel;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.interceptor.KeyGenerator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.StringUtils;

@EnableCaching
@Configuration
public class CacheConfig {

    @Bean(value = "classStatisticsKeyGenerator")
    public KeyGenerator classStatisticsKeyGenerator() {
        return (o, method, objects) -> {
            StringBuilder stringBuilder = new StringBuilder();
            String split = "-";
            ClassStatisticsModel params = (ClassStatisticsModel) objects[1];
            stringBuilder.append(method.getName());
            if (!StringUtils.isEmpty(params.getClassId())){
                stringBuilder.append(split);
                stringBuilder.append(params.getClassId());
            }
            if (!StringUtils.isEmpty(params.getClassName())){
                stringBuilder.append(split);
                stringBuilder.append(params.getClassName());
            }
            if (!StringUtils.isEmpty(params.getCourseIds())){
                stringBuilder.append(split);
                stringBuilder.append(params.getCourseIds());
            }
            if (!StringUtils.isEmpty(params.getWeakQuestionNum())){
                stringBuilder.append(split);
                stringBuilder.append(params.getWeakQuestionNum());
            }
            if (!StringUtils.isEmpty(params.getWrongQuestionNum())){
                stringBuilder.append(split);
                stringBuilder.append(params.getWrongQuestionNum());
            }
            return stringBuilder.toString();
        };
    }

    @Bean("classStudentKnowledgesKeyGenerator")
    public KeyGenerator classStudentKnowledgesKeyGenerator() {
        return (o, method, objects) -> {
            StringBuilder stringBuilder = new StringBuilder();
            String split = "-";
            StudentKnowledgeModel params = (StudentKnowledgeModel) objects[1];
            stringBuilder.append(method.getName());
            if (!StringUtils.isEmpty(params.getStudentId())){
                stringBuilder.append(split);
                stringBuilder.append(params.getStudentId());
            }
            if (!StringUtils.isEmpty(params.getStudentName())){
                stringBuilder.append(split);
                stringBuilder.append(params.getStudentName());
            }
            return stringBuilder.toString();
        };
    }

    @Bean("getCourseSignKeyGenerator")
    public KeyGenerator getCourseSignKeyGenerator() {
        return (o, method, objects) -> {
            StringBuilder stringBuilder = new StringBuilder();
            String split = "-";
            TransferStudent params = (TransferStudent) objects[1];
            stringBuilder.append(method.getName());
            if (!StringUtils.isEmpty(params.getStudentId())){
                stringBuilder.append(split);
                stringBuilder.append(params.getStudentId());
            }
            if (!StringUtils.isEmpty(params.getStudentName())){
                stringBuilder.append(split);
                stringBuilder.append(params.getStudentName());
            }
            if (!StringUtils.isEmpty(params.getSignFlag())){
                stringBuilder.append(split);
                stringBuilder.append(params.getSignFlag());
            }
            if (!StringUtils.isEmpty(params.getStudentAccount())){
                stringBuilder.append(split);
                stringBuilder.append(params.getStudentAccount());
            }
            return stringBuilder.toString();
        };
    }

    @Bean("getStudentSubjectResultKeyGenerator")
    public KeyGenerator getStudentSubjectResultKeyGenerator() {
        return (o, method, objects) -> {
            StringBuilder stringBuilder = new StringBuilder();
            String split = "-";
            StudentResultModel params = (StudentResultModel) objects[1];
            stringBuilder.append(method.getName());
            if (!StringUtils.isEmpty(params.getClassResultRate())){
                stringBuilder.append(split);
                stringBuilder.append(params.getClassResultRate());
            }
            if (!StringUtils.isEmpty(params.getGradeResultRate())){
                stringBuilder.append(split);
                stringBuilder.append(params.getGradeResultRate());
            }
            if (!StringUtils.isEmpty(params.getKnowledgeName())){
                stringBuilder.append(split);
                stringBuilder.append(params.getKnowledgeName());
            }
            if (!StringUtils.isEmpty(params.getKnowledgeRate())){
                stringBuilder.append(split);
                stringBuilder.append(params.getKnowledgeRate());
            }
            if (!StringUtils.isEmpty(params.getResultRate())){
                stringBuilder.append(split);
                stringBuilder.append(params.getResultRate());
            }
            return stringBuilder.toString();
        };
    }

    @Bean("getSubjectQuestionKeyGenerator")
    public KeyGenerator getSubjectQuestionKeyGenerator() {
        return (o, method, objects) -> {
            StringBuilder stringBuilder = new StringBuilder();
            String split = "-";
            HomeworkModelForTeacher params = (HomeworkModelForTeacher) objects[1];
            stringBuilder.append(method.getName());
            if (!StringUtils.isEmpty(params.getHomeworkId())){
                stringBuilder.append(split);
                stringBuilder.append(params.getHomeworkId());
            }
            if (!StringUtils.isEmpty(params.getClassId())){
                stringBuilder.append(split);
                stringBuilder.append(params.getClassId());
            }
            if (!StringUtils.isEmpty(params.getClassName())){
                stringBuilder.append(split);
                stringBuilder.append(params.getClassName());
            }
            if (!StringUtils.isEmpty(params.getHomeworkName())){
                stringBuilder.append(split);
                stringBuilder.append(params.getHomeworkName());
            }
            if (!StringUtils.isEmpty(params.getSubjectId())){
                stringBuilder.append(split);
                stringBuilder.append(params.getSubjectId());
            }
            if (!StringUtils.isEmpty(params.getSubjectName())){
                stringBuilder.append(split);
                stringBuilder.append(params.getSubjectName());
            }
            if (!StringUtils.isEmpty(params.getTeacherId())){
                stringBuilder.append(split);
                stringBuilder.append(params.getTeacherId());
            }
            if (!StringUtils.isEmpty(params.getTeacherName())){
                stringBuilder.append(split);
                stringBuilder.append(params.getTeacherName());
            }
            return stringBuilder.toString();
        };
    }
}
