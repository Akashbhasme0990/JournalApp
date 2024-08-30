package net.engineeringdigest.journalApp.controller;

import net.engineeringdigest.journalApp.entity.JournalEntry;
import net.engineeringdigest.journalApp.entity.Users;
import net.engineeringdigest.journalApp.repository.JournalEntryRepository;
import net.engineeringdigest.journalApp.repository.UserRepository;
import net.engineeringdigest.journalApp.service.JournalEntryService;
import net.engineeringdigest.journalApp.service.UserService;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@RestController
public class UserController {
    @Autowired
    private UserService userService;
    @Autowired
    private UserRepository userRepository;


    @GetMapping("/Users")
    public ResponseEntity<?> getUser() {
        List<Users> all = userService.findAll();
        return new ResponseEntity<>(all, HttpStatus.OK);
    }

    @PostMapping("/Users")
    public ResponseEntity<Users> createUser (@RequestBody Users user) {
        try {
            userService.save(user);
            return new ResponseEntity<>(user, HttpStatus.CREATED);
        }
        catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        }

    }

    @GetMapping("/Users/{id}")
    public ResponseEntity<Users> getUser (@PathVariable ObjectId id) {
       Optional<Users> journalEntry = userService.findById(id);
        return journalEntry.map(entry -> new ResponseEntity<>(entry, HttpStatus.OK)).orElseGet(() -> new ResponseEntity<>(null, HttpStatus.NOT_FOUND));
    }

    @DeleteMapping("/JournalEntries/{id}")
    public ResponseEntity<?> deleteJournalEntry(@PathVariable ObjectId id) {
        userService.delete(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @PutMapping("/Users/{id}")
    public ResponseEntity<?>updateUser(@PathVariable ObjectId id, @RequestBody Users user) {
        Users old=userService.findById(id).orElse(null);
        if(old != null){
            old.setUsername(!user.getUsername().isEmpty() ? user.getUsername():old.getUsername());
            old.setPassword(!user.getPassword().isEmpty() ? user.getPassword():old.getPassword());
            return new ResponseEntity<>(old,HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
}
