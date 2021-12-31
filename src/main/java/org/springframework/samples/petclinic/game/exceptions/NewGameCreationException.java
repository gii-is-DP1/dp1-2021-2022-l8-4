package org.springframework.samples.petclinic.game.exceptions;

public class NewGameCreationException extends Exception {
    public NewGameCreationException(String errorMessage){
        super(errorMessage);
    }
}
