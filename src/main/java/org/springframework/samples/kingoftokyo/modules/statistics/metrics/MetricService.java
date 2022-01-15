package org.springframework.samples.kingoftokyo.modules.statistics.metrics;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;


@Service
public class MetricService {

    @Autowired
    private MetricRepository metricRepository;

    /**
     * 
     * @return List of Users with their associated score of games played ordered
     */
    @Transactional
    public Page<MetricData> gamesPlayedRanking(int pageNumber, int numberOfElements){
        PageRequest pageable = PageRequest.of(pageNumber, numberOfElements);    
        return metricRepository.gamesPlayedRanking(pageable);
    }

    /**
     * 
     * @return List of Users with their associated score of wins ordered
     */
    @Transactional
    public Page<MetricData> winsRanking(int pageNumber, int numberOfElements){    
        PageRequest pageable = PageRequest.of(pageNumber, numberOfElements);    
        return metricRepository.winsRanking(pageable);
    }


    @Transactional
    public Page<MetricData> rankingByMetricType(MetricType metric, int pageNumber, int numberOfElements){
        switch(metric){
            case gamesPlayed:
                return gamesPlayedRanking(pageNumber, numberOfElements);
            case wins:
                return winsRanking(pageNumber, numberOfElements);
            default:
                break;
        }
        return null;
    }

}
