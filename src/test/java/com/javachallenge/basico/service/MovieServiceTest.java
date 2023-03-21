package com.javachallenge.basico.service;

import com.javachallenge.basico.client.MovieClient;
import com.javachallenge.basico.client.resources.MovieListResource;
import com.javachallenge.basico.entity.Movie;
import com.javachallenge.basico.entity.User;
import com.javachallenge.basico.repository.MovieRepository;
import com.javachallenge.basico.security.service.UserDetailsImpl;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.util.*;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class MovieServiceTest {

    @InjectMocks
    private MovieService service;

    @Mock
    private UserService userService;

    @Mock
    private MovieRepository repository;
    @Mock
    private MovieClient movieClient;

    private Movie testMovie;

    private void createMovie() {
        testMovie = new Movie();
        testMovie.setId("3");
        testMovie.setTitle("The Godfather");
        testMovie.setFullTitle("The Godfather (1972)");
    }

    @Test
    void shoudlReturnAllMovies() {
        List<Movie> movieListToReturn = new ArrayList<Movie>();
        movieListToReturn.add(new Movie());
        movieListToReturn.add(new Movie());
        movieListToReturn.add(new Movie());
        movieListToReturn.add(new Movie());

        when(repository.findAll()).thenReturn(movieListToReturn);

        List<Movie> moviesList = service.findAll();
        Assertions.assertThat(moviesList).isNotEmpty();
        Assertions.assertThat(moviesList.size()).isEqualTo(4);
    }

    @Test
    void shouldReturnMovieWhenSearchingForImdbId() {
        createMovie();
        when(movieClient.findByImdbId(anyString(), anyString())).thenReturn(testMovie);

        Movie movieResponse = service.findByImdbId("3");
        Assertions.assertThat(movieResponse).isEqualTo(testMovie);
    }

    @Test
    void shouldReturnMovieWhenImdbIdDoesntExist() {
        when(movieClient.findByImdbId(anyString(), anyString())).thenReturn(null);

        Movie movieResponse = service.findByImdbId("3");
        Assertions.assertThat(movieResponse).isNull();
    }

    @Test
    void shouldAddMovieWhenAddingToTopMovies() {
        createMovie();
        List<Movie> movieListToReturn = new ArrayList<Movie>();
        MovieListResource resource = new MovieListResource(movieListToReturn);

        movieListToReturn.add(testMovie);
        movieListToReturn.add(testMovie);
        movieListToReturn.add(testMovie);
        movieListToReturn.add(testMovie);

        when(movieClient.findAll(anyString())).thenReturn(resource);
        when(repository.findMovieById(anyString())).thenReturn(null);
        when(movieClient.findByImdbId(anyString(), eq("3"))).thenReturn(testMovie);

        service.saveTopMovies();
        verify(repository, times(4)).save(any());
    }

    @Test
    void findTopByFavorited() {
        createMovie();
        List<Movie> movieListToReturn = new ArrayList<Movie>();

        movieListToReturn.add(testMovie);
        movieListToReturn.add(testMovie);
        movieListToReturn.add(testMovie);
        movieListToReturn.add(testMovie);

        Page<Movie> pages = new PageImpl<>(movieListToReturn);
        when(repository.findByOrderByFavoritedDesc(Pageable.ofSize(4))).thenReturn(pages);
        Page<Movie> moviesList = service.findTopByFavorited(4);
        Assertions.assertThat(moviesList).isNotEmpty();
        Assertions.assertThat(moviesList.getSize()).isEqualTo(4);
    }

    @Test
    void shouldReturnNullWhenBestMatchIsEmpty() {
        when(userService.findMoviesByUsername(anyString())).thenReturn(new HashSet<>());

        Movie movie = service.findRandomMovie(new UserDetailsImpl(1L, "testuser", "testpassword", null));
        Assertions.assertThat(movie).isNull();
    }

    @Test
    @DisplayName("Should return a movie from favorites list of another user with common favorited movie.")
    void shouldReturnAMovieWhenBestMatchIsNotEmpty() {
        createMovie(); // Creates a full test movie at testMovie;

        // Creating common movie to add to both users favorites
        Movie commonMovie = new Movie();
        commonMovie.setId("1");

        // Creating movie list and adding commonMovie to favorites list for requested user
        Set<Movie> movieListToReturn = new HashSet<Movie>();
        movieListToReturn.add(commonMovie);

        // Creating movie list and adding commonMovie and testMovie to favorites list for the compared user
        Set<Movie> movieListToFavorite = new HashSet<Movie>();
        movieListToFavorite.add(commonMovie);
        movieListToFavorite.add(testMovie);

        // Creating user for favorite comparison; This user will be returned when searching for users with common favorited movies
        User user = new User("testuser", "testpassword");
        user.setId(1L);
        user.setFavorites(movieListToFavorite);

        // Creating a set of users to add to the common movie favorited list
        Set<User> userListToFavorite = new HashSet<User>();
        userListToFavorite.add(user);

        // Adding list of favorited users to both movies
        commonMovie.setUsersFavorited(userListToFavorite);
        testMovie.setUsersFavorited(userListToFavorite);

        // Setting response for the method called when
        when(userService.findById(anyLong())).thenReturn(user);
        // Setting response for the favorited movies list of the requested user
        when(userService.findMoviesByUsername(anyString())).thenReturn(movieListToReturn);
        // Setting repository response for movie calls
        when(repository.findMovieById(anyString())).thenReturn(commonMovie);

        Movie movie = service.findRandomMovie(new UserDetailsImpl(1L, "testuser", "testpassword", null));
        Assertions.assertThat(movie).isNotNull();
        Assertions.assertThat(movie).isEqualTo(testMovie);
    }
}