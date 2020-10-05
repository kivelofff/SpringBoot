package com.example.service;

import com.example.domain.Role;
import com.example.domain.User;
import com.example.repos.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.Collections;
import java.util.UUID;

@Service
public class UserService implements UserDetailsService {
    //@Autowired -depricated
    private final UserRepository userRepository;

    @Autowired
    private MailSender mailSender;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRepository.findByUsername(username);
    }

    public boolean addUser(User user) {
        User userFromDb = userRepository.findByUsername(user.getUsername());
        if (userFromDb != null) {
            return false;
        }
        user.setActive(true);
        user.setRoles(Collections.singleton(Role.USER));
        user.setActivationCode(UUID.randomUUID().toString());


        if (!StringUtils.isEmpty(user.getEmail())) {
            String message = String.format("Hello, %s! \n" +
                    "Welcome to Sweater. Acrivate your account: http://localhost/activate/%s", user.getUsername(), user.getActivationCode());
            mailSender.send(user.getEmail(), "Activation Sweater account", message);
        }
        userRepository.save(user);
        return true;
    }

    public boolean activateUser(String code) {
        User user = userRepository.findByActivationCode(code);

        if (user == null) {
            return false;
        }
        user.setActivationCode(null);
        userRepository.save(user);

        return true;
    }
}
