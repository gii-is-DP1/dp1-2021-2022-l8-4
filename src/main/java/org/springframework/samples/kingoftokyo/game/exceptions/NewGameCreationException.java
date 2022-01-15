package org.springframework.samples.kingoftokyo.game.exceptions;

public class NewGameCreationException extends Exception {
    public NewGameCreationException(String errorMessage){
        super(errorMessage);
    }
}
