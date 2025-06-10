package com.example.schoolmanagement.service;

import com.example.schoolmanagement.model.User;
import com.example.schoolmanagement.repository.UserRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.Set;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    private static final Logger logger = LoggerFactory.getLogger(UserDetailsServiceImpl.class);

    @Autowired
    private UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        logger.info("Attempting to load user by username: {}", username);

        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> {
                    logger.warn("User with username '{}' not found in database.", username);
                    return new UsernameNotFoundException("User tidak ditemukan: " + username);
                });

        String roleName = null;
        if (user.getRole() != null) {
            roleName = user.getRole().name();
        } else {
            logger.error("User '{}' found but has no role defined.", user.getUsername());
            throw new UsernameNotFoundException("User " + username + " ditemukan tapi tidak memiliki role.");
        }

        GrantedAuthority authority = new SimpleGrantedAuthority("ROLE_" + roleName);
        Set<GrantedAuthority> authorities = Collections.singleton(authority);

        logger.info("Authenticating user '{}' with roles {}", user.getUsername(), authorities);

        return new org.springframework.security.core.userdetails.User(
                user.getUsername(),
                user.getPassword(),
                authorities
        );
    }
}