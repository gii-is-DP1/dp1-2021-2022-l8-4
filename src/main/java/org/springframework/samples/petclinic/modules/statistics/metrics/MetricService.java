package org.springframework.samples.petclinic.modules.statistics.metrics;

import java.util.ArrayList;
import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.samples.petclinic.user.User;
import org.springframework.samples.petclinic.user.UserService;
import org.springframework.stereotype.Service;


@Service
public class MetricService {

    @Autowired
    private MetricRepository metricRepository;

    @Autowired
    private UserService userService;

    /**
     * 
     * @return Map or Users with their associated score of games played ordered
     */
    @Transactional
    public List<MetricData> gamesPlayed(){       
        return parseMetricData(metricRepository.gamesPlayed());
    }

    /**
     * 
     * @param data
     * @return Parsed data from the repository query
     */
    @Transactional
    public List<MetricData> parseMetricData(List<Long[]> data){
        List<MetricData> parsedData = new ArrayList<MetricData>();

        for(Long[] pair: data){
            User user = userService.findUserById(pair[0].intValue()).get();
            Long score = pair[1];
            MetricData metric = new MetricData(user,score);
           parsedData.add(metric);
        }  
        return parsedData;
    }


    @Transactional
    public List<MetricData> statisticsByMetricType(MetricType metric){
        List<MetricData> statistics = new ArrayList<MetricData>();

        switch(metric){
            case gamesPlayed:
                statistics = gamesPlayed();
                break;
            case cardsUsed:      
            
                break;
            case wins:
                
                break;
            default:
                break;
        }
        return statistics;
    }

}
