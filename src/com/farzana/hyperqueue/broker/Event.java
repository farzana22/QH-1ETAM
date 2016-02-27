/**
 * 
 */
package com.farzana.hyperqueue.broker;

/**
 * @author farza
 *
 */
public class Event {

	private String fieldName;
	private String value;

	public Event(String name, String value) {
		this.fieldName = name;
		this.value = value;
	}

	/**
	 * @return the value
	 */
	public String getValue() {
		return value;
	}

	/**
	 * @return the name
	 */
	public String getName() {
		return fieldName;
	}

}
