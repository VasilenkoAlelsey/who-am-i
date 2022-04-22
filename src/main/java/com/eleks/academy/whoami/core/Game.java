package com.eleks.academy.whoami.core;

import com.eleks.academy.whoami.networking.server.ServerImpl;

import java.io.BufferedReader;
import java.io.IOException;
import java.net.Socket;
import java.util.Map;

public interface Game {
	
	void addPlayer(Player player);
	
	boolean makeTurn();
	
	void assignCharacters();
	
	boolean isFinished();

	void changeTurn();

	void initGame();

	void playersConnect(ServerImpl server, int numberOfPlayers) throws IOException;

	Map<Integer, Socket> getPlayersSocketMap();

	Map<Integer, BufferedReader> getPlayersReaderMap();
}
