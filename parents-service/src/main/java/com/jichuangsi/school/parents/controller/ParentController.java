package com.jichuangsi.school.parents.controller;

import com.github.pagehelper.PageInfo;
import com.jichuangsi.microservice.common.model.ResponseModel;
import com.jichuangsi.microservice.common.model.UserInfoForToken;
import com.jichuangsi.school.parents.exception.ParentsException;
import com.jichuangsi.school.parents.model.MessageBoardModel;
import com.jichuangsi.school.parents.model.NoticeModel;
import com.jichuangsi.school.parents.model.ParentMessageModel;
import com.jichuangsi.school.parents.model.ParentModel;
import com.jichuangsi.school.parents.service.IParentService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

@RestController
@RequestMapping("/parent")
@Api("关于家长端的controller")
public class ParentController {

    @Resource
    private IParentService parentService;

    @ApiOperation(value = "发送家长留言", notes = "")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header", name = "accessToken", value = "用户token", required = true, dataType = "String")
    })
    @PostMapping(value = "/sendParentMessage")
    public ResponseModel sendParentMessage(@ModelAttribute UserInfoForToken userInfo, @RequestBody ParentMessageModel messageModel){
        try {
            parentService.sendParentMessage(userInfo, messageModel);
        } catch (ParentsException e) {
            return ResponseModel.fail("",e.getMessage());
        }
        return ResponseModel.sucessWithEmptyData("");
    }

    @ApiOperation(value = "查询家长端留言聊天记录", notes = "")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header", name = "accessToken", value = "用户token", required = true, dataType = "String")
    })
    @GetMapping(value = "/getParentMessage/{techerId}/{pageIndex}/{pageSize}")
    public ResponseModel<PageInfo<ParentMessageModel>> getParentMessage(@ModelAttribute UserInfoForToken userInfo,@PathVariable String teacherId,@PathVariable int pageIndex,@PathVariable int pageSize){
        try {
            return ResponseModel.sucess("",parentService.getParentMessage(userInfo, teacherId, pageIndex, pageSize));
        } catch (ParentsException e) {
            return ResponseModel.fail("",e.getMessage());
        }
    }

    @ApiOperation(value = "家长端留言板", notes = "")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header", name = "accessToken", value = "用户token", required = true, dataType = "String")
    })
    @GetMapping(value = "/leaveParentMessageBoard")
    public ResponseModel leaveParentMessageBoard(@ModelAttribute UserInfoForToken userInfo,@RequestBody MessageBoardModel model){
        try {
            parentService.insertMessageBoard(userInfo, model);
        } catch (ParentsException e) {
            return ResponseModel.fail("",e.getMessage());
        }
        return ResponseModel.sucessWithEmptyData("");
    }

    @ApiOperation(value = "家长绑定学生", notes = "")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header", name = "accessToken", value = "用户token", required = true, dataType = "String")
    })
    @GetMapping(value = "/parentBindStudent/{studentAccount}")
    public ResponseModel parentBindStudent(@ModelAttribute UserInfoForToken userInfo,@PathVariable String studentAccount){
        try {
            parentService.bindStudent(userInfo, studentAccount);
        } catch (ParentsException e) {
            return ResponseModel.fail("",e.getMessage());
        }
        return ResponseModel.sucessWithEmptyData("");
    }

    @ApiOperation(value = "家长获取最新非删除通知", notes = "")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header", name = "accessToken", value = "用户token", required = true, dataType = "String")
    })
    @GetMapping(value = "/parentGetNewNotices")
    public ResponseModel<List<NoticeModel>> parentGetNewNotices(@ModelAttribute UserInfoForToken userInfo){
        try {
            return ResponseModel.sucess("",parentService.parentGetNewNotices(userInfo));
        } catch (ParentsException e) {
            return ResponseModel.fail("",e.getMessage());
        }
    }

    @ApiOperation(value = "家长删除通知", notes = "")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header", name = "accessToken", value = "用户token", required = true, dataType = "String")
    })
    @DeleteMapping(value = "/deleteParentNotice/{noticeId}")
    public ResponseModel deleteParentNotice(@ModelAttribute UserInfoForToken userInfo,@PathVariable String noticeId){
        try {
            parentService.deleteParentNotice(userInfo, noticeId);
        } catch (ParentsException e) {
            return ResponseModel.fail("",e.getMessage());
        }
        return ResponseModel.sucessWithEmptyData("");
    }

    @ApiOperation(value = "家长端通过微信openId登录，返回token(请求token为固定token即可)", notes = "")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header", name = "accessToken", value = "用户token", required = true, dataType = "String")
    })
    @GetMapping(value = "/loginParentService/{openId}")
    public ResponseModel<String> loginParentService(@ModelAttribute UserInfoForToken userInfo,@PathVariable String openId){
        try {
            return ResponseModel.sucess("",parentService.loginParentService(userInfo, openId));
        } catch (ParentsException e) {
            return ResponseModel.fail("",e.getMessage());
        }
    }

    @ApiOperation(value = "家长端通过微信openId注册，返回token(请求token为固定token即可)", notes = "")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header", name = "accessToken", value = "用户token", required = true, dataType = "String")
    })
    @PostMapping(value = "/registParentService/{openId}")
    public ResponseModel registParentService(@ModelAttribute UserInfoForToken userInfo, @RequestBody ParentModel model){
        try {
            parentService.registParentService(userInfo, model);
        } catch (ParentsException e) {
            return ResponseModel.fail("",e.getMessage());
        }
        return ResponseModel.sucessWithEmptyData("");
    }
}
