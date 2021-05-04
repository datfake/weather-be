package com.datngo;

import com.datngo.entity.Weather;
import com.datngo.repositories.WeatherRepository;
import com.datngo.service.WeatherServiceImpl;
import com.google.gson.Gson;
import org.hamcrest.Matchers;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
@WebAppConfiguration
@EnableAutoConfiguration(exclude = {org.springframework.boot.autoconfigure.gson.GsonAutoConfiguration.class})
public class WeatherControllerTest {

    @Autowired
    private WeatherRepository weatherRepository;

    @Autowired
    private WeatherServiceImpl weatherService;

    private Weather weatherInit;

    @Autowired
    private MockMvc mockMvc;

    @Before
    public void init(){
        weatherInit = weatherService.getAndSaveWeatherByCityName("hanoi");
    }

    @After
    public void destroy(){
        weatherRepository.deleteAll();
    }

    @Test
    public void test_save_ok() throws Exception{

        Gson gson = new Gson();
        Map<String, Object> city = new HashMap<>();
        city.put("name","hanoi");
        String json = gson.toJson(city);

        mockMvc.perform(post("/api/weathers")
                .contentType(MediaType.APPLICATION_JSON).content(json))
                .andExpect(status().isOk());


        ArrayList<Weather> weathers = (ArrayList<Weather>) weatherRepository.findAll();
        Weather weather = weathers.get(weathers.size()-1);

        assertEquals (weather.getInputCity(), "hanoi");
    }

    @Test
    public void test_getAllWeather() throws Exception{
        mockMvc.perform(get("/api/weathers"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.data", Matchers.hasSize(1)))
                .andExpect(jsonPath("$.data[0].inputCity", Matchers.equalTo("hanoi")));
    }
}
