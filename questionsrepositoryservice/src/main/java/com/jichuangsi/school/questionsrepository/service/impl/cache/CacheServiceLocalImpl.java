/**
 * 
 */
package com.jichuangsi.school.questionsrepository.service.impl.cache;

import com.jichuangsi.microservice.common.cache.CaceheItem;
import com.jichuangsi.microservice.common.cache.ICacheService;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.Random;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * @author huangjiajun
 *
 */
@Service
public class CacheServiceLocalImpl implements ICacheService {

	private final Map<String, Object> map = new ConcurrentHashMap<String, Object>(1000);
	private final String timeLast = "_:val_time";
	private long clearKeyCount = 2000;
	ReadWriteLock lock = new ReentrantReadWriteLock();

	public CacheServiceLocalImpl() {	
	}

	public boolean set(String key, Object value, long time) {
		try {
			if (null != value) {
				lock.writeLock().lock();
				clear();
				map.put(key, value);
				map.put(key + timeLast, System.currentTimeMillis() + time);
				return true;
			}
			return false;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}finally{
			lock.writeLock().unlock();
		}
	}

	@SuppressWarnings("unchecked")
	public <T> T get(String key) {
		if (null != key) {
			try {
				lock.readLock().lock();
				Object o =  map.get(key + timeLast);
				if (null!=o && System.currentTimeMillis() > (Long)o) {
					map.remove(key);
					map.remove(key + timeLast);
					return null;
				} else {
					return (T) map.get(key);
				}
			} catch (Exception e) {				
				e.printStackTrace();
				return null;
			}finally{
				lock.readLock().unlock();
			}
		} else {
			return null;
		}
	}

	public long getLong(String key) {
		// TODO Auto-generated method stub
		return 0;
	}

	public boolean setValidity(String key, long time) {
		// TODO Auto-generated method stub
		return false;
	}

	public CaceheItem gets(String key) {
		// TODO Auto-generated method stub
		return null;
	}

	public boolean cas(String key, Object value, long time, int version) {
		// TODO Auto-generated method stub
		return false;
	}

	public void lock(String key) {
		// TODO Auto-generated method stub

	}

	public void releaseLock(String key) {
		// TODO Auto-generated method stub

	}

	public long incr(String key) {
		// TODO Auto-generated method stub
		return 0;
	}

	private void clear() {
		if (map.size() > clearKeyCount) {
			Set<Map.Entry<String, Object>> entrySet = map.entrySet();
			Object tempObj;
			for (Map.Entry<String, Object> entity : entrySet) {
				tempObj = entity.getValue();
				if (tempObj instanceof Long) {
					if (System.currentTimeMillis() > (Long) tempObj) {
						map.remove(entity.getKey());
						map.remove(entity.getKey().replaceAll(timeLast, ""));
					}
				}
			}
		}
	}

	public final long getClearKeyCount() {
		return clearKeyCount;
	}

	public final void setClearKeyCount(long clearKeyCount) {
		this.clearKeyCount = clearKeyCount;
	}

}
