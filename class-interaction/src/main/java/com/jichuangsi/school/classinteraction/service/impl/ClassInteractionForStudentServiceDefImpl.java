/**
 * 
 */
package com.jichuangsi.school.classinteraction.service.impl;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.jichuangsi.school.classinteraction.model.AddToCourseModel;
import com.jichuangsi.school.classinteraction.mq.producer.service.ISendService;
import com.jichuangsi.school.classinteraction.service.IClassInteractionForStudentService;

/**
 * @author huangjiajun
 *
 */
@Service
public class ClassInteractionForStudentServiceDefImpl implements IClassInteractionForStudentService{

    @Resource
	private ISendService sendService;
	
	@Override
	public void addToCourse(AddToCourseModel addToCourseModel) {
		//todo 判断课堂状态，未开始的课不能加入
		sendService.snedAddToCourse(addToCourseModel);	
	}

}
