package com.javachallenge.basico.service;

import com.javachallenge.basico.entity.Movie;
import com.javachallenge.basico.entity.User;
import com.javachallenge.basico.repository.MovieRepository;
import com.javachallenge.basico.security.service.UserDetailsImpl;
import org.assertj.core.api.Assertions;
import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Set;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class MovieService2Test {

    @InjectMocks
    private MovieService service;

    @Mock
    private UserService userService;

    @Mock
    private MovieRepository repository;

    @Test
    @DisplayName("Should return a movie from favorites list of another user with common favorited movie.")
    void shouldReturnAMovieWhenBestMatchIsNotEmpty() {
        Movie recommendedMovie = createRecommendedMovie();
        Movie commonMovie = createCommonMovie();
        User user = createSimilarInterestUser(commonMovie, recommendedMovie);

        addUserToMoviesFavorite(recommendedMovie, commonMovie, user);
        mockToCurrentUserContainsFavoriteMovie(commonMovie);

        Movie movie = service.findRandomMovie(new UserDetailsImpl(1L, "testuser", "testpassword", null));
        Assertions.assertThat(movie).isNotNull();
        Assertions.assertThat(movie).isEqualTo(recommendedMovie);
    }

    private void mockToCurrentUserContainsFavoriteMovie(Movie commonMovie) {
        Set<Movie> movieListToReturn = Set.of(commonMovie);
        when(userService.findMoviesByUsername(anyString())).thenReturn(movieListToReturn);
        when(repository.findMovieById(anyString())).thenReturn(commonMovie);
    }

    private static void addUserToMoviesFavorite(Movie testMovie, Movie commonMovie, User user) {
        Set<User> userListToFavorite = Set.of(user);

        // Adding list of favorited users to both movies
        commonMovie.setUsersFavorited(userListToFavorite);
        testMovie.setUsersFavorited(userListToFavorite);
    }

    @NotNull
    private static Movie createCommonMovie() {
        // Creating common movie to add to both users favorites
        Movie commonMovie = new Movie();
        commonMovie.setId("1");
        return commonMovie;
    }

    @NotNull
    private static Movie createRecommendedMovie() {
        Movie testMovie = new Movie();
        testMovie.setId("3");
        testMovie.setTitle("The Godfather");
        testMovie.setFullTitle("The Godfather (1972)");
        return testMovie;
    }

    @NotNull
    private User createSimilarInterestUser(Movie... movies) {
        User user = new User("testuser", "testpassword");
        user.setId(1L);
        Set<Movie> movieListToFavorite = Set.of(movies);
        user.setFavorites(movieListToFavorite);
        when(userService.findById(anyLong())).thenReturn(user);
        return user;
    }

    private class RandomMovieBuilder {
        private User user;

         public RandomMovieBuilder createUser() {
             user = new User("testuser", "testpassword");
             user.setId(1L);
             return this;
         }


    }
}