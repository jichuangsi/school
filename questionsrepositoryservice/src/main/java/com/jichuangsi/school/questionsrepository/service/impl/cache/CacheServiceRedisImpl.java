/**
 * 
 */
package com.jichuangsi.school.questionsrepository.service.impl.cache;

import com.jichuangsi.microservice.common.cache.CaceheItem;
import com.jichuangsi.microservice.common.cache.ICacheService;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.util.SerializationUtils;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.exceptions.JedisConnectionException;
import redis.clients.jedis.exceptions.JedisDataException;
import redis.clients.jedis.exceptions.JedisException;

import java.util.Random;

/**
 * @author huangjiajjun
 * 
 */
public class CacheServiceRedisImpl implements ICacheService {

	private Log logger = LogFactory.getLog(CacheServiceRedisImpl.class);
	private JedisPool jedisPool;

	private long lockMaxTime = 120 * 1000L;// 默认锁定最长时间(ms)，可注入修改
	private int waitLockTimeMax = 3 * 1000;// 默认获取不到锁每次重试等待最长时间(ms)，可注入修改
	private int waitLockTimeMin = 1 * 1000;// 默认获取不到锁每次重试等待最短时间(ms)，可注入修改
	private long getLockTryMaxTime = 120 * 1000L;// 默认最大尝试时间(ms)，可注入修改
	private long lockOffset = 50L;// releaseLock时get和del间需要时间，若这段时间内锁超时了，锁被其他请求获得，而这里又刚好del了，会使其他请求重复获得锁，为减少此缺陷的影响，判断时应加上一个提前量(ms)

	public boolean set(String key, Object value, long time) {
		Jedis jedis = null;
		boolean broken = false;
		try {
			jedis = safeGetResource();

			if (null == value) {
				jedis.del(key.getBytes());
			} else {
				jedis.setex(key.getBytes(), new Long(time / 1000).intValue(), SerializationUtils.serialize(value));
			}
			return true;
		} catch (JedisException e) {
			broken = handleJedisException(e);
			return false;
		} finally {
			if (null != jedis) {
				closeResource(jedis, broken);
			}
		}
	}

	public long incr(String key) {
		Jedis jedis = null;
		boolean broken = false;
		try {
			jedis = safeGetResource();
			if (key != null) {
				return jedis.incr(key.getBytes());
			}else{
				return 0;
			}
		} catch (JedisException e) {
			broken = handleJedisException(e);
			return 0;
		} finally {
			if (null != jedis) {
				closeResource(jedis, broken);
			}
		}
	}

	@SuppressWarnings("unchecked")
	public <T> T get(String key) {
		Jedis jedis = null;
		boolean broken = false;
		try {
			jedis = safeGetResource();
			byte[] data = jedis.get(key.getBytes());
			if (null == data) {
				return null;
			} else {
				return (T) SerializationUtils.deserialize(data);
			}
		} catch (JedisException e) {
			broken = handleJedisException(e);
			return null;
		} finally {
			if (null != jedis) {
				closeResource(jedis, broken);
			}
		}

	}

	@SuppressWarnings("unchecked")
	public long getLong(String key) {
		Jedis jedis = null;
		boolean broken = false;
		try {
			jedis = safeGetResource();
			byte[] data = jedis.get(key.getBytes());
			if (data == null) {
				return 0;
			} else {
				return Long.valueOf(new String(data));
			}
		} catch (JedisException e) {
			broken = handleJedisException(e);
			return 0;
		} finally {
			if (null != jedis) {
				closeResource(jedis, broken);
			}
		}

	}

	public boolean setValidity(String key, long time) {
		Jedis jedis = null;
		boolean broken = false;
		try {
			jedis = safeGetResource();
			if (0L < time) {
				jedis.expire(key.getBytes(), new Long(time / 1000).intValue());
			} else {
				jedis.del(key.getBytes());
			}
			return true;
		} catch (JedisException e) {
			broken = handleJedisException(e);
			return false;
		} finally {
			if (null != jedis) {
				closeResource(jedis, broken);
			}
		}
	}

	public CaceheItem gets(String key) {
		return null;
	}

	public boolean cas(String key, Object value, long time, int version) {
		return false;
	}

	public final JedisPool getJedisPool() {
		return jedisPool;
	}

	public final void setJedisPool(JedisPool jedisPool) {
		this.jedisPool = jedisPool;
	}

