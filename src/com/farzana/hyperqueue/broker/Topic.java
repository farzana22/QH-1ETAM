/**
 * 
 */
package com.farzana.hyperqueue.broker;

import java.util.HashMap;
import java.util.Iterator;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.CopyOnWriteArrayList;

import javax.servlet.http.Cookie;

/**
 * @author farza
 *
 */
public class Topic {

	private final String topicName;

	private final static Object lock = new Object();

	private final HashMap<ConsumerSession, Integer> consumerSessions = new HashMap<ConsumerSession, Integer>();

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
	public String[] getNextEvent(Cookie cookie) {

		int offset = 0;
		ConsumerSession session = ConsumerSession.getConsumerSession(new Integer(cookie.getValue()));
		cookie.setValue(Integer.toString(session.getSID()));

		synchronized (lock) {
			if (consumerSessions.containsKey(session)) {
				offset = consumerSessions.get(session);
			}
		}

		if (events.size() == 0 || offset > events.size() - 1) {
			return new String[2];
		}
		String[] eventStrings = new String[2];
		eventStrings[0] = events.get(offset).getKey();
		eventStrings[1] = events.get(offset).getValue();

		synchronized (lock) {
			consumerSessions.put(session, ++offset);
		}

		return eventStrings;

	}

	/**
	 * @return the topicName
	 */
	public String getTopicName() {
		return topicName;
	}

}
