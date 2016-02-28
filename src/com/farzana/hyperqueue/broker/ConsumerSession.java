/**
 * 
 */
package com.farzana.hyperqueue.broker;

import java.util.HashSet;
import java.util.Iterator;
import java.util.UUID;

/**
 * @author farza
 *
 */
public class ConsumerSession {

	private final UUID SID;

	private final static Object lock = new Object();

	private final static String sessionName = "ConsumerSID";

	private final static HashSet<ConsumerSession> consumerSessions = new HashSet<ConsumerSession>();

	private ConsumerSession() {
		SID = UUID.randomUUID();
	}

	public static ConsumerSession getConsumerSession(int SID) {

		ConsumerSession session;

		synchronized (lock) {
			Iterator<ConsumerSession> iterator = consumerSessions.iterator();
			while (iterator.hasNext()) {
				session = iterator.next();
				if (session.getSID() == SID) {
					return session;
				} // end if
			} // end while
		}

		session = new ConsumerSession();
		synchronized (lock) {
			consumerSessions.add(session);
		}

		return session;

	}

	@Override
	public boolean equals(Object obj) {
		if (!(obj instanceof ConsumerSession)) {
			return false;
		}

		ConsumerSession session = (ConsumerSession) obj;

		if (this.getSID() == session.getSID()) {
			return true;
		}

		return false;
	}

	/**
	 * @return the sessionID
	 */
	public int getSID() {
		return SID.hashCode();
	}

	/**
	 * @return the sessionName
	 */
	public static String getSessionName() {
		return sessionName;
	}
}
