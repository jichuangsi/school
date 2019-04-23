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
        if (StringUtils.isEmpty(schoolId) || StringUtils.isEmpty(gradeId))
            throw new ClassServiceException(MyResultCode.PARAM_MISS_MSG);
        SchoolInfo schoolInfo = schoolInfoRepository.findByGradeIdsContainingAndId(gradeId, schoolId);
        if (schoolInfo == null) throw new ClassServiceException(MyResultCode.SCHOOL_GRADE_NOT_MATCH);
        ClassInfo classInfo = MappingModel2EntityConverter.ConvertClass(classModel);
        List<SubjectInfo> subjectInfos = subjectInfoRepository.findByDeleteFlagOrderByCreatedTime("0");
        List<SubjectTeacherInfo> teacherInfos = new ArrayList<SubjectTeacherInfo>();
        for (SubjectInfo subjectInfo : subjectInfos) {
            SubjectTeacherInfo teacherInfo = new SubjectTeacherInfo();
            teacherInfo.setSubjectId(subjectInfo.getId());
            teacherInfo.setSubjectName(subjectInfo.getSubjectName());
            teacherInfos.add(teacherInfo);
        }
        classInfo.setTeacherInfos(teacherInfos);
        mongoTemplate.save(classInfo);
        GradeInfo gradeInfo = gradeInfoRepository.findFirstById(gradeId);
        if (gradeInfo == null) {
            throw new ClassServiceException(ResultCode.SELECT_NULL_MSG);
        }
        gradeInfo.getClassIds().add(classInfo.getId());
        gradeInfoRepository.save(gradeInfo);
    }

    @Override
    public void deleteClass(String gradeId, String classId) throws ClassServiceException {
        if (StringUtils.isEmpty(gradeId) || StringUtils.isEmpty(classId)) {
            throw new ClassServiceException(MyResultCode.PARAM_NOT_EXIST);
        }
        GradeInfo gradeInfo = gradeInfoRepository.findFirstById(gradeId);
        if (gradeInfo == null) throw new ClassServiceException(MyResultCode.GRADE_CLASS_NOT_SYNC);
        ClassInfo classInfo = classInfoRepository.findFirstByIdAndDeleteFlag(classId, "0");
        if (classInfo == null) throw new ClassServiceException(MyResultCode.CLASS_FAIL2REMOVE);
        gradeInfo.getClassIds().remove(classId);
        gradeInfoRepository.save(gradeInfo);
        classInfo.setDeleteFlag("1");
        classInfoRepository.save(classInfo);
    }

    @Override
    public ClassModel getClassInfo(String schoolId, String gradeId, String classId) throws ClassServiceException {
        SchoolInfo schoolInfo = mongoTemplate.findOne(new Query(Criteria.where("id").is(schoolId).andOperator(Criteria.where("gradeIds").is(gradeId))), SchoolInfo.class);
        if (schoolInfo == null) new ClassServiceException(MyResultCode.SCHOOL_GRADE_NOT_MATCH);
        GradeInfo gradeInfo = mongoTemplate.findOne(new Query(Criteria.where("id").is(gradeId).andOperator(Criteria.where("classIds").is(classId))), GradeInfo.class);
        if (gradeInfo == null) new ClassServiceException(MyResultCode.GRADE_CLASS_NOT_MATCH);
        return MappingEntity2ModelConverter.TransferClass(mongoTemplate.findById(classId, ClassInfo.class));
    }

    @Override
    public ClassDetailModel getClassDetail(String classId) throws ClassServiceException {
        if (StringUtils.isEmpty(classId)) throw new ClassServiceException(ResultCode.PARAM_MISS_MSG);
        GradeInfo gradeInfo = gradeInfoRepository.findFirstByClassIdsContaining(classId);
        if (null == gradeInfo) throw new ClassServiceException(ResultCode.SELECT_NULL_MSG);
        SchoolInfo schoolInfo = schoolInfoRepository.findByGradeIdsContaining(gradeInfo.getId());
        if (null == schoolInfo) throw new ClassServiceException(ResultCode.SELECT_NULL_MSG);
        ClassModel classModel = getClassInfo(schoolInfo.getId(), gradeInfo.getId(), classId);
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
        if (null == gradeInfo) {
            throw new SchoolServiceException(ResultCode.SELECT_NULL_MSG);
        }
        List<String> classIds = gradeInfo.getClassIds();
        List<ClassInfo> classInfos = classInfoRepository.findByIdInAndDeleteFlagOrderByCreateTime(classIds, "0");
        List<ClassModel> classModels = new ArrayList<ClassModel>();
        classInfos.forEach(classInfo -> {
            classModels.add(MappingEntity2ModelConverter.TransferClass(classInfo));
        });
        return classModels;
    }

    @Override
    public SchoolModel getSchoolBySchoolId(String schoolId) throws SchoolServiceException {
        if (StringUtils.isEmpty(schoolId)) {
            throw new SchoolServiceException(ResultCode.PARAM_MISS_MSG);
        }
        SchoolInfo schoolInfo = schoolInfoRepository.findFirstByDeleteFlagAndId("0", schoolId);
        if (null == schoolInfo) {
            throw new SchoolServiceException(ResultCode.SELECT_NULL_MSG);
        }
        return MappingEntity2ModelConverter.CONVERTEFROMSCHOOLINFO(schoolInfo);
    }

    @Override
    public List<SchoolModel> getBackSchools() throws SchoolServiceException {
        List<SchoolInfo> schoolInfos = schoolInfoRepository.findByDeleteFlagOrderByCreateTime("0");
        List<SchoolModel> schoolModels = new ArrayList<SchoolModel>();
        schoolInfos.forEach(schoolInfo -> {
            schoolModels.add(MappingEntity2ModelConverter.CONVERTEFROMSCHOOLINFO(schoolInfo));
        });
        return schoolModels;
    }

    @Override
    public void classRemoveTeacher(UserInfoForToken userInfo, String classId, String teacherId, boolean removeClass) throws SchoolServiceException {
        if (StringUtils.isEmpty(classId) || StringUtils.isEmpty(teacherId)) {
            throw new SchoolServiceException(ResultCode.PARAM_MISS_MSG);
        }
        ClassInfo classInfo = classInfoRepository.findFirstByIdAndDeleteFlag(classId, "0");
        if (null == classInfo) {
            throw new SchoolServiceException(ResultCode.CLASS_SELECT_NULL_MSG);
        }
        UserInfo teacher = userRepository.findFirstById(teacherId);
        if (null == teacher) {
            throw new SchoolServiceException(ResultCode.USER_SELECT_NULL_MSG);
        }
        if (teacher.getRoleInfos().get(0) instanceof TeacherInfo) {
            TeacherInfo teacherInfo = (TeacherInfo) teacher.getRoleInfos().get(0);
            if (null != teacherInfo.getPrimaryClass() && classId.equals(teacherInfo.getPrimaryClass().getClassId())) {
                teacherInfo.resetPrimaryClass();
            }
            for (int i = teacherInfo.getSecondaryClasses().size() - 1; i >= 0; i--) {
                if (classId.equals(teacherInfo.getSecondaryClasses().get(i).getClassId())) {
                    teacherInfo.getSecondaryClasses().remove(teacherInfo.getSecondaryClasses().get(i));
                }
            }
            if(teacherInfo.getPrimaryClass()==null&&teacherInfo.getSecondaryClasses().size()==0) teacherInfo.resetPhrase();
            List<RoleInfo> roleInfos = new ArrayList<RoleInfo>();
            roleInfos.add(teacherInfo);
            teacher.setRoleInfos(roleInfos);
            teacher.setUpdateTime(new Date().getTime());
            userRepository.save(teacher);
            /*for (SubjectTeacherInfo subjectTeacherInfo : classInfo.getTeacherInfos()) {
                if (teacher.getId().equals(subjectTeacherInfo.getTeacherId())) {
                    subjectTeacherInfo.setTeacherId("");
                    subjectTeacherInfo.setTeacherName("");
                }
            }
            if (teacher.getId().equals(classInfo.getHeadMasterId())) {
                classInfo.setHeadMasterId("");
                classInfo.setHeadMasterName("");
            }
            classInfoRepository.save(classInfo);*/
            Query query = new Query();
            query.addCriteria(Criteria.where("_id").is(classId).and("teacherInfos.teacherId").is(teacherId));
            List<ClassInfo> temp = mongoTemplate.find(query,ClassInfo.class);
            Update update = new Update();
            update.set("updateTime", new Date().getTime());
            if(removeClass) update.set("deleteFlag", "2");
            update.set("teacherInfos.$.teacherId", null);
            update.set("teacherInfos.$.teacherName", null);
            mongoTemplate.updateFirst(query, update, ClassInfo.class);
        }
    }

    @Override
    public void classInsertTeacher(UserInfoForToken userInfo, TeacherInsertModel model, String teacherId) throws SchoolServiceException {
        if ((StringUtils.isEmpty(model.getPrimaryClassId()) && StringUtils.isEmpty(model.getSecondaryClassId())) || StringUtils.isEmpty(teacherId)
                || StringUtils.isEmpty(model.getSubjectId()) || StringUtils.isEmpty(model.getSubjectName()) || StringUtils.isEmpty(model.getPhraseId())) {
            throw new SchoolServiceException(ResultCode.PARAM_MISS_MSG);
        }
        String classId = "";
        if (!StringUtils.isEmpty(model.getPrimaryClassId())) {
            classId = model.getPrimaryClassId();
            if (userExtraRepository.findByPrimaryClassAndTeacher(classId) > 0) {
                throw new SchoolServiceException(ResultCode.HEADMASTER_EXIST_MSG);
            }
        } else {
            classId = model.getSecondaryClassId();
        }
        ClassInfo classInfo = classInfoRepository.findFirstByIdAndDeleteFlag(classId, "0");
        if (null == classInfo) {
            throw new SchoolServiceException(ResultCode.CLASS_SELECT_NULL_MSG);
        }
        UserInfo teacher = userRepository.findFirstById(teacherId);
        if (null == teacher) {
            throw new SchoolServiceException(ResultCode.USER_SELECT_NULL_MSG);
        }
        if (teacher.getRoleInfos().get(0) instanceof TeacherInfo) {
            TeacherInfo teacherInfo = (TeacherInfo) teacher.getRoleInfos().get(0);
            if (null != teacherInfo.getPrimaryClass()) {
                if (classId.equals(teacherInfo.getPrimaryClass().getClassId())) {
                    throw new SchoolServiceException(ResultCode.TEACHER_INNER_CLASS_MES);
                }
            }
            if (null != teacherInfo.getSecondaryClasses()) {
                for (TeacherInfo.Class cla : teacherInfo.getSecondaryClasses()) {
                    if (classId.equals(cla.getClassId())) {
                        throw new SchoolServiceException(ResultCode.TEACHER_INNER_CLASS_MES);
                    }
                }
            }
            boolean flag = true;
            if (null != teacherInfo.getSecondarySubjects()) {
                for (TeacherInfo.Subject subject : teacherInfo.getSecondarySubjects()) {
                    if (model.getSubjectId().equals(subject.getSubjectId()) || model.getSubjectName().equals(subject.getSubjectName())) {
                        flag = false;
                    }
                }
            }
            if (null != teacherInfo.getPrimarySubject()) {
                if (model.getSubjectId().equals(teacherInfo.getPrimarySubject().getSubjectId()) || model.getSubjectName().equals(teacherInfo.getPrimarySubject().getSubjectName())) {
                    flag = false;
                }
            }
            if (flag) {
                throw new SchoolServiceException(ResultCode.TEACHER_SUBJECT_MSG);
            }
            for (SubjectTeacherInfo subjectTeacherInfo : classInfo.getTeacherInfos()) {
                if (model.getSubjectId().equals(subjectTeacherInfo.getSubjectId()) || model.getSubjectName().equals(subjectTeacherInfo.getSubjectName())) {
                    if (!StringUtils.isEmpty(subjectTeacherInfo.getTeacherId())) {
                        throw new SchoolServiceException(ResultCode.CLASS_SUBJECT_MES);
                    }
                }
            }
            if (!StringUtils.isEmpty(model.getPrimaryClassId())) {
                teacherInfo.setPrimaryClass(classInfo.getId(), classInfo.getName());
                for (int i = teacherInfo.getSecondaryClasses().size() - 1; i >= 0; i--) {
                    if (classId.equals(teacherInfo.getSecondaryClasses().get(i).getClassId())) {
                        teacherInfo.getSecondaryClasses().remove(teacherInfo.getSecondaryClasses().get(i));
                    }
                }
            } else {
                if (null != teacherInfo.getPrimaryClass() && classId.equals(teacherInfo.getPrimaryClass().getClassId())) {
                    throw new SchoolServiceException(ResultCode.TEACHER_INNER_CLASS_MES);
                }
                if (null != teacherInfo.getSecondaryClasses()) {
                    for (TeacherInfo.Class cla : teacherInfo.getSecondaryClasses()) {
                        if (cla.getClassId().equals(classId)) {
                            throw new SchoolServiceException(ResultCode.TEACHER_INNER_CLASS_MES);
                        }
                    }
                }
                teacherInfo.addSecondaryClasses(classInfo.getId(), classInfo.getName());
            }
            if (teacherInfo.getPhrase()!=null
                    &&!StringUtils.isEmpty(teacherInfo.getPhrase().getPhraseId())
                    &&!teacherInfo.getPhrase().getPhraseId().equalsIgnoreCase(model.getPhraseId())){
                throw new SchoolServiceException(ResultCode.PHRASE_TEACH_EXIST);
            }else if(teacherInfo.getPhrase()==null||(teacherInfo.getPhrase()!=null&&StringUtils.isEmpty(teacherInfo.getPhrase().getPhraseId()))){
                teacherInfo.setPhrase(model.getPhraseId(),model.getPhraseName(),model.getPhraseObjectId());
            }
            List<RoleInfo> roleInfos = new ArrayList<RoleInfo>();
            roleInfos.add(teacherInfo);
            teacher.setRoleInfos(roleInfos);
            teacher.setUpdateTime(new Date().getTime());
            userRepository.save(teacher);
            for (SubjectTeacherInfo subjectTeacherInfo : classInfo.getTeacherInfos()) {
                if (model.getSubjectId().equals(subjectTeacherInfo.getSubjectId()) || model.getSubjectName().equals(subjectTeacherInfo.getSubjectName())) {
                    subjectTeacherInfo.setTeacherId(teacher.getId());
                    subjectTeacherInfo.setTeacherName(teacher.getName());
                }
            }
            if (!StringUtils.isEmpty(model.getPrimaryClassId())) {
                classInfo.setHeadMasterName(teacher.getName());
                classInfo.setHeadMasterId(teacher.getId());
            }
            classInfoRepository.save(classInfo);
        }
    }

    @Override
    public void updateClassInsertSubject(UserInfoForToken userInfo, String subjectId,String classId) throws SchoolServiceException {
        if (StringUtils.isEmpty(subjectId) || StringUtils.isEmpty(classId)){
            throw new SchoolServiceException(ResultCode.PARAM_MISS_MSG);
        }
        ClassInfo classInfo = classInfoRepository.findFirstByIdAndDeleteFlag(classId,"0");
        if (null == classInfo){
            throw new SchoolServiceException(ResultCode.CLASS_SELECT_NULL_MSG);
        }
        SubjectInfo subjectInfo = subjectInfoRepository.findFirstByIdAndDeleteFlag(subjectId,"0");
        if (null == subjectInfo){
            throw new SchoolServiceException(ResultCode.SUBJECT_ISNOT_EXIST);
        }
        for (SubjectTeacherInfo teacherInfo : classInfo.getTeacherInfos()){
            if (subjectId.equals(teacherInfo.getSubjectId())){
                throw new SchoolServiceException(ResultCode.SUBJECT_ISEXIST_MSG);
            }
        }
        SubjectTeacherInfo subjectTeacherInfo = new SubjectTeacherInfo();
        subjectTeacherInfo.setSubjectName(subjectInfo.getSubjectName());
        subjectTeacherInfo.setSubjectId(subjectId);
        classInfo.getTeacherInfos().add(subjectTeacherInfo);
        classInfo.setUpdateTime(new Date().getTime());
        classInfo.setUpdateId(userInfo.getUserId());
        classInfo.setUpdateName(userInfo.getUserName());
        classInfoRepository.save(classInfo);
    }

    @Override
    public void updateClassDelSubject(UserInfoForToken userInfo, String subjectId, String classId) throws SchoolServiceException {
        if (StringUtils.isEmpty(subjectId) || StringUtils.isEmpty(classId)){
            throw new SchoolServiceException(ResultCode.PARAM_MISS_MSG);
        }
        ClassInfo classInfo = classInfoRepository.findFirstByIdAndDeleteFlag(classId,"0");
        if (null == classInfo){
            throw new SchoolServiceException(ResultCode.CLASS_SELECT_NULL_MSG);
        }
        SubjectInfo subjectInfo = subjectInfoRepository.findFirstByIdAndDeleteFlag(subjectId,"0");
        if (null == subjectInfo){
            throw new SchoolServiceException(ResultCode.SUBJECT_ISNOT_EXIST);
        }
        boolean flag = true;
        for (SubjectTeacherInfo teacherInfo : classInfo.getTeacherInfos()){
            if (subjectId.equals(teacherInfo.getSubjectId())){
                if (!StringUtils.isEmpty(teacherInfo.getTeacherId())){
                    throw new SchoolServiceException(ResultCode.SUBJECT_TEACHER_EXIST);
                }
                classInfo.getTeacherInfos().remove(teacherInfo);
                flag = false;
                break;
            }
        }
        if (flag){
            throw new SchoolServiceException(ResultCode.SUBJECT_ISNOT_EXIST);
        }
        classInfo.setUpdateName(userInfo.getUserName());
        classInfo.setUpdateId(userInfo.getUserId());
        classInfo.setUpdateTime(new Date().getTime());
        classInfoRepository.save(classInfo);
    }
}
