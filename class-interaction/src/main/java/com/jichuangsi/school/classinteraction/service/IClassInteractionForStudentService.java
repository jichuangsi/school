/**
 * 课堂互动非WS交互接口
 */
package com.jichuangsi.school.classinteraction.service;
import com.jichuangsi.school.classinteraction.model.AddToCourseModel;

/**
 * @author huangjiajun
 *
 */
public interface IClassInteractionForStudentService {
	/**
	 * 学生加入课堂
	 */
	void addToCourse(AddToCourseModel addToCourseModel);

	/**
	 * 学生抢答
	 */
	void raceAnswer(String courseId,String raceId,String studentId);
}
