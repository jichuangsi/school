package com.jichuangsi.school.parents.service.impl;

import com.jichuangsi.microservice.common.model.ResponseModel;
import com.jichuangsi.microservice.common.model.UserInfoForToken;
import com.jichuangsi.school.parents.commons.ResultCode;
import com.jichuangsi.school.parents.entity.GrowthDay;
import com.jichuangsi.school.parents.entity.ParentInfo;
import com.jichuangsi.school.parents.exception.ParentsException;
import com.jichuangsi.school.parents.feign.ICourseFeignService;
import com.jichuangsi.school.parents.feign.IHomeWorkFeignService;
import com.jichuangsi.school.parents.feign.ITestFeignService;
import com.jichuangsi.school.parents.feign.IUserFeignService;
import com.jichuangsi.school.parents.feign.model.ClassDetailModel;
import com.jichuangsi.school.parents.feign.model.ClassTeacherInfoModel;
import com.jichuangsi.school.parents.feign.model.HomeWorkParentModel;
import com.jichuangsi.school.parents.feign.model.TimeTableModel;
import com.jichuangsi.school.parents.model.GrowthModel;
import com.jichuangsi.school.parents.model.ParentStudentModel;
import com.jichuangsi.school.parents.model.file.ParentFile;
import com.jichuangsi.school.parents.model.statistics.KnowledgeStatisticsModel;
import com.jichuangsi.school.parents.model.statistics.ParentStatisticsModel;
import com.jichuangsi.school.parents.repository.IGrowDayRepository;
import com.jichuangsi.school.parents.repository.IParentInfoRepository;
import com.jichuangsi.school.parents.service.IFileStoreService;
import com.jichuangsi.school.parents.service.IStudentService;
import com.jichuangsi.school.parents.util.MappingEntity2ModelConverter;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
@CacheConfig(cacheNames = {"parentCache"})
public class StudentServiceImpl implements IStudentService {

    @Resource
    private IUserFeignService userFeignService;
    @Resource
    private IParentInfoRepository parentInfoRepository;
    @Resource
    private IFileStoreService fileStoreService;
    @Resource
    private IGrowDayRepository growDayRepository;
    @Resource
    private IHomeWorkFeignService homeWorkFeignService;
    @Resource
    private ICourseFeignService courseFeignService;
    @Resource
    private ITestFeignService testFeignService;

    @Override
    public List<ParentStudentModel> getStudentByStudentId(UserInfoForToken userInfo) throws ParentsException {
        if (StringUtils.isEmpty(userInfo.getUserId())) {
            throw new ParentsException(ResultCode.PARAM_MISS_MSG);
        }
        ParentInfo parentInfo = parentInfoRepository.findFirstByIdAndDeleteFlag(userInfo.getUserId(),"0");
        if (null == parentInfo.getStudentIds() || !(parentInfo.getStudentIds().size() > 0)) {
            throw new ParentsException(ResultCode.STUDNET_NOTBIND_MSG);
        }
        ResponseModel<List<ParentStudentModel>> responseModel = userFeignService.getParentStudent(parentInfo.getStudentIds());
        if (!ResultCode.SUCESS.equals(responseModel.getCode())) {
            throw new ParentsException(responseModel.getMsg());
        }
        return responseModel.getData();
    }

    @Override
    public void updateAttention(UserInfoForToken userInfo, String studentId) throws ParentsException {
        if (StringUtils.isEmpty(userInfo.getUserId()) || StringUtils.isEmpty(studentId)) {
            throw new ParentsException(ResultCode.PARAM_MISS_MSG);
        }
        ParentInfo parentInfo = parentInfoRepository.findFirstByIdAndDeleteFlag(userInfo.getUserId(),"0");
        if (null == parentInfo) {
            throw new ParentsException(ResultCode.PARENT_NOTFOUND_MSG);
        }
        if (!parentInfo.getStudentIds().contains(studentId)){
            throw new ParentsException(ResultCode.STUDNET_BING_ERR);
        }
        Set<String> studentIds = new HashSet<String>(parentInfo.getStudentIds());
        studentIds.remove(studentId);
        List<String> studentList = new ArrayList<String>(studentIds);
        studentList.add(0,studentId);
        parentInfo.setStudentIds(studentList);
        parentInfoRepository.save(parentInfo);
    }

