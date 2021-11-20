package org.springframework.samples.petclinic.dice;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.springframework.samples.petclinic.model.BaseEntity;

import lombok.Getter;
import lombok.Setter;

/**
 * @author Ricardo Nadal Garcia
 */
/*
@Entity
@Getter
@Setter
@Table(name="roll")
public class RollEntity extends BaseEntity {

    
    private Integer rollAmount;

   
    @OneToMany(mappedBy = "roll")
    private List<Dice> values;

    
    private Integer maxThrows;

    
    private DiceValues[] keep;
/*
    public RollEntity() {
        this.maxThrows=3;
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
        List<Dice> resultado=new ArrayList<Dice>();
        Integer tiradas=6;
        int i;
        int max=5;
        int min=0;
        
        for(DiceValues dadoValor:dadosConservados){
            Dice dado=new Dice();
            dado.setValue(dadoValor);
            resultado.add(dado);
        }
        
        for(i=0;i<tiradas-dadosConservados.size();i++) {
            Dice dado=new Dice();
            Integer valor=(int)Math.floor(Math.random()*(max-min+1)+min);
            dado.setValue(DiceValues.values()[valor]);
            resultado.add(dado);
        }
        
        this.values=resultado; 
    }

    public Boolean rollFinished() {
        return this.rollAmount >= this.maxThrows;
    }

    


    
}
*/
