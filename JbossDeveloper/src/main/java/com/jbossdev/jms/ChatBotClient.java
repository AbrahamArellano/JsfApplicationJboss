package com.jbossdev.jms;

import java.io.IOException;
import java.io.PrintWriter;

import javax.annotation.Resource;
import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.JMSException;
import javax.jms.MessageProducer;
import javax.jms.Queue;
import javax.jms.Session;
import javax.jms.TextMessage;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Servlet implementation class ChatBotClient
 */
@WebServlet("/ChatBotClient")
public class ChatBotClient extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
	private static final int MSG_COUNT = 7;
	
	@Resource(mappedName = "java:/ConnectionFactory")
	private ConnectionFactory connectionFactory;
	
	@Resource(mappedName = "java:/jms/queue/GameChatBot")
	private Queue queue;
	
    /**
     * @see HttpServlet#HttpServlet()
     */
    public ChatBotClient() {
        super();
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		resp.setContentType("text/html");
		PrintWriter out = resp.getWriter();
		Connection connection = null;
		out.write("<h1>WELCOME TO THE GAME CHATBOT!</h1>");
		try {
			connection = connectionFactory.createConnection();
			Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
			MessageProducer messageProducer = session.createProducer(queue);
			connection.start();
			out.write("<h2>Following messages will be sent to the chatBot: </h2>");
			TextMessage message = session.createTextMessage();
			for (int i = 0; i < MSG_COUNT; i++) {
				message.setText("Hi! my name is " + i);
				messageProducer.send(message);
				out.write("Message (" + i + "): " + message.getText() + "</br>");
			}
			out.write("</br>");
			out.write("<p><i>ChatBot should answer your message!</i></p>");
		} catch (JMSException e) {
			e.printStackTrace();
			out.write("An error occur with the ChatBot. Please check configuration!");
		} finally {
			if (connection != null) {
				try {
					connection.close();
				} catch (JMSException e1) {
					e1.printStackTrace();
				}
			}
			if (out != null) {
				out.close();
			}
		}
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doGet(request, response);
	}

}
