/**
 * 
 */
package com.farzana.hyperqueue.broker;

import java.util.Iterator;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * @author farza
 *
 */
public class Topic {

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
	Topic(String topicName) {
		this.topicName = topicName;

	}

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

	/**
	 * @param cookie
	 * @return
	 */
	public String[] getNextEvent(int offset) {

		if (events.size() == 0 || offset > events.size() - 1) {
			return new String[2];
		}
		String[] eventStrings = new String[2];
		eventStrings[0] = events.get(offset).getKey();
		eventStrings[1] = events.get(offset).getValue();

		return eventStrings;

	}

	@Override
	public boolean equals(Object obj) {
		if (!(obj instanceof Topic)) {
			return false;
		}

		Topic topic = (Topic) obj;

		if (this.getTopicName().equalsIgnoreCase(topic.getTopicName())) {
			return true;
		}

		return false;
	}

	/**
	 * @return the topicName
	 */
	public String getTopicName() {
		return topicName;
	}

}
