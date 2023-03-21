package com.javachallenge.basico.service;

import com.javachallenge.basico.entity.Movie;
import com.javachallenge.basico.entity.User;
import com.javachallenge.basico.controller.response.UserMovieResponse;
import com.javachallenge.basico.repository.UserRepository;
import com.javachallenge.basico.security.service.UserDetailsImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;

@Service
public class UserService {

    @Autowired private UserRepository repository;

    public User save(User user) {
        return repository.save(user);
    }

    public User findById(Long userId) {
        return repository.getReferenceById(userId);
    }

    public User findByUsername(String username) {
        return repository.findByUsername(username);
    }

    public Boolean existsByUsername(String username) {
        return repository.existsByUsername(username);
    }

    public Set<Movie> findMoviesByUser(UserDetailsImpl userImpl) {
        String username = userImpl.getUsername();
        return findMoviesByUsername(username);
    }

    public Set<Movie> findMoviesByUsername(String username) {
        User user = findByUsername(username);
        return user.getFavorites();
    }

    public List<UserMovieResponse> findFavoritedByUsers() {
        Set<User> userList = repository.findAllByFavoritesIsNotNull();
        return userList.stream()
                .map(UserMovieResponse::new)
                .toList();
    }
}