	public void lock(String key) {
		Jedis jedis = null;
		boolean broken = false;
		try {
			jedis = safeGetResource();
			long startTime = System.currentTimeMillis();
			long getLockflag;
			long curTime;
			Long newExpireTime;
			while (true) {
				curTime = System.currentTimeMillis();
				if (curTime - startTime > getLockTryMaxTime) {// 超时跳出循环
					break;
				}
				newExpireTime = curTime + lockMaxTime;
				getLockflag = jedis.setnx(key, Long.toString(newExpireTime));
				if (1L == getLockflag) {
					return;// 获得了锁
				} else {
					String oldExpireTimeStr = jedis.get(key);
					if (null == oldExpireTimeStr) {
						// 走到这里发现已经被解锁了
						getLockflag = jedis.setnx(key, Long.toString(newExpireTime));
						if (1L == getLockflag) {
							return;// 获得了锁
						}
					} else {
						if (curTime > new Long(oldExpireTimeStr).longValue()) {// tag_lock_timeout:这个锁已经超时，可以允许别的请求重新获取
							String currentExpireTimeStr = jedis.getSet(key, Long.toString(newExpireTime));
							if (null != currentExpireTimeStr) {
								if (oldExpireTimeStr.equals(currentExpireTimeStr)) {// 表明没有其他人获得锁
									return;// 获得了锁
								}
							} else {
								// 走到这里发现已经被解锁了
								getLockflag = jedis.setnx(key, Long.toString(newExpireTime));
								if (1L == getLockflag) {
									return;// 获得了锁
								}
							}
						}
					}
				}

				try {
					Thread.sleep(new Random().nextInt(waitLockTimeMax) % (waitLockTimeMax - waitLockTimeMin + 1)
							+ waitLockTimeMin);// 在范围中随机取等待继续等待锁
					continue;
				} catch (InterruptedException e) {
					e.printStackTrace();
				}

			}

			// 跳出循环表明获得锁的尝试超时
			throw new RuntimeException("get lock timeout.");

		} catch (JedisException e) {
			broken = handleJedisException(e);
		} finally {
			if (null != jedis) {
				closeResource(jedis, broken);
			}
		}
	}

	public void releaseLock(String key) {
		Jedis jedis = null;
		boolean broken = false;
		try {
			jedis = safeGetResource();
			Long curTime = System.currentTimeMillis() + lockOffset;
			String oldExpireTimeStr = jedis.get(key);
			if (curTime <= new Long(oldExpireTimeStr).longValue()) {
				// 没超时才删，超时了什么也不做，如果超时了也删的话，会影响注释中“tag_lock_timeout”下面的逻辑
				// （还是有点缺陷，get和del间需要时间，若这段时间内锁超时了，锁被其他请求获得，而这里又刚好del了，会使其他请求重复获得锁，为减少此缺陷的影响，判断时应加上一个约?ms的提前量）
				jedis.del(key);
			}

		} finally {
			if (null != jedis) {
				closeResource(jedis, broken);
			}
		}

	}

	/**
	 * Handle jedisException, write log and return whether the connection is
	 * broken.
	 */
	protected boolean handleJedisException(JedisException jedisException) {
		if (jedisException instanceof JedisConnectionException) {
			logger.error("Redis connection lost.", jedisException);
		} else if (jedisException instanceof JedisDataException) {
			if ((jedisException.getMessage() != null) && (jedisException.getMessage().indexOf("READONLY") != -1)) {
				logger.error("Redis connection are read-only slave.", jedisException);
			} else {
				// dataException, isBroken=false
				return false;
			}
		} else {
			logger.error("Jedis exception happen.", jedisException);
		}
		return true;
	}

	protected void closeResource(Jedis jedis, boolean conectionBroken) {
		try {
			jedis.close();
			/*if (conectionBroken) { //弃用
				jedisPool.returnBrokenResource(jedis);
			} else {
				jedisPool.returnResource(jedis);
			}*/
		} catch (Exception e) {
			logger.error("return back jedis failed, will fore close the jedis.", e);
			jedis.close();
		}
	}
	
	private synchronized Jedis safeGetResource() {
		return jedisPool.getResource();
	}

	public final long getLockMaxTime() {
		return lockMaxTime;
	}

	public final void setLockMaxTime(long lockMaxTime) {
		this.lockMaxTime = lockMaxTime;
	}

	public final long getGetLockTryMaxTime() {
		return getLockTryMaxTime;
	}

	public final void setGetLockTryMaxTime(long getLockTryMaxTime) {
		this.getLockTryMaxTime = getLockTryMaxTime;
	}

	public final long getLockOffset() {
		return lockOffset;
	}

	public final void setLockOffset(long lockOffset) {
		this.lockOffset = lockOffset;
	}

	public final int getWaitLockTimeMax() {
		return waitLockTimeMax;
	}

	public final void setWaitLockTimeMax(int waitLockTimeMax) {
		this.waitLockTimeMax = waitLockTimeMax;
	}

	public final int getWaitLockTimeMin() {
		return waitLockTimeMin;
	}

	public final void setWaitLockTimeMin(int waitLockTimeMin) {
		this.waitLockTimeMin = waitLockTimeMin;
	}

}
