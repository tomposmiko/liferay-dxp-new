/**
 * Copyright (c) 2000-present Liferay, Inc. All rights reserved.
 *
 * This library is free software; you can redistribute it and/or modify it under
 * the terms of the GNU Lesser General Public License as published by the Free
 * Software Foundation; either version 2.1 of the License, or (at your option)
 * any later version.
 *
 * This library is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public License for more
 * details.
 */

package com.liferay.jethr0.jms;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;

/**
 * @author Michael Hashimoto
 */
@Configuration("jmsFactory")
public class JMSFactory {

	public JMSQueue getJMSQueue(String queueName) {
		JMSQueue jmsQueue = _jmsQueues.get(queueName);

		if (jmsQueue == null) {
			jmsQueue = new JMSQueue(_jmsConnection.getConnection(), queueName);

			_jmsQueues.put(queueName, jmsQueue);
		}

		return jmsQueue;
	}

	public JMSTopic getJMSTopic(String topicName) {
		JMSTopic jmsTopic = _jmsTopics.get(topicName);

		if (jmsTopic == null) {
			jmsTopic = new JMSTopic(_jmsConnection.getConnection(), topicName);

			_jmsTopics.put(topicName, jmsTopic);
		}

		return jmsTopic;
	}

	@Autowired
	private JMSConnection _jmsConnection;

	private final Map<String, JMSQueue> _jmsQueues = new HashMap<>();
	private final Map<String, JMSTopic> _jmsTopics = new HashMap<>();

}