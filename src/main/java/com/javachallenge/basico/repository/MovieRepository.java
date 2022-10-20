package com.javachallenge.basico.repository;

import com.javachallenge.basico.entity.Movie;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MovieRepository extends JpaRepository<Movie, Long> {
    Movie findMovieByImdbId(String imdbId);

    Movie findMovieByIdOrImdbId(String id);
}
