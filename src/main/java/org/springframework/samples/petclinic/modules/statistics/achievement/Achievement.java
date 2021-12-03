package org.springframework.samples.petclinic.modules.statistics.achievement;

import javax.persistence.MappedSuperclass;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import org.springframework.samples.petclinic.model.NamedEntity;
import org.springframework.samples.petclinic.modules.statistics.metrics.MetricType;

import lombok.Getter;
import lombok.Setter;

/**
 * @author Carlos Varela Soult
 * @author Jose Maria Delgado Sanchez
 */
@MappedSuperclass
@Getter
@Setter
public abstract class Achievement extends NamedEntity {

   @NotEmpty
   private String name;

   @NotEmpty
   private String description;

   @NotNull
   private Integer rewardPoints;

   @NotNull
   private MetricType metric;

   abstract Boolean isObtained(Object data);

}