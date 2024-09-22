package net.engineeringdigest.journalApp.service;

import net.engineeringdigest.journalApp.entity.JournalEntry;
import net.engineeringdigest.journalApp.entity.User;
import net.engineeringdigest.journalApp.repository.JournalEntryRepository;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class JournalEntryService {
    @Autowired
    private JournalEntryRepository journalEntryRepository;
    @Autowired
    private UserService userService;
    

    @Transactional
    public void saveEntry(JournalEntry journalEntry, String Username) {
        try {
            User user= userService.findUserByUsername(Username);
            journalEntry.setDate(LocalDateTime.now());
            JournalEntry saved = journalEntryRepository.save(journalEntry);
            user.getJournalEntries().add(saved);
            userService.save(user);
        }
        catch (Exception e) {
            System.out.println(e.getMessage());
            throw new RuntimeException("ERROR OCCURED WHILE SAVING ENTRY");
        }
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
    @Transactional
    public boolean deleteJournalEntryById(ObjectId id, String username) {
        boolean removed = false;
        try {
            User user= userService.findUserByUsername(username);
            boolean remove = user.getJournalEntries().removeIf(journalEntry -> journalEntry.getId().equals(id));
            if (remove) {
                userService.save(user);
                journalEntryRepository.deleteById(id);
            }
        }
        catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return removed;
    }
}
