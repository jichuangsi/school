package com.jichuangsi.school.courseservice.util;

import com.jichuangsi.school.courseservice.constant.QuestionType;
import com.jichuangsi.school.courseservice.constant.Result;
import com.jichuangsi.school.courseservice.entity.Course;
import com.jichuangsi.school.courseservice.entity.Question;
import com.jichuangsi.school.courseservice.entity.StudentAnswer;
import com.jichuangsi.school.courseservice.model.message.AnswerMessageModel;
import com.jichuangsi.school.courseservice.model.message.CourseMessageModel;
import com.jichuangsi.school.courseservice.model.message.QuestionMessageModel;
import com.jichuangsi.school.courseservice.model.message.ShareAnswerMessageModel;
import org.springframework.util.StringUtils;

import java.util.Date;
import java.util.List;

public final class MappingEntity2MessageConverter {

    private MappingEntity2MessageConverter(){}

    public static final CourseMessageModel ConvertCourse(Course course){
        CourseMessageModel courseMessageModel = new CourseMessageModel();
        courseMessageModel.setCourseId(course.getId());
        courseMessageModel.setClassId(course.getClassId());
        courseMessageModel.setCourseName(course.getName());
        courseMessageModel.setTimestamp(new Date().getTime());
        return courseMessageModel;
    }

    public static final QuestionMessageModel ConvertQuestion(String courseId, Question question){
        QuestionMessageModel questionMessageModel = new QuestionMessageModel();
        questionMessageModel.setQuestionId(question.getId());
        questionMessageModel.setCourseId(courseId);
        questionMessageModel.setQuType(question.getType());
        questionMessageModel.setContent(null);
        return questionMessageModel;
    }
    public static final QuestionMessageModel ConvertQuestion2(String courseId, Question question, List<String> student){
        QuestionMessageModel questionMessageModel = new QuestionMessageModel();
        questionMessageModel.setQuestionId(question.getId());
        questionMessageModel.setCourseId(courseId);
        questionMessageModel.setQuType(question.getType());
        questionMessageModel.setContent(null);
        if(student.size()>0){
            student.forEach(s->{
                questionMessageModel.getStudent().add(s);
            });
        }
        return questionMessageModel;
    }
    public static final AnswerMessageModel ConvertAnswer(String courseId, StudentAnswer answer){
        AnswerMessageModel answerMessageModel = new AnswerMessageModel();
        answerMessageModel.setCourseId(courseId);
        answerMessageModel.setQuestionId(answer.getQuestionId());
        answerMessageModel.setScore(StringUtils.isEmpty(answer.getSubjectiveScore())?0d:answer.getSubjectiveScore());
        answerMessageModel.setRight(Result.CORRECT.getName().equalsIgnoreCase(answer.getResult())?true:false);
        answerMessageModel.setQuType(StringUtils.isEmpty(answer.getObjectiveAnswer())? QuestionType.SUBJECTIVE.getName():QuestionType.OBJECTIVE.getName());
        answerMessageModel.setAnswer(answer.getObjectiveAnswer());
        answerMessageModel.setStudentId(answer.getStudentId());
        return answerMessageModel;
    }

    public static final ShareAnswerMessageModel ConvertShareAnswer(String courseId, String questionId, String picPath){
        ShareAnswerMessageModel shareAnswerMessageModel = new ShareAnswerMessageModel();
        shareAnswerMessageModel.setCourseId(courseId);
        shareAnswerMessageModel.setQuestionId(questionId);
        shareAnswerMessageModel.setPicPath(picPath);
        return shareAnswerMessageModel;
    }
}
