package org.springframework.samples.petclinic.modules.statistics.achievement;

import javax.persistence.Entity;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import lombok.Getter;
import lombok.Setter;

/**
 * @author Jose Maria Delgado Sanchez
*/
@Entity
@Getter
@Setter
@Table(name = "Integer_Achievements")
public class IntegerAchievement extends Achievement{

    @NotNull
    private Integer limit;

    @Override
    Boolean isObtained(Object data) {
        return (Integer) data<=limit;
    }
}
