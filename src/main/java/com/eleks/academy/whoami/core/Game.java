package com.eleks.academy.whoami.core;

import java.util.List;

public interface Game {

	default String getId() {
		throw new UnsupportedOperationException();
	}

	default boolean isReadyToStart() {
		throw new UnsupportedOperationException();
	}

	default boolean hasPlayer(String player) {
		throw new UnsupportedOperationException();
	}

	default void addPlayer(String player) {
		throw new UnsupportedOperationException();
	}

	default boolean isAvailable() {
		throw new UnsupportedOperationException();
	}

	default List<String> getPlayers() {
		throw new UnsupportedOperationException();
	}

	default String getTurn() {
		throw new UnsupportedOperationException();
	}

	default void startGame() {
		throw new UnsupportedOperationException();
	}

	default void suggestCharacter(String player, String character) {
		throw new UnsupportedOperationException();
	}

	boolean makeTurn();
	
	boolean isFinished();

	void changeTurn();

	void initGame();
	
	void play();

}
