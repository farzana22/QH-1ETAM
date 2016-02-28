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

	private final UUID sessionID;

	private final static String sessionName = "ConsumerID";

	/**
	 * @param sessionID
	 */
	public ConsumerSession() {
		super();
		sessionID = UUID.randomUUID();
	}

	/**
	 * @return the sessionID
	 */
	public int getSessionID() {
		return sessionID.hashCode();
	}

	/**
	 * @return the sessionName
	 */
	public static String getSessionName() {
		return sessionName;
	}
}
