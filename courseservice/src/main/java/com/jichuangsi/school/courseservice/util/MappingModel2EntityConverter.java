package com.jichuangsi.school.courseservice.util;

import com.jichuangsi.microservice.common.model.UserInfoForToken;
import com.jichuangsi.school.courseservice.constant.QuestionType;
import com.jichuangsi.school.courseservice.entity.Course;
import com.jichuangsi.school.courseservice.entity.Question;
import com.jichuangsi.school.courseservice.entity.StudentAnswer;
import com.jichuangsi.school.courseservice.entity.TeacherAnswer;
import com.jichuangsi.school.courseservice.model.AnswerForStudent;
import com.jichuangsi.school.courseservice.model.AnswerForTeacher;
import com.jichuangsi.school.courseservice.model.CourseForTeacher;
import com.jichuangsi.school.courseservice.model.QuestionForTeacher;
import org.springframework.util.StringUtils;

import java.util.Date;
import java.util.UUID;

public  final class MappingModel2EntityConverter {

    private MappingModel2EntityConverter(){}

    public static final Course ConvertTeacherCourse(UserInfoForToken userInfo, CourseForTeacher courseForTeacher){
        Course course = new Course();
        course.setId(StringUtils.isEmpty(courseForTeacher.getCourseId())? UUID.randomUUID().toString().replaceAll("-", ""):courseForTeacher.getCourseId());
        course.setName(courseForTeacher.getCourseName());
        course.setInfo(courseForTeacher.getCourseInfo());
        course.setStatus(courseForTeacher.getCourseStatus()!=null?courseForTeacher.getCourseStatus().getName():null);
        course.setTeacherId(userInfo.getUserId());
        course.setTeacherName(userInfo.getUserName());
        course.setClassId(courseForTeacher.getClassId());
        course.setClassName(courseForTeacher.getClassName());
        course.setStartTime(courseForTeacher.getCourseStartTime());
        course.setEndTime(courseForTeacher.getCourseEndTime());
        if(courseForTeacher.getQuestions().size()>0){
            courseForTeacher.getQuestions().forEach(question -> {
                course.getQuestionIds().add(question.getQuestionId());
            });
        }
        if(StringUtils.isEmpty(courseForTeacher.getCourseId())) course.setCreateTime(new Date().getTime());
        course.setUpdateTime(new Date().getTime());

        return course;
    }

    public static final Question ConvertTeacherQuestion(QuestionForTeacher questionForTeacher){
        Question question = new Question();
        question.setId(StringUtils.isEmpty(questionForTeacher.getQuestionId())?UUID.randomUUID().toString().replaceAll("-", ""):questionForTeacher.getQuestionId());
        question.setContent(questionForTeacher.getQuestionContent());
        if(questionForTeacher.getOptions().size()>0){
            questionForTeacher.getOptions().forEach(option -> {
               question.getOptions().add(option);
            });
        }
        question.setAnswer(questionForTeacher.getAnswer());
        question.setAnswerDetail(questionForTeacher.getAnswerDetail());
        question.setParse(questionForTeacher.getParse());
        question.setType(translateQuestionType(questionForTeacher.getQuesetionType()).getName());
        question.setDifficulty(questionForTeacher.getDifficulty());
        question.setSubjectId(questionForTeacher.getSubjectId());
        question.setGradeId(questionForTeacher.getGradeId());
        question.setKnowledge(questionForTeacher.getKnowledge());
        question.setIdMD52(questionForTeacher.getQuestionIdMD52());
        question.setStatus(questionForTeacher.getQuestionStatus()!=null?questionForTeacher.getQuestionStatus().getName():null);
        if(StringUtils.isEmpty(questionForTeacher.getQuestionId())) question.setCreateTime(new Date().getTime());
        question.setUpdateTime(new Date().getTime());
        return question;
    }

    public static final TeacherAnswer ConvertTeacherAnswer(UserInfoForToken userInfo, String questionId, String studentAnswerId, AnswerForTeacher answerForTeacher){
        TeacherAnswer teacherAnswer = new TeacherAnswer();
        teacherAnswer.setId(StringUtils.isEmpty(answerForTeacher.getAnswerId())?UUID.randomUUID().toString().replaceAll("-", ""):answerForTeacher.getAnswerId());
        teacherAnswer.setTeacherId(userInfo.getUserId());
        teacherAnswer.setTeacherName(userInfo.getUserName());
        teacherAnswer.setSubjectivePic(answerForTeacher.getPicForSubjective());
        teacherAnswer.setSubjectivePicStub(answerForTeacher.getStubForSubjective());
        teacherAnswer.setSubjectiveScore(answerForTeacher.getScore());
        teacherAnswer.setQuestionId(questionId);
        teacherAnswer.setStudentAnswerId(studentAnswerId);
        if(StringUtils.isEmpty(answerForTeacher.getAnswerId())) teacherAnswer.setCreateTime(new Date().getTime());
        teacherAnswer.setUpdateTime(new Date().getTime());
        return teacherAnswer;
    }

    public static final StudentAnswer ConvertStudentAnswer(UserInfoForToken userInfo, String questionId, AnswerForStudent answerForStudent){
        StudentAnswer studentAnswer = new StudentAnswer();
        studentAnswer.setId(StringUtils.isEmpty(answerForStudent.getAnswerId())?UUID.randomUUID().toString().replaceAll("-", ""):answerForStudent.getAnswerId());
        studentAnswer.setStudentId(userInfo.getUserId());
        studentAnswer.setStudentName(userInfo.getUserName());
        studentAnswer.setObjectiveAnswer(answerForStudent.getAnswerForObjective());
        studentAnswer.setResult(answerForStudent.getResult().getName());
        studentAnswer.setSubjectivePic(answerForStudent.getPicForSubjective());
        studentAnswer.setSubjectivePicStub(answerForStudent.getStubForSubjective());
        //studentAnswer.setSubjectiveScore(answerForStudent.getSubjectiveScore());
        studentAnswer.setQuestionId(questionId);
        if(StringUtils.isEmpty(answerForStudent.getAnswerId())) studentAnswer.setCreateTime(new Date().getTime());
        studentAnswer.setUpdateTime(new Date().getTime());
        return studentAnswer;
    }

    private static QuestionType translateQuestionType(String quTypeInChinese) {
        switch (quTypeInChinese){
            case "选择题" : return QuestionType.OBJECTIVE;
            default: return QuestionType.SUBJECTIVE;
        }
    }
}
