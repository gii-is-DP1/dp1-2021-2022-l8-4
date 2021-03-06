package org.springframework.samples.kingoftokyo.card;

import java.util.List;
import java.util.Map;

import org.springframework.samples.kingoftokyo.dice.DiceValues;
import org.springframework.samples.kingoftokyo.dice.Roll;
import org.springframework.samples.kingoftokyo.game.MapGameRepository;
import org.springframework.samples.kingoftokyo.player.Player;
import org.springframework.samples.kingoftokyo.player.PlayerService;
import org.springframework.samples.kingoftokyo.playercard.PlayerCard;

/**
 * @author Ricardo Nadal Garcia
 * @author Noelia López Durán
 */

public enum CardEnum implements UseCardsInterface { // Primero estan todas las de descarte al usarlo
    APARTMENTBUILDING("Bloque de apartamentos", "Otorga 3 puntos de victoria") {

        @Override
        public void effect(Player player, PlayerService playerService, MapGameRepository mapGameRepository) {
            player.setVictoryPoints(player.getVictoryPoints() + 3);
            playerService.savePlayer(player);
        }

    },
    COMMUTERTRAIN("Tren de cercanias", "Otorga 2 puntos de victoria") {

        @Override
        public void effect(Player player, PlayerService playerService, MapGameRepository mapGameRepository) {
            player.setVictoryPoints(player.getVictoryPoints() + 2);
            playerService.savePlayer(player);
        }

    },
    ENERGIZE("Energizado", "Otorga 9 puntos de energía") {

        @Override
        public void effect(Player player, PlayerService playerService, MapGameRepository mapGameRepository) {
            player.setEnergyPoints(player.getEnergyPoints() + 9);
            playerService.savePlayer(player);
        }
    },
    FIREBLAST("Bola de fuego", "Todos los monstruos enemigos reciben 2 puntos de daño") {

        @Override
        public void effect(Player player, PlayerService playerService, MapGameRepository mapGameRepository) {
            for (Player play : player.getGame().getPlayers()) {
                if (!player.equals(play)) {
                    playerService.damagePlayer(play, 2);
                    playerService.savePlayer(play);
                }
            }
        }
    },
    EVACUATIONORDERS("Ordenes de evacuacion", "Todos los monstruos enemigos pierden 5 puntos de victoria") {

        @Override
        public void effect(Player player, PlayerService playerService, MapGameRepository mapGameRepository) {
            for (Player play : player.getGame().getPlayers()) {
                if (!player.equals(play)) {
                    playerService.substractVictoryPointsPlayer(play, 5);
                    playerService.savePlayer(play);
                }
            }
        }
    },
    HEAL("Curacion", "Tu monstruo se cura 2 puntos de vida") {
        @Override
        public void effect(Player player, PlayerService playerService, MapGameRepository mapGameRepository) {
            playerService.healDamage(player, 2);
            playerService.savePlayer(player);
        }
    },
    GASREFINERY("Refineria de gas",
            "Obtienes 2 puntos de victoria y todos los monstruos enemigos reciben 3 puntos de daño") {

        @Override
        public void effect(Player player, PlayerService playerService, MapGameRepository mapGameRepository) {
            player.setVictoryPoints(player.getVictoryPoints() + 2);
            playerService.savePlayer(player);

            for (Player play : player.getGame().getPlayers()) {
                if (!player.equals(play)) {
                    playerService.damagePlayer(play, 3);
                    playerService.savePlayer(play);
                }
            }
        }
    },

    HIGHALTITUDEBOMBING("Bombardeo de Gran Altura", "Todos los monstruos(incluyéndote a ti) reciben 3 puntos de daño") {

        @Override
        public void effect(Player player, PlayerService playerService, MapGameRepository mapGameRepository) {
            for (Player play : player.getGame().getPlayers()) {
                playerService.damagePlayer(play, 3);
                playerService.savePlayer(play);
            }
        }
    },

    JETFIGHTERS("Caza de combate", "Obtienes 5 puntos de victoria pero pierdes 4 puntos de vida") {

        @Override
        public void effect(Player player, PlayerService playerService, MapGameRepository mapGameRepository) {
            playerService.damagePlayer(player, 4);
            player.setVictoryPoints(player.getVictoryPoints() + 5);
            playerService.savePlayer(player);

        }
    },

    NATIONALGUARD("Guarda Nacional", "Obtienes 2 puntos de victoria pero pierdes 2 puntos de vida") {

        @Override
        public void effect(Player player, PlayerService playerService, MapGameRepository mapGameRepository) {
            playerService.damagePlayer(player, 2);
            player.setVictoryPoints(player.getVictoryPoints() + 2);
            playerService.savePlayer(player);

        }
    },

    CORNERSTORE("Bazar de la esquina", "Otorga 1 punto de victoria") {

        @Override
        public void effect(Player player, PlayerService playerService, MapGameRepository mapGameRepository) {
            player.setVictoryPoints(player.getVictoryPoints() + 1);
            playerService.savePlayer(player);

        }
    },

