/**
 * 
 */
package com.jichuangsi.school.classinteraction.mq.consumer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author huangjiajun
 *
 */
public abstract class AbstractReceiver {
	public abstract void process(String jsonData);

	private Logger logger = LoggerFactory.getLogger(getClass());

	public void processWithLog(String jsonData) {
		logger.debug("Receive " + getClass() + " messgae:" + jsonData);
		process(jsonData);
		logger.debug(getClass() + " messgae procss sucess.");
	}
}
