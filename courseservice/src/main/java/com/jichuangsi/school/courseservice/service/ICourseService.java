/**
 * 
 */
package com.jichuangsi.school.courseservice.service;

import org.springframework.transaction.annotation.Transactional;

import com.jichuangsi.school.courseservice.model.message.CourseMessageModel;

/**
 * @author huangjiajun
 *
 */
public interface ICourseService {
	
	@Transactional
	void startCourse(CourseMessageModel courseInfoModel);
}
