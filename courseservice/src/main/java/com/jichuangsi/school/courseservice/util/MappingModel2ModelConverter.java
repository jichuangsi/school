package com.jichuangsi.school.courseservice.util;

import com.jichuangsi.school.courseservice.model.QuestionForStudent;
import com.jichuangsi.school.courseservice.model.repository.QuestionNode;
import org.apache.commons.lang.StringUtils;

import java.lang.reflect.Field;

public final class MappingModel2ModelConverter {

    private MappingModel2ModelConverter(){}

    public static final QuestionForStudent ConvertQuestionNode(QuestionNode questionNode){
        QuestionForStudent questionForStudent = new QuestionForStudent();
        questionForStudent.setQuestionContent(questionNode.getTitle());
        Field[] fields = questionNode.getClass().getDeclaredFields();
        for (int i=0;i<fields.length;i++){//遍历
            try {
                Field field = fields[i];
                field.setAccessible(true);
                String name = field.getName();
                if(name.startsWith("option_")&&StringUtils.isNotEmpty((String)field.get(questionNode))){
                    questionForStudent.getOptions().add((String)field.get(questionNode));
                }
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
        questionForStudent.setAnswer(questionNode.getAnswer1());
        questionForStudent.setAnswerDetail(questionNode.getAnswer2());
        questionForStudent.setParse(questionNode.getParse());
        questionForStudent.setQuesetionType(questionNode.getQtpye());
        questionForStudent.setQuestionTypeInCN(questionNode.getQtpye());
        questionForStudent.setDifficulty(questionNode.getDiff());
        questionForStudent.setSubjectId(questionNode.getSubjectId());
        questionForStudent.setGradeId(questionNode.getGradeId());
        questionForStudent.setKnowledge(questionNode.getKnowledges());
        questionForStudent.setKnowledgeId(questionNode.getKnowledgeId());
        questionForStudent.setQuestionIdMD52(questionNode.getQid());

        return questionForStudent;
    }
}
