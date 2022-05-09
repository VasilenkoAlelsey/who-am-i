package com.eleks.academy.whoami.service.impl;

import com.eleks.academy.whoami.core.Game;
import com.eleks.academy.whoami.core.impl.PersistentGame;
import com.eleks.academy.whoami.model.request.CharacterSuggestion;
import com.eleks.academy.whoami.model.request.NewGameRequest;
import com.eleks.academy.whoami.model.request.PatchGameRequest;
import com.eleks.academy.whoami.model.response.GameDetails;
import com.eleks.academy.whoami.repository.GameRepository;
import com.eleks.academy.whoami.service.GameService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;
import java.util.function.UnaryOperator;

@Service
@RequiredArgsConstructor
public class GameServiceImpl implements GameService {

    private final GameRepository gameRepository;

    @Override
    public List<Game> findAvailableGames() {
        return this.gameRepository.findAllAvailable();
    }

    @Override
    public Game createGame(String player, NewGameRequest gameRequest) {
        return this.gameRepository.save(new PersistentGame(player, gameRequest.getMaxPlayers()));
    }

    @Override
    public void enrollToGame(String id, String player) {
        this.gameRepository.findById(id).
                filter(Game::isAvailable).
                filter(game -> !game.hasPlayer(player)).
                ifPresentOrElse(
                        game -> game.addPlayer(player),
                        () -> {
                            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Cannot enroll to a game");
                        });
    }

    @Override
    public Optional<GameDetails> findByIdAndPlayer(String id, String player) {
        return this.gameRepository.findById(id).
                filter(game -> game.hasPlayer(player)).
                map(GameDetails::of);
    }

    @Override
    public void suggestCharacter(String id, String player, CharacterSuggestion suggestion) {
        this.gameRepository.findById(id)
                .filter(game -> game.hasPlayer(player))
                .ifPresentOrElse(
                        game -> game.suggestCharacter(player, suggestion.getCharacter()),
                        () -> {
                            throw new ResponseStatusException(HttpStatus.NOT_FOUND, HttpStatus.NOT_FOUND.getReasonPhrase());
                        }
                );
    }

    @Override
    public Optional<GameDetails> updateGame(String id, String player, PatchGameRequest gameRequest) {
        UnaryOperator<Game> startGame = game -> {
            game.startGame();

            return game;
        };

        return this.gameRepository.findById(id)
                .filter(Game::isReadyToStart)
                .map(startGame)
                .map(GameDetails::of);
    }
}
