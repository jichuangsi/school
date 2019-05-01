package com.jichuangsi.school.parents.controller;

import com.jichuangsi.microservice.common.model.ResponseModel;

import com.jichuangsi.school.parents.exception.ParentsException;
import com.jichuangsi.school.parents.model.transfer.CourseSignModel;
import com.jichuangsi.school.parents.model.transfer.TransferNoticeToParent;
import com.jichuangsi.school.parents.service.IParentService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

@RestController
@RequestMapping("/feign")
@Api("parent的feign")
public class FeignController {

    @Resource
    private IParentService parentService;

    @ApiOperation(value = "通知同步家长", notes = "")
    @RequestMapping("/sendParentsNotice")
    public ResponseModel  sendParentsNotice(@RequestBody TransferNoticeToParent transferNoticeToParent) throws ParentsException{
        return ResponseModel.sucess("",parentService.addNoticeToParents(transferNoticeToParent));
    }

    @ApiOperation(value = "撤销通知家长", notes = "")
    @RequestMapping("/recallParentNotice")
    public ResponseModel  recallParentNotice(@RequestParam("messageId") String messageId) throws ParentsException{
        return ResponseModel.sucess("",parentService.removeNoticeToParents(messageId));
    }

    @ApiOperation(value = "发送学生的签到信息", notes = "")
    @RequestMapping("/sendParentStudentMsg")
    public ResponseModel sendParentStudentMsg(@RequestBody CourseSignModel model) throws ParentsException{
        return ResponseModel.sucess("",parentService.sendParentStudentMsg(model));
    }
}
