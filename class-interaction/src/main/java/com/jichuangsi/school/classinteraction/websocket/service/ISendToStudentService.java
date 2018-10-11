/**
 * 推送ws消息到学生端的服务接口
 */
package com.jichuangsi.school.classinteraction.websocket.service;

import com.jichuangsi.school.classinteraction.websocket.model.ClassInfoForStudent;

/**
 * @author huangjiajun
 *
 */
public interface ISendToStudentService {
	
	/**
	 * 推送班级信息
	 */
	void sendClassInfo(ClassInfoForStudent classInfoForStudent);
}
