package com.jichuangsi.school.user.service.impl;

import com.github.pagehelper.PageInfo;
import com.jichuangsi.microservice.common.model.UserInfoForToken;
import com.jichuangsi.school.user.constant.ResultCode;
import com.jichuangsi.school.user.constant.Status;
import com.jichuangsi.school.user.entity.UserInfo;
import com.jichuangsi.school.user.entity.org.*;
import com.jichuangsi.school.user.exception.SchoolServiceException;
import com.jichuangsi.school.user.exception.UserServiceException;
import com.jichuangsi.school.user.model.org.ClassModel;
import com.jichuangsi.school.user.model.school.GradeModel;
import com.jichuangsi.school.user.model.school.PhraseModel;
import com.jichuangsi.school.user.model.school.SchoolModel;
import com.jichuangsi.school.user.model.school.SubjectModel;
import com.jichuangsi.school.user.model.transfer.TransferTeacher;
import com.jichuangsi.school.user.repository.*;
import com.jichuangsi.school.user.service.ISchoolClassService;
import com.jichuangsi.school.user.service.ISchoolService;
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
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

@Service
public class SchoolServiceImpl implements ISchoolService {
    @Resource
    private ISchoolInfoRepository schoolInfoRepository;
    @Resource
    private IGradeInfoRepository gradeInfoRepository;
    @Resource
    private ISubjectInfoRepository subjectInfoRepository;
    @Resource
    private IPhraseInfoRepository phraseInfoRepository;
    @Resource
    private IClassInfoRepository classInfoRepository;
    @Resource
    private UserInfoService userInfoService;
    @Resource
    private ISchoolClassService schoolClassService;
    @Resource
    private IGradeInfoExtraRepository gradeInfoExtraRepository;
    @Resource
    private IClassInfoExtraRepository classInfoExtraRepository;
    @Resource
    private IUserExtraRepository userExtraRepository;
    @Resource
    private UserRepository userRepository;
    @Resource
    private MongoTemplate mongoTemplate;

    private SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");

    @Override
    public void insertSchool(UserInfoForToken userInfo, SchoolModel model) throws SchoolServiceException {
        if (StringUtils.isEmpty(model.getSchoolName()) || StringUtils.isEmpty(model.getAddress())) {
            throw new SchoolServiceException(ResultCode.PARAM_MISS_MSG);
        }
        if (schoolInfoRepository.countByDeleteFlagAndName("0", model.getSchoolName()) > 0) {
            throw new SchoolServiceException(ResultCode.SCHOOL_IS_EXIST);
        }
        SchoolInfo info = MappingModel2EntityConverter.CONVERTERFROMSCHOOLMODEL(model);
        info.setCreatorId(userInfo.getUserId());
        info.setCreatorName(userInfo.getUserName());
        info.setUpdateId(userInfo.getUserId());
        info.setUpdateName(userInfo.getUserName());
        schoolInfoRepository.save(info);
    }

    @Override
    public void updateSchool(UserInfoForToken userInfo, SchoolModel model) throws SchoolServiceException {
        if (StringUtils.isEmpty(model.getSchoolName()) || StringUtils.isEmpty(model.getAddress()) || StringUtils.isEmpty(model.getSchoolId())) {
            throw new SchoolServiceException(ResultCode.PARAM_MISS_MSG);
        }
        SchoolInfo info = schoolInfoRepository.findFirstById(model.getSchoolId());
        if (null == info) {
            throw new SchoolServiceException(ResultCode.SELECT_NULL_MSG);
        }
        info.setAddress(model.getAddress());
        info.setName(model.getSchoolName());
        info.setUpdateId(userInfo.getUserId());
        info.setUpdateName(userInfo.getUserName());
        info.setUpdateTime(new Date().getTime());
        schoolInfoRepository.save(info);
    }

