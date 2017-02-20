package com.jbossdev.beans;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.logging.Logger;

import javax.enterprise.inject.Alternative;
import javax.inject.Inject;

import com.jbossdev.interceptors.Logged;
import com.jbossdev.jpa.User;
import com.jbossdev.jpa.UserBean;

@Alternative
public class WordGuess extends Game implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private Logger logger = Logger.getLogger(WordGuess.class.getName());

	private String wordToFind;
	
	private String wordLeft;
	
	private List<String> givenWords;
	
	@Inject
	private UserBean bean;
	
	@Logged
	public void generateWordToFind() {
		logger.info("Geneating words");
		List<User> listUsers = bean.getListUsers();
		logger.info("List of users: " + listUsers);
		int pos = new Random(System.currentTimeMillis()).nextInt(listUsers.size());
		wordToFind = listUsers.get(pos).getName();
		wordLeft = wordToFind;
		givenWords = new ArrayList<>();
	}
	
	@Logged
	public boolean lookUpWord(String findWord) {
		if (findWord != null && !findWord.isEmpty() && wordLeft.contains(findWord)) {
			wordLeft = wordLeft.replaceAll(findWord, "");
			return true;
		}
		return false;
	}
	
	@Override
	public void addGivenWordToList(String word) {
		givenWords.add(word);
	}
	
	public boolean isFound() {
		return wordLeft.isEmpty();
	}

	public String getWordToFind() {
		return wordToFind;
	}

	public String getWordLeft() {
		return wordLeft;
	}

	public List<String> getGivenWords() {
		return givenWords;
	}
	
	
}
