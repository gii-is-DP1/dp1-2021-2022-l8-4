package org.springframework.samples.petclinic.playercard;

import java.util.List;
import java.util.Set;
import java.util.function.Consumer;
import java.util.stream.Collectors;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.samples.petclinic.card.Card;
import org.springframework.samples.petclinic.card.CardType;
import org.springframework.samples.petclinic.dice.Roll;
import org.springframework.samples.petclinic.game.Game;
import org.springframework.samples.petclinic.game.GameService;
import org.springframework.samples.petclinic.game.MapGameRepository;
import org.springframework.samples.petclinic.gamecard.GameCard;
import org.springframework.samples.petclinic.gamecard.GameCardService;
import org.springframework.samples.petclinic.player.Player;
import org.springframework.samples.petclinic.player.PlayerService;
import org.springframework.samples.petclinic.user.UserService;
import org.springframework.stereotype.Service;

/**
 * @author Jose Maria Delgado Sanchez
 * @author Ricardo Nadal Garcia
 */
@Service
public class PlayerCardService {

    @Autowired
    private PlayerCardRepository playerCardRepository;

    @Autowired
    private GameCardService gameCardService;

    @Autowired
    private GameService gameService;

    @Autowired
    private PlayerService playerService;


    @Autowired
    private UserService userService;

    @Transactional
    public void savePlayerCard(PlayerCard playerCard) throws DataAccessException {
        playerCardRepository.save(playerCard);
    }

    /**
     * Find cards that can be use by a player
     * 
     * @param player player
     * @return Set of cards that the player has not used yet
     */
    @Transactional
    // THIS CAN BE IMPLEMENTED IN A REPOSITORY METHOD
    public Set<Card> findAvailableCardsByPlayer(Player player) {
        return playerService.findPlayerById(player.getId()).getPlayerCard().stream()
                .filter(x -> x.getDiscarded() == Boolean.FALSE).map(x -> x.getCard()).collect(Collectors.toSet());
    }

    /**
     * Check if the player can buy the card then buys it. A player can buy a card if
     * he/she is still alive, the card is in the game's shop, the game still being
     * played and the player has enough energy points. This method check if the user
     * linked to the player is the same user that made the request in order to avoid
     * cheating and interfiering other players gameplay
     * 
     * @param player buying the card
     * @param card   card that the player wants to buy
     */
    // SHOULD ASO CHECK IF IT IS THE PLAYER'S TURN
    @Transactional
    public void buyCard(Player player, Card card) {
        // Retrieve the game linked to the player to check if the card is available to
        // buy
        Roll roll=MapGameRepository.getInstance().getRoll(player.getGame().getId());
        Game game = player.getGame();
        List<Card> availableCards = gameCardService.findAvailableCardsByGame(game);
        if (availableCards.contains(card) && game.isOnGoing() && !player.isDead()
                && userService.isAuthUserPlayingAsPlayer(player) 
                && gameService.isPlayerTurn(player.getGame().getId())
                && roll.isFinished()) {

            // Check if the player has enough energy
            Integer energyPoints = player.getEnergyPoints();
            Integer cost = card.getCost();
            if (energyPoints >= cost) {

                // Calculate new energyPoints value
                player.setEnergyPoints(energyPoints - cost);

                PlayerCard playerCard = new PlayerCard(player, card);
                savePlayerCard(playerCard);

                // Update status of the card
                GameCard gameCard = gameCardService.findByGameCard(game, card);
                gameCard.setSold(Boolean.TRUE);


                //Check if cards is used when bought
               useCardDiscardType(card,player);
               
                // Show new cards
                gameCardService.showCards(game);
            }
        }
    }

    @Transactional
    private void useCardDiscardType(Card card,Player player) {
        if(card.getType().equals(CardType.DESCARTAR)){
            card.getCardEnum().effect(player,playerService);
        }
    }

    @Transactional
    public void discardShopCards(Player player) {
        Game game = player.getGame();
        Roll roll=MapGameRepository.getInstance().getRoll(player.getGame().getId());
        List<Card> availableCards = gameCardService.findAvailableCardsByGame(game);
        if (game.isOnGoing() && !player.isDead()
                && userService.isAuthUserPlayingAsPlayer(player) 
                && gameService.isPlayerTurn(player.getGame().getId()) 
                && roll.isFinished()){

            // Check if the player has enough energy
            Integer energyPoints = player.getEnergyPoints();
            Integer cost = 2;
            if (energyPoints >= cost) {

                // Calculate new energyPoints value
                player.setEnergyPoints(energyPoints - cost);

                // Update status of the cards
                for(Card card:availableCards){
                    GameCard gameCard = gameCardService.findByGameCard(game, card);
                    gameCard.setSold(Boolean.TRUE);
                }
               
                // Show new cards
                gameCardService.showCards(game);
            }
        }
    }


    


   
}
