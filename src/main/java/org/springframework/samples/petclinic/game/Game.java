package org.springframework.samples.petclinic.game;

import javax.persistence.Entity;
import javax.persistence.Table;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import org.springframework.samples.petclinic.model.NamedEntity;

import lombok.Getter;
import lombok.Setter;

/**
 * @author José Maria Delgado Sanchez
 */

 @Entity
 @Table(name = "games")
 public class Game extends NamedEntity{

    @NotEmpty
    @Getter
    @Setter
    private String gameName;

    @NotEmpty
    @Getter
    @Setter
    private String gameCreator;

    @NotNull
    @Getter
    @Setter
    private Integer gameTurn;

 }