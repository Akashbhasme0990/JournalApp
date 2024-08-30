package net.engineeringdigest.journalApp.service;

import net.engineeringdigest.journalApp.entity.JournalEntry;
import net.engineeringdigest.journalApp.entity.Users;
import net.engineeringdigest.journalApp.repository.JournalEntryRepository;
import net.engineeringdigest.journalApp.repository.UserRepository;
import org.apache.catalina.User;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;
    public void save(Users user) {
        userRepository.save(user);
    }
   public List<Users> findAll() {
        return userRepository.findAll();
   }
    public Optional<Users> findById(ObjectId id) {
        return userRepository.findById(id);
    }
    public void delete(ObjectId id) {
        userRepository.deleteById(id);
    }
}
