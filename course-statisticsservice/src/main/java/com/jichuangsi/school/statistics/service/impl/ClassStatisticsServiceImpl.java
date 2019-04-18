package com.jichuangsi.school.statistics.service.impl;

import com.jichuangsi.microservice.common.constant.ResultCode;
import com.jichuangsi.microservice.common.model.ResponseModel;
import com.jichuangsi.microservice.common.model.UserInfoForToken;
import com.jichuangsi.school.statistics.entity.StudentAddCourseEntity;
import com.jichuangsi.school.statistics.exception.QuestionResultException;
import com.jichuangsi.school.statistics.feign.ICourseFeignService;
import com.jichuangsi.school.statistics.feign.IUserFeignService;
import com.jichuangsi.school.statistics.feign.model.ClassDetailModel;
import com.jichuangsi.school.statistics.feign.model.TransferStudent;
import com.jichuangsi.school.statistics.model.classType.ClassStatisticsModel;
import com.jichuangsi.school.statistics.model.classType.SearchStudentKnowledgeModel;
import com.jichuangsi.school.statistics.model.classType.StudentKnowledgeModel;
import com.jichuangsi.school.statistics.repository.StudentAddCourseRepository;
import com.jichuangsi.school.statistics.service.IClassStatisticsService;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.util.List;

@Service
public class ClassStatisticsServiceImpl implements IClassStatisticsService {

    @Resource
    private IUserFeignService userFeignService;
    @Resource
    private ICourseFeignService courseFeignService;
    @Resource
    private StudentAddCourseRepository studentAddCourseRepository;

    @Override
    @Cacheable(unless = "#result.empty",keyGenerator = "classStatisticsKeyGenerator")
    public List<ClassStatisticsModel> getTeachClassStatistics(UserInfoForToken userInfo) throws QuestionResultException {
        if (StringUtils.isEmpty(userInfo.getUserId())) {
            throw new QuestionResultException(ResultCode.PARAM_MISS_MSG);
        }
        ResponseModel<List<String>> responseModel = userFeignService.getTeachClassIds(userInfo.getUserId());
        if (!ResultCode.SUCESS.equals(responseModel.getCode())) {
            throw new QuestionResultException(responseModel.getMsg());
        }
        ResponseModel<List<ClassDetailModel>> response1 = userFeignService.getClassDetailByIds(responseModel.getData());
        if (!ResultCode.SUCESS.equals(response1.getCode())) {
            throw new QuestionResultException(response1.getMsg());
        }
        ResponseModel<List<ClassStatisticsModel>> response = courseFeignService.getClassStatisticsByClassIdsOnMonth(response1.getData());
        if (!ResultCode.SUCESS.equals(response.getCode())) {
            throw new QuestionResultException(response.getMsg());
        }
        List<ClassStatisticsModel> classStatisticsModels = response.getData();
        return classStatisticsModels;
    }

    @Override
    @Cacheable(unless = "#result.empty",keyGenerator = "classStudentKnowledgesKeyGenerator")
    public List<StudentKnowledgeModel> getClassStudentKnowledges(UserInfoForToken userInfo, SearchStudentKnowledgeModel model) throws QuestionResultException {
        if (StringUtils.isEmpty(model.getClassId()) || !(model.getQuestionIds().size() > 0)) {
            throw new QuestionResultException(ResultCode.PARAM_MISS_MSG);
        }
        ResponseModel<List<TransferStudent>> responseModel = userFeignService.getStudentsForClass(model.getClassId());
        if (!ResultCode.SUCESS.equals(responseModel.getCode())) {
            throw new QuestionResultException(responseModel.getMsg());
        }
        model.setTransferStudents(responseModel.getData());
        ResponseModel<List<StudentKnowledgeModel>> response = courseFeignService.getStudentKnowledges(model);
        if (!ResultCode.SUCESS.equals(response.getCode())) {
            throw new QuestionResultException(response.getMsg());
        }
        return response.getData();
    }

    @Override
    @Cacheable(unless = "#result.empty",keyGenerator = "getCourseSignKeyGenerator")
    public List<TransferStudent> getCourseSign(UserInfoForToken userInfo, String courseId,String classId) throws QuestionResultException {
        if (StringUtils.isEmpty(courseId) || StringUtils.isEmpty(classId)) {
            throw new QuestionResultException(ResultCode.PARAM_MISS_MSG);
        }
        List<StudentAddCourseEntity> studentAddCourseEntitys = studentAddCourseRepository
                .findByCourseId(courseId);
        ResponseModel<List<TransferStudent>> responseModel = userFeignService.getStudentsForClass(classId);
        if (!ResultCode.SUCESS.equals(responseModel.getCode())){
            throw new QuestionResultException(responseModel.getMsg());
        }
        for (StudentAddCourseEntity studentAddCourseEntity : studentAddCourseEntitys) {
            for (TransferStudent transferStudent : responseModel.getData()){
                if (studentAddCourseEntity.getUserId().equals(transferStudent.getStudentId())){
                    transferStudent.setSignFlag("1");
                }
            }
        }
        return responseModel.getData();
    }
}
