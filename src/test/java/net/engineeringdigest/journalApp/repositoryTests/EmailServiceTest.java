package net.engineeringdigest.journalApp.repositoryTests;

import net.engineeringdigest.journalApp.service.EmailService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class EmailServiceTest {
    @Autowired
    EmailService emailService;
    @Test
    public void testEmailService() {
        emailService.sendEmail("akashbhasme09@gmail.com", "Akash Bhasme", "Hello World");
    }
}
