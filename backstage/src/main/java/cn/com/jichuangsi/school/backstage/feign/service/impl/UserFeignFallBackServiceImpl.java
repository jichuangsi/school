package cn.com.jichuangsi.school.backstage.feign.service.impl;

import cn.com.jichuangsi.school.backstage.feign.model.SchoolModel;
import cn.com.jichuangsi.school.backstage.feign.service.IUserFeignService;
import com.jichuangsi.microservice.common.model.ResponseModel;

import java.util.List;

public class UserFeignFallBackServiceImpl implements IUserFeignService {

    @Override
    public ResponseModel<SchoolModel> getSchoolBySchoolId(String schoolId) {
        return ResponseModel.fail("");
    }

    @Override
    public ResponseModel<List<SchoolModel>> getBackSchools() {
        return ResponseModel.fail("");
    }
}
