package com.eleks.academy.whoami.core.impl;

import com.eleks.academy.whoami.core.Game;
import com.eleks.academy.whoami.core.GameCharacter;
import com.eleks.academy.whoami.core.Player;
import lombok.Getter;

import java.time.Instant;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;

import static com.eleks.academy.whoami.core.impl.GameStatus.SUGGESTING_CHARACTERS;
import static com.eleks.academy.whoami.core.impl.GameStatus.WAITING_FOR_PLAYERS;
import static java.util.stream.Collectors.collectingAndThen;
import static java.util.stream.Collectors.toList;

@Getter
public class PersistentGame implements Game {

    private String id;
    private Integer maxPlayers;
    private final Map<String, Player> players = new ConcurrentHashMap<>();

    private final Map<String, List<GameCharacter>> suggestedCharacters = new ConcurrentHashMap<>();
    private final Map<String, String> playerCharacterMap = new ConcurrentHashMap<>();

    private final List<String> turns = new ArrayList<>();

    private GameStatus gameStatus;

    private Integer currentPlayerIndex;

    public PersistentGame(String hostPlayer, Integer maxPlayers) {
        this.id = String.format("%d-%d",
                Instant.now().toEpochMilli(),
                Double.valueOf(Math.random() * 999).intValue());
        this.maxPlayers = maxPlayers;

        this.players.put(hostPlayer, PersistentPlayer.of(hostPlayer));
        this.turns.add(hostPlayer);
        this.gameStatus = WAITING_FOR_PLAYERS;
    }

    @Override
    public String getId() {
        return this.id;
    }

    @Override
    public boolean isAvailable() {
        return WAITING_FOR_PLAYERS.equals(this.gameStatus);
    }

    @Override
    public boolean isReadyToStart() {
        final var statusApplicable = SUGGESTING_CHARACTERS.equals(this.gameStatus);

        final var enoughCharacters = Optional.of(this.suggestedCharacters)
                .map(Map::values)
                .stream()
                .mapToLong(Collection::size)
                .sum() >= this.players.size();

        return statusApplicable
                && this.suggestedCharacters.size() > 1
                && enoughCharacters;
    }

    @Override
    public boolean hasPlayer(String player) {
        return this.players.containsKey(player);
    }

    @Override
    public void addPlayer(String player) {
        this.players.put(player, PersistentPlayer.of(player));
        this.turns.add(player);

        Optional.of(this.players.size())
                .filter(playersSize -> playersSize.equals(this.maxPlayers))
                .ifPresent(then -> this.setStatus(GameStatus.SUGGESTING_CHARACTERS));
    }

    @Override
    public List<String> getPlayers() {
        return new ArrayList<>(this.players.keySet());
    }

    @Override
    public String getTurn() {
        return this.turns.get(this.currentPlayerIndex);
    }

    @Override
    public void startGame() {
        this.assignCharacters();

        this.setStatus(GameStatus.WAITING_FOR_QUESTION);

        this.currentPlayerIndex = 0;
    }

    public void suggestCharacter(String player, String character) {
        List<GameCharacter> characters = this.suggestedCharacters.get(player);

        if (Objects.isNull(characters)) {
            final var newCharacters = new ArrayList<GameCharacter>();

            this.suggestedCharacters.put(player, newCharacters);

            characters = newCharacters;
        }

        characters.add(GameCharacter.of(character, player));
    }

    @Override
    public boolean makeTurn() {
        return false;
    }

    @Override
    public boolean isFinished() {
        return false;
    }

    @Override
    public void changeTurn() {

    }

    @Override
    public void initGame() {

    }

    @Override
    public void play() {

    }

    private void setStatus(GameStatus status) {
        this.gameStatus = status;
    }

    private void assignCharacters() {
        final var allCharacters = this.suggestedCharacters
                .values()
                .stream()
                .flatMap(Collection::stream)
                .collect(toList());

        this.players.keySet()
                .forEach(player -> {
                    final var assignedCharacter =
                            allCharacters.stream()
                                    .filter(character -> Objects.equals(character.getAuthor(), player))
                                    .collect(collectingAndThen(toList(), getRandomCharacter()));

                    this.playerCharacterMap.put(player, assignedCharacter.getCharacter());
                    allCharacters.remove(assignedCharacter);
                });
    }

    private Function<List<GameCharacter>, GameCharacter> getRandomCharacter() {
        return gameCharacter -> {
            int randomPos = (int) (Math.random() * gameCharacter.size());

            return gameCharacter.get(randomPos);
        };
    }
}
