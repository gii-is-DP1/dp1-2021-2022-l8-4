package org.springframework.samples.kingoftokyo.game.exceptions;

public class NewGameException extends Exception {
    public NewGameException(String errorMessage){
        super(errorMessage);
    }
}
