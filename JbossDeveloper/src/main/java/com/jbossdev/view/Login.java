package com.jbossdev.view;

import javax.enterprise.context.SessionScoped;
import javax.enterprise.inject.Produces;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.inject.Named;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;

import com.jbossdev.beans.Credentials;

import java.io.Serializable;

@Named
@SessionScoped
public class Login implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Produces
	@Named
	private Credentials credentials = new Credentials();
	
	/**
	 * Login method.
	 */
	public String login() {
		if (credentials.getName() == null || credentials.getPassword() == null || credentials.getName().isEmpty() || credentials.getPassword().isEmpty()) {
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Incorrect user or password"));
			return "login";
		}
		HttpServletRequest request = (HttpServletRequest)FacesContext.getCurrentInstance().getExternalContext().getRequest();
		try {
			request.login(credentials.getName(), credentials.getPassword());
		} catch (ServletException e) {
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Invalid user or password"));
			return "login";
		}
		return "secure/data?faces-redirect=true";
	}
	
	public String logout() {
		if (credentials != null) {
			HttpServletRequest request = (HttpServletRequest)FacesContext.getCurrentInstance().getExternalContext().getRequest();
			try {
				request.logout();
				request.getSession().invalidate();
				credentials = null;
				return "/login";
			} catch (ServletException e) {
				FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Error during logout!!!"));
			}
		}
		return "";
	}
	
	
}
