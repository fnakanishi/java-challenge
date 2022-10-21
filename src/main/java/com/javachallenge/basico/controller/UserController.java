package com.javachallenge.basico.controller;

import com.javachallenge.basico.entity.Movie;
import com.javachallenge.basico.entity.User;
import com.javachallenge.basico.controller.response.UserMovieResponse;
import com.javachallenge.basico.security.request.UserRequest;
import com.javachallenge.basico.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;


@RestController
@RequestMapping("/api/users")
public class UserController {

    @Autowired private UserService userService;

    @PutMapping("/upgrade-permissions")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity upgradePermissions(@RequestBody UserRequest request) {
        User user = userService.findByUsername(request.getUsername());
        user.addRole(User.Role.ROLE_ADMIN);
        userService.save(user);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/favorites/{username}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Set<Movie>> findFavorites(@PathVariable String username) {
        Set<Movie> movies = userService.findMoviesByUsername(username);
        return ResponseEntity.ok().body(movies);
    }

    @GetMapping("/favorites")
    public ResponseEntity<List<UserMovieResponse>> findFavorites() {
        List<UserMovieResponse> movies = userService.findFavoritedByUsers();
        return ResponseEntity.ok().body(movies);
    }
}
