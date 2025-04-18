package com.realestate.realestate.service;

import com.realestate.realestate.model.User;
import com.realestate.realestate.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.beans.factory.annotation.Autowired;

@Service
public class UserService implements UserDetailsService {

    private final UserRepository repo;
    private PasswordEncoder encoder;

    public UserService(UserRepository repo) {
        this.repo = repo;
    }

    @Autowired
    public void setPasswordEncoder(PasswordEncoder encoder) {
        this.encoder = encoder;
    }

    @Transactional
    public User register(User user) {
        user.setPassword(encoder.encode(user.getPassword()));
        return repo.save(user);
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = repo.findByUsername(username);
        if (user == null) {
            throw new UsernameNotFoundException("User not found");
        }
        return org.springframework.security.core.userdetails.User
                .withUsername(user.getUsername())
                .password(user.getPassword())
                .roles("USER")
                .build();
    }
}