package net.engineeringdigest.journalApp.controller;

import net.engineeringdigest.journalApp.apiResponse.WeatherResponse;
import net.engineeringdigest.journalApp.entity.User;
import net.engineeringdigest.journalApp.repository.UserRepository;
import net.engineeringdigest.journalApp.repository.UserRepositoryImpl;
import net.engineeringdigest.journalApp.service.UserService;
import net.engineeringdigest.journalApp.service.WeatherService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@RestController
@RequestMapping("/user")
public class UserController {
    @Autowired
    private  UserService userService;
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private WeatherService weatherService;
    @Autowired
    private UserRepositoryImpl userRepositoryImpl;


    @PutMapping()
    public ResponseEntity<?> updateUser(@RequestBody User user) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        User userInDB = userService.findUserByUsername(username);
        userInDB.setUsername(user.getUsername());
        userInDB.setPassword(user.getPassword());
        userService.saveUserEntry(userInDB);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
    @DeleteMapping
    public ResponseEntity<?> deleteUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        userRepository.deleteByUsername(username);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
    @GetMapping
    public ResponseEntity<?> greeting(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        WeatherResponse weatherResponse =weatherService.getWeatherResponse("Mumbai");
        String greeting = "";
        if (weatherResponse != null) {
            greeting= "hi " + authentication.getName() + " Weather feels like " + weatherResponse.getCurrent().getFeelsLike();
        }

        return new ResponseEntity<>( greeting , HttpStatus.OK );
    }
    @GetMapping("/getSentimentUsers")
    public ResponseEntity<?> getUsersForSentimentAnalysis() {
        List<User> sentUsers= userRepositoryImpl.getUsersForSA();
        if (sentUsers != null && !sentUsers.isEmpty()) {
            return ResponseEntity.ok(sentUsers);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }


}
