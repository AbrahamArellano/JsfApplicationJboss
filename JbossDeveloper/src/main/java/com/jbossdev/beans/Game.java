package com.jbossdev.beans;

import java.io.Serializable;
import java.util.List;
import java.util.logging.Logger;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class Game implements Serializable, IGame {
	
	

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private Logger logger = Logger.getLogger("GAME original");
	
	private String name;
	
	private List<String> rules;
	
	public Game() {
		
	}
	
	public Game(String name, List<String> rules) {
		this.name = name;
		this.rules = rules;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public List<String> getRules() {
		return rules;
	}

	public void setRules(List<String> rules) {
		this.rules = rules;
	}
	
	public void generateWordToFind() {
		logger.info("TODO - generating words");
	}
	

	public boolean lookUpWord(String findWord) {
		logger.info("TODO - looking up words");
		return false;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Game other = (Game) obj;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "Game [name=" + name + ", rules=" + rules + "]";
	}

	public boolean isFound() {
		logger.info("TODO - founding words");
		return false;
	}

	@Override
	public void addGivenWordToList(String word) {
		// TODO Auto-generated method stub
		
	}

	
	
}
