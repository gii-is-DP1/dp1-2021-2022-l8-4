package org.springframework.samples.kingoftokyo.modules.statistics.achievement;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.samples.kingoftokyo.modules.statistics.metrics.MetricType;
import org.springframework.samples.kingoftokyo.user.User;
import org.springframework.samples.kingoftokyo.user.UserService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author Carlos Varela Soult
 * @author Jose Maria Delgado Sanchez
 */

@Service
public class AchievementService {

    private AchievementRepository achievementRepository;
    private UserService userService;

    @Autowired
    public AchievementService(AchievementRepository achievementRepository, UserService userService){
        this.userService = userService;
        this.achievementRepository = achievementRepository;
    }


    @Transactional
    public Iterable<Achievement> findAll() {
        Iterable<Achievement> all = achievementRepository.findAll();
        return all;
    }

    @Transactional
    public void saveAchievement(Achievement achievement) {
        achievementRepository.save(achievement);
    }

    @Transactional
    public Optional<Achievement> findAchievementById(int id) throws DataAccessException {
        return achievementRepository.findById(id);
    }

    @Transactional
    public void deleteAchievement(Achievement achievement) {
        achievementRepository.delete(achievement);
    }



    /**
     * Given an user, returns the score linked to each metric
     * 
     * @param user
     * @return Map scores for each metrics
     */
    @Transactional
    public Map<MetricType, Integer> scoresByUser(User user) {
        Map<MetricType, Integer> scores = new HashMap<>();
        for (MetricType metric : MetricType.values()) {
            Integer score = getScoreByUser(metric, user);
            scores.putIfAbsent(metric, score);
        }
        return scores;
    }

    /**
     * Check if the user has new obtained achievements
     * 
     * @param user
     */
    @Transactional
    public void checkAchievements(User user) {
        Map<MetricType, Integer> scores = scoresByUser(user);

        Set<Achievement> obtainedAchievements = StreamSupport.stream(findAll().spliterator(), Boolean.FALSE)
                .filter(a -> a.isObtained(scores.get(a.getMetric()).longValue()))
                .collect(Collectors.toSet());

        user.setAchievements(obtainedAchievements);
        userService.saveUser(user);
    }

    @Transactional
    public Integer gamesPlayedByUser(User user) {
        return achievementRepository.gamesPlayedByUser(user.getId());
    }

    @Transactional
    public Integer winsByUser(User user) {
        return achievementRepository.winsByUser(user.getId());
    }

    @Transactional
    public Integer cardsUsedByUser(User user) {
        return achievementRepository.cardsUsedByUser(user.getId());
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