package org.springframework.samples.petclinic.association;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.springframework.samples.petclinic.board.Board;
import org.springframework.samples.petclinic.card.Card;
import org.springframework.samples.petclinic.model.BaseEntity;

import lombok.Getter;
import lombok.Setter;

/**
 * @author José Maria Delgado Sanchez
 */

 @Entity
 @Table(name="boards_cards")
 public class BoardCard extends BaseEntity{

    @Getter
    @Setter
    @ManyToOne(optional=false)
    @JoinColumn(name="board_id")
    private Board board;

    @Getter
    @Setter
    @ManyToOne(optional=false)
    @JoinColumn(name="game_id")
    private Card card;

    @Setter
    @Getter
    @Column(name="sold")
    private Boolean sold;
 }