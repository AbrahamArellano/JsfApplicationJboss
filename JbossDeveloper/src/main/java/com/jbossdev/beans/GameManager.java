package com.jbossdev.beans;

import java.io.Serializable;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.ejb.Stateful;
import javax.enterprise.context.Conversation;
import javax.enterprise.context.ConversationScoped;
import javax.enterprise.event.Event;
import javax.enterprise.event.Observes;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;

import com.jbossdev.event.Lookup;
import com.jbossdev.event.ReducePoints;
import com.jbossdev.event.TaskEvent;

@Named
@Stateful
@ConversationScoped
public class GameManager implements Serializable {
	
	private Logger logger = Logger.getLogger(GameManager.class.getName());
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private Map<String, Game> availableGames;
		
	@Inject
	private Game gameToPlay;
	
	@Inject
	private Conversation conversation;
	
	public GameManager() {
		
	}
	
	private String wordGuess;
	
	private boolean isPlaying;
	
	private int currentPoints;
	
	@Inject
	@Lookup
	private Event<TaskEvent> lookupEvent;
	
	@Inject
	@ReducePoints
	private Event<TaskEvent> reducePointsEvent;
	
	/**
	 * Init list of games.
	 */
	@PostConstruct
	public void init() {
		logger.info("Init gameManager");
		availableGames = new HashMap<>();
		availableGames.put("WordGuess", new Game("WordGuess", Collections.singletonList("rule1")));
		availableGames.put("CruzYRaya", new Game("CruzYRaya", Collections.singletonList("rule2")));
		availableGames.put("FindNumber", new Game("FindNumber", Collections.singletonList("rule3")));
		currentPoints = 10;
		isPlaying = false;
	}
	
	
	public void startGame() {
		if (conversation.isTransient()) {
			conversation.begin();
		}
		logger.info("Starting game");
		isPlaying = true;
		gameToPlay.generateWordToFind();
	}
	
	public String findWord() {
		logger.info("Finding words");
		
		lookupEvent.fire(new TaskEvent());
		
		if (gameToPlay.isFound()) {
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("You win."));
			stopGame();
		}
		if (currentPoints < 1) {
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("You lose."));
			stopGame();
		}
		return "game";
	}
	
	public void lookupWord(@Observes @Lookup TaskEvent event) {
		if (!gameToPlay.lookUpWord(wordGuess)) {
			reducePointsEvent.fire(new TaskEvent());
		}
	}
	
	public void reducePoints(@Observes @ReducePoints TaskEvent event) {
		currentPoints--;
	}
	
	public void stopGame() {
		logger.info("Stopping game");
		conversation.end();
		isPlaying = false;
		FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Game over."));
	}
	
	public String backToCollector() {
		stopGame();
		return "data";
	}
	

	public Collection<Game> getListAvailableGames() {
		return availableGames.values();
	}
	
	public Game findGameByName(String name) {
		return availableGames.get(name);
	}
	
	@PreDestroy
	public void destroy() {
		logger.info("Destroying game");
		availableGames.clear();
		availableGames = null;
	}

	public Game deleteGame(String name) {
		Game removedGame = availableGames.remove(name);
		return removedGame;
	}

	public Game addGame(String name, String rules) {
		Game newGame = new Game(name, Collections.singletonList(rules));
		availableGames.put(name, newGame);
		return newGame;
	}


	public int getCurrentPoints() {
		return currentPoints;
	}


	public String getWordGuess() {
		return wordGuess;
	}


	public void setWordGuess(String wordGuess) {
		this.wordGuess = wordGuess;
	}


	public boolean isPlaying() {
		return isPlaying;
	}


	public Game getGameToPlay() {
		return gameToPlay;
	}
	
	

}
