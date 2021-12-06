package org.springframework.samples.petclinic.modules.statistics.achievement;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Table;
import javax.validation.constraints.Min;
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
@Entity
@Getter
@Setter
@Table(name = "achievements")
public class Achievement extends NamedEntity {

   @NotEmpty
   @Column(name="description")
   private String description;

   @NotNull
   @Column(name="rewardPoints")
   private Integer rewardPoints;

   @NotNull
   @Enumerated(value = EnumType.ORDINAL)
   @Column(name="metric")
   private MetricType metric;

   @NotNull
   @Min(0)
   @Column(name="goal")
   private Long goal;

   public Boolean isObtained(Long score){
      return score != null && goal<=score;
   }

}