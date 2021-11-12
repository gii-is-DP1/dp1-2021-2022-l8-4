package org.springframework.samples.petclinic.dice;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Ricardo Nadal Garcia
 */

public class Roll {

    private Integer rollAmount;
    private List<DiceValues> DiceValues;

    public Roll() {
        rollAmount=0;
        DiceValues=rollDice();
        
    }
    
    public List<DiceValues> rollDice(){
        List<DiceValues> resultado=new ArrayList<DiceValues>();
        Integer tiradas=6;
        int i;
        int max=5;
        int min=0;
        for(i=0;i<tiradas;i++) {
            Integer valor=(int)Math.floor(Math.random()*(max-min+1)+min);
            resultado.add(DiceValues.get(valor));
        }
        return resultado; 
    }
}
