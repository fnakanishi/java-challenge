package com.javachallenge.basico.service;

import com.javachallenge.basico.entity.Movie;
import com.javachallenge.basico.entity.User;
import com.javachallenge.basico.controller.response.UserMovieResponse;
import com.javachallenge.basico.repository.UserRepository;
import com.javachallenge.basico.security.service.UserDetailsImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

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

    public Set<Movie> findMoviesByUsername(UserDetailsImpl userImpl) {
        String username = userImpl.getUsername();
        return findMoviesByUsername(username);
    }

    public Set<Movie> findMoviesByUsername(String username) {
        User user = repository.findByUsername(username);
        return user.getFavorites();
    }

    public List<UserMovieResponse> findFavoritedByUsers() {
        Set<User> userList = repository.findAllByFavoritesIsNotNull();
        List<UserMovieResponse> list = new ArrayList<>();
        for (User user: userList) {
            UserMovieResponse dto = new UserMovieResponse(user);
            list.add(dto);
        }
        return list;
    }
}
