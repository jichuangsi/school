package com.jichuangsi.school.user.util;

import com.jichuangsi.school.user.entity.RoleInfo;
import com.jichuangsi.school.user.entity.StudentInfo;
import com.jichuangsi.school.user.entity.TeacherInfo;
import com.jichuangsi.school.user.entity.UserInfo;
import com.jichuangsi.school.user.entity.org.ClassInfo;
import com.jichuangsi.school.user.model.System.Role;
import com.jichuangsi.school.user.model.System.User;
import com.jichuangsi.school.user.model.roles.Student;
import com.jichuangsi.school.user.model.roles.Teacher;
import com.jichuangsi.school.user.model.org.Class;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

public  final class MappingModel2EntityConverter {

    private MappingModel2EntityConverter(){}

    public static final UserInfo ConvertUser(User user){
        UserInfo userInfo = new UserInfo();
        userInfo.setId(StringUtils.isEmpty(user.getUserId())? UUID.randomUUID().toString().replaceAll("-", ""):user.getUserId());
        userInfo.setAccount(user.getUserAccount());
        userInfo.setName(user.getUserName());
        userInfo.setPwd(user.getUserPwd());
        userInfo.setSex(user.getUserSex().getName());
        userInfo.setStatus(user.getUserStatus().getName());
        List<RoleInfo> roleInfos = ConvertRole(user.getRoles());
        userInfo.setRoleInfos(roleInfos);

        if(StringUtils.isEmpty(user.getUserId())) userInfo.setCreateTime(new Date().getTime());
        userInfo.setUpdateTime(new Date().getTime());

        return userInfo;
    }

    public static final List<RoleInfo> ConvertRole(List<Role> roles){
        List<RoleInfo> roleInfos = new ArrayList<RoleInfo>();
        roles.forEach(role -> {
            if(role instanceof Teacher){
                TeacherInfo teacherInfo = new TeacherInfo();
                Teacher teacher = (Teacher) role;
                teacherInfo.setRoleName(teacher.getRoleName());
                if(teacher.getSchool()!=null)
                    teacherInfo.setSchool(teacher.getSchool().getSchoolId(), teacher.getSchool().getSchoolName());
                if(teacher.getPhrase()!=null)
                    teacherInfo.setPhrase(teacher.getPhrase().getPhraseId(), teacher.getPhrase().getPhraseName());
                if(teacher.getPrimaryClass()!=null)
                    teacherInfo.setPrimaryClass(teacher.getPrimaryClass().getClassId(), teacher.getPrimaryClass().getClassName());
                teacher.getSecondaryClass().forEach(c -> {
                    teacherInfo.addSecondaryClasses(c.getClassId(), c.getClassName());
                });
                if(teacher.getPrimaryGrade()!=null)
                    teacherInfo.setPrimaryGrade(teacher.getPrimaryGrade().getGradeId(), teacher.getPrimaryGrade().getGradeName());
                teacher.getSecondaryGrades().forEach(g -> {
                    teacherInfo.addSecondaryGrades(g.getGradeId(), g.getGradeName());
                });
                if(teacher.getPrimarySubject()!=null)
                    teacherInfo.setPrimarySubject(teacher.getPrimarySubject().getSubjectId(), teacher.getPrimarySubject().getSubjectName());
                teacher.getSecondarySubjects().forEach(s -> {
                    teacherInfo.addSecondarySubjects(s.getSubjectId(), s.getSubjectName());
                });
                roleInfos.add(teacherInfo);
            }else if(role instanceof Student){
                StudentInfo studentInfo = new StudentInfo();
                Student student = (Student) role;
                studentInfo.setRoleName(student.getRoleName());
                if(student.getSchool()!=null)
                    studentInfo.setSchool(student.getSchool().getSchoolId(), student.getSchool().getSchoolName());
                if(student.getPhrase()!=null)
                    studentInfo.setPhrase(student.getPhrase().getPhraseId(), student.getPhrase().getPhraseName());
                if(student.getPrimaryClass()!=null)
                    studentInfo.setPrimaryClass(student.getPrimaryClass().getClassId(),student.getPrimaryClass().getClassName());
                if(student.getPrimaryGrade()!=null)
                    studentInfo.setPrimaryGrade(student.getPrimaryGrade().getGradeId(),student.getPrimaryGrade().getGradeName());
                roleInfos.add(studentInfo);
            }
        });

        return roleInfos;
    }

    public static final ClassInfo ConvertClass(Class c){
        ClassInfo classInfo = new ClassInfo();
        classInfo.setId(StringUtils.isEmpty(c.getClassId())?UUID.randomUUID().toString().replaceAll("-", ""):c.getClassId());
        classInfo.setName(c.getClassName());
        if(StringUtils.isEmpty(c.getClassId())) classInfo.setCreateTime(new Date().getTime());
        classInfo.setUpdateTime(new Date().getTime());

        return classInfo;
    }
}
