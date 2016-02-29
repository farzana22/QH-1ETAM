package com.farzana.hyperqueue.servlet;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Arrays;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.farzana.hyperqueue.broker.Broker;
import com.farzana.hyperqueue.broker.ConsumerSession;
import com.farzana.hyperqueue.broker.Topic;

/**
 * Servlet implementation class HyperQueueServlet
 */
@WebServlet("/HyperQueueServlet")
public class HyperQueueServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	private Cookie cookie;

	/**
	 * Default constructor.
	 */
	public HyperQueueServlet() {
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		String topicName = getTopicName(request);

		if (topicName.equals("")) {
			return;
		}

		boolean isConsumerSessionID = false;

		Cookie[] cookies = request.getCookies();
		for (Cookie tmpCookie : cookies) {
			cookie = tmpCookie;
			isConsumerSessionID = cookie.getName().equalsIgnoreCase(ConsumerSession.getSessionName());
			if (isConsumerSessionID)
				break;
		}
		if (request.getCookies() == null || request.getSession().isNew() || !isConsumerSessionID) {
			cookie = new Cookie(ConsumerSession.getSessionName(), Integer.toString(-1));
		} else {
		}

		String[] event = Broker.getNextEvent(cookie, topicName);

		response.addCookie(cookie);

		String key = event[0];
		String value = event[1];

		if (key == null || value == null) {
			response.getWriter().println("No new event to display");
			return;
		}

		response.getWriter().println("Key = " + key + " Value = " + value);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		String keyValuePair = getBody(request);

		String topicName = getTopicName(request);

		Topic topic = Broker.getTopic(topicName);

		String[] strings = keyValuePair.split("=");
		topic.addEvent(strings[0], strings[1]);

		// doGet(request, response);
		response.getWriter().println("Event registered to the topic");

	} // end doGet()

	/*
	 * This will get the topic name from the URI Format:
	 * http://localhost:8080/HyperQueue/HyperQueueServlet/<topic name>
	 */
	private String getTopicName(HttpServletRequest request) {

		String uri = request.getRequestURI().trim();
		String[] strings = uri.split("/");
		strings = Arrays.copyOfRange(strings, 1, strings.length);
		String topicName = "";

		int stringPosition = 0;
		for (String tmpString : strings) {
			stringPosition++;
			if (tmpString.equalsIgnoreCase("HyperQueueServlet") && stringPosition < strings.length) {
				topicName = strings[stringPosition];
				break;
			} // end if
		} // end for

		topicName = topicName.replaceFirst("producer_", "");

		return topicName;
	}

	/**
	 * Parse POST request body
	 * 
	 * @param request
	 * @return
	 * @throws IOException
	 */
	public static String getBody(HttpServletRequest request) throws IOException {

		String body = null;
		StringBuilder stringBuilder = new StringBuilder();
		BufferedReader bufferedReader = null;

		try {
			InputStream inputStream = request.getInputStream();
			if (inputStream != null) {
				bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
				char[] charBuffer = new char[128];
				int bytesRead = -1;
				while ((bytesRead = bufferedReader.read(charBuffer)) > 0) {
					stringBuilder.append(charBuffer, 0, bytesRead);
				}
			} else {
				stringBuilder.append("");
			}
		} catch (IOException ex) {
			throw ex;
		} finally {
			if (bufferedReader != null) {
				try {
					bufferedReader.close();
				} catch (IOException ex) {
					throw ex;
				}
			}
		}

		body = stringBuilder.toString();
		return body;
	}

	/**
	 * @see HttpServlet#doHead(HttpServletRequest, HttpServletResponse)
	 */
	protected void doHead(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
	}

}
