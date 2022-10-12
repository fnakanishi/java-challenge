package com.javachallenge.basico.security.service;

import com.javachallenge.basico.entity.User;
import com.javachallenge.basico.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {
    @Autowired
    UserRepository userRepository;

    @Override
    @Transactional
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        try {
            User user = userRepository.findByUsername(username);

            if (user != null) {
                return UserDetailsImpl.build(user);
            }

            throw new RuntimeException();

        } catch (RuntimeException e) {
            throw new UsernameNotFoundException("User Not Found with username: " + username);
        }
    }

}
