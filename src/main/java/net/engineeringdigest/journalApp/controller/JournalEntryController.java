package net.engineeringdigest.journalApp.controller;

import net.engineeringdigest.journalApp.entity.JournalEntry;
import net.engineeringdigest.journalApp.entity.User;
import net.engineeringdigest.journalApp.service.JournalEntryService;
import net.engineeringdigest.journalApp.service.UserService;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/JournalEntries")
public class JournalEntryController {
    @Autowired
    private JournalEntryService journalEntryService;
    @Autowired
    private UserService userService;

    @GetMapping
    public ResponseEntity<?> getJournalEntriesOfUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        User user=userService.findUserByUsername(username);
        List<JournalEntry> all = user.getJournalEntries();
        if (all!=null&&!all.isEmpty()) {
            return new ResponseEntity<>(all, HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
    
    @PostMapping
    public ResponseEntity<JournalEntry> createJournalEntry(@RequestBody JournalEntry journalEntry) {

        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            String username = authentication.getName();
            journalEntryService.saveEntry(journalEntry,username);
            return new ResponseEntity<>(journalEntry, HttpStatus.CREATED);
        }
        catch (Exception e) {
            System.out.println(e.getMessage());
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);

        }
        finally{
            System.out.println(journalEntry);
        }

    }
    @PostMapping(value = "/withImage", consumes = "multipart/form-data")
    public ResponseEntity<?> createJournalEntryWithImage(
            @RequestPart("title") String title,
            @RequestPart("content") String content,
            @RequestPart("image") MultipartFile image) {

        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            String username = authentication.getName();

            JournalEntry journalEntry = new JournalEntry();
            journalEntry.setTitle(title);
            journalEntry.setContent(content);
            journalEntry.setDate(LocalDateTime.now());
            journalEntry.setImageName(image.getOriginalFilename());
            journalEntry.setImageType(image.getContentType());
            journalEntry.setImageData(image.getBytes());

            journalEntryService.addEntry(journalEntry, image, username);

            return new ResponseEntity<>(journalEntry, HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }



    @GetMapping("/id/{id}")
    public ResponseEntity<JournalEntry> getJournalEntryById(@PathVariable ObjectId id) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        User user=userService.findUserByUsername(username);
        List<JournalEntry> collect = user.getJournalEntries().stream().filter(journalEntry -> journalEntry.getId().equals(id)).collect(Collectors.toList());
        if(!collect.isEmpty()) {
            Optional<JournalEntry> journalEntry = journalEntryService.findJournalEntryById(id);
            if (journalEntry.isPresent()) {
                return new ResponseEntity<>(journalEntry.get(), HttpStatus.OK);
            }
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @DeleteMapping("/id/{id}")
    public ResponseEntity<?> deleteJournalEntry(@PathVariable ObjectId id) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        boolean removed =journalEntryService.deleteJournalEntryById(id,username);
        if (removed){
            return new ResponseEntity<>(HttpStatus.OK);
        }
        else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
        @PutMapping("/id/{id}")
        public ResponseEntity<?>updateJournalEntry(@PathVariable ObjectId id, @RequestBody JournalEntry newJournalEntry) {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            String username = authentication.getName();
            User user=userService.findUserByUsername(username);
            List<JournalEntry> collect = user.getJournalEntries().stream().filter(x -> x.getId().equals(id)).collect(Collectors.toList());
            if (!collect.isEmpty()) {
                    Optional<JournalEntry> journalEntry = journalEntryService.findJournalEntryById(id);
                    if (journalEntry.isPresent()) {
                        JournalEntry old = journalEntry.get();
                        old.setTitle(!newJournalEntry.getTitle().isEmpty() ? newJournalEntry.getTitle() : old.getTitle());
                        old.setContent(newJournalEntry.getContent() != null && !newJournalEntry.getContent().isEmpty() ? newJournalEntry.getContent() : old.getContent());
                        journalEntryService.saveEntry(old,username);
                        return new ResponseEntity<>(old, HttpStatus.OK);
                    }
            }
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

}




