package com.datngo.controllers;

import com.datngo.entity.Weather;
import com.datngo.repositories.WeatherRepository;
import com.datngo.service.WeatherServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/weathers")
public class WeatherController {

    @Autowired
    private WeatherRepository weatherRepository;

    @Autowired
    private WeatherServiceImpl weatherService;

    @GetMapping
    public Map<String, Object> get(){
        Map<String, Object> result = new HashMap<String, Object>();
        try {
            result.put("code", 200);
            result.put("message", "get success");
            result.put("data", weatherRepository.findAll(Sort.by(Sort.Direction.ASC, "id")));
        }
        catch (Exception e) {
            result.put("code", 500);
            result.put("message", "get fail");
        }
        return result;
    }

    @PostMapping
    public Map<String, Object> saveWeatherByCityName(@RequestBody Map<String, Object> data){
        Map<String, Object> result = new HashMap<String, Object>();
        result.put("code", 500);
        result.put("message", "save fail");
        try {
            String nameCity = data.get("name").toString();
            Weather weather = weatherService.getAndSaveWeatherByCityName(nameCity);
            if(weather != null) {
                result.put("code", 201);
                result.put("message", "save success");
                result.put("data", weather);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return result;
    }

}
