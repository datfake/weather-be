package com.datngo.schedule;

import com.datngo.entity.Weather;
import com.datngo.repositories.WeatherRepository;
import com.datngo.service.WeatherServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.concurrent.TimeUnit;

@Service
public class WeatherSchedule {

    @Autowired
    private WeatherRepository weatherRepository;

    @Autowired
    private WeatherServiceImpl weatherService;

    @Scheduled(cron="0 * * * * *")
    public void updateInforWeather() {
        weatherRepository.findAll(Sort.by(Sort.Direction.ASC, "id")).forEach(weather -> {
            if(LocalDateTime.now().isAfter(weather.getUpdateOn().plusMinutes(10))) {
                weatherService.updateWeatherByCityName(weather);
            }
        });
    }
}
