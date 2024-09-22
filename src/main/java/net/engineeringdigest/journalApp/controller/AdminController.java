package net.engineeringdigest.journalApp.controller;

import net.engineeringdigest.journalApp.cache.AppCache;
import net.engineeringdigest.journalApp.entity.User;
import net.engineeringdigest.journalApp.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(("/admin"))
public class AdminController {
    @Autowired
    private UserService userService;


    @Autowired
    private AppCache appCache;

    @GetMapping("/getAll-users")
    public ResponseEntity<?> getAllUsers() {
        List<User>all = userService.findAll();
        if (all != null && !all.isEmpty()) {
            return ResponseEntity.ok(all);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
    @PostMapping("/createAdmin")
    public ResponseEntity<?> createAdmin(@RequestBody User user) {
        userService.createAdmin(user);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }
    @GetMapping("/clear-app-cache")
    public void clearAppCache(){
        appCache.init();
    }


}
