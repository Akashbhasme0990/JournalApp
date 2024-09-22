package net.engineeringdigest.journalApp.Scheduler;

import net.engineeringdigest.journalApp.cache.AppCache;
import net.engineeringdigest.journalApp.entity.JournalEntry;
import net.engineeringdigest.journalApp.entity.User;
import net.engineeringdigest.journalApp.repository.UserRepositoryImpl;
import net.engineeringdigest.journalApp.service.EmailService;
import net.engineeringdigest.journalApp.service.SentimentAnalysisService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class UserScheduler {
    @Autowired
    private SentimentAnalysisService sentimentAnalysisService;
    @Autowired
    private EmailService emailService;
    @Autowired
    private UserRepositoryImpl userRepository;
    @Autowired
    private AppCache appCache;
    @Scheduled(cron ="0 0 9 * * Sun")
    public void fetchUsers() {
        List<User> users =userRepository.getUsersForSA();
        for (User user : users) {
            List<JournalEntry> journalEntries= user.getJournalEntries();
            List<String> collect = journalEntries.stream().filter(x -> x.getDate().isAfter(LocalDateTime.now().minus(7, ChronoUnit.DAYS))).map(x -> x.getContent()).collect(Collectors.toList());
            String entry = String.join(" ",collect);
            String sentiment = sentimentAnalysisService.getSentiments(entry);
            emailService.sendEmail(user.getEmail(),"sentiment for last 7 days",sentiment);
        }
    }
    @Scheduled(cron = "0 */10 * * * *")
    public void clearAppcache(){
        appCache.init();
    }


}
