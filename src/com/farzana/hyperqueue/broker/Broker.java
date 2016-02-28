/**
 * 
 */
package com.farzana.hyperqueue.broker;

import java.util.Iterator;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * Topic (queue) blueprint
 * 
 * @author farza
 *
 */
public class Broker {

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
		return topic;

	} // end getTopic()

}
