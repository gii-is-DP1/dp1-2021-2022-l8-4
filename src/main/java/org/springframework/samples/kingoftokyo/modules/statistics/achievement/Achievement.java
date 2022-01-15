package org.springframework.samples.kingoftokyo.modules.statistics.achievement;

import java.util.Set;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.ManyToMany;
import javax.persistence.Table;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import org.springframework.samples.kingoftokyo.model.NamedEntity;
import org.springframework.samples.kingoftokyo.modules.statistics.metrics.MetricType;
import org.springframework.samples.kingoftokyo.user.User;

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
   private String description;

   @NotNull
   private Integer rewardPoints;

   @NotNull
   @Enumerated(value = EnumType.ORDINAL)
   private MetricType metric;

   @NotNull
   @Min(0)
   private Long goal;

   @ManyToMany(mappedBy = "achievements")
   private Set<User> users;

   public Boolean isObtained(Long score){
      return score != null && goal<=score;
   }

}