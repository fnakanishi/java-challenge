package com.javachallenge.basico.service;

import com.javachallenge.basico.client.MovieClient;
import com.javachallenge.basico.client.resources.MovieListResource;
import com.javachallenge.basico.entity.Movie;
import com.javachallenge.basico.entity.User;
import com.javachallenge.basico.repository.MovieRepository;
import com.javachallenge.basico.security.service.UserDetailsImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

import static com.javachallenge.basico.Constants.IMDB_API_KEY;
import static com.javachallenge.basico.client.MovieClient.buildMovieClient;

@Service
public class MovieService {

    @Autowired private UserService userService;
    @Autowired private MovieRepository repository;

    private MovieClient movieClient = buildMovieClient();

    private void create(String imdbId) {
        Movie movie = repository.findMovieById(imdbId);
        if (movie == null) {
            movie = findByImdbId(imdbId);
            repository.save(movie);
        }
    }

    public List<Movie> findAll() {
        return repository.findAll();
    }

    public List<Movie> findAll(Sort sort) {
        return repository.findAll(sort);
    }

    public Movie findById(String id) {
        return repository.findMovieById(id);
    }

    public Movie findByImdbId(String imdbId) {
        return movieClient.findByImdbId(IMDB_API_KEY, imdbId);
    }

    public void saveTopMovies() {
        MovieListResource response = movieClient.findAll(IMDB_API_KEY);
        List<Movie> list = response.getItems();
        for (Movie movie: list) {
            String imdbId = movie.getId();
            create(imdbId);
        }
    }

    public List<Movie> findTopByFavorited(int amount) {
        return repository.findByOrderByFavoritedDesc(Pageable.ofSize(amount));
    }

    public void addFavorite(UserDetailsImpl userImpl, String id) {
        Movie movie = findById(id);
        if (movie != null) {
            User user = userService.findByUsername(userImpl.getUsername());
            movie.getUsersFavorited().add(user);
            movie.setFavorited(movie.getUsersFavorited().size());
            repository.save(movie);
        }
    }

    public void removeFavorite(UserDetailsImpl userImpl, String id) {
        Movie movie = findById(id);
        if (movie != null) {
            User user = userService.findByUsername(userImpl.getUsername());
            movie.getUsersFavorited().remove(user);
            movie.setFavorited(movie.getUsersFavorited().size());
            repository.save(movie);
        }
    }
}