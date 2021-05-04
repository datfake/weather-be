package com.datngo.repositories;

import com.datngo.entity.Weather;
import org.springframework.data.domain.Sort;
import org.springframework.data.repository.CrudRepository;

public interface WeatherRepository extends CrudRepository<Weather, Integer> {
    Iterable<Weather> findAll(Sort sort);
}
