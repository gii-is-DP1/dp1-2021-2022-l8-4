package org.springframework.samples.kingoftokyo.game;

import java.time.LocalDateTime;

import org.springframework.util.StringUtils;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

/**
 * @author Jose Maria Delgado Sanchez
 */
public class NewGameValidator implements Validator{

    private static final String REQUIRED = "Requerido";

    @Override
    public boolean supports(Class<?> clazz) {
        return Game.class.isAssignableFrom(clazz);
    }

    @Override
    public void validate(Object obj, Errors errors) {
        Game game = (Game) obj;

        //name validation
        String name = game.getName();
        if (!StringUtils.hasLength(name) || name.length()>50 || name.length()<3) {
			errors.rejectValue("name", REQUIRED+" y debe tener entre 3 y 50 caracteres", REQUIRED+" y debe tener entre 3 y 50 caracteres");
		}

        //turn validation
        Integer turn = game.getTurn();
        if(turn != 0){
            errors.rejectValue("turn", "Debe ser 0", "Debe ser 0");
        }

        //winner validation
        String winner = game.getWinner();
        if(winner != null){
            errors.rejectValue("winner", "Debe ser nulo", "Debe ser nulo");
        }

        //winner validation
        LocalDateTime startTime = game.getStartTime();
        if(startTime != null){
            errors.rejectValue("startTime", "Debe ser nulo", "Debe ser nulo");
        }

        //winner validation
        LocalDateTime endTime = game.getEndTime();
        if(endTime != null){
            errors.rejectValue("endTime", "Debe ser nulo", "Debe ser nulo");
        }

        //maxNumberOfPlayers validation
        Integer maxNumberOfPlayers = game.getMaxNumberOfPlayers();
        if(maxNumberOfPlayers == null || maxNumberOfPlayers > 6 || maxNumberOfPlayers < 2){
            errors.rejectValue("maxNumberOfPlayers", "Debe estar entre 2 y 6", "Debe estar entre 2 y 6");
        }


    }
    
}
