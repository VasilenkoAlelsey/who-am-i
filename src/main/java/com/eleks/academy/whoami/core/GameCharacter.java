package com.eleks.academy.whoami.core;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor(staticName = "of")
public class GameCharacter {

    private String character;

    private String author;
}
