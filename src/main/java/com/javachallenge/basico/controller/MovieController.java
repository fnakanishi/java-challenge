package com.javachallenge.basico.controller;

import com.javachallenge.basico.entity.Movie;
import com.javachallenge.basico.security.service.UserDetailsImpl;
import com.javachallenge.basico.service.MovieService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/movies")
@SecurityRequirement(name = "Bearer Authentication")
public class MovieController {

    @Autowired private MovieService movieService;

    @GetMapping("/populate")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity findTop250() {
        movieService.saveTopMovies();
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @GetMapping("/list")
    public ResponseEntity findAll() {
        List<Movie> movies = movieService.findAll();
        return ResponseEntity.ok().body(movies);
    }

    @GetMapping("/list-top/{amount}")
    public ResponseEntity findTop10(@PathVariable int amount) {
        List<Movie> movies = movieService.findTopByFavorited(amount);
        return ResponseEntity.ok().body(movies);
    }

    @GetMapping("/suggestion")
    public ResponseEntity findRandomMovie(@AuthenticationPrincipal UserDetailsImpl user) {
        Movie movie = movieService.findRandomMovie(user);
        return ResponseEntity.ok().body(movie);
    }

    @PutMapping("/add-favorite/{id}")
    public ResponseEntity addFavorite(@AuthenticationPrincipal UserDetailsImpl user, @PathVariable String id) {
        movieService.addFavorite(user, id);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/remove-favorite/{id}")
    public ResponseEntity removeFavorite(@AuthenticationPrincipal UserDetailsImpl user, @PathVariable String id) {
        movieService.removeFavorite(user, id);
        return ResponseEntity.noContent().build();
    }
}
