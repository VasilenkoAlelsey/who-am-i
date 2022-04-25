package com.eleks.academy.whoami.core.impl;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.util.*;
import java.util.stream.Collectors;
import com.eleks.academy.whoami.core.Game;
import com.eleks.academy.whoami.core.Player;
import com.eleks.academy.whoami.core.Turn;
import com.eleks.academy.whoami.networking.client.ClientPlayer;
import com.eleks.academy.whoami.networking.server.ServerImpl;

public class RandomGame implements Game {
	
	private Map<String, String> playersCharacter = new HashMap<>();
	private List<Player> players = new ArrayList<>();
	private List<String> availableCharacters;
	private Turn currentTurn;
	private Set<String> answers = new HashSet<>();
	
	private final static String YES = "Yes";
	private final static String NO = "No";
	
	public RandomGame(List<String> availableCharacters) {
		this.availableCharacters = new ArrayList<String>(availableCharacters);
	}

	@Override
	public void addPlayer(Player player) {
		this.players.add(player);
	}

	@Override
	public boolean makeTurn() {
		Player currentGuesser = currentTurn.getGuesser();

		if (currentGuesser.isReadyForGuess()) {
			List<Player> otherPlayers = currentTurn.getOtherPlayers();
			String guess = currentGuesser.getGuess();
			for (int i = 0; i < otherPlayers.size(); i++) {
				Player otherPlayer = otherPlayers.get(i);
				Thread thread = new Thread(() -> addGuesses(otherPlayer, guess, currentGuesser));
				thread.start();
			}

			long positiveCount = answers.stream().filter(a -> YES.equals(a)).count();
			long negativeCount = answers.stream().filter(a -> NO.equals(a)).count();

			boolean win = positiveCount > negativeCount;

			if (win) {
				players.remove(currentGuesser);
			}
			return win;

		} else {
			List<Player> otherPlayers = currentTurn.getOtherPlayers();
			String question = currentGuesser.getQuestion();

			for (int i = 0; i < otherPlayers.size(); i++) {
				Player otherPlayer = otherPlayers.get(i);
				Thread thread = new Thread(() -> addQuestions(otherPlayer, question, currentGuesser));
				thread.start();
			}

			long positiveCount = answers.stream().filter(a -> YES.equals(a)).count();
			long negativeCount = answers.stream().filter(a -> NO.equals(a)).count();
			return positiveCount > negativeCount;
		}
	}

	@Override
	public void assignCharacters() {
		System.out.println("---------------------------------------------");
		System.out.println("All players have been connected! Let's start!");
		System.out.println("---------------------------------------------");
		players.stream().forEach(player -> this.playersCharacter.put(player.getName(), this.getRandomCharacter()));
	}
	
	@Override
	public void initGame() {
		this.currentTurn = new TurnImpl(this.players);
	}


	@Override
	public boolean isFinished() {
		return players.size() == 1;
	}
	
	private String getRandomCharacter() {
		int randomPos = (int)(Math.random() * this.availableCharacters.size()); 
		return this.availableCharacters.remove(randomPos);
	}

	@Override
	public void changeTurn() {
		this.currentTurn.changeTurn();
	}

	private void addGuesses(Player otherPlayer, String guess, Player currentGuesser) {
		answers.add(otherPlayer.answerGuess(guess, this.playersCharacter.get(currentGuesser.getName())));
	}

	private void addQuestions(Player otherPlayer, String question, Player currentGuesser) {
		answers.add(otherPlayer.answerQuestion(question, this.playersCharacter.get(currentGuesser.getName())));
	}
}
