package com.jbossdev.decorator;

import javax.decorator.Decorator;
import javax.decorator.Delegate;
import javax.enterprise.inject.Any;
import javax.inject.Inject;

import com.jbossdev.beans.IGame;
import com.jbossdev.interceptors.Logged;

@Decorator
public abstract class GameDecorator implements IGame {

	
	@Inject
	@Delegate
	@Any
	private IGame iGame;

	public GameDecorator() {
	}

	@Override
	@Logged
	public void generateWordToFind() {
		iGame.generateWordToFind();
	}

	@Override
	@Logged
	public boolean lookUpWord(String findWord) {
		addGivenWordToList(findWord);
		return iGame.lookUpWord(findWord);
	}

}
