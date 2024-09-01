package net.engineeringdigest.journalApp.controller;
import net.engineeringdigest.journalApp.entity.JournalEntry;
import net.engineeringdigest.journalApp.service.JournalEntryService;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.time.LocalDateTime;
import java.util.*;

@RestController
public class JournalEntryController {
    @Autowired
    private JournalEntryService journalEntryService;


    @GetMapping("/JournalEntries")
    public ResponseEntity<?> getJournalEntries() {
        List<JournalEntry> all = journalEntryService.getJournalEntries();
        return new ResponseEntity<>(all, HttpStatus.OK);
    }

    @PostMapping("/JournalEntries")
    public ResponseEntity<JournalEntry> createJournalEntry(@RequestBody JournalEntry journalEntry) {
        try {
            journalEntryService.saveEntry(journalEntry);
            journalEntry.setDate(LocalDateTime.now());
            return new ResponseEntity<>(journalEntry, HttpStatus.CREATED);
        }
        catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        }

    }

    @GetMapping("/JournalEntries/{id}")
    public ResponseEntity<JournalEntry> getJournalEntry(@PathVariable ObjectId id) {
       Optional<JournalEntry> journalEntry = journalEntryService.findJournalEntryById(id);
        return journalEntry.map(entry -> new ResponseEntity<>(entry, HttpStatus.OK)).orElseGet(() -> new ResponseEntity<>(null, HttpStatus.NOT_FOUND));
    }

    @DeleteMapping("/JournalEntries/{id}")
    public ResponseEntity<?> deleteJournalEntry(@PathVariable ObjectId id) {
        journalEntryService.deleteJournalEntryById(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @PutMapping("/JournalEntries/{id}")
    public ResponseEntity<?>updateJournalEntry(@PathVariable ObjectId id, @RequestBody JournalEntry newJournalEntry) {
        JournalEntry old=journalEntryService.findJournalEntryById(id).orElse(null);
        if(old != null){
            old.setTitle(!newJournalEntry.getTitle().isEmpty() ? newJournalEntry.getTitle():old.getTitle());
            old.setContent(newJournalEntry.getContent()!=null&& !newJournalEntry.getContent().isEmpty() ? newJournalEntry.getContent():old.getContent());
            journalEntryService.saveEntry(old);
            return new ResponseEntity<>(old,HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
}
