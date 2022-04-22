package com.eleks.academy.whoami.networking.server;

import java.io.BufferedReader;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.List;
import java.util.Map;

import com.eleks.academy.whoami.core.Game;
import com.eleks.academy.whoami.core.Player;
import com.eleks.academy.whoami.core.impl.RandomGame;
import com.eleks.academy.whoami.core.impl.RandomPlayer;

public class ServerImpl implements Server {

	private List<String> characters = List.of("Batman", "Superman", "Pinocchio", "Baby Shark");
	private List<String> questions = List.of("Am i a human?", "Am i a character from a movie?");
	private List<String> guessess = List.of("Batman", "Superman");
	private Map<Integer, Socket> playersSocketMap;
	private Map<Integer, BufferedReader> playersReaderMap;

	private RandomGame game = new RandomGame(characters);

	private final ServerSocket serverSocket;

	public ServerImpl(int port) throws IOException {
		this.serverSocket = new ServerSocket(port);
	}

	@Override
	public Game startGame() {
		game.addPlayer(new RandomPlayer("Bot", questions, guessess));
		System.out.println("Server starts");
		System.out.println("Enter number of players");
		return game;
	}

	@Override
	public Socket waitForPlayer() throws IOException {
		return serverSocket.accept();
	}

	@Override
	public void addPlayer(Player player) {
		game.addPlayer(player);
		System.out.println("Player: " + player.getName() + " Connected to the game!");
	}

	@Override
	public void stopServer(Game game, int numberOfPlayers) throws IOException {
		playersReaderMap = game.getPlayersReaderMap();
		playersSocketMap = game.getPlayersSocketMap();

		for (int i = 0; i < numberOfPlayers; i++) {
			playersSocketMap.get(i).close();
			playersReaderMap.get(i).close();
		}
	}

}
