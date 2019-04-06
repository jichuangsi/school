package cn.com.jichuangsi.school.backstage.feign.service;

import cn.com.jichuangsi.school.backstage.feign.model.SchoolModel;
import cn.com.jichuangsi.school.backstage.feign.service.impl.UserFeignFallBackServiceImpl;
import com.jichuangsi.microservice.common.model.ResponseModel;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@FeignClient(value = "userservice",fallbackFactory = UserFeignFallBackServiceImpl.class)
public interface IUserFeignService {

    @RequestMapping("/feign/getSchoolBySchoolId")
    ResponseModel<SchoolModel> getSchoolBySchoolId(@RequestParam("schoolId") String schoolId);

    @RequestMapping("/feign/getBackSchools")
    ResponseModel<List<SchoolModel>> getBackSchools();
}