    @Override
    public void insertGrade(UserInfoForToken userInfo, GradeModel model) throws SchoolServiceException {
        if (StringUtils.isEmpty(model.getSchoolId()) || StringUtils.isEmpty(model.getGradeName()) ||
                StringUtils.isEmpty(model.getPhraseId())) {
            throw new SchoolServiceException(ResultCode.PARAM_MISS_MSG);
        }
        SchoolInfo sinfo = schoolInfoRepository.findFirstById(model.getSchoolId());
        PhraseInfo phraseInfo = phraseInfoRepository.findFirstById(model.getPhraseId());
        if (null == sinfo || null == phraseInfo) {
            throw new SchoolServiceException(ResultCode.SELECT_NULL_MSG);
        }
        if (gradeInfoRepository.countByDeleteFlagAndNameAndIdIn("0", model.getGradeName(), phraseInfo.getGradeIds()) > 0) {
            throw new SchoolServiceException(ResultCode.GRADE_IS_EXIST);
        }
        GradeInfo info = MappingModel2EntityConverter.CONVERTERFROMGRADEMODEL(model);
        info.setCreatorId(userInfo.getUserId());
        info.setCreatorName(userInfo.getUserName());
        info.setUpdateId(userInfo.getUserId());
        info.setUpdateName(userInfo.getUserName());
        info = gradeInfoRepository.save(info);
        sinfo.getGradeIds().add(info.getId());
        sinfo.setUpdateName(userInfo.getUserName());
        sinfo.setUpdateId(userInfo.getUserId());
        sinfo.setUpdateTime(new Date().getTime());
        schoolInfoRepository.save(sinfo);
        phraseInfo.getGradeIds().add(info.getId());
        phraseInfo.setUpdateName(userInfo.getUserName());
        phraseInfo.setUpdateId(userInfo.getUserId());
        phraseInfo.setUpdatedTime(new Date().getTime());
        phraseInfoRepository.save(phraseInfo);
    }

    @Override
    public void updateGrade(UserInfoForToken userInfo, GradeModel model) throws SchoolServiceException {
        if (StringUtils.isEmpty(model.getGradeName()) || StringUtils.isEmpty(model.getGradeId())) {
            throw new SchoolServiceException(ResultCode.PARAM_MISS_MSG);
        }
        GradeInfo info = gradeInfoRepository.findFirstById(model.getGradeId());
        if (null == info) {
            throw new SchoolServiceException(ResultCode.SELECT_NULL_MSG);
        }
        info.setName(model.getGradeName());
        info.setUpdateId(userInfo.getUserId());
        info.setUpdateName(userInfo.getUserName());
        info.setUpdateTime(new Date().getTime());
        gradeInfoRepository.save(info);
    }

    @Override
    public List<SchoolModel> getSchools() throws SchoolServiceException {
        List<SchoolInfo> schools = schoolInfoRepository.findByDeleteFlagOrderByCreateTime("0");
        if (!(schools.size() > 0)) {
            throw new SchoolServiceException(ResultCode.SELECT_NULL_MSG);
        }
        List<SchoolModel> models = new ArrayList<SchoolModel>();
        schools.forEach(schoolInfo -> {
            SchoolModel model = MappingEntity2ModelConverter.CONVERTEFROMSCHOOLINFO(schoolInfo);
            models.add(model);
        });
        return models;
    }

    @Override
    public List<GradeModel> getGrades(String schoolId) throws SchoolServiceException {
        SchoolInfo schoolInfo = schoolInfoRepository.findFirstById(schoolId);
        if (null == schoolInfo) {
            throw new SchoolServiceException(ResultCode.SELECT_NULL_MSG);
        }
        List<String> gradeIds = schoolInfo.getGradeIds();
        List<GradeModel> gradeModels = new ArrayList<GradeModel>();
        if (null == gradeIds || !(gradeIds.size() > 0)) {
            return gradeModels;
        }
        List<GradeInfo> gradeInfos = gradeInfoRepository.findByDeleteFlagAndIdInOrderByCreateTime("0", gradeIds);
        gradeInfos.forEach(gradeInfo -> {
            GradeModel model = MappingEntity2ModelConverter.CONVERTERFROMGRADEINFO(gradeInfo);
            gradeModels.add(model);
        });
        return gradeModels;
    }

