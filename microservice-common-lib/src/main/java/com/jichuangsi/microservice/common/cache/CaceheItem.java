/**
 * 缓存数据包装类
 */
package com.jichuangsi.microservice.common.cache;

/**
 * @author huangjiajjun
 * 
 */
public class CaceheItem {

	private int version;// 版本号
	private final Object value;// 值
	private long invalidityTimeMills;// 过期时间

	public CaceheItem(int version, Object value, long invalidityTimeMills) {
		this.version = version;
		this.value = value;
		this.invalidityTimeMills = invalidityTimeMills;
	}

	public final int getVersion() {
		return version;
	}

	protected final void setVersion(int version) {
		this.version = version;
	}

	public final Object getValue() {
		return value;
	}

	public final long getInvalidityTimeMills() {
		return invalidityTimeMills;
	}

	protected final void setInvalidityTimeMills(long invalidityTimeMills) {
		this.invalidityTimeMills = invalidityTimeMills;
	}

}
