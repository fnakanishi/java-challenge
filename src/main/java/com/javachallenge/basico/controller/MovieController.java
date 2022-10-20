package com.javachallenge.basico.controller;

import com.javachallenge.basico.entity.Movie;
import com.javachallenge.basico.entity.User;
import com.javachallenge.basico.service.MovieService;
import com.javachallenge.basico.service.UserService;
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
    @Autowired private UserService userService;


    @GetMapping("/populate")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity findTop250() {
        movieService.saveTopMovies();
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }
    @GetMapping("/list-all")
    public ResponseEntity findAll() {
        List<Movie> movies = movieService.findAll();
        return ResponseEntity.ok().body(movies);
    }

    @PutMapping("/add-favorite/{id}")
    public ResponseEntity addFavorite(@AuthenticationPrincipal User user, @RequestParam String id) {
        Movie movie = movieService.findByIdOrImdbId(id);
        if (movie != null) {
            userService.addFavorite(user, movie);
        }
        return ResponseEntity.noContent().build();
    }
}
