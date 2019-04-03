package com.jichuangsi.school.courseservice.util;

import com.jichuangsi.school.courseservice.constant.Result;
import com.jichuangsi.school.courseservice.constant.Status;
import com.jichuangsi.school.courseservice.entity.*;
import com.jichuangsi.school.courseservice.entity.Course;
import com.jichuangsi.school.courseservice.entity.Question;
import com.jichuangsi.school.courseservice.model.*;
import com.jichuangsi.school.courseservice.model.Knowledge;

public final class MappingEntity2ModelConverter {

    private MappingEntity2ModelConverter(){}

    public static final CourseForTeacher ConvertTeacherCourse(Course course){
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
        courseForTeacher.setCreateTime(course.getCreateTime());
        courseForTeacher.setUpdateTime(course.getUpdateTime());
        courseForTeacher.setCoursePic(course.getPicAddress());
        courseForTeacher.setSubjectName(course.getSubjectName());
        courseForTeacher.setSubjectId(course.getSubjectId());
        if(course.getAttachments()!=null&&course.getAttachments().size()>0){
            course.getAttachments().forEach(attachment -> {
                courseForTeacher.getAttachments().add(new AttachmentModel(attachment.getName(),attachment.getSub(), attachment.getContentType()));
            });
        }
        return courseForTeacher;
    }

    public static final CourseForStudent ConvertStudentCourse(Course course){
        CourseForStudent courseForStudent = new CourseForStudent();
        courseForStudent.setCourseId(course.getId());
        courseForStudent.setCourseName(course.getName());
        courseForStudent.setCourseInfo(course.getInfo());
        courseForStudent.setCourseStatus(Status.getStatus(course.getStatus()));
        courseForStudent.setTeacherId(course.getTeacherId());
        courseForStudent.setTeacherName(course.getTeacherName());
        courseForStudent.setClassId(course.getClassId());
        courseForStudent.setClassName(course.getClassName());
        courseForStudent.setCourseStartTime(course.getStartTime());
        courseForStudent.setCourseEndTime(course.getEndTime());
        courseForStudent.setCreateTime(course.getCreateTime());
        courseForStudent.setUpdateTime(course.getUpdateTime());
        courseForStudent.setSubjectName(course.getSubjectName());
        courseForStudent.setSubjectId(course.getSubjectId());
        courseForStudent.setQuestionIds(course.getQuestionIds());
        return courseForStudent;
    }

    public static final QuestionForTeacher ConvertTeacherQuestion(Question question){
        QuestionForTeacher questionForTeacher = new QuestionForTeacher();
        questionForTeacher.setQuestionId(question.getId());
        questionForTeacher.setQuestionContent(question.getContent());
        if(question.getOptions()!=null && question.getOptions().size()>0){
            question.getOptions().forEach(option -> {
                questionForTeacher.getOptions().add(option);
            });
        }
        questionForTeacher.setAnswer(question.getAnswer());
        questionForTeacher.setAnswerDetail(question.getAnswerDetail());
        questionForTeacher.setParse(question.getParse());
        questionForTeacher.setQuesetionType(question.getType());
        questionForTeacher.setQuestionTypeInCN(question.getTypeInCN());
        questionForTeacher.setDifficulty(question.getDifficulty());
        questionForTeacher.setSubjectId(question.getSubjectId());
        questionForTeacher.setGradeId(question.getGradeId());
        question.getKnowledges().forEach(q->{
            questionForTeacher.getKnowledges().add(new Knowledge(q.getKnowledgeId(),q.getKnowledge(),q.getCapabilityId(),q.getCapability()));
        });
        questionForTeacher.setQuestionIdMD52(question.getIdMD52());
        questionForTeacher.setQuestionStatus(Status.getStatus(question.getStatus()));
        questionForTeacher.setQuestionPic(question.getPic());
        questionForTeacher.setCreateTime(question.getCreateTime());
        questionForTeacher.setUpdateTime(question.getUpdateTime());
        return questionForTeacher;
    }

