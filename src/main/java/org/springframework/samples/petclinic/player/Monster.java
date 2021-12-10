package org.springframework.samples.petclinic.player;


/**
 * @author Ricardo Nadal Garcia
 * @author Jose Maria Delgado Sanchez
 */

public enum Monster {
    gigaZaur("GigaZaur","/resources/images/monsterIcons/gigazaur.png"),
    kraken("Kraken","/resources/images/monsterIcons/kraken.png"),
    mekaDracon("Meka Dragon","/resources/images/monsterIcons/mekadragon.png"),
    king("King","/resources/images/monsterIcons/king.png"),
    cyberBunny("CyberBunny","/resources/images/monsterIcons/cyberbunny.png"),
    alien("Alien","/resources/images/monsterIcons/alien.png");

    private final String name;
    private final String icon;

    private Monster(final String name, final String icon){
        this.name = name;
        this.icon = icon;
    }

    public String getName(){
        return name;
    }

    public String getIcon(){
        return icon;
    }
}
