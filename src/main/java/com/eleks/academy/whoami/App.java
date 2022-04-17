package com.eleks.academy.whoami;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;

import com.eleks.academy.whoami.core.Game;
import com.eleks.academy.whoami.networking.client.ClientPlayer;
import com.eleks.academy.whoami.networking.server.ServerImpl;

public class App {

	public static void main(String[] args) throws IOException {

		ServerImpl server = new ServerImpl(888);

		Game game = server.startGame();

		Socket socket1 = server.waitForPlayer();
		Socket socket2 = server.waitForPlayer();
		Socket socket3 = server.waitForPlayer();

		BufferedReader reader1 = new BufferedReader(new InputStreamReader(socket1.getInputStream()));
		BufferedReader reader2 = new BufferedReader(new InputStreamReader(socket2.getInputStream()));
		BufferedReader reader3 = new BufferedReader(new InputStreamReader(socket3.getInputStream()));

		boolean gameStatus = true;

		String playerName1 = reader1.readLine();
		String playerName2 = reader2.readLine();
		String playerName3 = reader3.readLine();

		server.addPlayer(new ClientPlayer(1, playerName1, socket1));
		server.addPlayer(new ClientPlayer(2, playerName2, socket2));
		server.addPlayer(new ClientPlayer(3, playerName3, socket3));

		game.assignCharacters();

		if (!playerName3.isEmpty()){
			game.initGame();
		}

		while (gameStatus) {
			boolean turnResult = game.makeTurn();

			while (turnResult) {
				turnResult = game.makeTurn();
			}
			game.changeTurn();
			gameStatus = !game.isFinished();
		}

		server.stopServer(socket1, reader1);
		server.stopServer(socket2, reader2);
		server.stopServer(socket3, reader3);
	}

}
