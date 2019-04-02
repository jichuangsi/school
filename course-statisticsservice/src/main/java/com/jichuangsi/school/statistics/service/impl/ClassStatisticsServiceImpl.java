package com.jichuangsi.school.statistics.service.impl;

import com.jichuangsi.microservice.common.constant.ResultCode;
import com.jichuangsi.microservice.common.model.ResponseModel;
import com.jichuangsi.microservice.common.model.UserInfoForToken;
import com.jichuangsi.school.statistics.exception.QuestionResultException;
import com.jichuangsi.school.statistics.feign.ICourseFeignService;
import com.jichuangsi.school.statistics.feign.IUserFeignService;
import com.jichuangsi.school.statistics.model.classType.ClassStatisticsModel;
import com.jichuangsi.school.statistics.service.IClassStatisticsService;
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

    @Override
    public List<ClassStatisticsModel> getTeachClassStatistics(UserInfoForToken userInfo) throws QuestionResultException {
        if (StringUtils.isEmpty(userInfo.getUserId())){
            throw new QuestionResultException(ResultCode.PARAM_MISS_MSG);
        }
        ResponseModel<List<String>> responseModel = userFeignService.getTeachClassIds(userInfo.getUserId());
        if (!ResultCode.SUCESS.equals(responseModel.getCode())){
            throw new QuestionResultException(responseModel.getMsg());
        }
        ResponseModel<List<ClassStatisticsModel>> response = courseFeignService.getClassStatisticsByClassIdsOnMonth(responseModel.getData());
        if (!ResultCode.SUCESS.equals(response.getCode())){
            throw new QuestionResultException(response.getMsg());
        }
        List<ClassStatisticsModel> classStatisticsModels = response.getData();
        for (ClassStatisticsModel model : classStatisticsModels){
            
        }
        return classStatisticsModels;
    }
}
