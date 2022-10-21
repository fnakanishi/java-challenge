package com.javachallenge.basico.controller.response;

import com.javachallenge.basico.entity.Movie;
import com.javachallenge.basico.entity.User;

import java.util.Set;

public class UserMovieResponse {
    private Long id;
    private String username;
    private Set<Movie> movies;

    public UserMovieResponse(User user) {
        Set<Movie> movies = user.getFavorites();
        this.id = user.getId();
        this.username = user.getUsername();
        this.movies = movies;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public Set<Movie> getMovies() {
        return movies;
    }

    public void setMovies(Set<Movie> movies) {
        this.movies = movies;
    }
}
