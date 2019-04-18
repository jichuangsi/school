package com.jichuangsi.school.user.service.impl;

import com.github.pagehelper.PageInfo;
import com.jichuangsi.microservice.common.model.UserInfoForToken;
import com.jichuangsi.school.user.constant.ResultCode;
import com.jichuangsi.school.user.entity.UserInfo;
import com.jichuangsi.school.user.entity.backstage.SchoolAttachment;
import com.jichuangsi.school.user.entity.backstage.SchoolNoticeInfo;
import com.jichuangsi.school.user.entity.backstage.TimeTableInfo;
import com.jichuangsi.school.user.entity.org.ClassInfo;
import com.jichuangsi.school.user.entity.org.GradeInfo;
import com.jichuangsi.school.user.entity.org.PhraseInfo;
import com.jichuangsi.school.user.entity.org.SchoolInfo;
import com.jichuangsi.school.user.entity.parent.ParentInfo;
import com.jichuangsi.school.user.entity.parent.ParentNotice;
import com.jichuangsi.school.user.exception.BackUserException;
import com.jichuangsi.school.user.model.SchoolMessageModel;
import com.jichuangsi.school.user.model.backstage.SchoolNoticeModel;
import com.jichuangsi.school.user.model.backstage.TimeTableModel;
import com.jichuangsi.school.user.model.file.UserFile;
import com.jichuangsi.school.user.repository.*;
import com.jichuangsi.school.user.repository.backstage.ISchoolAttachmentRepository;
import com.jichuangsi.school.user.repository.parent.IParentInfoRepository;
import com.jichuangsi.school.user.repository.parent.IParentNoticeRepository;
import com.jichuangsi.school.user.service.IBackSchoolService;
import com.jichuangsi.school.user.service.IFileStoreService;
import com.jichuangsi.school.user.util.ExcelReadUtils;
import com.jichuangsi.school.user.util.MappingEntity2ModelConverter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class BackSchoolServiceImpl implements IBackSchoolService {

    @Resource
    private IFileStoreService fileStoreService;
    @Resource
    private IClassInfoRepository classInfoRepository;
    @Resource
    private ITimeTableInfoRepository timeTableInfoRepository;
    @Resource
    private ISchoolAttachmentRepository schoolAttachmentRepository;
    @Resource
    private IGradeInfoRepository gradeInfoRepository;
    @Resource
    private IPhraseInfoRepository phraseInfoRepository;
    @Resource
    private ISchoolInfoRepository schoolInfoRepository;
    @Resource
    private ISchoolNoticeInfoRepository schoolNoticeInfoRepository;
    @Resource
    private IUserExtraRepository userExtraRepository;
    @Resource
    private IParentInfoRepository parentInfoRepository;
    @Resource
    private IParentNoticeRepository parentNoticeRepository;
    @Resource
    private ISchoolNoticeInfoExtraRepository schoolNoticeInfoExtraRepository;

    @Value("${com.jichuangsi.school.mq.send_parent_notice}")
    private String sendNotice;

    @Override
    public TimeTableModel findByClassId(String classId) throws BackUserException {
        if (StringUtils.isEmpty(classId)){
            throw new BackUserException(ResultCode.PARAM_MISS_MSG);
        }
        TimeTableInfo timeTableInfo = timeTableInfoRepository.findFirstByClassIdAndDeleteOrderByCreatedTimeDesc(classId,"0");
        if (null == timeTableInfo){
            throw new BackUserException(ResultCode.CLASS_TABLE_NULL_MSG);
        }
        return MappingEntity2ModelConverter.CONVERTERFROMTIMETABLEINFO(timeTableInfo);
    }

    @Override
    public void saveTimeTableByExcel(MultipartFile file, String classId, UserInfoForToken userInfo) throws BackUserException {
        String fileName = file.getOriginalFilename();
        if (!fileName.endsWith(".xls")) {
            System.out.println("文件不是.xls类型");
        }
        ClassInfo classInfo = classInfoRepository.findFirstByIdAndDeleteFlag(classId, "0");
        if (null == classInfo) {
            throw new BackUserException(ResultCode.CLASS_SELECT_NULL_MSG);
        }
        if (timeTableInfoRepository.countByClassIdAndDelete(classId,"0") > 0){
            throw new BackUserException(ResultCode.CLASS_TABLE_EXIST_MSG);
        }
        UserFile userFile = new UserFile();
        try {
            userFile = new UserFile(file.getContentType(), file.getOriginalFilename(), file.getBytes());
            fileStoreService.uploadFile(userFile);
        } catch (Exception e) {
            throw new BackUserException(e.getMessage());
        }
        TimeTableInfo timeTableInfo = new TimeTableInfo();
        timeTableInfo.setClassId(classInfo.getId());
        timeTableInfo.setClassName(classInfo.getName());
        timeTableInfo.setSub(userFile.getSubName());
        timeTableInfo.setCreatorId(userInfo.getUserId());
        timeTableInfo.setCreatorName(userInfo.getUserName());
        timeTableInfo.setUpdatedId(userInfo.getUserId());
        timeTableInfo.setUpdatedName(userInfo.getUserName());
        List<String> monday = new ArrayList<String>();
        List<String> tuesday = new ArrayList<String>();
        List<String> wednesday = new ArrayList<String>();
        List<String> thursday = new ArrayList<String>();
        List<String> friday = new ArrayList<String>();
        List<String> classBegin = new ArrayList<String>();
        for (int i = 0; i < 9; i++){
            List<String> results = new ArrayList<>();
            try {
                results = ExcelReadUtils.getOneRow(i,file);
            } catch (Exception e) {
                throw new BackUserException(ResultCode.FILE_CHANGE_ERR);
            }
            classBegin = insertListValue(0,results,classBegin);
            monday = insertListValue(1,results,monday);
            tuesday = insertListValue(2,results,tuesday);
            wednesday = insertListValue(3,results,wednesday);
            thursday = insertListValue(4,results,thursday);
            friday = insertListValue(5,results,friday);
        }
        timeTableInfo.setClassBegin(classBegin);
        timeTableInfo.setMonday(monday);
        timeTableInfo.setTuesday(tuesday);
        timeTableInfo.setWednesday(wednesday);
        timeTableInfo.setThursday(thursday);
        timeTableInfo.setFriday(friday);
        timeTableInfoRepository.save(timeTableInfo);
    }

    @Override
    public void delTimeTable(String tableId, String classId, UserInfoForToken userInfo) throws BackUserException {
        if (StringUtils.isEmpty(tableId) || StringUtils.isEmpty(classId)){
            throw new BackUserException(ResultCode.PARAM_MISS_MSG);
        }
        TimeTableInfo timeTableInfo = timeTableInfoRepository.findFirstByIdAndClassIdAndDelete(tableId,classId,"0");
        if (null == timeTableInfo){
            throw new BackUserException(ResultCode.CLASS_TABLE_NULL_MSG);
        }
        timeTableInfo.setDelete("1");
        timeTableInfo.setUpdatedName(userInfo.getUserName());
        timeTableInfo.setUpdatedId(userInfo.getUserId());
        timeTableInfo.setUpdatedTime(new Date().getTime());
        timeTableInfoRepository.save(timeTableInfo);
    }

    private List<String> insertListValue(int i,List<String> values,List<String> result){
        if (values.size() >= i+1){
            result.add(values.get(i));
        }
        return result;
    }

    @Override
    public List<UserFile> getAttachments(UserInfoForToken userInfo) throws BackUserException {
        List<SchoolAttachment> schoolAttachments = schoolAttachmentRepository.findAll();
        List<UserFile> userFiles = new ArrayList<UserFile>();
        schoolAttachments.forEach(schoolAttachment -> {
            userFiles.add(MappingEntity2ModelConverter.CONVERTERFROMSCHOOLATTACHMENT(schoolAttachment));
        });
        return userFiles;
    }

    @Override
    public UserFile downAttachment(UserInfoForToken userInfo, String subName) throws BackUserException {
        if (StringUtils.isEmpty(subName)){
            throw new BackUserException(ResultCode.PARAM_MISS_MSG);
        }
        try {
            UserFile file = fileStoreService.downFile(subName);
         /*   saveFile(file);*/
            return file;
        } catch (Exception e) {
            throw new BackUserException(e.getMessage());
        }
    }

    //测试是否成功下载
   /* public static void saveFile(UserFile userFile)throws Exception{
        if(userFile.getContent() != null){
            String filepath ="F:\\file\\" + userFile.getOriginalName();
            File file  = new File(filepath);
            if(file.exists()){
                file.delete();
            }
            FileOutputStream fos = new FileOutputStream(file);
            fos.write(userFile.getContent(),0,userFile.getContent().length);
            fos.flush();
            fos.close();
        }
    }*/

    @Override
    public void sendSchoolMessage(UserInfoForToken userInfo, String schoolId, SchoolMessageModel model) throws BackUserException {
        if (StringUtils.isEmpty(schoolId) || StringUtils.isEmpty(model.getTiltle()) || StringUtils.isEmpty(model.getContent())){
            throw new BackUserException(ResultCode.PARAM_MISS_MSG);
        }
        List<String> classIds = new ArrayList<String>();
        SchoolNoticeInfo noticeInfo = new SchoolNoticeInfo();
        if(!StringUtils.isEmpty(model.getClassId())){
            classIds.add(model.getClassId());
            noticeInfo.setClassName(model.getClassName());
        }else if(!StringUtils.isEmpty(model.getGradeId())){
            List<String> gradeIds = new ArrayList<String>();
            gradeIds.add(model.getGradeId());
            classIds.addAll(getClassIdsByGradeIds(gradeIds));
            noticeInfo.setGradeName(model.getGradeName());
        }else if (!StringUtils.isEmpty(model.getPharseId())){
            List<String> ids = new ArrayList<String>();
            ids.add(model.getPharseId());
            ids = getGradeIdsByPharseIds(ids);
            classIds.addAll(getClassIdsByGradeIds(ids));
            noticeInfo.setPharseName(model.getPharseName());
        }else {
            SchoolInfo schoolInfo = schoolInfoRepository.findFirstById(schoolId);
            if (null == schoolInfo){
                throw new BackUserException(ResultCode.SCHOOL_SELECT_NULL_MSG);
            }
            List<String> id = schoolInfo.getPhraseIds();
            classIds.addAll(getClassIdsByGradeIds(getGradeIdsByPharseIds(id)));
        }
        noticeInfo.setSchoolId(schoolId);
        noticeInfo.setClassIds(classIds);
        noticeInfo.setContent(model.getContent());
        noticeInfo.setCreatorId(userInfo.getUserId());
        noticeInfo.setCreatorName(userInfo.getUserName());
        noticeInfo.setTitle(model.getTiltle());
        noticeInfo.setUpdatedId(userInfo.getUserId());
        noticeInfo.setUpdatedName(userInfo.getUserName());
        noticeInfo = schoolNoticeInfoRepository.save(noticeInfo);
        List<UserInfo> userInfos = getStudentsByClassIds(classIds,schoolId);
        List<String> studentIds = new ArrayList<String>();
        for (UserInfo student : userInfos){
            studentIds.add(student.getId());
        }
        List<ParentInfo> parentInfos = parentInfoRepository.findByStudentIdsIn(studentIds);
        List<ParentNotice> parentNotices = new ArrayList<ParentNotice>();
        for (ParentInfo parentInfo : parentInfos){
            ParentNotice notice = new ParentNotice();
            notice.setMessageId(noticeInfo.getId());
            notice.setNoticeType(ParentNotice.COLLEGE_NOTICE);
            notice.setParentId(parentInfo.getId());
            notice.setParentName(parentInfo.getUserName());
            notice.setTitle(model.getTiltle());
            parentNotices.add(notice);
        }
        parentNoticeRepository.saveAll(parentNotices);
    }

    private List<String> getClassIdsByGradeIds(List<String> gradeIds){
        List<String> classIds = new ArrayList<String>();
        List<GradeInfo> gradeInfos = gradeInfoRepository.findByDeleteFlagAndIdInOrderByCreateTime("0",gradeIds);
        for (GradeInfo gradeInfo : gradeInfos){
            classIds.addAll(gradeInfo.getClassIds());
        }
        return classIds;
    }

    private List<String> getGradeIdsByPharseIds(List<String> pharseIds){
        List<String> gradeIds = new ArrayList<String>();
        List<PhraseInfo> phraseInfos = phraseInfoRepository.findByDeleteFlagAndIdIn("0",pharseIds);
        for (PhraseInfo phraseInfo : phraseInfos){
            gradeIds.addAll(phraseInfo.getGradeIds());
        }
        return gradeIds;
    }

    private List<UserInfo> getStudentsByClassIds(List<String> classIds,String schoolId){
        List<UserInfo> students = userExtraRepository.findByConditions(schoolId,classIds,"","Student","",0,0);
        return students;
    }

    @Override
    public PageInfo<SchoolNoticeModel> getSchoolNotices(UserInfoForToken userInfo, String schoolId, int pageIndex, int pageSize) throws BackUserException {
        if (StringUtils.isEmpty(schoolId)){
            throw new BackUserException(ResultCode.PARAM_MISS_MSG);
        }

        List<SchoolNoticeInfo> noticeInfos = schoolNoticeInfoExtraRepository.pageBySchoolIdAndDeleteFlagOrderByCreatedTimeDesc(schoolId,"0",pageIndex,pageSize);
        List<SchoolNoticeModel> models = new ArrayList<SchoolNoticeModel>();
        for (SchoolNoticeInfo info : noticeInfos){
            models.add(MappingEntity2ModelConverter.CONVERTERFROMSCHOOLNOTICEINFOTOSCHOOLNOTICEMODEL(info));
        }
        PageInfo<SchoolNoticeModel> pageInfo = new PageInfo<>();
        pageInfo.setTotal(schoolNoticeInfoRepository.countBySchoolIdAndDeleteFlagOrderByCreatedTimeDesc(schoolId,"0"));
        pageInfo.setPageSize(pageSize);
        pageInfo.setPageNum(pageIndex);
        pageInfo.setList(models);
        return pageInfo;
    }

    @Override
    public void deleteSchoolNotice(UserInfoForToken userInfo, String schoolId, String noticeId) throws BackUserException {
        if (StringUtils.isEmpty(schoolId) || StringUtils.isEmpty(noticeId)){
            throw new BackUserException(ResultCode.PARAM_MISS_MSG);
        }
        SchoolNoticeInfo schoolNoticeInfo = schoolNoticeInfoRepository.findFirstByIdAndDeleteFlagAndSchoolId(noticeId,"0",schoolId);
        if (null == schoolNoticeInfo){
            throw new BackUserException(ResultCode.SELECT_NULL_MSG);
        }
        schoolNoticeInfo.setDeleteFlag("1");
        schoolNoticeInfo.setUpdatedName(userInfo.getUserName());
        schoolNoticeInfo.setUpdatedId(userInfo.getUserId());
        schoolNoticeInfo.setUpdatedTime(new Date().getTime());
        schoolNoticeInfoRepository.save(schoolNoticeInfo);
    }
}
