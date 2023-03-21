package com.javachallenge.basico.repository;

import com.javachallenge.basico.entity.Movie;
import io.github.resilience4j.ratelimiter.annotation.RateLimiter;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MovieRepository extends JpaRepository<Movie, Long> {
    Movie findMovieById(String id);

//    @RateLimiter(name = "top-movies", fallbackMethod = )
    Page<Movie> findByOrderByFavoritedDesc(Pageable pageable);
}
