package org.springframework.samples.petclinic.achievements;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import org.springframework.samples.petclinic.model.NamedEntity;
import lombok.Getter;
import lombok.Setter;

/**
/* @author Carlos Varela Soult
*/

 @Entity
 @Getter
 @Setter
 @Table (name = "achievements")
 public class Achievements extends NamedEntity{
    
    @Getter
    @Setter
    @NotNull
    @Column(name="reward_points")
    private Integer rewardPoints;

    @Getter
    @Setter
    @NotNull
    @Column(name="condition")
    private Boolean condition;
 }