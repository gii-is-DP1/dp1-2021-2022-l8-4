package org.springframework.samples.petclinic.dice;

import java.util.ArrayList;
import java.util.List;

import javassist.expr.NewArray;
import lombok.Getter;
import lombok.Setter;

/**
 * @author Ricardo Nadal Garcia
 */

@Getter
@Setter
public class Roll {

    
    private Integer rollAmount;

   
    private List<DiceValues> values;

    private List<DiceValues> cardExtraValues; //For values that are obtained through card effects

    
    private Integer maxThrows;

    
    private DiceValues[] keep;



    public Roll() {
        this.maxThrows=3;
        this.rollAmount=0;
        this.keep=new DiceValues[8];
        this.cardExtraValues=new ArrayList<DiceValues>();

        List<DiceValues> dadosDeTodo=new ArrayList<DiceValues>();
        dadosDeTodo.add(DiceValues.ONE);
        dadosDeTodo.add(DiceValues.TWO);
        dadosDeTodo.add(DiceValues.THREE);
        dadosDeTodo.add(DiceValues.ATTACK);
        dadosDeTodo.add(DiceValues.ENERGY);
        dadosDeTodo.add(DiceValues.HEAL);
        this.values=dadosDeTodo;
        
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
        Integer tiradas=values.size();
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

    public Boolean isFinished() {
        return this.rollAmount >= this.maxThrows;
    }
}
