/**
 * 
 */
package com.farzana.hyperqueue.broker;

import java.util.Iterator;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * Topic (queue) blueprint
 * 
 * @author farza
 *
 */
public class Topic {

	/**
	 * Concurrent list of topics
	 */
	private final static CopyOnWriteArrayList<Topic> topics = new CopyOnWriteArrayList<Topic>();

	private final String topicName;

	/**
	 * This concurrent queue serves as a FIFO queue
	 */
	private final ConcurrentLinkedQueue<Event> fifoEvents = new ConcurrentLinkedQueue<Event>();

	/**
	 * The list of events in this Topic
	 */
	private final CopyOnWriteArrayList<Event> events = new CopyOnWriteArrayList<Event>();

	/*
	 * Private constructor to allow create only new Topic
	 */
	private Topic(String topicName) {
		this.topicName = topicName;
		topics.add(this);
	}

	/**
	 * This will return an existing or a new Topic (queue) based on internal
	 * checking
	 * 
	 * @param topicName
	 * @return the Topic object
	 */
	public static Topic getTopic(String topicName) {
		if (topics.size() < 1) {
			return new Topic(topicName);
		}

		Iterator<Topic> topicIterator = topics.iterator();
		while (topicIterator.hasNext()) {
			Topic topic = topicIterator.next();
			if (topic.topicName.equals(topicName)) {
				return topic;
			} // end if
		} // end while

		return new Topic(topicName);

	} // end getTopic()

	/**
	 * Adds events to the current Topic (queue)
	 * 
	 * @param fieldName
	 * @param value
	 */
	public void addEvent(String fieldName, String value) {
		if (events.size() != 0) {
			Iterator<Event> iter = events.iterator();
			while (iter.hasNext()) {
				if (iter.next().getValue().equalsIgnoreCase(value)) {
					return;
				} // end inner if
			} // end while 
		} // end outer if

		fifoEvents.add(new Event(fieldName, value));
		addToList(fifoEvents.poll());
	}

	/*
	 * adds the event to the Topic's events list
	 */
	private void addToList(Event poll) {
		events.add(poll);
	}

	public void printEvents() {
		Iterator<Event> eventIterator = events.iterator();
		while (eventIterator.hasNext()) {
			Event e = eventIterator.next();
			System.out.println(e.getName() + " " + e.getValue());

		}

	}
}
