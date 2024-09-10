package net.engineeringdigest.journalApp.controller;
import net.engineeringdigest.journalApp.entity.User;
import net.engineeringdigest.journalApp.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
@RestController
@RequestMapping("/public")
public class PublicController {
    @Autowired
    private UserService userService;
    @GetMapping()
    public List<User> getAllUsers() {
        return userService.findAll();
    }
    @PostMapping()
    public void createUser(@RequestBody User user) {
        userService.saveUserEntry(user);
    }
}
