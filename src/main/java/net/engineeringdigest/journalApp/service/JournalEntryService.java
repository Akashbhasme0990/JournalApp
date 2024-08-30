package net.engineeringdigest.journalApp.service;

import net.engineeringdigest.journalApp.entity.JournalEntry;
import net.engineeringdigest.journalApp.repository.JournalEntryRepository;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class JournalEntryService {
    @Autowired
    private JournalEntryRepository journalEntryRepository;
    public void saveEntry(JournalEntry journalEntry) {
        journalEntryRepository.save(journalEntry);
    }
    public List<JournalEntry> getJournalEntries() {
        return journalEntryRepository.findAll();
    }
    public Optional<JournalEntry> findJournalEntryById(ObjectId id) {
        return journalEntryRepository.findById(id);
    }
    public void deleteJournalEntryById(ObjectId id) {
        journalEntryRepository.deleteById(id);
    }
}
