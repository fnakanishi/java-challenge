package com.javachallenge.basico.service;

import com.javachallenge.basico.client.MovieClient;
import com.javachallenge.basico.client.resources.MovieListResource;
import com.javachallenge.basico.client.resources.dto.MovieDTO;
import com.javachallenge.basico.entity.Movie;
import com.javachallenge.basico.repository.MovieRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

import static com.javachallenge.basico.Constants.IMDB_API_KEY;
import static com.javachallenge.basico.client.MovieClient.buildMovieClient;

@Service
public class MovieService {
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
            movie = findById(imdbId);
            repository.save(movie);
        }
    }

    public List<Movie> findAll() {
        return repository.findAll();
    }

    public List<Movie> findAll(Sort sort) {
        return repository.findAll(sort);
    }

    public Movie findByIdOrImdbId(String id) {
        return repository.findMovieByIdOrImdbId(id);
    }

    public Movie findById(String imdbId) {
        MovieDTO dto = movieClient.findById(IMDB_API_KEY, imdbId);
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
}