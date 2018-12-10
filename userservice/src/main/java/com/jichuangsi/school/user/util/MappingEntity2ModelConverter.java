package com.jichuangsi.school.user.util;

import com.jichuangsi.school.user.constant.Sex;
import com.jichuangsi.school.user.constant.Status;
import com.jichuangsi.school.user.entity.RoleInfo;
import com.jichuangsi.school.user.entity.StudentInfo;
import com.jichuangsi.school.user.entity.TeacherInfo;
import com.jichuangsi.school.user.entity.UserInfo;
import com.jichuangsi.school.user.entity.org.ClassInfo;
import com.jichuangsi.school.user.model.System.Role;
import com.jichuangsi.school.user.model.System.User;
import com.jichuangsi.school.user.model.basic.Phrase;
import com.jichuangsi.school.user.model.basic.Subject;
import com.jichuangsi.school.user.model.org.Grade;
import com.jichuangsi.school.user.model.org.School;
import com.jichuangsi.school.user.model.roles.Student;
import com.jichuangsi.school.user.model.roles.Teacher;
import com.jichuangsi.school.user.model.org.Class;
import com.jichuangsi.school.user.model.transfer.TransferClass;
import com.jichuangsi.school.user.model.transfer.TransferSchool;
import com.jichuangsi.school.user.model.transfer.TransferTeacher;

import java.util.ArrayList;
import java.util.List;

public final class MappingEntity2ModelConverter {

    private MappingEntity2ModelConverter(){}

    public static final User ConvertUser(UserInfo userInfo){
        User user = new User();
        user.setUserId(userInfo.getId());
        user.setUserAccount(userInfo.getAccount());
        user.setUserStatus(Status.getStatus(userInfo.getStatus()));
        user.setUserName(userInfo.getName());
        user.setUserSex(Sex.getSex(userInfo.getSex()));
        user.setRoles(ConvertRole(userInfo.getRoleInfos()));
        user.setUserPwd(userInfo.getPwd());
        user.setCreateTime(userInfo.getCreateTime());
        user.setUpdateTime(userInfo.getUpdateTime());

        return user;
    }

    public static final List<Role> ConvertRole(List<RoleInfo> roleInfos){
        List<Role> roles = new ArrayList<Role>();
        roleInfos.forEach(roleInfo -> {
            if(roleInfo instanceof TeacherInfo){
                Teacher teacher = new Teacher();
                TeacherInfo teacherInfo = (TeacherInfo) roleInfo;
                teacher.setRoleName(teacherInfo.getRoleName());
                if(teacherInfo.getSchool()!=null)
                    teacher.setSchool(new School(teacherInfo.getSchool().getSchoolId(), teacherInfo.getSchool().getSchoolName()));
                if(teacherInfo.getPhrase()!=null)
                    teacher.setPhrase(new Phrase(teacherInfo.getPhrase().getPhraseId(), teacherInfo.getPhrase().getPhraseName()));
                if(teacherInfo.getPrimaryClass()!=null)
                    teacher.setPrimaryClass(new Class(teacherInfo.getPrimaryClass().getClassId(), teacherInfo.getPrimaryClass().getClassName()));
                List<Class> classes = new ArrayList<Class>();
                teacherInfo.getSecondaryClasses().forEach(c -> {
                    classes.add(new Class(c.getClassId(), c.getClassName()));
                });
                teacher.setSecondaryClass(classes);
                if(teacherInfo.getPrimaryGrade()!=null)
                    teacher.setPrimaryGrade(new Grade(teacherInfo.getPrimaryGrade().getGradeId(), teacherInfo.getPrimaryGrade().getGradeName()));
                List<Grade> grades = new ArrayList<Grade>();
                teacherInfo.getSecondaryGrades().forEach(g -> {
                    grades.add(new Grade(g.getGradeId(), g.getGradeName()));
                });
                teacher.setSecondaryGrades(grades);
                if(teacherInfo.getPrimarySubject()!=null)
                    teacher.setPrimarySubject(new Subject(teacherInfo.getPrimarySubject().getSubjectId(), teacherInfo.getPrimarySubject().getSubjectName()));
                List<Subject> subjects = new ArrayList<Subject>();
                teacherInfo.getSecondarySubjects().forEach(s -> {
                    subjects.add(new Subject(s.getSubjectId(), s.getSubjectName()));
                });
                teacher.setSecondarySubjects(subjects);
                roles.add(teacher);
            }else if(roleInfo instanceof StudentInfo){
                Student student = new Student();
                StudentInfo studentInfo = (StudentInfo)roleInfo;
                student.setRoleName(studentInfo.getRoleName());
                if(studentInfo.getSchool()!=null)
                    student.setSchool(new School(studentInfo.getSchool().getSchoolId(), studentInfo.getSchool().getSchoolName()));
                if(studentInfo.getPhrase()!=null)
                    student.setPhrase(new Phrase(studentInfo.getPhrase().getPhraseId(), studentInfo.getPhrase().getPhraseName()));
                if(studentInfo.getPrimaryClass()!=null)
                    student.setPrimaryClass(new Class(studentInfo.getPrimaryClass().getClassId(), studentInfo.getPrimaryClass().getClassName()));
                if(studentInfo.getPrimaryGrade()!=null)
                    student.setPrimaryGrade(new Grade(studentInfo.getPrimaryGrade().getGradeId(), studentInfo.getPrimaryGrade().getGradeName()));
                roles.add(student);
            }
        });

        return roles;
    }

