package com.eleks.academy.whoami.repository;

import com.eleks.academy.whoami.core.Game;
import com.eleks.academy.whoami.core.impl.PersistentGame;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

@Repository
public class GameInMemoryRepository implements GameRepository{

    private final Map<String, Game> games = new ConcurrentHashMap<>();

    @Override
    public List<Game> findAllAvailable() {
        return this.games.values().stream().filter(Game::isAvailable).collect(Collectors.toList());
    }

    @Override
    public Game save(PersistentGame game) {
        this.games.put(game.getId(), game);
        return game;
    }

    @Override
    public Optional<Game> findById(String id) {
        return Optional.ofNullable(this.games.get(id));
    }
}
