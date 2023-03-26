package com.javachallenge.basico.service;

import com.hazelcast.core.HazelcastInstance;
import com.javachallenge.basico.client.imdb.IMDBMovieClient;
import com.javachallenge.basico.client.imdb.resources.ImdbMovieDTO;
import com.javachallenge.basico.client.imdb.resources.ImdbMovieList;
import com.javachallenge.basico.entity.Movie;
import com.javachallenge.basico.entity.User;
import com.javachallenge.basico.repository.MovieRepository;
import com.javachallenge.basico.security.service.UserDetailsImpl;
import io.github.resilience4j.ratelimiter.annotation.RateLimiter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.atomic.AtomicInteger;

import static com.javachallenge.basico.Constants.IMDB_API_KEY;
import static com.javachallenge.basico.client.imdb.IMDBMovieClient.buildMovieClient;

@Service
public class MovieService {

    @Autowired private UserService userService;
    @Autowired private MovieRepository repository;

    @Autowired
    private HazelcastInstance hazelcastInstance;

    private ConcurrentMap<String,Object> retrieveMap() {
        return hazelcastInstance.getMap("movies");
    }

    private IMDBMovieClient movieClient = buildMovieClient();

    private void createWithImdb(String imdbId) {
        ImdbMovieDTO dto = movieClient.findByImdbId(IMDB_API_KEY, imdbId);
        repository.save(new Movie(dto));
    }

    public List<Movie> findAll() {
        return repository.findAll();
    }

    public void populateImdbMovies() {
        ImdbMovieList response = movieClient.findAll(IMDB_API_KEY);
        List<String> imdbIdsFromExternalAPI = new ArrayList<>(response.getItems().stream().map(ImdbMovieDTO::getId).toList());
        List<String> imdbIdsFromDB = repository.findAllImdbIdExistingByImdbId(imdbIdsFromExternalAPI);
        imdbIdsFromExternalAPI.remove(imdbIdsFromDB);
        imdbIdsFromExternalAPI.stream().parallel().forEach(this::createWithImdb);
    }

    public Page<Movie> findTopFavorited() {
        Page<Movie> movieList = repository.findByOrderByFavoritedDesc(Pageable.ofSize(10));
        retrieveMap().put("topMovies", movieList);
        return movieList;
    }

    public Movie findRandomMovie(UserDetailsImpl user) {
        Long userId = user.getId();
        Set<Movie> userMovielist = userService.findMoviesByUserId(userId);
        Map<Long, AtomicInteger> map = getSimiliarFavoritedMoviesPerUser(userMovielist);

        return getBestMatchingMovie(map, userMovielist);
    }

    private Map<Long, AtomicInteger> getSimiliarFavoritedMoviesPerUser(Set<Movie> movieSet) {
        Map<Long, AtomicInteger> map = new HashMap<>();
        movieSet.stream().parallel().forEach(movie -> {
            Set<Long> usersThatFavoritedMovie = repository.findUserIdsByFavoritedMovie(movie.getId());
            usersThatFavoritedMovie.stream().parallel().forEach(userId -> {
                AtomicInteger amount = map.get(userId);
                if (amount == null) {
                    map.put(userId, new AtomicInteger(1));
                } else {
                    amount.incrementAndGet();
                }
            });
        });
        return map;
    }

    private Movie getBestMatchingMovie(Map<Long, AtomicInteger> map, Set<Movie> userMovielist) {
        Set<Movie> bestMatchList = null;
        int highestMatch = 0;
        for (Long userId: map.keySet()) {
            User user1 = userService.findById(userId);
            int amount = map.get(userId).get();
            Set<Movie> userMatchList = user1.getFavorites();
            System.out.println("User " + user1.getUsername() + " has " + amount + " match(es).");
            if (highestMatch < amount && amount != userMatchList.size()) {
                highestMatch = amount;
                bestMatchList = new HashSet<>(userMatchList);
            }
        }

        if (bestMatchList != null) {
            bestMatchList.removeAll(userMovielist);
            return bestMatchList.iterator().next();
        }

        return null;
    }

    public Movie findRandomMovie2(UserDetailsImpl user) {
        Long loggedUserId = user.getId();
        Set<Movie> userMovielist = userService.findMoviesByUserId(loggedUserId);
        Map<Long, Integer> map = new HashMap<>();
        for (Movie m: userMovielist) {
            Movie movie = repository.findMovieByImdbId(m.getImdbId());
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
                bestMatchList = new HashSet<>(userMatchList);
            }
        }

        if (bestMatchList != null) {
            bestMatchList.removeAll(userMovielist);
            return bestMatchList.iterator().next();
        }

        return null;
    }

    @Transactional(rollbackFor = Exception.class)
    public void addFavorite(UserDetailsImpl userImpl, Long id) {
        Movie movie = repository.findById(id).orElseThrow();
        User user = userService.findById(userImpl.getId());
        movie.getUsersFavorited().add(user);
        movie.setFavorited(movie.getUsersFavorited().size());
        repository.save(movie);
    }

    @Transactional(rollbackFor = Exception.class)
    public void removeFavorite(UserDetailsImpl userImpl, Long id) {
        Movie movie = repository.findById(id).orElseThrow();
        User user = userService.findById(userImpl.getId());
        movie.getUsersFavorited().remove(user);
        movie.setFavorited(movie.getUsersFavorited().size());
        repository.save(movie);
    }
}