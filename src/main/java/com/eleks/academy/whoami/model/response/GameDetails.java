package com.eleks.academy.whoami.model.response;

import com.eleks.academy.whoami.core.Game;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class GameDetails {

    private List<String> players;

    public static GameDetails of(Game game) {
        return new GameDetails(game.getPlayers());
    }
}
