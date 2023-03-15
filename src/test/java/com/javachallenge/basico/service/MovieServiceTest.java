package com.javachallenge.basico.service;

import com.javachallenge.basico.client.MovieClient;
import com.javachallenge.basico.client.resources.MovieListResource;
import com.javachallenge.basico.entity.Movie;
import com.javachallenge.basico.entity.User;
import com.javachallenge.basico.repository.MovieRepository;
import com.javachallenge.basico.security.service.UserDetailsImpl;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Pageable;

import java.text.ParseException;
import java.text.SimpleDateFormat;
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

    private Movie movieToReturnFromRepository;

    @BeforeEach
    void setup() throws Exception {
        createMovie();
    }

    private void createMovie() throws ParseException {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        movieToReturnFromRepository = new Movie();
        movieToReturnFromRepository.setId("3");
        movieToReturnFromRepository.setTitle("The Godfather");
        movieToReturnFromRepository.setFullTitle("The Godfather (1972)");
        movieToReturnFromRepository.setReleaseDate(formatter.parse("1972-03-24"));
        movieToReturnFromRepository.setRuntime("2h 55min");
        movieToReturnFromRepository.setPlot("The aging patriarch of an organized crime dynasty in postwar New York City transfers control of his clandestine empire to his reluctant youngest son.");
        movieToReturnFromRepository.setAwards("Top rated movie #2 | Won 3 Oscars, 32 wins & 30 nominations total");
        movieToReturnFromRepository.setDirectors("Francis Ford Coppola");
        movieToReturnFromRepository.setWriters("Mario Puzo, Francis Ford Coppola");
        movieToReturnFromRepository.setStars("Marlon Brando, Al Pacino, James Caan");
        movieToReturnFromRepository.setGenres("Crime, Drama");
        movieToReturnFromRepository.setLanguages("English, Italian, Latin");
        movieToReturnFromRepository.setContentRating("R");
        movieToReturnFromRepository.setImage("https://m.media-amazon.com/images/M/MV5BM2MyNjYxNmUtYTAwNi00MTYxLWJmNWYtYzZlODY3ZTk3OTFlXkEyXkFqcGdeQXVyNzkwMjQ5NzM@._V1_Ratio0.7046_AL_.jpg");
        movieToReturnFromRepository.setYear(1972);
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
        when(movieClient.findByImdbId(anyString(), anyString())).thenReturn(movieToReturnFromRepository);

        Movie movieResponse = service.findByImdbId("3");
        Assertions.assertThat(movieResponse).isEqualTo(movieToReturnFromRepository);
    }

    @Test
    void shouldReturnMovieWhenImdbIdDoesntExist() {
        when(movieClient.findByImdbId(anyString(), anyString())).thenReturn(null);

        Movie movieResponse = service.findByImdbId("3");
        Assertions.assertThat(movieResponse).isNull();
    }

    @Test
    void shouldAddMovieWhenAddingToTopMovies() {
        List<Movie> movieListToReturn = new ArrayList<Movie>();
        MovieListResource resource = new MovieListResource(movieListToReturn);

        movieListToReturn.add(movieToReturnFromRepository);
        movieListToReturn.add(movieToReturnFromRepository);
        movieListToReturn.add(movieToReturnFromRepository);
        movieListToReturn.add(movieToReturnFromRepository);

        when(movieClient.findAll(anyString())).thenReturn(resource);
        when(repository.findMovieById(anyString())).thenReturn(null);
        when(movieClient.findByImdbId(anyString(), eq("3"))).thenReturn(movieToReturnFromRepository);

        service.saveTopMovies();
        verify(repository, times(4)).save(any());
    }

    @Test
    void findTopByFavorited() {
        List<Movie> movieListToReturn = new ArrayList<Movie>();

        movieListToReturn.add(movieToReturnFromRepository);
        movieListToReturn.add(movieToReturnFromRepository);
        movieListToReturn.add(movieToReturnFromRepository);
        movieListToReturn.add(movieToReturnFromRepository);

        when(repository.findByOrderByFavoritedDesc(Pageable.ofSize(4))).thenReturn(movieListToReturn);
        List<Movie> moviesList = service.findTopByFavorited(4);
        Assertions.assertThat(moviesList).isNotEmpty();
        Assertions.assertThat(moviesList.size()).isEqualTo(4);
    }

    @Test
    void shouldReturnNullWhenBestMatchIsEmpty() {
        when(userService.findMoviesByUsername(anyString())).thenReturn(new HashSet<>());

        Movie movie = service.findRandomMovie(new UserDetailsImpl(1L, "testuser", "testpassword", null));
        Assertions.assertThat(movie).isNull();
    }

    @Test
    void shouldReturnAMovieWhenBestMatchIsNotEmpty() {
        Set<Movie> movieListToReturn = new HashSet<Movie>();
        Set<Movie> movieListToFavorite = new HashSet<Movie>();
        Set<User> userListToFavorite = new HashSet<User>();

        Movie movie1 = new Movie();
        Movie movie2 = new Movie();
        movie1.setId("1");
        movie2.setId("2");

        movieListToReturn.add(movie1);
        movieListToReturn.add(movie2);
        movieListToFavorite.add(movie1);
        movieListToFavorite.add(movie2);
        movieListToFavorite.add(movieToReturnFromRepository);

        User user = new User("testuser", "testpassword");
        user.setId(1L);
        user.setFavorites(movieListToFavorite);
        userListToFavorite.add(user);
        movie1.setUsersFavorited(userListToFavorite);
        movie2.setUsersFavorited(userListToFavorite);
        movieToReturnFromRepository.setUsersFavorited(new HashSet<>());

        when(userService.findMoviesByUsername(anyString())).thenReturn(movieListToReturn);
        when(repository.findMovieById(anyString())).thenReturn(movie1);
        when(userService.findById(anyLong())).thenReturn(user);

        Movie movie = service.findRandomMovie(new UserDetailsImpl(1L, "testuser", "testpassword", null));
        Assertions.assertThat(movie).isNotNull();
        Assertions.assertThat(movie).isEqualTo(movieToReturnFromRepository);
    }
}