package com.jichuangsi.school.user.util;

import com.jichuangsi.microservice.common.model.UserInfoForToken;
import com.jichuangsi.school.user.constant.Sex;
import com.jichuangsi.school.user.constant.Status;
import com.jichuangsi.school.user.entity.*;
import com.jichuangsi.school.user.entity.app.AppInfoEntity;
import com.jichuangsi.school.user.entity.backstage.*;
import com.jichuangsi.school.user.entity.org.*;
import com.jichuangsi.school.user.feign.model.NoticeModel;
import com.jichuangsi.school.user.model.RoleUrlUseWayModel;
import com.jichuangsi.school.user.model.System.Role;
import com.jichuangsi.school.user.model.System.User;
import com.jichuangsi.school.user.model.app.AppInfoModule;
import com.jichuangsi.school.user.model.backstage.BackRoleModel;
import com.jichuangsi.school.user.model.backstage.BackUserModel;
import com.jichuangsi.school.user.model.backstage.SchoolNoticeModel;
import com.jichuangsi.school.user.model.backstage.TimeTableModel;
import com.jichuangsi.school.user.model.basic.Phrase;
import com.jichuangsi.school.user.model.basic.Subject;
import com.jichuangsi.school.user.model.file.UserFile;
import com.jichuangsi.school.user.model.org.ClassModel;
import com.jichuangsi.school.user.model.roles.Student;
import com.jichuangsi.school.user.model.roles.Teacher;
import com.jichuangsi.school.user.model.school.*;
import com.jichuangsi.school.user.model.transfer.TransferClass;
import com.jichuangsi.school.user.model.transfer.TransferSchool;
import com.jichuangsi.school.user.model.transfer.TransferStudent;
import com.jichuangsi.school.user.model.transfer.TransferTeacher;
import com.jichuangsi.school.user.model.user.StudentModel;
import com.jichuangsi.school.user.model.user.TeacherModel;

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
        user.setPortrait(userInfo.getPortrait());
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
                    teacher.setPhrase(new Phrase(teacherInfo.getPhrase().getPhraseId(), teacherInfo.getPhrase().getPhraseName(),teacherInfo.getPhrase().getPid()));
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
                    student.setPhrase(new Phrase(studentInfo.getPhrase().getPhraseId(), studentInfo.getPhrase().getPhraseName(),studentInfo.getPhrase().getPid()));
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
        model.setPharseId(info.getPhraseId());
        return model;
    }

    public final static UserInfoForToken CONVERTERFROMBACKUSERINFO(BackUserInfo userInfo){
        UserInfoForToken userInfoForToken = new UserInfoForToken();
        userInfoForToken.setSchoolId(userInfo.getSchoolId());
        userInfoForToken.setUserId(userInfo.getId());
        userInfoForToken.setUserName(userInfo.getUserName());
        userInfoForToken.setUserNum(userInfo.getAccount());
        userInfoForToken.setRoleName(userInfo.getRoleName());
        return userInfoForToken;
    }

    public final static BackRoleModel CONVERTERFROMBACKROLEINFO(BackRoleInfo roleInfo){
        BackRoleModel model = new BackRoleModel();
        model.setRoleId(roleInfo.getId());
        model.setRoleName(roleInfo.getRoleName());
        return model;
    }

    public final static BackUserModel CONVERTERFROMBACKUSERINFOTOMODEL(BackUserInfo userInfo){
        BackUserModel model = new BackUserModel();
        model.setAccount(userInfo.getAccount());
        model.setId(userInfo.getId());
        model.setRoleId(userInfo.getRoleId());
        model.setRoleName(userInfo.getRoleName());
        model.setSchoolId(userInfo.getSchoolId());
        model.setSchoolName(userInfo.getSchoolName());
        model.setUserName(userInfo.getUserName());
        return model;
    }

    public final static SchoolRoleModel CONVERTERFROMSCHOOLROLEINFO(SchoolRoleInfo schoolRoleInfo){
        SchoolRoleModel model = new SchoolRoleModel();
        model.setId(schoolRoleInfo.getId());
        model.setRoleName(schoolRoleInfo.getRoleName());
        return model;
    }

    public final static TeacherModel CONVERTERFROMUSERINFO(UserInfo userInfo){
        TeacherModel model = new TeacherModel();
        model.setAccount(userInfo.getAccount());
        model.setId(userInfo.getId());
        model.setName(userInfo.getName());
        model.setSex(userInfo.getSex());
        model.setStatus(userInfo.getStatus());
        if (userInfo.getRoleInfos().get(0) instanceof  TeacherInfo){
            TeacherInfo teacherInfo = (TeacherInfo) userInfo.getRoleInfos().get(0);
            model.setPrimarySubject(new Subject(teacherInfo.getPrimarySubject().getSubjectId(),teacherInfo.getPrimarySubject().getSubjectName()));
            List<Subject> subjects = new ArrayList<Subject>();
            teacherInfo.getSecondarySubjects().forEach(subject -> {
                subjects.add(new Subject(subject.getSubjectId(),subject.getSubjectName()));
            });
            model.setSecondarySubjects(subjects);
            model.setRoleIds(teacherInfo.getRoleIds());
            model.setSchool(new SchoolModel(teacherInfo.getSchool().getSchoolId(),teacherInfo.getSchool().getSchoolName()));
            if(teacherInfo.getPhrase()!=null)
                model.setPhrase(new Phrase(teacherInfo.getPhrase().getPhraseId(),teacherInfo.getPhrase().getPhraseName(),teacherInfo.getPhrase().getPid()));
        }
        return model;
    }

    public final static StudentModel CONVERTESTUDENTMODELRFROMUSERINFO(UserInfo userInfo){
        StudentModel model = new StudentModel();
        model.setAccount(userInfo.getAccount());
        model.setId(userInfo.getId());
        model.setName(userInfo.getName());
        model.setSex(userInfo.getSex());
        model.setStatus(userInfo.getStatus());
        if (userInfo.getRoleInfos().get(0) instanceof StudentInfo) {
            StudentInfo studentInfo = (StudentInfo) userInfo.getRoleInfos().get(0);
            model.setPhrase(new Phrase(studentInfo.getPhrase().getPhraseId(),studentInfo.getPhrase().getPhraseName(),studentInfo.getPhrase().getPid()));
            model.setPrimaryClass(new ClassModel(studentInfo.getPrimaryClass().getClassId(),studentInfo.getPrimaryClass().getClassName()));
            model.setPrimaryGrade(new GradeModel(studentInfo.getPrimaryGrade().getGradeId(),studentInfo.getPrimaryGrade().getGradeName()));
            model.setSchool(new SchoolModel(studentInfo.getSchool().getSchoolId(),studentInfo.getSchool().getSchoolName()));
        }
        return model;
    }

    public final static TimeTableModel CONVERTERFROMTIMETABLEINFO(TimeTableInfo tableInfo){
        TimeTableModel model = new TimeTableModel();
        model.setClassId(tableInfo.getClassId());
        model.setClassName(tableInfo.getClassName());
        model.setId(tableInfo.getId());
        for (int i = 0;i < tableInfo.getClassBegin().size() ; i++){
            List<String> info = new ArrayList<String>();
            info.add(tableInfo.getClassBegin().get(i));
            if (tableInfo.getMonday().size() > i){
                info.add(tableInfo.getMonday().get(i));
            }
            if (tableInfo.getTuesday().size() > i){
                info.add(tableInfo.getTuesday().get(i));
            }
            if (tableInfo.getWednesday().size() > i){
                info.add(tableInfo.getWednesday().get(i));
            }
            if (tableInfo.getThursday().size() > i){
                info.add(tableInfo.getThursday().get(i));
            }
            if (tableInfo.getFriday().size() > i){
                info.add(tableInfo.getFriday().get(i));
            }
            model.getDataInfo().add(info);
        }
        return model;
    }

    public static  final UserFile CONVERTERFROMSCHOOLATTACHMENT(SchoolAttachment schoolAttachment){
        UserFile userFile = new UserFile();
        userFile.setContentType(schoolAttachment.getContentType());
        userFile.setName(schoolAttachment.getSubName());
        userFile.setOriginalName(schoolAttachment.getOriginalName());
        return userFile;
    }

    public static final NoticeModel CONVERTERFROMSCHOOLNOTICEINFO(SchoolNoticeInfo schoolNoticeInfo){
        NoticeModel model = new NoticeModel();
        model.setContent(schoolNoticeInfo.getContent());
        model.setCreatedTime(schoolNoticeInfo.getCreatedTime());
        model.setId(schoolNoticeInfo.getId());
        model.setTitle(schoolNoticeInfo.getTitle());
        return model;
    }

    public static final SchoolNoticeModel CONVERTERFROMSCHOOLNOTICEINFOTOSCHOOLNOTICEMODEL(SchoolNoticeInfo schoolNoticeInfo){
        SchoolNoticeModel model = new SchoolNoticeModel();
        model.setClassName(schoolNoticeInfo.getClassName());
        model.setContent(schoolNoticeInfo.getContent());
        model.setCreatedTime(schoolNoticeInfo.getCreatedTime());
        model.setGradeName(schoolNoticeInfo.getGradeName());
        model.setId(schoolNoticeInfo.getId());
        model.setPharseName(schoolNoticeInfo.getPharseName());
        model.setTitle(schoolNoticeInfo.getTitle());
        return model;
    }

    public static final List<RoleUrlUseWayModel> CONVERTERFROMROLEURLTOROLEURLMODEL(List<Roleurl> roleurls){
        List<RoleUrlUseWayModel> roleUrlModels=new ArrayList<>();
        for (Roleurl roleurl: roleurls) {
            RoleUrlUseWayModel model=new RoleUrlUseWayModel();
            model.setId(roleurl.getId());
            model.setName(roleurl.getName());
            model.setUrl(roleurl.getUrl());
            model.setUsewayid(roleurl.getUseWay().getId());
            model.setUsewayname(roleurl.getUseWay().getName());
            roleUrlModels.add(model);
        }
        return roleUrlModels;
    }
    public static final Roleurl CONVERTERFROMROLEURLMODELTOROLEURL(RoleUrlUseWayModel model){
        Roleurl roleurl=new Roleurl();
        roleurl.setId(model.getId());
        roleurl.setName(model.getName());
        roleurl.setUrl(model.getUrl());
        UseWay useWay=new UseWay();
        useWay.setId(model.getUsewayid());
        useWay.setName(model.getName());
        roleurl.setUseWay(useWay);
        return roleurl;
    }
}
