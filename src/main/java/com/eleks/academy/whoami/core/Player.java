package com.eleks.academy.whoami.core;

import java.util.concurrent.Future;

public interface Player {

	Future<String> getName();
	
	Future<String> suggestCharacter();

	Future<String> getQuestion();

	Future<String> answerQuestion(String question, String character);

	Future<String> getGuess();

	Future<String> isReadyForGuess();

	Future<String> answerGuess();
	
	void close();
	
}
