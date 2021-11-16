package org.springframework.samples.petclinic.dice;

import java.util.ArrayList;
import java.util.List;

import lombok.Getter;
import lombok.Setter;

/**
 * @author Ricardo Nadal Garcia
 */

public class Roll {

    @Getter
    @Setter
    private Integer rollAmount=0;

    @Getter
    @Setter
    private List<DiceValues> values;

    @Getter
    @Setter
    private Integer maxThrows=3;


    public Roll() {
        this.rollAmount=0;
        rollDice();
        
    }
   
    public void rollDice(){
        List<DiceValues> resultado=new ArrayList<DiceValues>();
        Integer tiradas=6;
        int i;
        int max=5;
        int min=0;
        for(i=0;i<tiradas;i++) {
            Integer valor=(int)Math.floor(Math.random()*(max-min+1)+min);
            resultado.add(DiceValues.values()[valor]);
        }
        this.values=resultado; 
        this.rollAmount++;
    }
    
    public void rollDice(List<DiceValues> dadosConservados) {
        List<DiceValues> resultado=new ArrayList<DiceValues>();
        Integer tiradas=6;
        int i;
        int max=5;
        int min=0;
        resultado.addAll(dadosConservados);
        for(i=0;i<tiradas-dadosConservados.size();i++) {
            Integer valor=(int)Math.floor(Math.random()*(max-min+1)+min);
            resultado.add(DiceValues.values()[valor]);
        }
      //  this.rollAmount++;
        this.values=resultado; 
/*
        if(this.rollAmount==this.maxThrows) {
            this.rollAmount=0;
        }
        */
    }

    
}
