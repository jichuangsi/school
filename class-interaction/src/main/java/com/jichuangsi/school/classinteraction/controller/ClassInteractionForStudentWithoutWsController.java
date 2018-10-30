/**
 * 课堂互动非WS接口
 */
package com.jichuangsi.school.classinteraction.controller;

import javax.annotation.Resource;

import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import com.jichuangsi.microservice.common.model.ResponseModel;
import com.jichuangsi.microservice.common.model.UserInfoForToken;
import com.jichuangsi.school.classinteraction.model.AddToCourseModel;
import com.jichuangsi.school.classinteraction.service.IClassInteractionForStudentService;

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
@Api("ClassInteractionForStudentWithoutWsController相关的api")
public class ClassInteractionForStudentWithoutWsController {

	@Resource
	private IClassInteractionForStudentService classInteractionForStudentService;

	@ApiOperation(value = "学生加入课堂", notes = "")
	@ApiImplicitParams({
			@ApiImplicitParam(paramType = "header", name = "accessToken", value = "用户token", required = true, dataType = "String"),
			@ApiImplicitParam(paramType = "path", name = "courseId", value = "课堂ID", required = true, dataType = "String") })
	@PostMapping("/addToCourse/{courseId}")
	public ResponseModel<Object> addToCourse(@PathVariable String courseId,
			@ModelAttribute @ApiIgnore UserInfoForToken userInfo) {
		AddToCourseModel addToCourseModel = new AddToCourseModel();
		addToCourseModel.setCourseId(courseId);
		addToCourseModel.setUserId(userInfo.getUserId());
		addToCourseModel.setUserName(userInfo.getUserName());
		
		classInteractionForStudentService.addToCourse(addToCourseModel);
		return ResponseModel.sucessWithEmptyData("");
	}
}