    @Override
    public void deleteSchool(UserInfoForToken userInfo, String schoolId) throws SchoolServiceException {
        SchoolInfo info = schoolInfoRepository.findFirstById(schoolId);
        if (null == info) {
            throw new SchoolServiceException(ResultCode.SELECT_NULL_MSG);
        }
        info.setDeleteFlag("1");
        info.setUpdateId(userInfo.getUserId());
        info.setUpdateName(userInfo.getUserName());
        info.setUpdateTime(new Date().getTime());
        List<String> gradeIds = new ArrayList<String>();
        List<PhraseInfo> phraseInfos = phraseInfoRepository.findByDeleteFlagAndIdIn("0", info.getPhraseIds());
        for (PhraseInfo phraseInfo : phraseInfos) {
            phraseInfo.setDeleteFlag("1");
            phraseInfo.setUpdateId(userInfo.getUserId());
            phraseInfo.setUpdateName(userInfo.getUserName());
            phraseInfo.setUpdatedTime(new Date().getTime());
            gradeIds.addAll(phraseInfo.getGradeIds());
        }
        phraseInfoRepository.saveAll(phraseInfos);
        List<String> classIds = new ArrayList<String>();
        List<GradeInfo> gradeInfos = gradeInfoRepository.findByDeleteFlagAndIdInOrderByCreateTime("0", gradeIds);
        for (GradeInfo gradeInfo : gradeInfos) {
            gradeInfo.setDeleteFlag("1");
            gradeInfo.setUpdateId(userInfo.getUserId());
            gradeInfo.setUpdateName(userInfo.getUserName());
            gradeInfo.setUpdateTime(new Date().getTime());
            classIds.addAll(gradeInfo.getClassIds());
        }
        gradeInfoRepository.saveAll(gradeInfos);
        List<ClassInfo> classInfos = classInfoRepository.findByIdInAndDeleteFlagOrderByCreateTime(classIds, "0");
        //todo 删除班级，解除老师和班级的关联
        untyingClass(userInfo, classInfos);
        schoolInfoRepository.save(info);
    }

    @Override
    public void deleteGrade(UserInfoForToken userInfo, String gradeId) throws SchoolServiceException {
        GradeInfo info = gradeInfoRepository.findFirstById(gradeId);
        if (null == info) {
            throw new SchoolServiceException(ResultCode.SELECT_NULL_MSG);
        }
        info.setDeleteFlag("1");
        info.setUpdateId(userInfo.getUserId());
        info.setUpdateName(userInfo.getUserName());
        /*SchoolInfo schoolInfo = schoolInfoRepository.findByGradeIdsContaining(gradeId);
        if (null != schoolInfo) {
            schoolInfo.getGradeIds().remove(gradeId);
            schoolInfo.setUpdateName(userInfo.getUserName());
            schoolInfo.setUpdateId(userInfo.getUserId());
            schoolInfo.setUpdateTime(new Date().getTime());
            schoolInfoRepository.save(schoolInfo);
        }
        PhraseInfo phraseInfo = phraseInfoRepository.findFirstByGradeIdsContaining(gradeId);
        if (null != phraseInfo) {
            phraseInfo.getGradeIds().remove(gradeId);
            phraseInfo.setUpdateName(userInfo.getUserName());
            phraseInfo.setUpdateId(userInfo.getUserId());
            phraseInfo.setUpdatedTime(new Date().getTime());
            phraseInfoRepository.save(phraseInfo);
        }*/
        gradeInfoRepository.save(info);
        List<ClassInfo> classInfos = classInfoRepository.findByIdInAndDeleteFlagOrderByCreateTime(info.getClassIds(), "0");
        untyingClass(userInfo, classInfos);
    }

