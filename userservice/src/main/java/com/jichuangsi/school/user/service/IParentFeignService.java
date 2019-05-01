package com.jichuangsi.school.user.service;

import com.jichuangsi.microservice.common.model.ResponseModel;
import com.jichuangsi.school.user.model.transfer.TransferNoticeToParent;
import com.jichuangsi.school.user.service.impl.ParentFallBackFeignServiceImpl;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(value = "parentservice",fallbackFactory = ParentFallBackFeignServiceImpl.class)
public interface IParentFeignService {

    @RequestMapping("/feign/sendParentsNotice")
    ResponseModel sendParentsNotice(@RequestBody TransferNoticeToParent transferNoticeToParent);

    @RequestMapping("/feign/recallParentNotice")
    ResponseModel recallParentNotice(@RequestParam("messageId") String messageId);
}
