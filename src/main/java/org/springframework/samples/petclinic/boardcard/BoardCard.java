package org.springframework.samples.petclinic.boardcard;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import org.springframework.samples.petclinic.board.Board;
import org.springframework.samples.petclinic.card.Card;
import org.springframework.samples.petclinic.model.BaseEntity;

import lombok.Getter;
import lombok.Setter;

/**
 * @author José Maria Delgado Sanchez
 */
 @Entity
 @Table(name="boards_cards",
 uniqueConstraints = @UniqueConstraint(name = "uniqueBoardCard", columnNames = {"board_id", "card_id"}))
 public class BoardCard extends BaseEntity{

    @Getter
    @Setter
    @ManyToOne(optional=false, cascade = CascadeType.ALL)
    @JoinColumn(name="board_id")
    private Board board;

    @Getter
    @Setter
    @ManyToOne(optional=false, cascade = CascadeType.ALL)
    @JoinColumn(name="card_id")
    private Card card;

    @Setter
    @Getter
    @Column(name="sold")
    private Boolean sold;
 }