package org.springframework.samples.petclinic.dice;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Ricardo Nadal Garcia
 */

public class Roll {

    private Integer rollAmount;
    private static List<DiceValues> values;

    public Roll() {
        rollAmount=0;
        values=rollDice();
        
    }
   
    public static List<DiceValues> rollDice(){
        List<DiceValues> resultado=new ArrayList<DiceValues>();
        Integer tiradas=6;
        int i;
        int max=5;
        int min=0;
        for(i=0;i<tiradas;i++) {
            Integer valor=(int)Math.floor(Math.random()*(max-min+1)+min);
            resultado.add(DiceValues.values()[valor]);
        }
        return resultado; 
    }
    
}
