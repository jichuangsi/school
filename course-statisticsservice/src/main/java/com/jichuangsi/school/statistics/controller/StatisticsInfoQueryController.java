/**
 * 统计信息查询接口
 */
package com.jichuangsi.school.statistics.controller;

import javax.annotation.Resource;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import com.jichuangsi.microservice.common.model.ResponseModel;
import com.jichuangsi.school.statistics.model.CourseStatisticsModel;
import com.jichuangsi.school.statistics.model.QuestionStatisticsListModel;
import com.jichuangsi.school.statistics.service.ICourseStatisticsService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;

/**
 * @author huangjiajun
 *
 */
@RestController
@Api("StatisticsInfoQueryController相关的api")
public class StatisticsInfoQueryController {

	@Resource
	private ICourseStatisticsService courseStatisticsService;

	@ApiOperation(value = "根据课堂id查询课堂统计信息", notes = "")
	@ApiImplicitParams({
			@ApiImplicitParam(paramType = "path", name = "courseId", value = "课堂ID", required = true, dataType = "String") })
	@GetMapping("/getCourseStatistics/{courseId}")
	public ResponseModel<CourseStatisticsModel> getCourseStatistics(@PathVariable String courseId) {
		CourseStatisticsModel model = courseStatisticsService.getCourseStatistics(courseId);
		return ResponseModel.sucess("", model);
	}

	@ApiOperation(value = "根据课堂id查询课堂中题目的统计信息", notes = "")
	@ApiImplicitParams({
			@ApiImplicitParam(paramType = "path", name = "courseId", value = "课堂ID", required = true, dataType = "String") })
	@GetMapping("/getQuestionStatisticsList/{courseId}")
	public ResponseModel<QuestionStatisticsListModel> getQuestionStatisticsList(@PathVariable String courseId) {
		QuestionStatisticsListModel model = courseStatisticsService.getQuestionStatisticsList(courseId);
		return ResponseModel.sucess("", model);
	}

}
