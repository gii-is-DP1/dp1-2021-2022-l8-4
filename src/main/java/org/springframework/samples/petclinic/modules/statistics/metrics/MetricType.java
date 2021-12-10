package org.springframework.samples.petclinic.modules.statistics.metrics;

/**
 * @author Jose Maria Delgado Sanchez
*/
public enum MetricType {
        gamesPlayed("Partidas jugadas"),
        wins("Victorias"),
        cardsUsed("Cartas usadas");

        private final String name;

        private MetricType(final String name){
                this.name = name;
        }

        public String getName(){
                return name;
        }
}
