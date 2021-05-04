package com.datngo.service;

import com.datngo.entity.Weather;
import com.datngo.repositories.WeatherRepository;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;
import org.json.JSONArray;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Locale;

@Component
public class WeatherServiceImpl {

    private final Logger log = LoggerFactory.getLogger(WeatherServiceImpl.class);

    @Value("${api-key}")
    private String API_KEY;

    @Autowired
    private WeatherRepository weatherRepository;

    public Weather getAndSaveWeatherByCityName(String nameCity) {
        String url = "http://api.openweathermap.org/data/2.5/weather?q=" + nameCity + "&appid=" + API_KEY + "&units=metric";
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder().url(url).build();
        log.debug("URL -----> {}", url);
        try {
            Response response = client.newCall(request).execute();
            String body = response.body().string();
            JSONObject jsonObject = new JSONObject(body);

            if(jsonObject.getInt("cod") != 404) {
                Weather weather = new Weather();
                weather.setInputCity(nameCity);
                weather.setCity(jsonObject.getString("name"));
                weather.setMinTemp(jsonObject.getJSONObject("main").getFloat("temp_min"));
                weather.setMaxTemp(jsonObject.getJSONObject("main").getFloat("temp_max"));
                JSONArray jsonWeatherArray = jsonObject.getJSONArray("weather");
                weather.setIcon(((JSONObject)jsonWeatherArray.get(0)).optString("icon"));
                weather.setMain(((JSONObject)jsonWeatherArray.get(0)).optString("main"));
                weather.setDate(convertUTCTimeToLocalTime(jsonObject.getInt("dt")));
                weather.setCreateOn(LocalDateTime.now());
                weather.setUpdateOn(LocalDateTime.now());

                Weather weatherNew = weatherRepository.save(weather);
                log.debug("response -----> {}", jsonObject);
                return weatherNew;
            } else {
                return null;
            }
        } catch (Exception e) {
            log.debug(e.getMessage());
            return null;
        }
    }

    public Weather updateWeatherByCityName(Weather weather) {
        String url = "http://api.openweathermap.org/data/2.5/weather?q=" + weather.getCity() + "&appid=" + API_KEY + "&units=metric";
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder().url(url).build();
        log.debug("URL -----> {}", url);
        try {
            Response response = client.newCall(request).execute();
            String body = response.body().string();
            JSONObject jsonObject = new JSONObject(body);

            weather.setMinTemp(jsonObject.getJSONObject("main").getFloat("temp_min"));
            weather.setMaxTemp(jsonObject.getJSONObject("main").getFloat("temp_max"));
            JSONArray jsonWeatherArray = jsonObject.getJSONArray("weather");
            weather.setIcon(((JSONObject)jsonWeatherArray.get(0)).optString("icon"));
            weather.setMain(((JSONObject)jsonWeatherArray.get(0)).optString("main"));
            weather.setDate(convertUTCTimeToLocalTime(jsonObject.getInt("dt")));
            weather.setUpdateOn(LocalDateTime.now());

            Weather weatherUpdate = weatherRepository.save(weather);
            log.debug("response -----> {}", jsonObject);
            return weatherUpdate;
        } catch (Exception e) {
            log.debug(e.getMessage());
            return null;
        }
    }

    public static LocalDateTime convertUTCTimeToLocalTime(int UTCTime) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
        java.util.Date currenTimeZone=new java.util.Date((long)UTCTime*1000);
        return LocalDateTime.ofInstant(currenTimeZone.toInstant(), ZoneId.systemDefault());
    }

}