    public static final TransferTeacher ConvertTransferTeacher(UserInfo userInfo){
        TransferTeacher transferTeacher = new TransferTeacher();
        transferTeacher.setTeacherId(userInfo.getId());
        transferTeacher.setTeacherName(userInfo.getName());
        userInfo.getRoleInfos().forEach(roleInfo -> {
            if(roleInfo instanceof TeacherInfo){
                TeacherInfo teacherInfo = (TeacherInfo) roleInfo;
                if(teacherInfo.getPhrase()!=null){
                    transferTeacher.setPhraseId(teacherInfo.getPhrase().getPhraseId());
                    transferTeacher.setPhraseName(teacherInfo.getPhrase().getPhraseName());
                }

                if(teacherInfo.getPrimarySubject()!=null){
                    transferTeacher.setSubjectId(teacherInfo.getPrimarySubject().getSubjectId());
                    transferTeacher.setSubjectName(teacherInfo.getPrimarySubject().getSubjectName());
                }

                if(teacherInfo.getPrimaryGrade()!=null){
                    transferTeacher.setGradeId(teacherInfo.getPrimaryGrade().getGradeId());
                    transferTeacher.setGradeName(teacherInfo.getPrimaryGrade().getGradeName());
                }

            }
        });
        return transferTeacher;
    }

    public static final List<TransferClass> TransferClassInfoFromTeacher(UserInfo userInfo){
        List<TransferClass> list = new ArrayList<TransferClass>();
        List<RoleInfo> roles = userInfo.getRoleInfos();
        for(int i=0;i<roles.size();i++){
            if(roles.get(i) instanceof TeacherInfo){
                TeacherInfo teacherInfo = (TeacherInfo) roles.get(i);
                for(int j=0;j<teacherInfo.getSecondaryClasses().size();j++){
                    TransferClass cl = new TransferClass();
                    cl.setClassName(teacherInfo.getSecondaryClasses().get(j).getClassName());
                    cl.setClassId(teacherInfo.getSecondaryClasses().get(j).getClassId());
                    list.add(cl);
                };
                if(teacherInfo.getPrimaryClass()!=null)
                    list.add(new TransferClass(teacherInfo.getPrimaryClass().getClassId(),teacherInfo.getPrimaryClass().getClassName()));
            }
        }
        return list;
    }

    public static final TransferSchool TransferSchoolInfoFromTeacher(UserInfo userInfo){
        TransferSchool transferSchool = new TransferSchool();
        List<RoleInfo> roles = userInfo.getRoleInfos();
        roles.forEach(role->{
            if(role instanceof TeacherInfo){
                TeacherInfo teacherInfo = (TeacherInfo) role;
                if(teacherInfo.getSchool()!=null){
                    transferSchool.setSchoolId(teacherInfo.getSchool().getSchoolId());
                    transferSchool.setSchoolName(teacherInfo.getSchool().getSchoolName());
                }
            }
        });
        return transferSchool;
    }

    public static final Class TransferClass(ClassInfo classInfo){
        Class c = new Class();
        c.setClassId(classInfo.getId());
        c.setClassName(classInfo.getName());
        c.setCreateTime(classInfo.getCreateTime());
        c.setUpdateTime(classInfo.getUpdateTime());

        return c;
    }
}
