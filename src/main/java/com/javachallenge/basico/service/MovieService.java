package com.javachallenge.basico.service;

import com.javachallenge.basico.client.MovieClient;
import com.javachallenge.basico.client.resources.MovieListResource;
import com.javachallenge.basico.entity.Movie;
import com.javachallenge.basico.entity.User;
import com.javachallenge.basico.repository.MovieRepository;
import com.javachallenge.basico.security.service.UserDetailsImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

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

    @Cacheable("topMovies")
    public List<Movie> findTopByFavorited(int amount) {
        return repository.findByOrderByFavoritedDesc(Pageable.ofSize(amount));
    }

    public Movie findRandomMovie(UserDetailsImpl user) {
        String username = user.getUsername();
        Set<Movie> userMovielist = userService.findMoviesByUsername(username);
        Map<Long, Integer> map = new HashMap<>();
        for (Movie m: userMovielist) {
            Movie movie = repository.findMovieById(m.getId());
            Set<User> usersFavorited = movie.getUsersFavorited();
            for (User userFavorited: usersFavorited) {
                Long userId = userFavorited.getId();
                Integer amount = map.get(userId);
                if (amount == null) {
                    map.put(userId, 1);
                } else {
                    map.put(userId, ++amount);
                }
            }
        }

        Set<Movie> bestMatchList = null;
        int highestMatch = 0;
        for (Long userId: map.keySet()) {
            User user1 = userService.findById(userId);
            Integer amount = map.get(userId);
            Set<Movie> userMatchList = user1.getFavorites();
            System.out.println("User " + user1.getUsername() + " has " + amount + " match(es).");
            if (highestMatch < amount && amount != userMatchList.size()) {
                highestMatch = amount;
                bestMatchList = userMatchList;
            }
        }

        if (bestMatchList != null) {
            bestMatchList.removeAll(userMovielist);
            return bestMatchList.iterator().next();
        }

        return null;
    }

    @Transactional(rollbackFor = Exception.class)
    public void addFavorite(UserDetailsImpl userImpl, String id) {
        Movie movie = findById(id);
        if (movie != null) {
            User user = userService.findByUsername(userImpl.getUsername());
            movie.getUsersFavorited().add(user);
            movie.setFavorited(movie.getUsersFavorited().size());
            repository.save(movie);
        }
    }

    @Transactional(rollbackFor = Exception.class)
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