package org.springframework.samples.petclinic.achievement;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
/* @author Carlos Varela Soult
*/

@Service
public class AchievementService{
    @Autowired
    private AchievementRepository achievementRepository;

    @Transactional
    public Iterable<Achievement> findAll() {
        Iterable<Achievement> all = achievementRepository.findAll();
        return all;
    }

    @Transactional
    public int achievementsCount() {
        return (int) achievementRepository.count();
    }

    @Transactional
    public void saveAchievement(Achievement achievement) {
        achievementRepository.save(achievement);
    }

    @Transactional
    public Optional<Achievement> findAchievementById(int id) throws DataAccessException{
        return achievementRepository.findById(id);
    }

    @Transactional
    public void deleteAchievement(Achievement achievement) {
        achievementRepository.delete(achievement);
    }
}