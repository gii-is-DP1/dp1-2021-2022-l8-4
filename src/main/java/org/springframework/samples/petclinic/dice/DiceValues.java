package org.springframework.samples.petclinic.dice;

/**
 * @author Ricardo Nadal Garcia
 */

public enum DiceValues {
    ONE("/resources/images/diceValues/one.png"),
    TWO("/resources/images/diceValues/two.png"),
    THREE("/resources/images/diceValues/three.png"),
    ATTACK("/resources/images/diceValues/attack.png"),
    HEAL("/resources/images/diceValues/heal.png"),
    ENERGY("/resources/images/diceValues/energy.png");

    private final String icon;

    private DiceValues(final String icon){
        this.icon = icon;
    }

    public String getIcon(){
        return icon;
    }
}
