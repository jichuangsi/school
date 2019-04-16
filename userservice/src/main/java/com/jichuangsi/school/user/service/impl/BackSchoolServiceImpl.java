package com.jichuangsi.school.user.service.impl;

import com.jichuangsi.microservice.common.model.UserInfoForToken;
import com.jichuangsi.school.user.constant.ResultCode;
import com.jichuangsi.school.user.entity.backstage.TimeTableInfo;
import com.jichuangsi.school.user.entity.org.ClassInfo;
import com.jichuangsi.school.user.exception.BackUserException;
import com.jichuangsi.school.user.model.backstage.TimeTableModel;
import com.jichuangsi.school.user.model.file.UserFile;
import com.jichuangsi.school.user.repository.IClassInfoRepository;
import com.jichuangsi.school.user.repository.ITimeTableInfoRepository;
import com.jichuangsi.school.user.service.IBackSchoolService;
import com.jichuangsi.school.user.service.IFileStoreService;
import com.jichuangsi.school.user.util.ExcelReadUtils;
import com.jichuangsi.school.user.util.MappingEntity2ModelConverter;
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
}
