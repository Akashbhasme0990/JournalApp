package net.engineeringdigest.journalApp.service;

import net.engineeringdigest.journalApp.apiResponse.WeatherResponse;
import net.engineeringdigest.journalApp.cache.AppCache;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
public class WeatherService {
    private final RestTemplate restTemplate;
    @Value("${weather.api.key}")
    private String apiKey;
    private final AppCache appCache;

    private final RedisService redisService;

    public WeatherService(RedisService redisService, AppCache appCache, RestTemplate restTemplate) {
        this.redisService = redisService;
        this.appCache = appCache;
        this.restTemplate = restTemplate;
    }

    public WeatherResponse getWeatherResponse(String city){
        WeatherResponse weatherResponse = redisService.get("weather_of_" + city, WeatherResponse.class);
        if (weatherResponse != null){
            return weatherResponse;
        }else{
            String finalAPI = appCache.APP_CACHE.get("weather_api").replace("<city>", city).replace("<apiKey>",apiKey);
            ResponseEntity<WeatherResponse> response = restTemplate.exchange(finalAPI, HttpMethod.GET,null, WeatherResponse.class);
            WeatherResponse body= response.getBody();
            if (body != null){
                redisService.set("weather_of_"+city,body, 300L);
            }
            return body;
        }
    }

}
