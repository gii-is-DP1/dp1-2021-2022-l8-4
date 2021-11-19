package org.springframework.samples.petclinic.dice;

import java.util.ArrayList;
import java.util.List;

import lombok.Getter;
import lombok.Setter;

/**
 * @author Ricardo Nadal Garcia
 */
@Getter
@Setter
public class Roll {

    
    private Integer rollAmount=0;

   
    private List<DiceValues> values;

    
    private Integer maxThrows=3;

    
    private DiceValues[] keep;

    public Roll() {
        this.rollAmount=0;
        this.keep=new DiceValues[8];
        List<DiceValues> dadosDeTodo=new ArrayList<DiceValues>();
        dadosDeTodo.add(DiceValues.ONE);
        dadosDeTodo.add(DiceValues.TWO);
        dadosDeTodo.add(DiceValues.THREE);
        dadosDeTodo.add(DiceValues.ATTACK);
        dadosDeTodo.add(DiceValues.ENERGY);
        dadosDeTodo.add(DiceValues.HEAL);
        rollDice(dadosDeTodo);
        
    }
   

    public void rollDiceInitial() {
        List<DiceValues> dadosIniciales =new ArrayList<DiceValues>();
        rollDice(dadosIniciales);
        this.rollAmount++;
    }

    public void rollDiceNext(List<DiceValues> dadosConservados) {
        rollDice(dadosConservados);
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
        
        this.values=resultado; 
    }

    public Boolean rollFinished() {
        return this.rollAmount >= this.maxThrows;
    }

    


    
}
