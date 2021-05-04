package com.datngo.controllers;

import com.datngo.entity.Weather;
import com.datngo.repositories.WeatherRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/weathers")
public class WeatherController {

    @Autowired
    private WeatherRepository weatherRepository;

    @GetMapping
    public Iterable<Weather> get(){
        return  weatherRepository.findAll(Sort.by(Sort.Direction.ASC, "id"));
    }
}
