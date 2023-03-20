package com.javachallenge.basico.controller;

import com.javachallenge.basico.client.MovieClient;
import com.javachallenge.basico.client.resources.MovieListResource;
import com.javachallenge.basico.entity.Movie;
import com.javachallenge.basico.entity.User;
import com.javachallenge.basico.repository.MovieRepository;
import com.javachallenge.basico.security.jwt.AuthEntryPointJwt;
import com.javachallenge.basico.security.jwt.JwtUtils;
import com.javachallenge.basico.security.service.UserDetailsImpl;
import com.javachallenge.basico.security.service.UserDetailsServiceImpl;
import com.javachallenge.basico.service.MovieService;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.util.List;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@WebMvcTest
@ContextConfiguration(classes= {
        MovieController.class,
        AuthEntryPointJwt.class
})
class MovieControllerTest {

    @Autowired
    private MockMvc mvc;

    @Autowired
    private WebApplicationContext context;

    @MockBean
    private MovieService movieService;

    @MockBean
    private MovieRepository movieRepository;

    @MockBean
    private UserDetailsServiceImpl userDetailsService;

    @MockBean
    private JwtUtils jwtUtils;

    @MockBean
    private MovieClient movieClient;

    @BeforeEach
    public void setup() {
        this.mvc = MockMvcBuilders
                .webAppContextSetup(context)
                .build();
    }

    @Test
    void shouldSaveMoviesToDB() throws Exception {
        mockAuth();
        // Creating movie resources object which will be returned from IMDB API call
        Movie movie = createMovie();
        MovieListResource resource = new MovieListResource(List.of(movie));

        // Mocking list of 250 Top Movies from IMDB
        when(movieClient.findAll(anyString())).thenReturn(resource);
        // Mocking DB search for movie ID (used when checking if a Movie already exists in DB before saving)
        when(movieRepository.findMovieById(anyString())).thenReturn(null);
        // Mocking search for movie on IMDB for detailed info
        when(movieClient.findByImdbId(anyString(), eq("3"))).thenReturn(movie);

        mvc.perform(MockMvcRequestBuilders
                        .get("/api/movies/populate")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated());
    }

    @Test
    void shouldFindAllMovies() throws Exception {
        mockAuth();

        // Mocking DB call for movie list
        when(movieService.findAll()).thenReturn(List.of(createMovie()));

        mvc.perform(MockMvcRequestBuilders
                        .get("/api/movies/list")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType("application/json"))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0]").exists())
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].title", Matchers.is("The Godfather")))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].id", Matchers.is("3")));
    }

    @Test
    void shouldFindTopNFavoritedMovies() throws Exception {
        mockAuth();

        // Mocking DB call for favorited movie list
        when(movieService.findTopByFavorited(anyInt())).thenReturn(List.of(createMovie()));

        mvc.perform(MockMvcRequestBuilders
                        .get("/api/movies/list-top/3")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType("application/json"))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0]").exists())
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].title", Matchers.is("The Godfather")))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].id", Matchers.is("3")))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].favorited", Matchers.is(3)));
    }

    @Test
    void shouldFindRandomMovie() throws Exception {
        mockAuth();

        // Mocking call to RandomMovie method
        when(movieService.findRandomMovie(any())).thenReturn(createMovie());

        mvc.perform(MockMvcRequestBuilders
                        .get("/api/movies/suggestion")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType("application/json"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.title", Matchers.is("The Godfather")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.id", Matchers.is("3")));
    }

    @Test
    void shouldAddFavoriteMovieToUser() throws Exception {
        mockAuth();

        mvc.perform(MockMvcRequestBuilders
                        .put("/api/movies/add-favorite/3")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());
    }

    @Test
    void shouldRemoveFavoriteMovieFromUser() throws Exception {
        mockAuth();

        mvc.perform(MockMvcRequestBuilders
                        .delete("/api/movies/remove-favorite/3")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());
    }

    private Movie createMovie() {
        Movie movie =  new Movie();
        movie.setId("3");
        movie.setTitle("The Godfather");
        movie.setFullTitle("The Godfather (1972)");
        movie.setFavorited(3);
        return movie;
    }

    private void mockAuth() {
        when(jwtUtils.validateJwtToken(anyString())).thenReturn(true);
        when(jwtUtils.getUserNameFromJwtToken(anyString())).thenReturn("normalUser");
        User user = new User();
        user.addRole(User.Role.ROLE_USER);
        when(userDetailsService.loadUserByUsername("normalUser")).thenReturn(UserDetailsImpl.build(user));
    }
}