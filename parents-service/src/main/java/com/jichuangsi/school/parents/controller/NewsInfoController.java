package com.jichuangsi.school.parents.controller;

import com.jichuangsi.microservice.common.model.ResponseModel;
import com.jichuangsi.microservice.common.model.UserInfoForToken;
import com.jichuangsi.school.parents.exception.ParentsException;
import com.jichuangsi.school.parents.model.NewsDetailModel;
import com.jichuangsi.school.parents.model.NewsSummaryModel;
import com.jichuangsi.school.parents.service.INewsInfoService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

@RestController
@RequestMapping("/news")
@Api("家长端教育信息")
public class NewsInfoController {

    @Resource
    private INewsInfoService newsInfoService;

    @ApiOperation(value = "获取教育信息列表", notes = "")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header", name = "accessToken", value = "用户token", required = true, dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "status", value = "信息分类", required = true, dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "pageindex", value = "页数", required = true, dataType = "int"),
            @ApiImplicitParam(paramType = "query", name = "pagesize", value = "页面条数", required = true, dataType = "int"),
    })
    @GetMapping(value = "/getNewsList")
    public ResponseModel<List<NewsSummaryModel>> getNewsList(@ModelAttribute UserInfoForToken userInfo, @RequestParam String status, @RequestParam int pageindex, @RequestParam int pagesize) throws ParentsException{
        return ResponseModel.sucess("",newsInfoService.getNewsInfoList(userInfo, status, pageindex, pagesize));
    }

    @ApiOperation(value = "获取教育信息详情", notes = "")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header", name = "accessToken", value = "用户token", required = true, dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "status", value = "信息主键", required = true, dataType = "String")
    })
    @GetMapping(value = "/getNewsDetail")
    public ResponseModel<List<NewsDetailModel>> getNewsDetail(@ModelAttribute UserInfoForToken userInfo, @RequestParam String infoid) throws ParentsException{
        return ResponseModel.sucess("",newsInfoService.getNewsInfoDetail(userInfo, infoid));
    }

    @ApiOperation(value = "查询教育信息", notes = "")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header", name = "accessToken", value = "用户token", required = true, dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "title", value = "查询内容", required = true, dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "pageindex", value = "页数", required = true, dataType = "int"),
            @ApiImplicitParam(paramType = "query", name = "pagesize", value = "页面条数", required = true, dataType = "int"),
    })
    @GetMapping(value = "/queryNews")
    public ResponseModel<List<NewsSummaryModel>> queryNews(@ModelAttribute UserInfoForToken userInfo, @RequestParam String title, @RequestParam int pageindex, @RequestParam int pagesize) throws ParentsException{
        return ResponseModel.sucess("",newsInfoService.queryNewsByKeywords(userInfo, title, pageindex, pagesize));
    }
}
