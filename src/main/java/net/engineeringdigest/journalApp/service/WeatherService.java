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
    @Autowired
    private RestTemplate restTemplate;
    @Value("${weather.api.key}")
    private String apiKey;
    @Autowired
    private AppCache appCache;

    public WeatherResponse getWeatherResponse(String city){
        String finalAPI = appCache.APP_CACHE.get("weather_api").replace("<city>", city).replace("<apiKey>",apiKey);
        ResponseEntity<WeatherResponse> response = restTemplate.exchange(finalAPI, HttpMethod.GET,null, WeatherResponse.class);
        WeatherResponse body= response.getBody();
        return body;
    }

}