    public static final QuestionForStudent ConvertStudentQuestion(Question question){
        QuestionForStudent questionForStudent = new QuestionForStudent();
        questionForStudent.setQuestionId(question.getId());
        questionForStudent.setQuestionContent(question.getContent());
        if(question.getOptions()!=null && question.getOptions().size()>0){
            question.getOptions().forEach(option -> {
                questionForStudent.getOptions().add(option);
            });
        }
        questionForStudent.setAnswer(question.getAnswer());
        questionForStudent.setAnswerDetail(question.getAnswerDetail());
        questionForStudent.setParse(question.getParse());
        questionForStudent.setQuesetionType(question.getType());
        questionForStudent.setQuestionTypeInCN(question.getTypeInCN());
        questionForStudent.setDifficulty(question.getDifficulty());
        questionForStudent.setSubjectId(question.getSubjectId());
        questionForStudent.setGradeId(question.getGradeId());
        question.getKnowledges().forEach(q->{
            questionForStudent.getKnowledges().add(new Knowledge(q.getKnowledgeId(),q.getKnowledge(),q.getCapabilityId(),q.getCapability()));
        });
        questionForStudent.setQuestionIdMD52(question.getIdMD52());
        questionForStudent.setQuestionStatus(Status.getStatus(question.getStatus()));
        questionForStudent.setQuestionPic(question.getPic());
        questionForStudent.setCreateTime(question.getCreateTime());
        questionForStudent.setUpdateTime(question.getUpdateTime());
        return questionForStudent;
    }

    public static final AnswerForStudent ConvertStudentAnswer(StudentAnswer studentAnswer){
        AnswerForStudent answerForStudent = new AnswerForStudent();
        answerForStudent.setAnswerId(studentAnswer.getId());
        answerForStudent.setStudentId(studentAnswer.getStudentId());
        answerForStudent.setStudentName(studentAnswer.getStudentName());
        answerForStudent.setAnswerForObjective(studentAnswer.getObjectiveAnswer());
        //answerForStudent.setPicForSubjective(studentAnswer.getSubjectivePic());
        answerForStudent.setStubForSubjective(studentAnswer.getSubjectivePicStub());
        answerForStudent.setResult(Result.getResult(studentAnswer.getResult()));
        answerForStudent.setSubjectiveScore(null==studentAnswer.getSubjectiveScore()?0:studentAnswer.getSubjectiveScore());
        answerForStudent.setCreateTime(studentAnswer.getCreateTime());
        answerForStudent.setUpdateTime(studentAnswer.getUpdateTime());
        answerForStudent.setReviseTime(studentAnswer.getReviseTime());
        return answerForStudent;
    }

    public static final AnswerForTeacher ConvertTeacherAnswer(TeacherAnswer teacherAnswer){
        AnswerForTeacher answerForTeacher = new AnswerForTeacher();
        answerForTeacher.setAnswerId(teacherAnswer.getId());
        answerForTeacher.setTeacherId(teacherAnswer.getTeacherId());
        answerForTeacher.setTeacherName(teacherAnswer.getTeacherName());
        answerForTeacher.setPicForSubjective(teacherAnswer.getSubjectivePic());
        answerForTeacher.setStubForSubjective(teacherAnswer.getSubjectivePicStub());
        answerForTeacher.setShare(teacherAnswer.isShare());
        answerForTeacher.setCreateTime(teacherAnswer.getCreateTime());
        answerForTeacher.setUpdateTime(teacherAnswer.getUpdateTime());
        answerForTeacher.setShareTime(teacherAnswer.getShareTime());
        return answerForTeacher;
    }

    public static AttachmentModel CONVERTERFROMATTACHMENT(Attachment attachment){
        AttachmentModel model = new AttachmentModel();
        model.setContentType(attachment.getContentType());
        model.setName(attachment.getName());
        model.setSub(attachment.getSub());
        model.setPublishFlag(attachment.getPublishStatus());
        return model;
    }


}
