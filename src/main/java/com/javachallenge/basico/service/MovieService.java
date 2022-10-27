package com.javachallenge.basico.service;

import com.javachallenge.basico.client.MovieClient;
import com.javachallenge.basico.client.resources.MovieListResource;
import com.javachallenge.basico.client.resources.dto.MovieDTO;
import com.javachallenge.basico.entity.Movie;
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

    private Movie build(MovieDTO dto) {
        Movie movie = new Movie();
        movie.setImdbId(dto.getId());
        movie.setTitle(dto.getTitle());
        movie.setOriginalTitle(dto.getOriginalTitle());
        movie.setFullTitle(dto.getFullTitle());
        movie.setYear(dto.getYear());
        movie.setImage(dto.getImage());
        movie.setReleaseDate(dto.getReleaseDate());
        movie.setRuntime(dto.getRuntimeStr());
        movie.setPlot(dto.getPlot());
        movie.setAwards(dto.getAwards());
        movie.setDirectors(dto.getDirectors());
        movie.setWriters(dto.getWriters());
        movie.setStars(dto.getStars());
        movie.setGenres(dto.getGenres());
        movie.setLanguages(dto.getLanguages());
        movie.setContentRating(dto.getContentRating());

        return movie;
    }

    private void create(String imdbId) {
        Movie movie = repository.findMovieByImdbId(imdbId);
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

    public Movie findById(Long id) {
        return repository.findById(id).get();
    }

    public Movie findByImdbId(String imdbId) {
        MovieDTO dto = movieClient.findByImdbId(IMDB_API_KEY, imdbId);
        Movie movie = build(dto);

        return movie;
    }

    public void saveTopMovies() {
        MovieListResource response = movieClient.findAll(IMDB_API_KEY);
        List<MovieDTO> list = response.getItems();
        for (MovieDTO dto: list) {
            String imdbId = dto.getId();
            create(imdbId);
        }
    }

    public List<Movie> findTopByFavorited(int amount) {
        return repository.findByOrderByFavoritedDesc(Pageable.ofSize(amount));
    }

    public void addFavorite(UserDetailsImpl user, Long id) {
        Movie movie = findById(id);
        if (movie != null) {
            userService.addFavorite(user, movie);
            movie.editFavorites(1);
            repository.save(movie);
        }
    }

    public void removeFavorite(UserDetailsImpl user, Long id) {
        Movie movie = findById(id);
        if (movie != null) {
            userService.removeFavorite(user, movie);
            movie.editFavorites(-1);
            repository.save(movie);
        }
    }
}