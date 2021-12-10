package org.springframework.samples.petclinic.modules.statistics.metrics;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
    public List<Metric> gamesPlayed(){       
        return metricRepository.gamesPlayed();
    }

    /**
     * 
     * @param data
     * @return Parsed data from the repository query
     */
    public List<Metric> parseMetricData(List<Long[]> data){
        List<Metric> parsedData = new ArrayList<Metric>();

        for(Long[] pair: data){
            User user = userService.findUserById(pair[0].intValue()).get();
            Long score = pair[1];
            Metric metric = new Metric(user,score);
           parsedData.add(metric);
        }  
        return parsedData;
    }


}
