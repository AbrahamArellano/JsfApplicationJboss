package com.jbossdev.beans;

public interface IGame {

	public void generateWordToFind();

	public boolean lookUpWord(String findWord);

	public void addGivenWordToList(String word);
}
