package com.javachallenge.basico.repository;

import com.javachallenge.basico.entity.Movie;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Set;

public interface MovieRepository extends JpaRepository<Movie, Long> {
    Movie findMovieByImdbId(String id);

    @Query(value = "SELECT user_id FROM tb_favorite WHERE movie_id = ?1", nativeQuery = true)
    Set<Long> findUserIdsByFavoritedMovie(Long movieId);

    Page<Movie> findByOrderByFavoritedDesc(Pageable pageable);

    @Query(value = "SELECT imdb_id FROM tb_movie WHERE imdb_id IN ?1", nativeQuery = true)
    List<String> findAllImdbIdExistingByImdbId(List<String> list);
}