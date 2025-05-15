package net.engineeringdigest.journalApp.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.mongodb.core.mapping.Document;


@Setter
@AllArgsConstructor
@NoArgsConstructor
@Document(collection = "Weather API")
public class ConfigJournalAppEntity {
    private String key;
    private String value;

    public String getKey() {
        return key;
    }
    public String getValue(){
        return value;
    }
}
