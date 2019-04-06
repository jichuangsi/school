package com.jichuangsi.school.user.util;

import com.jichuangsi.microservice.common.model.UserInfoForToken;
import com.jichuangsi.school.user.constant.Sex;
import com.jichuangsi.school.user.constant.Status;
import com.jichuangsi.school.user.entity.RoleInfo;
import com.jichuangsi.school.user.entity.StudentInfo;
import com.jichuangsi.school.user.entity.TeacherInfo;
import com.jichuangsi.school.user.entity.UserInfo;
import com.jichuangsi.school.user.entity.app.AppInfoEntity;
import com.jichuangsi.school.user.entity.backstage.BackUserInfo;
import com.jichuangsi.school.user.entity.org.*;
import com.jichuangsi.school.user.model.System.Role;
import com.jichuangsi.school.user.model.System.User;
import com.jichuangsi.school.user.model.app.AppInfoModule;
import com.jichuangsi.school.user.model.basic.Phrase;
import com.jichuangsi.school.user.model.basic.Subject;
import com.jichuangsi.school.user.model.org.ClassModel;
import com.jichuangsi.school.user.model.roles.Student;
import com.jichuangsi.school.user.model.roles.Teacher;
import com.jichuangsi.school.user.model.school.GradeModel;
import com.jichuangsi.school.user.model.school.PhraseModel;
import com.jichuangsi.school.user.model.school.SchoolModel;
import com.jichuangsi.school.user.model.school.SubjectModel;
import com.jichuangsi.school.user.model.transfer.TransferClass;
import com.jichuangsi.school.user.model.transfer.TransferSchool;
import com.jichuangsi.school.user.model.transfer.TransferStudent;
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
                    teacher.setSchool(new SchoolModel(teacherInfo.getSchool().getSchoolId(), teacherInfo.getSchool().getSchoolName()));
                if(teacherInfo.getPhrase()!=null)
                    teacher.setPhrase(new Phrase(teacherInfo.getPhrase().getPhraseId(), teacherInfo.getPhrase().getPhraseName()));
                if(teacherInfo.getPrimaryClass()!=null)
                    teacher.setPrimaryClass(new ClassModel(teacherInfo.getPrimaryClass().getClassId(), teacherInfo.getPrimaryClass().getClassName()));
                List<ClassModel> classes = new ArrayList<ClassModel>();
                teacherInfo.getSecondaryClasses().forEach(c -> {
                    classes.add(new ClassModel(c.getClassId(), c.getClassName()));
                });
                teacher.setSecondaryClass(classes);
                if(teacherInfo.getPrimaryGrade()!=null)
                    teacher.setPrimaryGrade(new GradeModel(teacherInfo.getPrimaryGrade().getGradeId(), teacherInfo.getPrimaryGrade().getGradeName()));
                List<GradeModel> grades = new ArrayList<GradeModel>();
                teacherInfo.getSecondaryGrades().forEach(g -> {
                    grades.add(new GradeModel(g.getGradeId(), g.getGradeName()));
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
                    student.setSchool(new SchoolModel(studentInfo.getSchool().getSchoolId(), studentInfo.getSchool().getSchoolName()));
                if(studentInfo.getPhrase()!=null)
                    student.setPhrase(new Phrase(studentInfo.getPhrase().getPhraseId(), studentInfo.getPhrase().getPhraseName()));
                if(studentInfo.getPrimaryClass()!=null)
                    student.setPrimaryClass(new ClassModel(studentInfo.getPrimaryClass().getClassId(), studentInfo.getPrimaryClass().getClassName()));
                if(studentInfo.getPrimaryGrade()!=null)
                    student.setPrimaryGrade(new GradeModel(studentInfo.getPrimaryGrade().getGradeId(), studentInfo.getPrimaryGrade().getGradeName()));
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
                if (null != teacherInfo.getPrimaryClass()){
                    transferTeacher.setPrimaryClassId(teacherInfo.getPrimaryClass().getClassId());
                    transferTeacher.setPrimaryClassName(teacherInfo.getPrimaryClass().getClassName());
                }
                if (null != teacherInfo.getSecondaryClasses() && teacherInfo.getSecondaryClasses().size() > 0){
                    List<ClassModel> classModels = new ArrayList<ClassModel>();
                    for (TeacherInfo.Class cl : teacherInfo.getSecondaryClasses()){
                        ClassModel classModel = new ClassModel();
                        classModel.setClassId(cl.getClassId());
                        classModel.setClassName(cl.getClassName());
                        classModels.add(classModel);
                    }
                    transferTeacher.setSecondaryClasses(classModels);
                }
                if (null != teacherInfo.getSecondarySubjects() && teacherInfo.getSecondarySubjects().size() > 0){
                    List<SubjectModel> subjectModels = new ArrayList<SubjectModel>();
                    for (TeacherInfo.Subject subject : teacherInfo.getSecondarySubjects()){
                        SubjectModel subjectModel = new SubjectModel();
                        subjectModel.setSubjectName(subject.getSubjectName());
                        subjectModel.setId(subject.getSubjectId());
                        subjectModels.add(subjectModel);
                    }
                    transferTeacher.setSecondarySubjects(subjectModels);
                }
                if (null != teacherInfo.getSecondaryGrades() && teacherInfo.getSecondaryGrades().size() > 0){
                    List<GradeModel> gradeModels = new ArrayList<GradeModel>();
                    for (TeacherInfo.Grade grade : teacherInfo.getSecondaryGrades()){
                        GradeModel gradeModel = new GradeModel();
                        gradeModel.setGradeName(grade.getGradeName());
                        gradeModel.setGradeId(grade.getGradeId());
                        gradeModels.add(gradeModel);
                    }
                    transferTeacher.setSecondaryGrades(gradeModels);
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

    public static final ClassModel TransferClass(ClassInfo classInfo){
        ClassModel c = new ClassModel();
        c.setClassId(classInfo.getId());
        c.setClassName(classInfo.getName());
        c.setCreateTime(classInfo.getCreateTime());
        c.setUpdateTime(classInfo.getUpdateTime());
        return c;
    }

    public static final TransferStudent TransferStudent(UserInfo userInfo){
        TransferStudent transferStudent = new TransferStudent();
        transferStudent.setStudentId(userInfo.getId());
        transferStudent.setStudentName(userInfo.getName());
        transferStudent.setStudentAccount(userInfo.getAccount());
        return transferStudent;
    }

    public static final AppInfoModule ConvertAppInfo(AppInfoEntity appInfoEntity){
        AppInfoModule appInfoModule = new AppInfoModule();
        appInfoModule.setPackageName(appInfoEntity.getPkName());
        appInfoModule.setDownloadPath(appInfoEntity.getDlPath());
        appInfoModule.setAppVersion(appInfoEntity.getVersion());
        appInfoModule.setRemark(appInfoEntity.getRemark());
        appInfoModule.setMandatory(appInfoEntity.isMandatory());

        return appInfoModule;
    }


    public static SchoolModel CONVERTEFROMSCHOOLINFO(SchoolInfo info){
        SchoolModel model = new SchoolModel();
        model.setAddress(info.getAddress());
        model.setCreatedTime(info.getCreateTime());
        model.setSchoolId(info.getId());
        model.setSchoolName(info.getName());
        model.setUpdateTime(info.getUpdateTime());
        return model;
    }

    public static GradeModel CONVERTERFROMGRADEINFO(GradeInfo info){
        GradeModel model = new GradeModel();
        model.setCreatedTime(info.getCreateTime());
        model.setGradeId(info.getId());
        model.setGradeName(info.getName());
        model.setUpdateTime(info.getUpdateTime());
        return model;
    }

    public static ClassModel CONVERTERFRONCLASSINFO(ClassInfo info){
        ClassModel model = new ClassModel();
        model.setClassId(info.getId());
        model.setClassName(info.getName());
        model.setCreateTime(info.getCreateTime());
        model.setUpdateTime(info.getUpdateTime());
        return model;
    }

    public static SubjectModel CONVERTERFROMSUBJECTINFO(SubjectInfo info){
        SubjectModel model = new SubjectModel();
        model.setId(info.getId());
        model.setSubjectName(info.getSubjectName());
        return model;
    }

    public static PhraseModel CONVERTERFROMPHRASEINFO(PhraseInfo info){
        PhraseModel model = new PhraseModel();
        model.setId(info.getId());
        model.setPhraseName(info.getPhraseName());
        return model;
    }

    public final static UserInfoForToken CONVERTERFROMBACKUSERINFO(BackUserInfo userInfo){
        UserInfoForToken userInfoForToken = new UserInfoForToken();
        userInfoForToken.setRoleName(userInfo.getRoleName());
        userInfoForToken.setSchoolId(userInfo.getSchoolId());
        userInfoForToken.setUserId(userInfo.getId());
        userInfoForToken.setUserName(userInfo.getUserName());
        userInfoForToken.setUserNum(userInfo.getAccount());
        return userInfoForToken;
    }
}
