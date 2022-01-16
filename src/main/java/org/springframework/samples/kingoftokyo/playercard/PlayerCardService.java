package org.springframework.samples.kingoftokyo.playercard;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.samples.kingoftokyo.card.Card;
import org.springframework.samples.kingoftokyo.card.CardType;
import org.springframework.samples.kingoftokyo.dice.Roll;
import org.springframework.samples.kingoftokyo.game.Game;
import org.springframework.samples.kingoftokyo.game.GameService;
import org.springframework.samples.kingoftokyo.game.MapGameRepository;
import org.springframework.samples.kingoftokyo.gamecard.GameCard;
import org.springframework.samples.kingoftokyo.gamecard.GameCardService;
import org.springframework.samples.kingoftokyo.player.Player;
import org.springframework.samples.kingoftokyo.player.PlayerService;
import org.springframework.samples.kingoftokyo.player.exceptions.InvalidPlayerActionException;
import org.springframework.samples.kingoftokyo.user.UserService;
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
    private MapGameRepository mapGameRepository;

    @Autowired
    private UserService userService;

    @Transactional
    public void savePlayerCard(PlayerCard playerCard) throws DataAccessException {
        playerCardRepository.save(playerCard);
    }

    @Transactional
    public PlayerCard findByPlayerCard(Player player, Card card) throws DataAccessException{
        return playerCardRepository.findByPlayerCard(player.getId(), card.getId());
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
    @Transactional
    public void buyCard(Player player, Card card) throws InvalidPlayerActionException {
        // Retrieve the game linked to the player to check if the card is available to
        // buy
        Roll roll=mapGameRepository.getRoll(player.getGame().getId());
        Game game = player.getGame();
        List<Card> availableCards = gameCardService.findAvailableCardsByGame(game);
        if (availableCards.contains(card) && game.isOnGoing() && !player.isDead()
                && userService.isAuthUserPlayingAsPlayer(player) 
                && gameService.isPlayerTurn(player.getGame().getId())
                && roll.isFinished()) {

            // Check if the player has enough energy
            Integer energyPoints = player.getEnergyPoints();
            Integer cost = card.getCost();
            energyPoints=useCardsWhenBuy(player, energyPoints, cost);
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
            }else{
                throw new InvalidPlayerActionException("El jugador no tiene suficiente energia para comprar la carta");
            }
        }else{
            throw new InvalidPlayerActionException("No se puede llevar a cabo la compra de la carta");
        }
    }

    //Use all card from a player that are activated when you buy a card
    public Integer useCardsWhenBuy(Player player,Integer energy,Integer cost) {
        for(Card card:player.getAvailableCards()) {
            energy=card.getCardEnum().effectBuy(player, playerService, energy,cost,mapGameRepository);
        }
        return energy;
    }

    @Transactional
    private void useCardDiscardType(Card card,Player player) {
        if(card.getType().equals(CardType.DESCARTAR)){
            card.getCardEnum().effect(player,playerService,mapGameRepository);
        }
    }

    @Transactional
    public void discardShopCards(Player player) throws InvalidPlayerActionException {
        Game game = player.getGame();
        Roll roll=mapGameRepository.getRoll(player.getGame().getId());
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
            }else{
                throw new InvalidPlayerActionException("El jugador no tiene suficiente energia para descartar la tienda");
            }
        }else{
            throw new InvalidPlayerActionException("No se puede llevar a cabo el descarte de la tienda");
        }
    }


    


   
}
