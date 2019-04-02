/**
 * 发送服务接口
 */
package com.jichuangsi.school.classinteraction.mq.producer.service;

import com.jichuangsi.school.classinteraction.model.AddToCourseModel;

/**
 * @author huangjiajun
 *
 */
public interface ISendService {
	
	/**
	 * 发送上课消息
	 */
	void snedAddToCourse(AddToCourseModel addToCourseModel);


}
