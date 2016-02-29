/**
 * 
 */
package com.farzana.hyperqueue.broker;

import java.util.UUID;

/**
 * @author farza
 *
 */
public class ConsumerSession {

	private final UUID SID;

	private final static String sessionName = "ConsumerSID";

	public ConsumerSession() {
		SID = UUID.randomUUID();
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
