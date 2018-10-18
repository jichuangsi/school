package com.jichuangsi.school.courseservice.util;

import com.jichuangsi.school.courseservice.constant.Result;
import com.jichuangsi.school.courseservice.constant.Status;
import com.jichuangsi.school.courseservice.entity.Course;
import com.jichuangsi.school.courseservice.entity.Question;
import com.jichuangsi.school.courseservice.entity.StudentAnswer;
import com.jichuangsi.school.courseservice.model.AnswerForStudent;
import com.jichuangsi.school.courseservice.model.CourseForTeacher;
import com.jichuangsi.school.courseservice.model.QuestionForTeacher;

import java.util.List;

public final class MappingEntity2ModelConverter {

    private MappingEntity2ModelConverter(){}

    public static final CourseForTeacher ConvertCourse(Course course){
        CourseForTeacher courseForTeacher = new CourseForTeacher();
        courseForTeacher.setCourseId(course.getId());
        courseForTeacher.setCourseName(course.getName());
        courseForTeacher.setCourseInfo(course.getInfo());
        courseForTeacher.setCourseStatus(Status.getStatus(course.getStatus()));
        courseForTeacher.setTeacherId(course.getTeacherId());
        courseForTeacher.setTeacherName(course.getTeacherName());
        courseForTeacher.setClassId(course.getClassId());
        courseForTeacher.setClassName(course.getClassName());
        courseForTeacher.setCourseStartTime(course.getStartTime());
        courseForTeacher.setCourseEndTime(course.getEndTime());

        return courseForTeacher;
    }

    public static final QuestionForTeacher ConvertQuestion(Question question){
        QuestionForTeacher questionForTeacher = new QuestionForTeacher();
        questionForTeacher.setQuestionId(question.getId());
        questionForTeacher.setQuestionContent(question.getContent());
        if(question.getOptions().size()>0){
            question.getOptions().forEach(option -> {
                questionForTeacher.getOptions().add(option);
            });
        }
        questionForTeacher.setAnswer(question.getAnswer());
        questionForTeacher.setAnswerDetail(question.getAnswerDetail());
        questionForTeacher.setParse(question.getParse());
        questionForTeacher.setQuesetionType(question.getType());
        questionForTeacher.setDifficulty(question.getDifficulty());
        questionForTeacher.setSubjectId(question.getSubjectId());
        questionForTeacher.setGradeId(question.getGradeId());
        questionForTeacher.setKnowledge(question.getKnowledge());
        questionForTeacher.setQuestionIdMD52(question.getIdMD52());
        questionForTeacher.setQuestionStatus(Status.getStatus(question.getStatus()));
        return questionForTeacher;
    }

    public static final AnswerForStudent ConvertStudentAnswer(StudentAnswer studentAnswer){
        AnswerForStudent answerForStudent = new AnswerForStudent();
        answerForStudent.setAnswerId(studentAnswer.getId());
        answerForStudent.setStudentId(studentAnswer.getStudentId());
        answerForStudent.setStudentName(studentAnswer.getStudentName());
        answerForStudent.setAnswerForObjective(studentAnswer.getObjectiveAnswer());
        answerForStudent.setPicForSubjective(studentAnswer.getSubjectivePic());
        answerForStudent.setStubForSubjective(studentAnswer.getSubjectivePicStub());
        answerForStudent.setObjectiveResult(Result.getResult(studentAnswer.getObjectiveResult()));
        answerForStudent.setSubjectiveScore(studentAnswer.getSubjectiveScore());
        return answerForStudent;
    }
}
