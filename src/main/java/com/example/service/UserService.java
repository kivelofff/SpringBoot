package com.example.service;

import com.example.domain.Role;
import com.example.domain.User;
import com.example.repos.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class UserService implements UserDetailsService {
    //@Autowired -depricated
    private final UserRepository userRepository;

    @Autowired
    private MailSender mailSender;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Autowired
    private PasswordEncoder passwordEncoder;

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
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        userRepository.save(user);
        sendActivationCode(user);
        return true;
    }

    private void sendActivationCode(User user) {

        if (!StringUtils.isEmpty(user.getEmail())) {
            String message = String.format("Hello, %s! \n" +
                    "Welcome to Sweater. Acrivate your account: http://localhost/activate/%s", user.getUsername(), user.getActivationCode());
            mailSender.send(user.getEmail(), "Activation Sweater account", message);
        }
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

    public List<User> findAll() {
        return userRepository.findAll();
    }

    public void saveUser(String username, User user, Map<String, String> form) {
        user.setUsername(username);
        Set<String> roles = Arrays.stream(Role.values()).map(Role::name).collect(Collectors.toSet());
        user.getRoles().clear();
        for (String key : form.keySet()) {
            if (roles.contains(key)) {
                user.getRoles().add(Role.valueOf(key));
            }
        }
        userRepository.save(user);
    }

    public void updateProfile(User user, String password, String email) {
        String userEmail = user.getEmail();
        if (!StringUtils.isEmpty(email) && !email.equals(userEmail)) {
            user.setEmail(email);
            sendActivationCode(user);
        }

        if (StringUtils.isEmpty(password)) {
            user.setPassword(password);
        }

        userRepository.save(user);
    }
}
