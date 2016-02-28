package com.farzana.hyperqueue.servlet;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.UUID;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
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

	private boolean isPostResponse = false;

	/**
	 * Default constructor.
	 */
	public HyperQueueServlet() {
		// TODO Auto-generated constructor stub
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		if (isPostResponse) {
			response.getWriter().println("Event registered in the topic");
			return;
		}

		System.out.println(request.getIntHeader(ConsumerSession.getSessionName()));

		response.setIntHeader(ConsumerSession.getSessionName(), UUID.randomUUID().hashCode());

		String topicName = getTopicName(request);

		// Get topic from topic name
		Topic topic = Broker.getTopic(topicName);

		response.getWriter().append("Hello World\n");
		response.getWriter().println("Hello World");

		String key = topic.getNextEvent()[0];
		String value = topic.getNextEvent()[1];

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

		isPostResponse = true;

		String keyValuePair = getBody(request);
		System.out.println(keyValuePair);
		System.out.println(request.getRequestURI());

		String topicName = getTopicName(request);

		System.out.println(topicName);

		Topic topic = Broker.getTopic(topicName);

		String[] strings = keyValuePair.split("=");
		topic.addEvent(strings[0], strings[1]);

		doGet(request, response);

		isPostResponse = false;

	} // end doGet()

	/*
	 * This will get the topic name from the URI Format:
	 * http://localhost:8080/HyperQueue/HyperQueueServlet/<topic name>
	 */
	private String getTopicName(HttpServletRequest request) {

		String uri = request.getRequestURI();
		String[] strings = uri.split("/");
		String topicName = "";

		int stringPosition = 0;
		for (String tmpString : strings) {
			stringPosition++;
			if (tmpString.equalsIgnoreCase("HyperQueueServlet")) {
				topicName = strings[stringPosition];
				break;
			} // end if
		} // end for

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
		// TODO Auto-generated method stub
	}

}