    @Override
    public void uploadGrowth(MultipartFile file, UserInfoForToken userInfo, GrowthModel model) throws ParentsException {
        ParentFile parentFile;
        try {
            parentFile = new ParentFile(file.getContentType(),file.getOriginalFilename(),file.getBytes());
            fileStoreService.uploadFile(parentFile);
        } catch (Exception e) {
            throw new ParentsException(ResultCode.FILE_CHANGE_ERR);
        }
        GrowthDay growthDay = new GrowthDay();
        growthDay.setCreatedId(userInfo.getUserId());
        growthDay.setStudentId(model.getStudentId());
        growthDay.setSub(parentFile.getSubName());
        growthDay.setTitle(model.getTitle());
        growDayRepository.save(growthDay);
    }

    @Override
    public void deleteGrowth(UserInfoForToken userInfo, String studentId, String growthId) throws ParentsException {
        if (StringUtils.isEmpty(studentId) || StringUtils.isEmpty(growthId)){
            throw new ParentsException(ResultCode.PARAM_MISS_MSG);
        }
        GrowthDay growthDay = growDayRepository.findFirstByIdAndStudentId(growthId,studentId);
        if (null == growthDay){
            throw new ParentsException(ResultCode.GROWTH_NOTEXIST_ERR);
        }
        try {
            fileStoreService.deleteFile(growthDay.getSub());
        } catch (Exception e) {
            throw new ParentsException(e.getMessage());
        }
        growDayRepository.delete(growthDay);
    }

    @Override
    public List<GrowthModel> getGrowths(UserInfoForToken userInfo, String studentId) throws ParentsException {
        if (StringUtils.isEmpty(studentId)){
            throw new ParentsException(ResultCode.PARAM_MISS_MSG);
        }
        List<GrowthDay> growthDays = growDayRepository.findByStudentIdOrderByCreatedTime(studentId);
        List<GrowthModel> growthModels = new ArrayList<GrowthModel>();
        for (GrowthDay growthDay :growthDays) {
            if (StringUtils.isEmpty(growthDay.getSub())){
                continue;
            }
            ParentFile file;
            try {
                file = fileStoreService.downFile(growthDay.getSub());
            } catch (Exception e) {
                file = new ParentFile();
            }
            growthModels.add(MappingEntity2ModelConverter.CONVERTERFROMGROWTHDAYANDPARENTFILE(growthDay,file));
        }
        return growthModels;
    }

    @Override
    public List<HomeWorkParentModel> getStudentHomeWork(UserInfoForToken userInfo, String studentId) throws ParentsException {
        if (StringUtils.isEmpty(studentId)){
            throw new ParentsException(ResultCode.PARAM_MISS_MSG);
        }
        ResponseModel<ClassDetailModel> responseModel = userFeignService.getStudentClassDetail(studentId);
        if (!ResultCode.SUCESS.equals(responseModel.getCode())){
            throw new ParentsException(responseModel.getMsg());
        }
        ResponseModel<List<HomeWorkParentModel>> response = homeWorkFeignService.getParentHomeWork(responseModel.getData().getClassId(),studentId);
        if (!ResultCode.SUCESS.equals(response.getCode())){
            throw new ParentsException(response.getMsg());
        }
        List<HomeWorkParentModel> homeWorkParentModels = response.getData();
        /*Map<String,List<HomeWorkParentModel>> map = new HashMap<String, List<HomeWorkParentModel>>();
        for (HomeWorkParentModel model : homeWorkParentModels){
            String subject = model.getSubjectName();
            List<HomeWorkParentModel> homeworks = new ArrayList<HomeWorkParentModel>();
            if (map.containsKey(subject)){
                homeworks.addAll(map.get(subject));
            }
            homeworks.add(model);
            map.put(subject,homeworks);
        }*/
        return homeWorkParentModels;
    }

    @Override
    public List<ClassTeacherInfoModel> getStudentTeachers(UserInfoForToken userInfo, String studentId) throws ParentsException {
        if (StringUtils.isEmpty(studentId)){
            throw new ParentsException(ResultCode.PARAM_MISS_MSG);
        }
        ResponseModel<List<ClassTeacherInfoModel>> responseModel = userFeignService.getStudentTeachers(studentId);
        if (!ResultCode.SUCESS.equals(responseModel.getCode())){
            throw new ParentsException(responseModel.getMsg());
        }
        List<ClassTeacherInfoModel> models = responseModel.getData();
        ClassTeacherInfoModel model = new ClassTeacherInfoModel();
        for (ClassTeacherInfoModel teacherInfoModel : models){
            if (!StringUtils.isEmpty(teacherInfoModel.getHeadMaster())){
                model = teacherInfoModel;
                models.remove(model);
                break;
            }
        }
        models.add(0,model);
        return models;
    }

