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

/**
 * @author huangjiajun
 *
 */
@RestController
public class ClassInteractionForStudentWithoutWsController {

	@Resource
	private IClassInteractionForStudentService classInteractionForStudentService;

	/**
	 * 加入课堂
	 */
	@PostMapping("/addToCourse/{courseId}")
	public ResponseModel<Object> addToCourse(@PathVariable String courseId,
			@ModelAttribute UserInfoForToken userInfo) {
		AddToCourseModel addToCourseModel = new AddToCourseModel();
		addToCourseModel.setCourseId(courseId);
		addToCourseModel.setUserId(userInfo.getUserId());
		addToCourseModel.setUserName(userInfo.getUserName());
		
		classInteractionForStudentService.addToCourse(addToCourseModel);
		return ResponseModel.sucessWithEmptyData("");
	}
}
