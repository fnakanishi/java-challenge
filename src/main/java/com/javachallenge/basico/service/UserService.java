package com.javachallenge.basico.service;

import com.javachallenge.basico.entity.Movie;
import com.javachallenge.basico.entity.User;
import com.javachallenge.basico.repository.UserRepository;
import com.javachallenge.basico.security.service.UserDetailsImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {

    @Autowired private UserRepository repository;

    public User save(User user) {
        return repository.save(user);
    }

    public List<User> findAll() {
        return repository.findAll();
    }

    public User findByUsername(String username) {
        return repository.findByUsername(username);
    }

    public Boolean existsByUsername(String username) {
        return repository.existsByUsername(username);
    }

    public void addFavorite(UserDetailsImpl userImpl, Movie movie) {
        User user = findByUsername(userImpl.getUsername());
        user.addFavorite(movie);
        repository.save(user);
    }

    public void removeFavorite(UserDetailsImpl userImpl, Movie movie) {
        User user = findByUsername(userImpl.getUsername());
        user.getFavorites().remove(movie);
        repository.save(user);
    }
}