    @Override
    public void insertSubject(UserInfoForToken userInfo, SubjectModel model) throws SchoolServiceException {
        if (StringUtils.isEmpty(model.getSubjectName())) {
            throw new SchoolServiceException(ResultCode.PARAM_MISS_MSG);
        }
        //检查是否存在重复的科目
        if (subjectInfoRepository.countByDeleteFlagAndSubjectName("0", model.getSubjectName()) > 0) {
            throw new SchoolServiceException(ResultCode.SUBJECT_IS_EXIST);
        }
        SubjectInfo info = new SubjectInfo();
        info.setCreatorId(userInfo.getUserId());
        info.setCreatorName(userInfo.getUserName());
        info.setUpdateId(userInfo.getUserId());
        info.setUpdateName(userInfo.getUserName());
        info.setSubjectName(model.getSubjectName());
        subjectInfoRepository.save(info);
    }

    @Override
    public List<SubjectModel> getSubjects(UserInfoForToken userInfo) {
        List<SubjectInfo> subjectInfos = subjectInfoRepository.findByDeleteFlagOrderByCreatedTime("0");
        return converterfromsubjectInfos(subjectInfos);
    }

    private List<SubjectModel> converterfromsubjectInfos(List<SubjectInfo> infos) {
        List<SubjectModel> subjectModels = new ArrayList<SubjectModel>();
        infos.forEach(info -> {
            subjectModels.add(MappingEntity2ModelConverter.CONVERTERFROMSUBJECTINFO(info));
        });
        return subjectModels;
    }

    @Override
    public void insertPhrase(UserInfoForToken userInfo, PhraseModel model) throws SchoolServiceException {
        if (StringUtils.isEmpty(model.getPhraseName()) || StringUtils.isEmpty(model.getSchoolId())) {
            throw new SchoolServiceException(ResultCode.PARAM_MISS_MSG);
        }
        SchoolInfo schoolInfo = schoolInfoRepository.findFirstById(model.getSchoolId());
        if (null == schoolInfo) {
            throw new SchoolServiceException(ResultCode.SELECT_NULL_MSG);
        }
        if (phraseInfoRepository.countByPhraseNameAndDeleteFlagAndIdIn(model.getPhraseName(), "0", schoolInfo.getPhraseIds()) > 0) {
            throw new SchoolServiceException(ResultCode.PHRASE_IS_EXIST);
        }
        String phraseName = model.getPhraseName().substring(0,1);
        switch (phraseName){
            case "小":
                phraseName = "1";
                break;
            case "初":
                phraseName = "2";
                break;
            case "高":
                phraseName = "3";
                break;
            default:
                throw new SchoolServiceException(ResultCode.PHARSE_NAME_ERR);
        }
        PhraseInfo info = new PhraseInfo();
        info.setCreatorId(userInfo.getUserId());
        info.setCreatorName(userInfo.getUserName());
        info.setPhraseName(model.getPhraseName());
        info.setUpdateId(userInfo.getUserId());
        info.setUpdateName(userInfo.getUserName());
        info.setPhraseId(phraseName);
        info = phraseInfoRepository.save(info);
        schoolInfo.getPhraseIds().add(info.getId());
        schoolInfo.setUpdateTime(new Date().getTime());
        schoolInfo.setUpdateId(userInfo.getUserId());
        schoolInfo.setUpdateName(userInfo.getUserName());
        schoolInfoRepository.save(schoolInfo);
    }

    @Override
    public List<PhraseModel> getPhraseBySchool(String schoolId) throws SchoolServiceException {
        if (StringUtils.isEmpty(schoolId)) {
            throw new SchoolServiceException(ResultCode.PARAM_MISS_MSG);
        }
        SchoolInfo schoolInfo = schoolInfoRepository.findFirstById(schoolId);
        if (null == schoolInfo) {
            throw new SchoolServiceException(ResultCode.SELECT_NULL_MSG);
        }
        List<String> phraseIds = schoolInfo.getPhraseIds();
        List<PhraseInfo> phraseInfos = phraseInfoRepository.findByDeleteFlagAndIdIn("0", phraseIds);
        return converterListPhraseInfo(phraseInfos);
    }

