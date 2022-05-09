package com.eleks.academy.whoami.model.request;

import com.eleks.academy.whoami.core.impl.GameStatus;
import com.fasterxml.jackson.annotation.JsonSetter;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import javax.validation.constraints.NotNull;
import java.util.Arrays;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PatchGameRequest {

    @NotNull
    private GameStatus gameStatus;

    @JsonSetter
    public void setGameStatus(String status) {
        this.gameStatus = Arrays.stream(GameStatus.values())
                .filter(enumerated -> enumerated.name().equalsIgnoreCase(status))
                .findAny()
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST));
    }
}
