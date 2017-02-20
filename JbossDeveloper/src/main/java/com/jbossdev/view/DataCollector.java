package com.jbossdev.view;

import java.io.Serializable;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Map.Entry;
import java.util.logging.Logger;

import javax.enterprise.context.Conversation;
import javax.enterprise.context.ConversationScoped;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.event.ValueChangeEvent;
import javax.inject.Inject;
import javax.inject.Named;

@Named("collector")
@ConversationScoped
public class DataCollector implements Serializable {
	
	private Logger logger = Logger.getLogger("DataCollector");

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private Map<String, Locale> map;
	
	private String currentLocale;
	
	@javax.validation.constraints.Future
	private Date currentDate;
	
	@Inject
	private Conversation conversation;
	
	
	public DataCollector() {
		map = new HashMap<>();
		map.put("English", Locale.ENGLISH);
		map.put("French", Locale.FRENCH);
	}
	
	/**
	 * Update locale.
	 */
	public void updateLocale(ValueChangeEvent event) {
		if (conversation.isTransient()) {
			conversation.begin();
		}
		String currentLocale = (String)event.getNewValue();
		if (currentLocale == null) {
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Error in Locale selection"));
			return;
		}
		logger.info("Calculating the locale!");
		for (Entry<String, Locale> entry : map.entrySet()) {
			if (entry.getValue().toString().equals(currentLocale)) {
				FacesContext.getCurrentInstance().getViewRoot().setLocale(entry.getValue());
				setCurrentLocale(currentLocale);
			}
		}
		
	}
	
	public String toGame() {
		logger.info("TO GAME!");
		conversation.end();
		return "game?faces-redirect=true";
	}
	
	public String toConfiguration() {
		logger.info("To Config!");
		conversation.end();
		return "/configuration/configuration?faces-redirect=true";
	}

	public Map<String, Locale> getMap() {
		return map;
	}
	
	public void setMap(Map<String, Locale> map) {
		this.map = map;
	}

	public String getCurrentLocale() {
		return currentLocale;
	}

	public void setCurrentLocale(String currentLocale) {
		this.currentLocale = currentLocale;
	}

	public Date getCurrentDate() {
		return currentDate;
	}

	public void setCurrentDate(Date currentDate) {
		this.currentDate = currentDate;
	}
	
	
}
