package com.javachallenge.basico.controller;

import com.javachallenge.basico.entity.Movie;
import com.javachallenge.basico.security.service.UserDetailsImpl;
import com.javachallenge.basico.service.MovieService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/movies")
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

    @GetMapping("/list-top-10")
    public ResponseEntity findTop10() {
        List<Movie> movies = movieService.findTop10ByFavorited();
        return ResponseEntity.ok().body(movies);
    }

    @PutMapping("/add-favorite/{id}")
    public ResponseEntity addFavorite(@AuthenticationPrincipal UserDetailsImpl user, @PathVariable Long id) {
        movieService.addFavorite(user, id);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/remove-favorite/{id}")
    public ResponseEntity removeFavorite(@AuthenticationPrincipal UserDetailsImpl user, @PathVariable Long id) {
        movieService.removeFavorite(user, id);
        return ResponseEntity.noContent().build();
    }
}
