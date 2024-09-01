package net.engineeringdigest.journalApp.service;

import net.engineeringdigest.journalApp.entity.JournalEntry;
import net.engineeringdigest.journalApp.entity.User;
import net.engineeringdigest.journalApp.repository.JournalEntryRepository;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class JournalEntryService {
    @Autowired
    private JournalEntryRepository journalEntryRepository;
    @Autowired
    UserService userService;
    public void saveEntry(JournalEntry journalEntry, String Username) {
        User user= userService.findUserByUsername(Username);
        journalEntry.setDate(LocalDateTime.now());
        JournalEntry saved = journalEntryRepository.save(journalEntry);
        user.getJournalEntries().add(saved);
        userService.createUser(user);
    }
    public void saveEntry(JournalEntry journalEntry) {
        journalEntryRepository.save(journalEntry);
    }

    public List<JournalEntry> getJournalEntries() {
        return journalEntryRepository.findAll();
    }
    public Optional<JournalEntry> findJournalEntryById(ObjectId id) {

        return journalEntryRepository.findById(id);
    }
    public void deleteJournalEntryById(ObjectId id, String username) {
        User user= userService.findUserByUsername(username);
        user.getJournalEntries().removeIf(journalEntry -> journalEntry.getId().equals(id));
        userService.createUser(user);
        journalEntryRepository.deleteById(id);
    }
}
