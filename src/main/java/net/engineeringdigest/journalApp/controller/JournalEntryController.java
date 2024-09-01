package net.engineeringdigest.journalApp.controller;
import net.engineeringdigest.journalApp.entity.JournalEntry;
import net.engineeringdigest.journalApp.entity.User;
import net.engineeringdigest.journalApp.service.JournalEntryService;
import net.engineeringdigest.journalApp.service.UserService;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.time.LocalDateTime;
import java.util.*;

@RestController
@RequestMapping("/JournalEntries")
public class JournalEntryController {
    @Autowired
    private JournalEntryService journalEntryService;
    @Autowired
    private UserService userService;

    @GetMapping("/{Username}")
    public ResponseEntity<?> getJournalEntriesOfUser(@PathVariable String Username) {
        User user=userService.findUserByUsername(Username);
        List<JournalEntry> all = user.getJournalEntries();
        if (all!=null&&!all.isEmpty()) {
            return new ResponseEntity<>(all, HttpStatus.OK);
        }
        return new ResponseEntity<>(all, HttpStatus.OK);
    }

    @PostMapping("/{Username}")
    public ResponseEntity<JournalEntry> createJournalEntry(@RequestBody JournalEntry journalEntry,@PathVariable String Username) {
        try {
            journalEntryService.saveEntry(journalEntry,Username);
            journalEntry.setDate(LocalDateTime.now());
            return new ResponseEntity<>(journalEntry, HttpStatus.CREATED);
        }
        catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        }

    }

    @GetMapping("/id/{id}")
    public ResponseEntity<JournalEntry> getJournalEntryById(@PathVariable ObjectId id) {
       Optional<JournalEntry> journalEntry = journalEntryService.findJournalEntryById(id);
        return journalEntry.map(entry -> new ResponseEntity<>(entry, HttpStatus.OK)).orElseGet(() -> new ResponseEntity<>(null, HttpStatus.NOT_FOUND));
    }
//
    @DeleteMapping("/id/{id}/{Username}")
    public ResponseEntity<?> deleteJournalEntry(@PathVariable ObjectId id,@PathVariable String Username) {
        journalEntryService.deleteJournalEntryById(id,Username);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @PutMapping("/id/{Username}/{id}")
    public ResponseEntity<?>updateJournalEntry(@PathVariable ObjectId id, @RequestBody JournalEntry newJournalEntry,@PathVariable String Username) {
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
