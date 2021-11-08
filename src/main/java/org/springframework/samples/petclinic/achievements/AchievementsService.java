package org.springframework.samples.petclinic.achievements;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
/* @author Carlos Varela Soult
*/

@Service
public class AchievementsService{
    @Autowired
    private AchievementsRepository achievementsRepository;

    @Transactional
    public Iterable<Achievements> findAll() {
        Iterable<Achievements> all = achievementsRepository.findAll();
        return all;
    }

    @Transactional
    public int achievementsCount() {
        return (int) achievementsRepository.count();
    }

    @Transactional
    public void saveAchievement(Achievements achievement) {
        achievementsRepository.save(achievement);
    }

    @Transactional
    public Optional<Achievements> findAchievementById(int id) throws DataAccessException{
        return achievementsRepository.findById(id);
    }

    @Transactional
    public void deleteAchievement(Achievements achievement) {
        achievementsRepository.delete(achievement);
    }
}