    @Override
    @Cacheable(unless = "#result.empty",keyGenerator = "getParentCourseStatisticsKeyGenerator")
    public List<KnowledgeStatisticsModel> getParentCourseStatistics(UserInfoForToken userInfo, ParentStatisticsModel model) throws ParentsException {
        if (StringUtils.isEmpty(model.getStudentId())){
            throw new ParentsException(ResultCode.PARAM_MISS_MSG);
        }
        ResponseModel<ClassDetailModel> responseModel = userFeignService.getStudentClassDetail(model.getStudentId());
        if (!ResultCode.SUCESS.equals(responseModel.getCode())){
            throw new ParentsException(responseModel.getMsg());
        }
        model.setClassId(responseModel.getData().getClassId());
        model.setStudentNum(responseModel.getData().getStudentNum());
        ResponseModel<List<KnowledgeStatisticsModel>> response = courseFeignService.getParentStatistics(model);
        if (!ResultCode.SUCESS.equals(response.getCode())){
            throw new ParentsException(response.getMsg());
        }
        return response.getData();
    }

    @Override
    @Cacheable(unless = "#result.empty", keyGenerator = "getParentHomeworkStatisticsKeyGenerator")
    public List<KnowledgeStatisticsModel> getParentHomeworkStatistics(UserInfoForToken userInfo, ParentStatisticsModel model) throws ParentsException {
        if (StringUtils.isEmpty(model.getStudentId())){
            throw new ParentsException(ResultCode.PARAM_MISS_MSG);
        }
        ResponseModel<ClassDetailModel> responseModel = userFeignService.getStudentClassDetail(model.getStudentId());
        if (!ResultCode.SUCESS.equals(responseModel.getCode())){
            throw new ParentsException(responseModel.getMsg());
        }
        model.setClassId(responseModel.getData().getClassId());
        model.setStudentNum(responseModel.getData().getStudentNum());
        ResponseModel<List<KnowledgeStatisticsModel>> response = homeWorkFeignService.getParentStudentHomeworkStatistics(model);
        if (!ResultCode.SUCESS.equals(response.getCode())){
            throw new ParentsException(response.getMsg());
        }
        return response.getData();
    }

    @Override
    public TimeTableModel getStudentTimeTable(UserInfoForToken userInfo, String studentId) throws ParentsException {
        if (StringUtils.isEmpty(studentId)){
            throw new ParentsException(ResultCode.PARAM_MISS_MSG);
        }
        ResponseModel<TimeTableModel> responseModel = userFeignService.getStudentTimeTable(studentId);
        if(!ResultCode.SUCESS.equals(responseModel.getCode())){
            throw new ParentsException(responseModel.getMsg());
        }
        return responseModel.getData();
    }


//考试统计
    @Override
    @Cacheable(unless = "#result.empty", keyGenerator = "getParentTestStatisticsKeyGenerator")
    public List<KnowledgeStatisticsModel> getParentTestStatistics(UserInfoForToken userInfo, ParentStatisticsModel model) throws  ParentsException{
        if (StringUtils.isEmpty(model.getStudentId())){
            throw new ParentsException(ResultCode.PARAM_MISS_MSG);
        }
        ResponseModel<ClassDetailModel> responseModel = userFeignService.getStudentClassDetail(model.getStudentId());

        if (!ResultCode.SUCESS.equals(responseModel.getCode())){
            throw new ParentsException(responseModel.getMsg());
        }
        model.setClassId(responseModel.getData().getClassId());
        model.setStudentNum(responseModel.getData().getStudentNum());
        ResponseModel<List<KnowledgeStatisticsModel>> response = testFeignService.getParentTestStatistics(model);
        System.out.println(response.getData().size());
        if (!ResultCode.SUCESS.equals(response.getCode())){
            throw new ParentsException(response.getMsg());
        }
        return response.getData();
    }
}
