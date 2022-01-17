package org.springframework.samples.kingoftokyo.player.exceptions;

public class InvalidPlayerActionException extends Exception {
    public InvalidPlayerActionException(String errorMessage){
        super(errorMessage);
    }
}
