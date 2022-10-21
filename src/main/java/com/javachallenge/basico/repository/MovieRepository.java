package com.javachallenge.basico.repository;

import com.javachallenge.basico.entity.Movie;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MovieRepository extends JpaRepository<Movie, Long> {
    Movie findMovieByImdbId(String imdbId);

//    List<Movie> findTopByFavorited(Pageable pageable);
    List<Movie> findTop10ByOrderByFavoritedDesc();

}
