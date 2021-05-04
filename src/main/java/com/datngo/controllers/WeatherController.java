package com.datngo.controllers;

import com.datngo.entity.Weather;
import com.datngo.repositories.WeatherRepository;
import com.datngo.service.WeatherServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/weathers")
public class WeatherController {

    @Autowired
    private WeatherRepository weatherRepository;

    @Autowired
    private WeatherServiceImpl weatherService;

    @GetMapping
    public Iterable<Weather> get(){
        return  weatherRepository.findAll(Sort.by(Sort.Direction.ASC, "id"));
    }

    @PostMapping
    public Weather saveWeatherByCityName(@RequestBody Map<String, Object> data){
        try {
            String nameCity = data.get("name").toString();
            Weather weather = weatherService.getAndSaveWeatherByCityName(nameCity);
            return weather;
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return null;
    }

}