    EVENBIGGER("Coloso", "La vida maxima de tu monstruo pasa a ser 12 y se cura 2 puntos de vida") {

        @Override
        public void effect(Player player, PlayerService playerService, MapGameRepository mapGameRepository) {
            playerService.healDamage(player, 2);
            playerService.savePlayer(player);

        }
    },
    ACIDATTACK("Ataque ácido", "Obtienes en cada turno un dado de daño extra") {

        @Override
        public void effectInRoll(Player player, PlayerService playerService, MapGameRepository mapGameRepository) {
            Roll roll = mapGameRepository.getRoll(player.getGame().getId());

            List<DiceValues> dices = roll.getCardExtraValues();
            dices.add(DiceValues.ATTACK);
            roll.setCardExtraValues(dices);
            mapGameRepository.putRoll(player.getGame().getId(), roll);

        }
    },
    ALPHAMONSTER("Monstruo alfa", "Obtienes un punto de victoria cuando consigues al menos 1 dado de daño") {

        @Override
        public void effectInRoll(Player player, PlayerService playerService, MapGameRepository mapGameRepository) {
            Roll roll = mapGameRepository.getRoll(player.getGame().getId());

            Map<String, Integer> rollValues = playerService.countRollValues(roll.getValues());
            if (rollValues.get(damageString) > 0) {
                player.setVictoryPoints(player.getVictoryPoints() + 1);
                playerService.savePlayer(player);

            }
        }
    },
    FIREBREATHING("Aliento de fuego", "Dañas a tus monstruos vecinos cuando consigues almenos 1 dado de daño") {

        @Override
        public void effectInRoll(Player player, PlayerService playerService, MapGameRepository mapGameRepository) {
            Roll roll = mapGameRepository.getRoll(player.getGame().getId());

            Map<String, Integer> rollValues = playerService.countRollValues(roll.getValues());
            if (rollValues.get(damageString) > 0) {
                if (Boolean.TRUE.equals(player.isInTokyo())) {
                    for (Player playTokyo : player.getGame().playersInTokyo()) {
                        if (!player.getId().equals(playTokyo.getId())) {
                            playerService.damagePlayer(playTokyo, 1);
                            playerService.savePlayer(player);
                        }
                    }
                } else {
                    for (Player playOutTokyo : player.getGame().playersOutOfTokyo()) {
                        if (!player.getId().equals(playOutTokyo.getId())) {
                            playerService.damagePlayer(playOutTokyo, 1);
                            playerService.savePlayer(player);
                        }
                    }
                }
            }

        }
    },
    FRIENDOFCHILDREN("Amigo de los niños",
            "Si en tu turno obtienes al menos 1 punto de energía, ganas 1 punto de energía extra") {

        @Override
        public void effectInRoll(Player player, PlayerService playerService, MapGameRepository mapGameRepository) {
            Roll roll = mapGameRepository.getRoll(player.getGame().getId());

            Map<String, Integer> rollValues = playerService.countRollValues(roll.getValues());
            if (rollValues.get("energy") > 0) {
                List<DiceValues> dices = roll.getCardExtraValues();
                dices.add(DiceValues.ENERGY);
                roll.setCardExtraValues(dices);
                mapGameRepository.putRoll(player.getGame().getId(), roll);
            }

        }
    },
    EXTRAHEAD("Segunda cabeza", "Tienes un dado adicional") {

        @Override
        public void effectStartTurn(Player player, PlayerService playerService, MapGameRepository mapGameRepository) {
            Roll roll = mapGameRepository.getRoll(player.getGame().getId());
            List<DiceValues> dices = roll.getValues();

            dices.add(DiceValues.HEAL);
            roll.setValues(dices);

            mapGameRepository.putRoll(player.getGame().getId(), roll);

        }
    },
    GIANTBRAIN("Cerebro galaxia", "Tienes una tirada adicional") {

        @Override
        public void effectStartTurn(Player player, PlayerService playerService, MapGameRepository mapGameRepository) {
            Roll roll = mapGameRepository.getRoll(player.getGame().getId());

            roll.setMaxThrows(roll.getMaxThrows() + 1);

            mapGameRepository.putRoll(player.getGame().getId(), roll);

        }
    },
    COMPLETEDESTRUCTION("Destrucion total",
            "Si obtienes un dado de cada tipo obtienes 9 puntos de victoria adicionales") {

        @Override
        public void effectInRoll(Player player, PlayerService playerService, MapGameRepository mapGameRepository) {
            Roll roll = mapGameRepository.getRoll(player.getGame().getId());

            Map<String, Integer> rollValues = playerService.countRollValues(roll.getValues());
            if (rollValues.get("heal") > 0 && rollValues.get(damageString) > 0 && rollValues.get("energy") > 0
                    && rollValues.get("ones") > 0 && rollValues.get("twos") > 0 && rollValues.get("threes") > 0) {
                player.setVictoryPoints(player.getVictoryPoints() + 9);
                playerService.savePlayer(player);
            }

        }
    },
    ARMORPLATING("Armadura blindada", "Si tu monstruo fuera a recibir solo uno de daño, no recibes daño en su lugar") {

        @Override
        public Integer effectDamage(Player player, PlayerService playerService, Integer damage, MapGameRepository mapGameRepository) {
            if (damage == 1) {
                damage = 0;
            }
            return damage;
        }
    },
    CAMOUFLAGE("Camuflaje", "Por cada daño recibido, hay una posibilidad de 1 entre 6 de que que no haga daño") {

        @Override
        public Integer effectDamage(Player player, PlayerService playerService, Integer damage, MapGameRepository mapGameRepository) {
            Integer newDamage = damage;
            int random = 0;
            int max = 6;
            int min = 1;
            for (int i = 0; i < damage; i++) {
                random = (int) Math.floor(Math.random() * (max - min + 1) + min);
                if (random == 1) {
                    newDamage--;
                }
            }

            return newDamage;
        }
    },
    ITHASACHILD("¡Tiene un hijo!",
            "Si eres eliminado, descartas tus cartas, pierdes todos tus puntos de victoria y tu vida vuelve a 10") {

        @Override
        public Integer effectDamage(Player player, PlayerService playerService, Integer damage, MapGameRepository mapGameRepository) {
            if (damage > player.getLifePoints()) {
                player.setLifePoints(10);
                player.setVictoryPoints(0);
                List<PlayerCard> playerCards = player.getPlayerCard();
                playerCards.forEach(card -> card.setDiscarded(Boolean.TRUE));
                player.setPlayerCard(playerCards);
                playerService.savePlayer(player);
                damage = 0;
            }
            return damage;
        }
    },
    ALIENMETABOLISM("Metabolismo monstruoso",
            "Cuando compras una carta te cuesta uno de energia menos") {

        @Override
        public Integer effectBuy(Player player, PlayerService playerService, Integer energy, Integer cost, MapGameRepository mapGameRepository) {
            energy++;
            return energy;
        }
    },
    DEDICATEDNEWSTEAM("El preferido de la prensa",
            "Obtienes 1 punto de victoria al comprar una carta") {

        @Override
        public Integer effectBuy(Player player, PlayerService playerService, Integer energy, Integer cost, MapGameRepository mapGameRepository) {
            if (energy >= cost) {
                player.setVictoryPoints(player.getVictoryPoints() + 1);
            }
            return energy;
        }
    },
    HERBIVORE("Herbivoro", "Obtienes 1 punto de victoria si no atacas a nadie en un turno") {

        @Override
        public void effectEndTurn(Player player, PlayerService playerService, MapGameRepository mapGameRepository) {
            Roll roll = mapGameRepository.getRoll(player.getGame().getId());
            Map<String, Integer> tiradas = playerService.countRollValues(roll.getValues());
            Map<String, Integer> efectosCartas = playerService.countRollValues(roll.getCardExtraValues());

            if (tiradas.get(damageString) + efectosCartas.get(damageString) == 0) {
                player.setVictoryPoints(player.getVictoryPoints() + 1);
            }

        }
    },
    ENERGYHOARDER("Acaparador de energia", "Obtienes 1 punto de victoria por cada 6 puntos de energia al final de tu turno") {

        @Override
        public void effectEndTurn(Player player, PlayerService playerService, MapGameRepository mapGameRepository) {

            Integer energyCount=player.getEnergyPoints();
            Integer victoryPoints=Math.floorDiv(energyCount, 6);
            
            player.setVictoryPoints(player.getVictoryPoints() + victoryPoints);
            

        }
    },
    NOVABREATH("Aliento nova", "Al atacar dañas a todos los demas jugadores") {

        @Override
        public void effectAfterRoll(Player playerRolling, PlayerService playerService, MapGameRepository mapGameRepository) {
            Roll roll = mapGameRepository.getRoll(playerRolling.getGame().getId());
            Map<String, Integer> tiradas = playerService.countRollValues(roll.getValues());
            Map<String, Integer> efectosCartas = playerService.countRollValues(roll.getCardExtraValues());

            for (Player player : playerRolling.getGame().getPlayers()) {
                if (player.isOutOfTokyo() && playerRolling.isOutOfTokyo() || player.isInTokyo() && playerRolling.isInTokyo()) {
                    Integer damageTotal=tiradas.get(damageString) + efectosCartas.get(damageString);
                    playerService.damagePlayer(player,damageTotal);
                }
            }

        }
    },
    GOURMET("Gourmet", "Cuando consigas 3 o más dados 'ONE', recibirás 2 puntos de victoria extra") {
        @Override
        public void effectInRoll(Player player, PlayerService playerService, MapGameRepository mapGameRepository) {
            Roll roll = mapGameRepository.getRoll(player.getGame().getId());

            Map<String, Integer> rollValues = playerService.countRollValues(roll.getValues());
            if (rollValues.get("ones") > 2) {
                player.setVictoryPoints(player.getVictoryPoints() + 2);
                playerService.savePlayer(player);
            }

        }
    };

    private String name;
    private String description;

    private CardEnum(String name, String description) {
        this.name = name;
        this.description = description;
    }

    public String getName() {
        return this.name;
    }

    public String getDescription() {
        return this.description;
    }

    public void setName(String newName) {
        this.name = newName;
    }

    public void setDescription(String newDescription) {
        this.description = newDescription;
    }

}
