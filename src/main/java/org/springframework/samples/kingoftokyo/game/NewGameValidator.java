package org.springframework.samples.kingoftokyo.game;

import java.time.LocalDateTime;

import org.springframework.util.StringUtils;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

/**
 * @author Jose Maria Delgado Sanchez
 */
public class NewGameValidator implements Validator{

    private static final String VALORINCORRECTO = "Valor incorrecto";
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
			errors.rejectValue("name", "Obligatorio y debe tener entre 3 y 50 caracteres", "Obligatorio y debe tener entre 3 y 50 caracteres");
		}

        //turn validation
        Integer turn = game.getTurn();
        if(turn != 0){
            errors.rejectValue("", VALORINCORRECTO , VALORINCORRECTO);
        }

        //winner validation
        String winner = game.getWinner();
        if(winner != null){
            errors.rejectValue("", VALORINCORRECTO , VALORINCORRECTO);
        }

        //winner validation
        LocalDateTime startTime = game.getStartTime();
        if(startTime != null){
            errors.rejectValue("", VALORINCORRECTO , VALORINCORRECTO);
        }

        //winner validation
        LocalDateTime endTime = game.getEndTime();
        if(endTime != null){
            errors.rejectValue("", VALORINCORRECTO , VALORINCORRECTO);
        }

        //maxNumberOfPlayers validation
        Integer maxNumberOfPlayers = game.getMaxNumberOfPlayers();
        if(maxNumberOfPlayers == null || maxNumberOfPlayers > 6 || maxNumberOfPlayers < 2){
            errors.rejectValue("maxNumberOfPlayers" , VALORINCORRECTO , VALORINCORRECTO);
        }
    }
    
}
