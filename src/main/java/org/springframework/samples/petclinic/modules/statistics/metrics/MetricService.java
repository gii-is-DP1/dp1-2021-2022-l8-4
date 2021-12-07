package org.springframework.samples.petclinic.modules.statistics.metrics;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.samples.petclinic.user.User;
import org.springframework.stereotype.Service;

@Service
public class MetricService {

    @Autowired
    private MetricRepository metricRepository;

    @Transactional
    public Integer gamesPlayedByUser(User user) {
        return metricRepository.gamesPlayedByUser(user.getId());
    }

    @Transactional
    public Integer winsByUser(User user) {
        return metricRepository.winsByUser(user.getId());
    }

    @Transactional
    public Integer cardsUsedByUser(User user) {
        return metricRepository.cardsUsedByUser(user.getId());
    }

    /**
     * Given a metric type and a user, returns the score in that metric by the user
     * 
     * @param metric type
     * @return Integer score
     */
    @Transactional
    public Integer getScoreByUser(MetricType metric, User user) {
        Integer score = null;
        
        switch(metric){
            case gamesPlayed:
                score = gamesPlayedByUser(user);
                break;
            case cardsUsed:
                score = cardsUsedByUser(user);
                break;
            case wins:
                score = winsByUser(user);
                break;
            default:
                break;
        }
        return score;
    }
}
