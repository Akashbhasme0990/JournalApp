package net.engineeringdigest.journalApp.service;
import net.engineeringdigest.journalApp.entity.User;
import net.engineeringdigest.journalApp.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

@Component
public class UserDetailsServiceImpl implements UserDetailsService {
    @Autowired
    private UserRepository userRepository;
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByUsername(username);
        System.out.println(user.getUsername() + " " + user.getPassword() + " " + user.getUsername());

        if (user != null) {
            UserDetails userDetails=org.springframework.security.core.userdetails.User.builder()
                    .username(user.getUsername())
                    .password(user.getPassword())
                    .roles(user.getRoles().toArray(new String[0]))
                    .build();
            System.out.println(userDetails.getUsername() + " " + userDetails.getPassword() + " " + userDetails.getUsername());
            return userDetails;
        }
        throw new UsernameNotFoundException("UserName not found: " + username);
    }
}
