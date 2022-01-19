package org.springframework.samples.kingoftokyo.modules.statistics.metrics;

/**
 * @author Jose Maria Delgado Sanchez
*/
public enum MetricType {
        gamesPlayed("Partidas jugadas"),
        wins("Victorias"),
        cardsUsed("Cartas compradas"),
        turnsTokyo("Turnos en tokyo");

        private final String name;

        private MetricType(final String name){
                this.name = name;
        }

        public String getName(){
                return name;
        }
}
