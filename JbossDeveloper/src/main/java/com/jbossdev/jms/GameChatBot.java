package com.jbossdev.jms;

import java.util.logging.Logger;

import javax.ejb.ActivationConfigProperty;
import javax.ejb.MessageDriven;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.TextMessage;

/**
 * Message-Driven Bean implementation class for: GameChatBot
 */
@MessageDriven(name="GameChatBot",
		activationConfig = { @ActivationConfigProperty(
				propertyName = "destination", propertyValue = "java:/jms/queue/GameChatBot"),
				@ActivationConfigProperty(
						propertyName = "acknowledgeMode", propertyValue = "Auto_acknowledge"),
				@ActivationConfigProperty(
						propertyName = "destinationType", propertyValue = "javax.jms.Queue")
		})
public class GameChatBot implements MessageListener {

	private static final Logger logger = Logger.getLogger(GameChatBot.class.getName());
    /**
     * Default constructor. 
     */
    public GameChatBot() {
        // TODO Auto-generated constructor stub
    }
	
	/**
     * @see MessageListener#onMessage(Message)
     */
    public void onMessage(Message message) {
        if (message != null && message instanceof TextMessage) {
        	TextMessage text = (TextMessage)message;
        	try {
				logger.info("I received your message: " + text.getText());
			} catch (JMSException e) {
				e.printStackTrace();
				logger.info("Error with your message");
			}
        }
    }

}
