package org.springframework.samples.petclinic.modules.statistics.achievement;

import java.util.HashSet;
import java.util.Set;

import org.springframework.samples.petclinic.modules.statistics.metrics.MetricType;

public class AchievementFactory implements AchievementAbstractFactory<Achievement> {

    @Override
    public Achievement create(MetricType metric) {
        if(isIntegerType(metric)){
            return new IntegerAchievement();
        }else{
            return null;
        }
    }

    private static Boolean isIntegerType(MetricType metric){
        Set<MetricType> integerTypes = new HashSet<>();
        integerTypes.add(MetricType.wins);
        integerTypes.add(MetricType.cardsUsed);
        integerTypes.add(MetricType.gamesPlayed);
        return integerTypes.contains(metric);
    }
    
}
