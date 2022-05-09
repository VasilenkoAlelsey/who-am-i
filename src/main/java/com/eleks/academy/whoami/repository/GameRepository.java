package com.eleks.academy.whoami.repository;

import com.eleks.academy.whoami.core.Game;
import com.eleks.academy.whoami.core.impl.PersistentGame;

import java.util.List;
import java.util.Optional;

public interface GameRepository {

    List<Game> findAllAvailable();

    Game save(PersistentGame game);

    Optional<Game> findById(String id);
}
