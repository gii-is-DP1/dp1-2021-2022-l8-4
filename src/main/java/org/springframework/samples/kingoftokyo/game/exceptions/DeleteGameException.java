package org.springframework.samples.kingoftokyo.game.exceptions;

public class DeleteGameException extends Exception{
    public DeleteGameException(String errorMessage){
        super(errorMessage);
    }
}
