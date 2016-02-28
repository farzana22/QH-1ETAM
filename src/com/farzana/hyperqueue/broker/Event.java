/**
 * 
 */
package com.farzana.hyperqueue.broker;

/**
 * @author farza
 *
 */
public class Event {

	private String key;
	private String value;

	public Event(String name, String value) {
		this.key = name;
		this.value = value;
	}

	/**
	 * @return the value
	 */
	public String getValue() {
		return value;
	}

	/**
	 * @return the key
	 */
	public String getKey() {
		return key;
	}

}
