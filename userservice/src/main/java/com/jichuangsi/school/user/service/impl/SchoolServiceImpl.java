package com.jichuangsi.school.user.service.impl;

import com.jichuangsi.microservice.common.constant.ResultCode;
import com.jichuangsi.school.user.entity.org.GradeInfo;
import com.jichuangsi.school.user.entity.org.SchoolInfo;
import com.jichuangsi.school.user.exception.SchoolServiceException;
import com.jichuangsi.school.user.model.school.GradeModel;
import com.jichuangsi.school.user.model.school.SchoolModel;
import com.jichuangsi.school.user.repository.IGradeInfoRepository;
import com.jichuangsi.school.user.repository.ISchoolInfoRepository;
import com.jichuangsi.school.user.service.ISchoolService;
import com.jichuangsi.school.user.util.MappingModel2EntityConverter;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;

@Service
public class SchoolServiceImpl implements ISchoolService {
    @Resource
    private ISchoolInfoRepository schoolInfoRepository;
    @Resource
    private IGradeInfoRepository gradeInfoRepository;

    @Override
    public void insertSchool(SchoolModel model) throws SchoolServiceException {
        if (StringUtils.isEmpty(model.getSchoolName()) || StringUtils.isEmpty(model.getAddress())){
            throw new SchoolServiceException(ResultCode.PARAM_MISS_MSG);
        }
        SchoolInfo info = MappingModel2EntityConverter.CONVERTERFROMSCHOOLMODEL(model);
        schoolInfoRepository.save(info);
    }

    @Override
    public void updateSchool(SchoolModel model) throws SchoolServiceException {
        if (StringUtils.isEmpty(model.getSchoolName()) || StringUtils.isEmpty(model.getAddress()) || StringUtils.isEmpty(model.getSchoolId())){
            throw new SchoolServiceException(ResultCode.PARAM_MISS_MSG);
        }
        SchoolInfo info = schoolInfoRepository.findFirstById(model.getSchoolId());
        if (null == info){
            throw new SchoolServiceException(ResultCode.SELECT_NULL_MSG);
        }
        info.setAddress(model.getAddress());
        info.setName(model.getSchoolName());
        schoolInfoRepository.save(info);
    }

    @Override
    public void insertGrade(GradeModel model) throws SchoolServiceException {
        if (StringUtils.isEmpty(model.getSchoolId()) || StringUtils.isEmpty(model.getGradeName())){
            throw new SchoolServiceException(ResultCode.PARAM_MISS_MSG);
        }
        SchoolInfo sinfo = schoolInfoRepository.findFirstById(model.getSchoolId());
        if (null == sinfo){
            throw new SchoolServiceException(ResultCode.SELECT_NULL_MSG);
        }
        GradeInfo info = MappingModel2EntityConverter.CONVERTERFROMGRADEMODEL(model);
        info = gradeInfoRepository.save(info);
        sinfo.getGradeIds().add(info.getId());
        schoolInfoRepository.save(sinfo);
    }

    @Override
    public void updateGrade(GradeModel model) throws SchoolServiceException {
        if (StringUtils.isEmpty(model.getGradeName())  || StringUtils.isEmpty(model.getGradeId())){
            throw new SchoolServiceException(ResultCode.PARAM_MISS_MSG);
        }
        GradeInfo info = gradeInfoRepository.findFirstById(model.getGradeId());
        if (null == info){
            throw new SchoolServiceException(ResultCode.SELECT_NULL_MSG);
        }
        info.setName(model.getGradeName());
        gradeInfoRepository.save(info);
    }
}
