package com.eleks.academy.whoami.service;

import com.eleks.academy.whoami.core.Game;
import com.eleks.academy.whoami.model.request.CharacterSuggestion;
import com.eleks.academy.whoami.model.request.NewGameRequest;
import com.eleks.academy.whoami.model.request.PatchGameRequest;
import com.eleks.academy.whoami.model.response.GameDetails;

import java.util.List;
import java.util.Optional;

public interface GameService {

    List<Game> findAvailableGames();

    Game createGame(String player, NewGameRequest gameRequest);

    void enrollToGame(String id, String player);

    Optional<GameDetails> findByIdAndPlayer(String id, String player);

    void suggestCharacter(String id, String player, CharacterSuggestion suggestion);

    Optional<GameDetails> updateGame(String id, String player, PatchGameRequest gameRequest);
}
