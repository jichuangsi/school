/**
 * 
 */
package com.jichuangsi.school.statistics.controller;

import java.util.Date;

import javax.annotation.Resource;

import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.jichuangsi.microservice.common.model.ResponseModel;
import com.jichuangsi.microservice.common.model.UserInfoForToken;
import com.jichuangsi.school.statistics.entity.CourseStatistics;
import com.jichuangsi.school.statistics.model.CourseStatiResponseModel;
import com.jichuangsi.school.statistics.service.ICourseStatisticsService;

/**
 * @author huangjiajun
 *
 */
@RestController
public class ClassInteractionStatisticsController {

	@Resource
	private ICourseStatisticsService courseStatisticsServic;

	@PostMapping("/addToCourse/{courseId}")
	public ResponseModel<Object> addToCourse(@PathVariable String courseId,
			@ModelAttribute UserInfoForToken userInfo) {
		CourseStatistics entity = new CourseStatistics();
		entity.setCourseId(courseId);
		entity.setUserId(userInfo.getUserId());
		entity.setCreatedTime(new Date());
		courseStatisticsServic.addToCourse(entity);
		return ResponseModel.sucessWithEmptyData("");

	}
}
