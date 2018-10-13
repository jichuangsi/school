/**
 * 
 */
package com.jichuangsi.school.courseservice.service;

/**
 * @author huangjiajun
 *
 */
public interface ITokenService {
	/**
	 * 根据用户ID获取tooken
	 */
	String getToken(String userId);
	
	/**
	 * 校验token
	 */
	boolean checkToken(String token);
}
