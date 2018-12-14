/**
 * 
 */
package com.jichuangsi.microservice.common.cache;

/**
 * @author huangjiajjun
 * 
 */
public interface ICacheService {
	
	boolean set(String key, Object value, long time);

	<T> T get(String key);

	long getLong(String key);

	boolean setValidity(String key, long time);
	
	CaceheItem gets(String key);
	
	boolean cas(String key, Object value, long time, int version);
	
	void lock(String key);
	
	void releaseLock(String key);

	long incr(String key);
}
