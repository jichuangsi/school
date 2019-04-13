package com.jichuangsi.school.user.service.impl;

import com.jichuangsi.microservice.common.model.UserInfoForToken;
import com.jichuangsi.school.user.constant.MyResultCode;
import com.jichuangsi.school.user.constant.ResultCode;
import com.jichuangsi.school.user.entity.RoleInfo;
import com.jichuangsi.school.user.entity.TeacherInfo;
import com.jichuangsi.school.user.entity.UserInfo;
import com.jichuangsi.school.user.entity.org.*;
import com.jichuangsi.school.user.exception.ClassServiceException;
import com.jichuangsi.school.user.exception.SchoolServiceException;
import com.jichuangsi.school.user.feign.model.ClassDetailModel;
import com.jichuangsi.school.user.model.org.ClassModel;
import com.jichuangsi.school.user.model.school.SchoolModel;
import com.jichuangsi.school.user.model.school.TeacherInsertModel;
import com.jichuangsi.school.user.model.transfer.TransferStudent;
import com.jichuangsi.school.user.repository.*;
import com.jichuangsi.school.user.service.ISchoolClassService;
import com.jichuangsi.school.user.service.UserInfoService;
import com.jichuangsi.school.user.util.MappingEntity2ModelConverter;
import com.jichuangsi.school.user.util.MappingModel2EntityConverter;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class SchoolClassServiceImpl implements ISchoolClassService {
    @Resource
    private MongoTemplate mongoTemplate;
    @Resource
    private IGradeInfoRepository gradeInfoRepository;
    @Resource
    private ISchoolInfoRepository schoolInfoRepository;
    @Resource
    private UserInfoService userInfoService;
    @Resource
    private IClassInfoRepository classInfoRepository;
    @Resource
    private UserRepository userRepository;
    @Resource
    private IUserExtraRepository userExtraRepository;
    @Resource
    private ISubjectInfoRepository subjectInfoRepository;


    @Override
    public void saveOrUpClass(String schoolId, String gradeId, ClassModel classModel) throws ClassServiceException {
        if(StringUtils.isEmpty(schoolId) || StringUtils.isEmpty(gradeId)) throw new ClassServiceException(MyResultCode.PARAM_MISS_MSG);
        SchoolInfo schoolInfo = mongoTemplate.findOne(new Query(Criteria.where("id").is(schoolId).andOperator(Criteria.where("gradeIds").is(gradeId))), SchoolInfo.class);
        if(schoolInfo == null) throw new ClassServiceException(MyResultCode.SCHOOL_GRADE_NOT_MATCH);
        ClassInfo classInfo = MappingModel2EntityConverter.ConvertClass(classModel);
        List<SubjectInfo> subjectInfos = subjectInfoRepository.findByDeleteFlag("0");
        List<SubjectTeacherInfo> teacherInfos = new ArrayList<SubjectTeacherInfo>();
        for (SubjectInfo subjectInfo : subjectInfos){
            SubjectTeacherInfo teacherInfo = new SubjectTeacherInfo();
            teacherInfo.setSubjectId(subjectInfo.getId());
            teacherInfo.setSubjectName(subjectInfo.getSubjectName());
            teacherInfos.add(teacherInfo);
        }
        classInfo.setTeacherInfos(teacherInfos);
        mongoTemplate.save(classInfo);
        GradeInfo gradeInfo = mongoTemplate.findOne(new Query(Criteria.where("id").is(gradeId).andOperator(Criteria.where("classIds").is(classInfo.getId()))), GradeInfo.class);
        if(gradeInfo == null){
            gradeInfo = mongoTemplate.findAndModify(new Query(Criteria.where("id").in(gradeId)), new Update().push("classIds", classInfo.getId()), GradeInfo.class);
            if(gradeInfo == null) throw  new ClassServiceException(MyResultCode.GRADE_CLASS_NOT_SYNC);
        }
    }

    @Override
    public void deleteClass( String gradeId, String classId) throws ClassServiceException{
        if (StringUtils.isEmpty(gradeId) || StringUtils.isEmpty(classId)) {
            throw new ClassServiceException(MyResultCode.PARAM_NOT_EXIST);
        }
        GradeInfo gradeInfo = gradeInfoRepository.findFirstById(gradeId);
        if(gradeInfo == null) throw new ClassServiceException(MyResultCode.GRADE_CLASS_NOT_SYNC);
        ClassInfo classInfo = classInfoRepository.findFirstByIdAndDeleteFlag(classId,"0");
        if(classInfo == null) throw new ClassServiceException(MyResultCode.CLASS_FAIL2REMOVE);
        gradeInfo.getClassIds().remove(classId);
        gradeInfoRepository.save(gradeInfo);
        classInfo.setDeleteFlag("1");
        classInfoRepository.save(classInfo);
    }

    @Override
    public ClassModel getClassInfo(String schoolId, String gradeId, String classId) throws ClassServiceException{
        SchoolInfo schoolInfo = mongoTemplate.findOne(new Query(Criteria.where("id").is(schoolId).andOperator(Criteria.where("gradeIds").is(gradeId))), SchoolInfo.class);
        if(schoolInfo == null) new ClassServiceException(MyResultCode.SCHOOL_GRADE_NOT_MATCH);
        GradeInfo gradeInfo = mongoTemplate.findOne(new Query(Criteria.where("id").is(gradeId).andOperator(Criteria.where("classIds").is(classId))), GradeInfo.class);
        if(gradeInfo == null) new ClassServiceException(MyResultCode.GRADE_CLASS_NOT_MATCH);
        return MappingEntity2ModelConverter.TransferClass(mongoTemplate.findById(classId,ClassInfo.class));
    }

    @Override
    public ClassDetailModel getClassDetail(String classId) throws ClassServiceException {
        if (StringUtils.isEmpty(classId)) throw new ClassServiceException(ResultCode.PARAM_MISS_MSG);
        GradeInfo gradeInfo = gradeInfoRepository.findByClassIdsContaining(classId);
        if (null == gradeInfo) throw new ClassServiceException(ResultCode.SELECT_NULL_MSG);
        SchoolInfo schoolInfo = schoolInfoRepository.findByGradeIdsContaining(gradeInfo.getId());
        if (null == schoolInfo) throw new ClassServiceException(ResultCode.SELECT_NULL_MSG);
        ClassModel classModel = getClassInfo(schoolInfo.getId(),gradeInfo.getId(),classId);
        ClassDetailModel model = new ClassDetailModel();
        model.setClassId(classId);
        model.setClassName(classModel.getClassName());
        model.setGradeId(gradeInfo.getId());
        model.setGradeName(gradeInfo.getName());
        model.setSchoolId(schoolInfo.getId());
        model.setSchoolName(schoolInfo.getName());
        List<TransferStudent> transferStudents = userInfoService.getStudentsByClassId(classId);
        if (null == transferStudents) throw new ClassServiceException(ResultCode.SELECT_NULL_MSG);
        model.setStudentNum(transferStudents.size());
        return model;
    }

    @Override
    public List<ClassModel> getClassesByGradeId(String gradeId) throws SchoolServiceException {
        GradeInfo gradeInfo = gradeInfoRepository.findFirstById(gradeId);
        if (null == gradeInfo){
            throw new SchoolServiceException(ResultCode.SELECT_NULL_MSG);
        }
        List<String> classIds = gradeInfo.getClassIds();
        List<ClassInfo> classInfos = classInfoRepository.findByIdInAndDeleteFlag(classIds,"0");
        List<ClassModel> classModels = new ArrayList<ClassModel>();
        classInfos.forEach(classInfo -> {
            classModels.add(MappingEntity2ModelConverter.TransferClass(classInfo));
        });
        return classModels;
    }

    @Override
    public SchoolModel getSchoolBySchoolId(String schoolId) throws SchoolServiceException {
        if (StringUtils.isEmpty(schoolId)){
            throw new SchoolServiceException(ResultCode.PARAM_MISS_MSG);
        }
        SchoolInfo schoolInfo = schoolInfoRepository.findFirstByDeleteFlagAndId("0",schoolId);
        if (null == schoolInfo){
            throw new SchoolServiceException(ResultCode.SELECT_NULL_MSG);
        }
        return MappingEntity2ModelConverter.CONVERTEFROMSCHOOLINFO(schoolInfo);
    }

    @Override
    public List<SchoolModel> getBackSchools() throws SchoolServiceException {
        List<SchoolInfo> schoolInfos = schoolInfoRepository.findByDeleteFlag("0");
        List<SchoolModel> schoolModels = new ArrayList<SchoolModel>();
        schoolInfos.forEach(schoolInfo -> {
            schoolModels.add(MappingEntity2ModelConverter.CONVERTEFROMSCHOOLINFO(schoolInfo));
        });
        return schoolModels;
    }

    @Override
    public void classRemoveTeacher(UserInfoForToken userInfo, String classId, String teacherId) throws SchoolServiceException {
        if (StringUtils.isEmpty(classId) || StringUtils.isEmpty(teacherId)){
            throw new SchoolServiceException(ResultCode.PARAM_MISS_MSG);
        }
        ClassInfo classInfo = classInfoRepository.findFirstByIdAndDeleteFlag(classId,"0");
        if (null == classInfo){
            throw new SchoolServiceException(ResultCode.CLASS_SELECT_NULL_MSG);
        }
        UserInfo teacher = userRepository.findFirstById(teacherId);
        if(null == teacher){
            throw new SchoolServiceException(ResultCode.USER_SELECT_NULL_MSG);
        }
        if (teacher.getRoleInfos().get(0) instanceof TeacherInfo){
            TeacherInfo teacherInfo = (TeacherInfo) teacher.getRoleInfos().get(0);
            if (classId.equals(teacherInfo.getPrimaryClass().getClassId())){
                teacherInfo.setPrimaryClass("","");
            }
            for (int i = teacherInfo.getSecondaryClasses().size() - 1; i >= 0 ; i--){
                if (classId.equals(teacherInfo.getSecondaryClasses().get(i).getClassId())){
                    teacherInfo.getSecondaryClasses().remove(teacherInfo.getSecondaryClasses().get(i));
                }
            }
            List<RoleInfo> roleInfos =  new ArrayList<RoleInfo>();
            roleInfos.add(teacherInfo);
            teacher.setRoleInfos(roleInfos);
            teacher.setUpdateTime(new Date().getTime());
            userRepository.save(teacher);
            for (SubjectTeacherInfo subjectTeacherInfo: classInfo.getTeacherInfos()){
                if (teacherInfo.getPrimarySubject().getSubjectId().equals(subjectTeacherInfo.getSubjectId()) || teacherInfo.getPrimarySubject().getSubjectName().equals(subjectTeacherInfo.getSubjectName())){
                    subjectTeacherInfo.setTeacherId("");
                    subjectTeacherInfo.setTeacherName("");
                }
            }
            classInfoRepository.save(classInfo);
        }
    }

    @Override
    public void classInsertTeacher(UserInfoForToken userInfo, TeacherInsertModel model,String teacherId) throws SchoolServiceException {
        if ((StringUtils.isEmpty(model.getPrimaryClassId()) && StringUtils.isEmpty(model.getSecondaryClassId())) || StringUtils.isEmpty(teacherId)
                ||StringUtils.isEmpty(model.getSubjectId()) ||StringUtils.isEmpty(model.getSubjectName())){
            throw new SchoolServiceException(ResultCode.PARAM_MISS_MSG);
        }
        String classId = "";
        if (!StringUtils.isEmpty(model.getPrimaryClassId())){
            classId = model.getPrimaryClassId();
            if (userExtraRepository.findByPrimaryClassAndTeacher(classId) > 0){
                throw new SchoolServiceException(ResultCode.HEADMASTER_EXIST_MSG);
            }
        }else {
            classId = model.getSecondaryClassId();
        }
        ClassInfo classInfo = classInfoRepository.findFirstByIdAndDeleteFlag(classId,"0");
        if (null == classInfo){
            throw new SchoolServiceException(ResultCode.CLASS_SELECT_NULL_MSG);
        }
        UserInfo teacher = userRepository.findFirstById(teacherId);
        if (null == teacher){
            throw new SchoolServiceException(ResultCode.USER_SELECT_NULL_MSG);
        }
        if (teacher.getRoleInfos().get(0) instanceof TeacherInfo){
            TeacherInfo teacherInfo = (TeacherInfo) teacher.getRoleInfos().get(0);
            if (null != teacherInfo.getPrimarySubject() && !model.getSubjectId().equals(teacherInfo.getPrimarySubject().getSubjectId())){
                if (null != teacherInfo.getSecondarySubjects()){
                    boolean flag = true;
                    for (TeacherInfo.Subject subject : teacherInfo.getSecondarySubjects()){
                        if (model.getSubjectId().equals(subject.getSubjectId()) || model.getSubjectName().equals(subject.getSubjectName())){
                            flag = false;
                        }
                    }
                    if (flag){ throw new SchoolServiceException(ResultCode.TEACHER_SUBJECT_MSG);}
                }
            }
            for (SubjectTeacherInfo subjectTeacherInfo : classInfo.getTeacherInfos()){
                if (model.getSubjectId().equals(subjectTeacherInfo.getSubjectId()) || model.getSubjectName().equals(subjectTeacherInfo.getSubjectName())){
                    if (!StringUtils.isEmpty(subjectTeacherInfo.getTeacherId())){
                        throw new SchoolServiceException(ResultCode.CLASS_SUBJECT_MES);
                    }
                }
            }
            if (!StringUtils.isEmpty(model.getPrimaryClassId())){
                teacherInfo.setPrimaryClass(classInfo.getId(),classInfo.getName());
                for (int i = teacherInfo.getSecondaryClasses().size() -1 ; i >= 0 ; i--){
                    if (classId.equals(teacherInfo.getSecondaryClasses().get(i).getClassId())){
                        teacherInfo.getSecondaryClasses().remove(teacherInfo.getSecondaryClasses().get(i));
                    }
                }
            }else {
                if (classId.equals(teacherInfo.getPrimaryClass().getClassId())){
                    throw new SchoolServiceException(ResultCode.TEACHER_INNER_CLASS_MES);
                }
                for (TeacherInfo.Class cla : teacherInfo.getSecondaryClasses()){
                    if (cla.getClassId().equals(classId)){
                        throw new SchoolServiceException(ResultCode.TEACHER_INNER_CLASS_MES);
                    }
                }
                teacherInfo.addSecondaryClasses(classInfo.getId(),classInfo.getName());
            }
            List<RoleInfo> roleInfos = new ArrayList<RoleInfo>();
            roleInfos.add(teacherInfo);
            teacher.setRoleInfos(roleInfos);
            teacher.setUpdateTime(new Date().getTime());
            userRepository.save(teacher);
            for (SubjectTeacherInfo subjectTeacherInfo: classInfo.getTeacherInfos()){
                if (model.getSubjectId().equals(subjectTeacherInfo.getSubjectId()) || model.getSubjectName().equals(subjectTeacherInfo.getSubjectName())){
                    subjectTeacherInfo.setTeacherId(teacher.getId());
                    subjectTeacherInfo.setTeacherName(teacher.getName());
                }
            }
            classInfoRepository.save(classInfo);
        }
    }
}