    private List<PhraseModel> converterListPhraseInfo(List<PhraseInfo> infos) {
        List<PhraseModel> phraseModels = new ArrayList<PhraseModel>();
        infos.forEach(info -> {
            phraseModels.add(MappingEntity2ModelConverter.CONVERTERFROMPHRASEINFO(info));
        });
        return phraseModels;
    }

    @Override
    public List<GradeModel> getGradeByPhrase(String phrase) throws SchoolServiceException {
        if (StringUtils.isEmpty(phrase)) {
            throw new SchoolServiceException(ResultCode.PARAM_MISS_MSG);
        }
        PhraseInfo phraseInfo = phraseInfoRepository.findFirstById(phrase);
        if (null == phraseInfo) {
            throw new SchoolServiceException(ResultCode.SELECT_NULL_MSG);
        }
        List<String> gradeIds = phraseInfo.getGradeIds();
        List<GradeInfo> gradeInfos = gradeInfoRepository.findByDeleteFlagAndIdInOrderByCreateTime("0", gradeIds);
        return converterListGradeInfo(gradeInfos);
    }

    private List<GradeModel> converterListGradeInfo(List<GradeInfo> infos) {
        List<GradeModel> gradeModels = new ArrayList<GradeModel>();
        infos.forEach(info -> {
            gradeModels.add(MappingEntity2ModelConverter.CONVERTERFROMGRADEINFO(info));
        });
        return gradeModels;
    }

    @Override
    public void deleteSubject(UserInfoForToken userInfo, String subjectId) throws SchoolServiceException {
        if (StringUtils.isEmpty(subjectId)) {
            throw new SchoolServiceException(ResultCode.PARAM_MISS_MSG);
        }
        SubjectInfo info = subjectInfoRepository.findFirstById(subjectId);
        if (null == info) {
            throw new SchoolServiceException(ResultCode.SELECT_NULL_MSG);
        }
        info.setDeleteFlag("1");
        info.setUpdateName(userInfo.getUserName());
        info.setUpdateId(userInfo.getUserId());
        info.setUpdateTime(new Date().getTime());
        subjectInfoRepository.save(info);
    }

    @Override
    public void deletePhrase(UserInfoForToken userInfo, String phrase) throws SchoolServiceException {
        if (StringUtils.isEmpty(phrase)) {
            throw new SchoolServiceException(ResultCode.PARAM_MISS_MSG);
        }
        PhraseInfo info = phraseInfoRepository.findFirstById(phrase);
        if (null == info) {
            throw new SchoolServiceException(ResultCode.SELECT_NULL_MSG);
        }
        info.setDeleteFlag("1");
        info.setUpdateName(userInfo.getUserName());
        info.setUpdateId(userInfo.getUserId());
        info.setUpdatedTime(new Date().getTime());
        /*SchoolInfo schoolInfo = schoolInfoRepository.findFirstByPhraseIdsContaining(phrase);
        if (null != schoolInfo){
            schoolInfo.getPhraseIds().remove(phrase);
            schoolInfo.setUpdateTime(new Date().getTime());
            schoolInfo.setUpdateId(userInfo.getUserId());
            schoolInfo.setUpdateName(userInfo.getUserName());
            schoolInfoRepository.save(schoolInfo);
        }*/
        phraseInfoRepository.save(info);
        List<String> classIds = new ArrayList<String>();
        List<GradeInfo> gradeInfos = gradeInfoRepository.findByDeleteFlagAndIdInOrderByCreateTime("0", info.getGradeIds());
        for (GradeInfo gradeInfo : gradeInfos) {
            gradeInfo.setDeleteFlag("1");
            gradeInfo.setUpdateId(userInfo.getUserId());
            gradeInfo.setUpdateTime(new Date().getTime());
            gradeInfo.setUpdateName(userInfo.getUserName());
            classIds.addAll(gradeInfo.getClassIds());
        }
        gradeInfoRepository.saveAll(gradeInfos);
        List<ClassInfo> classInfos = classInfoRepository.findByIdInAndDeleteFlagOrderByCreateTime(classIds, "0");
        untyingClass(userInfo, classInfos);
    }

