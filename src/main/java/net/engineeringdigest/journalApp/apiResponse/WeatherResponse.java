package net.engineeringdigest.journalApp.apiResponse;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.util.ArrayList;


@Getter
@Setter
public class WeatherResponse {
    private Current current;
    @Getter
    @Setter
    public class Current {
        private int temperature;
        @JsonProperty("weather_descriptions")
        private ArrayList<String> weatherDescriptions;
        @JsonProperty("feelslike")
        private int feelsLike;
    }
}
