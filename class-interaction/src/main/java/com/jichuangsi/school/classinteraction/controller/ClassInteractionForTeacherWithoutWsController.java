/**
 * 课堂互动非WS接口（教师）
 */
package com.jichuangsi.school.classinteraction.controller;

import javax.annotation.Resource;

import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import com.jichuangsi.microservice.common.model.ResponseModel;
import com.jichuangsi.microservice.common.model.UserInfoForToken;
import com.jichuangsi.school.classinteraction.service.IClassInteractionForTeacherService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import springfox.documentation.annotations.ApiIgnore;

/**
 * @author huangjiajun
 *
 */
@RestController
@Api("ClassInteractionForTeacherWithoutWsController相关的api")
public class ClassInteractionForTeacherWithoutWsController {

	@Resource
	private IClassInteractionForTeacherService classInteractionForTeacherService;

	@ApiOperation(value = "教师发布抢答信息", notes = "")
	@ApiImplicitParams({
			@ApiImplicitParam(paramType = "header", name = "accessToken", value = "用户token", required = true, dataType = "String"),
			@ApiImplicitParam(paramType = "path", name = "courseId", value = "课堂ID", required = true, dataType = "String"),
			@ApiImplicitParam(paramType = "path", name = "raceId", value = "抢答ID", required = true, dataType = "String") })
	@PostMapping("/pubRaceQuestion/{courseId}/{raceId}")
	public ResponseModel<Object> pubRaceQuestion(@PathVariable String courseId, @PathVariable String raceId,
			@ModelAttribute @ApiIgnore UserInfoForToken userInfo) {

		classInteractionForTeacherService.pubRaceQuestion(courseId, raceId);
		return ResponseModel.sucessWithEmptyData("");
	}
}
