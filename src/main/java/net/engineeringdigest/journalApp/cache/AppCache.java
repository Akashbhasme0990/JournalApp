package net.engineeringdigest.journalApp.cache;

import net.engineeringdigest.journalApp.entity.ConfigJournalAppEntity;
import net.engineeringdigest.journalApp.repository.WeatherRepository;
import net.engineeringdigest.journalApp.service.WeatherService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class AppCache {
    @Autowired
    private WeatherRepository weatherRepository;

    public Map<String, String> APP_CACHE;

    @PostConstruct
    public void init() {
        List<ConfigJournalAppEntity> all  = weatherRepository.findAll();
        APP_CACHE = new HashMap<>();
        for (ConfigJournalAppEntity app : all) {
            APP_CACHE.put(app.getKey(), app.getValue() );
        }
    }
}
