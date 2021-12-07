package org.springframework.samples.petclinic.modules.statistics.achievement;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.samples.petclinic.modules.statistics.metrics.MetricService;
import org.springframework.samples.petclinic.modules.statistics.metrics.MetricType;
import org.springframework.samples.petclinic.user.User;
import org.springframework.samples.petclinic.user.UserService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * /* @author Carlos Varela Soult
 */

@Service
public class AchievementService {

    @Autowired
    private AchievementRepository achievementRepository;

    @Autowired
    private MetricService metricService;

    @Autowired
    private UserService userService;

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
            Integer score = metricService.getScoreByUser(metric, user);
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
}