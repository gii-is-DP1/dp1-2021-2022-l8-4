package org.springframework.samples.petclinic.playercard;

import java.util.Set;
import java.util.stream.Collectors;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.samples.petclinic.board.Board;
import org.springframework.samples.petclinic.boardcard.BoardCard;
import org.springframework.samples.petclinic.boardcard.BoardCardService;
import org.springframework.samples.petclinic.card.Card;
import org.springframework.samples.petclinic.game.Game;
import org.springframework.samples.petclinic.player.Player;
import org.springframework.samples.petclinic.player.PlayerService;
import org.springframework.stereotype.Service;

/**
 * @author Jose Maria Delgado Sanchez
 */
@Service
public class PlayerCardService {

    @Autowired
    private PlayerCardRepository playerCardRepository;

    @Autowired
    private BoardCardService boardCardService;

    @Autowired
    private PlayerService playerService;

    @Transactional
    public Set<Card> findAvailableCardsByPlayer(Player player) {
        return playerService.findPlayerById(player.getId()).getPlayerCard()
                                            .stream()
                                            .filter(x -> x.getDiscarded() == false)
                                            .map(x -> x.getCard())
                                            .collect(Collectors.toSet());
    }

    @Transactional
    public void savePlayerCard(PlayerCard playerCard) throws DataAccessException{
        playerCardRepository.save(playerCard);
    }
    
    /**
     * Check if the player is eligible to buy the card then buys it if it is possible
     * @param player buying the card
     * @param card card that the player wants to buy
     */
    @Transactional
    public void buyCard(Player player, Card card){
        //Retrieve the game linked to the player to check if the card is available to buy
        Game game = player.getGame();
        Board board = game.getBoard();
        Set<Card> availableCards = boardCardService.findAvailableCardsByBoard(board);
        if(availableCards.contains(card) 
            && !game.isFinished()
            && game.isStarted()
            && !player.isDead()){

            //Check if the player has enough energy
            Integer energyPoints = player.getEnergyPoints();
            Integer cost = card.getCost();
            if(energyPoints >= cost){
            
            //Calculate new energyPoints value
            player.setEnergyPoints(energyPoints-cost);

            //Create a PlayerCard object and save it
            PlayerCard playerCard = new PlayerCard(player, card);
            savePlayerCard(playerCard);

            //Update status of the card
            BoardCard boardCard = boardCardService.findByBoardCard(board, card);
            boardCard.setSold(true);
            }
        }   
    }
}
