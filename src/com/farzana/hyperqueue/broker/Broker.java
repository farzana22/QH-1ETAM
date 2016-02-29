/**
 * 
 */
package com.farzana.hyperqueue.broker;

import java.util.HashMap;
import java.util.Iterator;
import java.util.concurrent.CopyOnWriteArrayList;

import javax.servlet.http.Cookie;

/**
 * Topic (queue) blueprint
 * 
 * @author farza
 *
 */
public class Broker {

	private final static Object lock = new Object();

	private final static HashMap<Topic, HashMap<ConsumerSession, Integer>> topicSessions = new HashMap<Topic, HashMap<ConsumerSession, Integer>>();

	/**
	 * Concurrent list of topics
	 */
	private final static CopyOnWriteArrayList<Topic> topics = new CopyOnWriteArrayList<Topic>();

	/**
	 * This will return an existing or a new Topic (queue) based on internal
	 * checking
	 * 
	 * @param topicName
	 * @return the Topic object
	 */
	public static Topic getTopic(String topicName) {
		if (topics.size() < 1) {
			Topic topic = new Topic(topicName);
			topics.add(topic);
			synchronized (lock) {
				topicSessions.put(topic, new HashMap<ConsumerSession, Integer>());
			}
			return topic;
		}

		Iterator<Topic> topicIterator = topics.iterator();
		while (topicIterator.hasNext()) {
			Topic topic = topicIterator.next();
			if (topic.getTopicName().equals(topicName)) {
				return topic;
			} // end if
		} // end while

		Topic topic = new Topic(topicName);
		topics.add(topic);
		synchronized (lock) {
			topicSessions.put(topic, new HashMap<ConsumerSession, Integer>());
		}
		return topic;

	} // end getTopic()

	public static String[] getNextEvent(Cookie cookie, String topicName) {
		int offset = 0;
		int SID = new Integer(cookie.getValue());
		HashMap<ConsumerSession, Integer> consumerSessions;
		synchronized (lock) {
			consumerSessions = topicSessions.get(getTopic(topicName));
		}

		ConsumerSession session = new ConsumerSession();

		synchronized (lock) {
			Iterator<ConsumerSession> iterator = consumerSessions.keySet().iterator();
			while (iterator.hasNext()) {
				session = iterator.next();
				if (session.getSID() == SID) {
					break;
				} else {
					consumerSessions.put(session, offset);
					cookie.setValue(Integer.toString(session.getSID()));
				} // end else
			} // end while
			Integer tmpOffset = consumerSessions.get(session);
			if (tmpOffset == null) {
				tmpOffset = new Integer(offset);
			}
			offset = tmpOffset;
		}

		// Get topic from topic name
		Topic topic = getTopic(topicName);

		String[] event = topic.getNextEvent(offset);
		if (event[0] != null || event[1] != null) {
			synchronized (lock) {
				consumerSessions.put(session, ++offset);
			}
		}

		return event;

	}

}
