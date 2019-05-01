package com.jichuangsi.school.user.service.impl;

import com.jichuangsi.microservice.common.model.ResponseModel;
import com.jichuangsi.school.user.model.transfer.TransferNoticeToParent;
import com.jichuangsi.school.user.service.IParentFeignService;
import org.springframework.stereotype.Service;

@Service
public class ParentFallBackFeignServiceImpl implements IParentFeignService {

    private final String ERR_MSG ="调用微服务失败";

    @Override
    public ResponseModel sendParentsNotice(TransferNoticeToParent transferNoticeToParent) {
        return ResponseModel.fail("",ERR_MSG);
    }

    @Override
    public ResponseModel recallParentNotice(String messageId) {
        return ResponseModel.fail("",ERR_MSG);
    }
}
