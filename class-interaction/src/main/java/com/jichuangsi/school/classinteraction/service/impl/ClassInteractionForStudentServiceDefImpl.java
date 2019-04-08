/**
 * 
 */
package com.jichuangsi.school.classinteraction.service.impl;

import java.util.WeakHashMap;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.jichuangsi.school.classinteraction.model.AddToCourseModel;
import com.jichuangsi.school.classinteraction.model.StudentAnswerModel;
import com.jichuangsi.school.classinteraction.mq.producer.service.ISendService;
import com.jichuangsi.school.classinteraction.service.IClassInteractionForStudentService;
import com.jichuangsi.school.classinteraction.websocket.service.ISendToTeacherService;

/**
 * @author huangjiajun
 *
 */
@Service
public class ClassInteractionForStudentServiceDefImpl implements IClassInteractionForStudentService{

    @Resource
	private ISendService sendService;
    
    @Resource
    private ISendToTeacherService sendToTeacherService;
    
    private WeakHashMap<String, String> raceIdMap = new WeakHashMap<>();
	
	@Override
	public void addToCourse(AddToCourseModel addToCourseModel) {
		//todo 判断课堂状态，未开始的课不能加入
		sendService.snedAddToCourse(addToCourseModel);	
	}

	@Override
	public void raceAnswer(String courseId, String raceId,String studentId) {
		//每个抢答ID仅仅只有第一个学生作答时通知老师（这里仅处理单机，集群todo）
		synchronized(raceId.intern()) {
			if(!raceIdMap.containsKey(raceId)) {
				StudentAnswerModel studentAnswerModel = new StudentAnswerModel();
				studentAnswerModel.setCourseId(courseId);
				studentAnswerModel.setQuestionId(raceId);
				studentAnswerModel.setStudentId(studentId);
				studentAnswerModel.setQuType(StudentAnswerModel.QUTYPE_RACE);
				sendToTeacherService.sendRaceAnswerInfo(studentAnswerModel);
				raceIdMap.put(raceId, "");
			}
		}
		
		
		
		
	}

}
