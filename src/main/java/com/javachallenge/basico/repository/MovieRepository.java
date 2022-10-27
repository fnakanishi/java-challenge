package com.javachallenge.basico.repository;

import com.javachallenge.basico.entity.Movie;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MovieRepository extends JpaRepository<Movie, Long> {
    Movie findMovieByImdbId(String imdbId);

    List<Movie> findByOrderByFavoritedDesc(Pageable pageable);
}
