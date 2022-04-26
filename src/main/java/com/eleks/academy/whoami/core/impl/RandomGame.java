package com.eleks.academy.whoami.core.impl;

import java.util.*;

import com.eleks.academy.whoami.core.Game;
import com.eleks.academy.whoami.core.Player;
import com.eleks.academy.whoami.core.Turn;

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
        List<Player> otherPlayers = currentTurn.getOtherPlayers();
        boolean isReady = currentGuesser.isReadyForGuess();

        for (int i = 0; i < otherPlayers.size(); i++) {
            Player otherPlayer = otherPlayers.get(i);
            Thread thread = new Thread(() -> addAnswers(otherPlayer, currentGuesser, isReady));
            thread.start();
        }

        long positiveCount = answers.stream().filter(a -> YES.equals(a)).count();
        long negativeCount = answers.stream().filter(a -> NO.equals(a)).count();

        boolean result = positiveCount > negativeCount;

        if (result) {
            players.remove(currentGuesser);
        }
        return result;
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
        int randomPos = (int) (Math.random() * this.availableCharacters.size());
        return this.availableCharacters.remove(randomPos);
    }

    @Override
    public void changeTurn() {
        this.currentTurn.changeTurn();
    }

    private void addAnswers(Player otherPlayer, Player currentGuesser, boolean isReady) {
        if (isReady) {
            String guess = currentGuesser.getGuess();
            answers.add(otherPlayer.answerGuess(guess, this.playersCharacter.get(currentGuesser.getName())));
        } else {
            String question = currentGuesser.getQuestion();
            answers.add(otherPlayer.answerQuestion(question, this.playersCharacter.get(currentGuesser.getName())));
        }
    }
}
