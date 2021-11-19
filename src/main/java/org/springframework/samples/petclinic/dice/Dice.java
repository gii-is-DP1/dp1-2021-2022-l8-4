package org.springframework.samples.petclinic.dice;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import org.springframework.samples.petclinic.model.BaseEntity;

import lombok.Getter;
import lombok.Setter;
/*
@Entity
@Setter
@Getter
public class Dice extends BaseEntity {
    
    private DiceValues value;

    

    @ManyToOne(optional=false) 
    @JoinColumn(name="roll_id")
    private RollEntity roll;

}
*/