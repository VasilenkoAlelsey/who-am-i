package com.eleks.academy.whoami.model.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class NewGameRequest {

    @NotNull
    @Min(2)
    @Max(4)
    private Integer maxPlayers;
}