    @Override
    public void updateSubject(UserInfoForToken userInfo, SubjectModel model) throws SchoolServiceException {
        if (StringUtils.isEmpty(model.getSubjectName()) || StringUtils.isEmpty(model.getId())) {
            throw new SchoolServiceException(ResultCode.PARAM_MISS_MSG);
        }
        SubjectInfo info = subjectInfoRepository.findFirstById(model.getId());
        if (null == info) {
            throw new SchoolServiceException(ResultCode.SELECT_NULL_MSG);
        }
        info.setSubjectName(model.getSubjectName());
        info.setUpdateId(userInfo.getUserId());
        info.setUpdateName(userInfo.getUserName());
        info.setUpdateTime(new Date().getTime());
        subjectInfoRepository.save(info);
    }

    @Override
    public void updatePhrase(UserInfoForToken userInfo, PhraseModel model) throws SchoolServiceException {
        if (StringUtils.isEmpty(model.getPhraseName()) || StringUtils.isEmpty(model.getId())) {
            throw new SchoolServiceException(ResultCode.PARAM_MISS_MSG);
        }
        PhraseInfo info = phraseInfoRepository.findFirstById(model.getId());
        if (null == info) {
            throw new SchoolServiceException(ResultCode.SELECT_NULL_MSG);
        }
        info.setPhraseName(model.getPhraseName());
        info.setUpdateId(userInfo.getUserId());
        info.setUpdateName(userInfo.getUserName());
        info.setUpdatedTime(new Date().getTime());
        phraseInfoRepository.save(info);
    }

    @Override
    public SchoolModel getSchoolById(UserInfoForToken userInfo, String schoolId) throws SchoolServiceException {
        if (StringUtils.isEmpty(schoolId)) {
            throw new SchoolServiceException(ResultCode.PARAM_MISS_MSG);
        }
        SchoolInfo schoolInfo = schoolInfoRepository.findFirstByDeleteFlagAndId("0", schoolId);
        if (null == schoolInfo) {
            throw new SchoolServiceException(ResultCode.SELECT_NULL_MSG);
        }
        return MappingEntity2ModelConverter.CONVERTEFROMSCHOOLINFO(schoolInfo);
    }

    private void untyingClass(UserInfoForToken userInfo, List<ClassInfo> classInfos) throws SchoolServiceException {
        for (ClassInfo classInfo : classInfos) {
            //classInfo.setDeleteFlag("1");
            try {
                List<TransferTeacher> transferTeachers = userInfoService.getTeachersByClassId(classInfo.getId());
                for (TransferTeacher transferTeacher : transferTeachers) {
                    if (!StringUtils.isEmpty(transferTeacher.getTeacherId())) {
                        schoolClassService.classRemoveTeacher(userInfo, classInfo.getId(), transferTeacher.getTeacherId(), true);
                    }
                }
            } catch (UserServiceException e) {
                throw new SchoolServiceException(e.getMessage());
            }
        }
        //classInfoRepository.saveAll(classInfos);
    }

