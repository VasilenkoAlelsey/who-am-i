package com.eleks.academy.whoami;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.eleks.academy.whoami.core.Game;
import com.eleks.academy.whoami.core.impl.RandomPlayer;
import com.eleks.academy.whoami.networking.client.ClientPlayer;
import com.eleks.academy.whoami.networking.server.ServerImpl;

public class App {

	public static void main(String[] args) throws IOException {

		ServerImpl server = new ServerImpl(888);

		Game game = server.startGame();

		BufferedReader enterNumberOfPlayers = new BufferedReader(new InputStreamReader(System.in));
		int numberOfPlayers = Integer.parseInt(enterNumberOfPlayers.readLine());
		System.out.println("Waiting for " + numberOfPlayers + " clients connect....");

		game.playersConnect(server, numberOfPlayers);

		boolean gameStatus = true;

		game.assignCharacters();

		while (gameStatus) {
			boolean turnResult = game.makeTurn();

			while (turnResult) {
				turnResult = game.makeTurn();
			}
			game.changeTurn();
			gameStatus = !game.isFinished();
		}

		server.stopServer(game, numberOfPlayers);
		enterNumberOfPlayers.close();

	}

}
