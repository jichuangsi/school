/**
 * 提供给课堂互动的controller
 */
package com.jichuangsi.school.courseservice.controller;

import javax.annotation.Resource;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.jichuangsi.microservice.common.model.RequestModel;
import com.jichuangsi.microservice.common.model.ResponseModel;
import com.jichuangsi.school.courseservice.model.message.CourseMessageModel;
import com.jichuangsi.school.courseservice.service.ICourseService;

/**
 * @author huangjiajun
 *
 */
@RestController
public class ClassInteractionController {
	
	@Resource
	private ICourseService courseService; 

	@PostMapping("/courseStart")
	public ResponseModel<Object> courseStart(@RequestBody RequestModel<CourseMessageModel> request) {
		courseService.startCourse(request.getRequestParams());
		return ResponseModel.sucess("", null);
	}
}