    @Override
    public PageInfo<GradeModel> findPastGrades(UserInfoForToken userInfo, String pharseId, String graduation, int pageIndex, int pageSize) throws SchoolServiceException {
        if (StringUtils.isEmpty(pharseId) || StringUtils.isEmpty(graduation)) {
            throw new SchoolServiceException(ResultCode.PARAM_MISS_MSG);
        }
        PhraseInfo phraseInfo = phraseInfoRepository.findFirstById(pharseId);
        if (null == phraseInfo) {
            throw new SchoolServiceException(ResultCode.PHRASE_SELECT_NULL_MSG);
        }
        long graduationTime = 0;
        try {
            graduationTime = sdf.parse(graduation).getTime();
        } catch (ParseException e) {
            throw new SchoolServiceException(e.getMessage());
        }
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date(graduationTime));
        calendar.set(Calendar.MONTH, 11);
        calendar.set(Calendar.DAY_OF_MONTH, 31);
        calendar.set(Calendar.HOUR_OF_DAY, 24);
        List<GradeInfo> gradeInfos = gradeInfoExtraRepository.findByDeleteFlagAndIdInAndUpdateTimeGreaterThanAndUpdateTimeLessThanOrderByCreateTime("1", phraseInfo.getGradeIds(), graduationTime, calendar.getTimeInMillis(), pageIndex, pageSize);
        List<GradeModel> models = new ArrayList<GradeModel>();
        for (GradeInfo gradeInfo : gradeInfos) {
            models.add(MappingEntity2ModelConverter.CONVERTERFROMGRADEINFO(gradeInfo));
        }
        PageInfo<GradeModel> pageInfo = new PageInfo<GradeModel>();
        pageInfo.setPageNum(pageIndex);
        pageInfo.setPageSize(pageSize);
        pageInfo.setList(models);
        pageInfo.setTotal(gradeInfoExtraRepository.countByDeleteFlagAndIdInAndUpdateTimeGreaterThanAndUpdateTimeLessThanOrderByCreateTime("1", phraseInfo.getGradeIds(), graduationTime, calendar.getTimeInMillis()));
        return pageInfo;
    }

    @Override
    public PageInfo<ClassModel> findPastClass(UserInfoForToken userInfo, String gradeId, String graduation, int pageIndex, int pageSize) throws SchoolServiceException {
        if (StringUtils.isEmpty(gradeId) || StringUtils.isEmpty(graduation)) {
            throw new SchoolServiceException(ResultCode.PARAM_MISS_MSG);
        }
        GradeInfo gradeInfo = gradeInfoRepository.findFirstById(gradeId);
        if (null == gradeInfo) {
            throw new SchoolServiceException(ResultCode.GRADE_SELECT_NULL_MSG);
        }
        long graduationTime = 0;
        try {
            graduationTime = sdf.parse(graduation).getTime();
        } catch (ParseException e) {
            throw new SchoolServiceException(e.getMessage());
        }
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date(graduationTime));
        calendar.set(Calendar.MONTH, 11);
        calendar.set(Calendar.DAY_OF_MONTH, 31);
        calendar.set(Calendar.HOUR_OF_DAY, 24);
        List<ClassInfo> classInfos = classInfoExtraRepository.findByClassIdInAndDeleteFlagAndUpdateTimeGreaterThanAndUpdateTimeLessThanOrderByCreatedTime(gradeInfo.getClassIds(), "1", graduationTime, calendar.getTimeInMillis(), pageIndex, pageSize);
        List<ClassModel> classModels = new ArrayList<ClassModel>();
        classInfos.forEach(classInfo -> {
            classModels.add(MappingEntity2ModelConverter.CONVERTERFRONCLASSINFO(classInfo));
        });
        PageInfo<ClassModel> pageInfo = new PageInfo<ClassModel>();
        pageInfo.setList(classModels);
        pageInfo.setTotal(classInfoExtraRepository.countByClassIdInAndDeleteFlagAndUpdateTimeGreaterThanAndUpdateTimeLessThanOrderByCreatedTime(gradeInfo.getClassIds(), "1", graduationTime, calendar.getTimeInMillis()));
        pageInfo.setPageSize(pageSize);
        pageInfo.setPageNum(pageIndex);
        return pageInfo;
    }

    @Override
    public void coldStudentInGraduation(UserInfoForToken userInfo, String classId, String schoolId) throws SchoolServiceException {
        if (StringUtils.isEmpty(classId) || StringUtils.isEmpty(schoolId)) {
            throw new SchoolServiceException(ResultCode.PARAM_MISS_MSG);
        }
        List<String> classIds = new ArrayList<String>();
        classIds.add(classId);
        List<UserInfo> students = userExtraRepository.findByConditions(schoolId,classIds,"","","",0,0);
        for (UserInfo student : students){
            student.setStatus(Status.DELETE.getName());
        }
        userRepository.saveAll(students);
    }
}
