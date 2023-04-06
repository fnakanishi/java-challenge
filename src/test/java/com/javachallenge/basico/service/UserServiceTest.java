package com.javachallenge.basico.service;

import com.javachallenge.basico.controller.response.UserMovieResponse;
import com.javachallenge.basico.entity.Movie;
import com.javachallenge.basico.entity.User;
import com.javachallenge.basico.repository.UserRepository;
import com.javachallenge.basico.security.service.UserDetailsImpl;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @InjectMocks
    private UserService service;

    @Mock
    private UserRepository repository;

    private User userToReturnFromRepository;

    @BeforeEach
    void setup() {
        userToReturnFromRepository = new User("testuser", "testpassword");
    }

    @Test
    void shouldSaveAndReturnSameObjectWhenSaving() {
        when(repository.save(eq(userToReturnFromRepository))).thenReturn(userToReturnFromRepository);

        User userResponse = service.save(userToReturnFromRepository);
        Assertions.assertThat(userResponse).isNotNull();
        Assertions.assertThat(userResponse.getUsername()).isEqualTo(userToReturnFromRepository.getUsername());
        Assertions.assertThat(userResponse.getRoles()).isNotEmpty().isEqualTo(userToReturnFromRepository.getRoles());
    }

    @Test
    void shouldReturnSingleUserWhenUserIdExists() {
        when(repository.getReferenceById(1L)).thenReturn(userToReturnFromRepository);

        User userResponse = service.findById(1L);
        Assertions.assertThat(userResponse).isNotNull();
        Assertions.assertThat(userResponse).isEqualTo(userToReturnFromRepository);
    }

    @Test
    void shouldReturnNullWhenUserIdDoesntExistsOrNullId() {
        when(repository.getReferenceById(anyLong())).thenReturn(null);

        User userResponse = service.findById(1L);
        Assertions.assertThat(userResponse).isNull();
        userResponse = service.findById(null);
        Assertions.assertThat(userResponse).isNull();
    }

    @Test
    void shouldReturnSingleUserWhenUsernNameExists() {
        when(repository.findByUsername(anyString())).thenReturn(userToReturnFromRepository);

        User userResponse = service.findByUsername("testuser");
        Assertions.assertThat(userResponse).isNotNull();
        Assertions.assertThat(userResponse).isEqualTo(userToReturnFromRepository);
    }

    @Test
    void shouldReturnNullWhenUsernameDoesntExistsOrNull() {
        when(repository.findByUsername(anyString())).thenReturn(null);

        User userResponse = service.findByUsername("testuser");
        Assertions.assertThat(userResponse).isNull();
        userResponse = service.findByUsername(null);
        Assertions.assertThat(userResponse).isNull();
    }

    @Test
    void shouldReturnTrueWhenUsernameExists() {
        when(repository.existsByUsername(anyString())).thenReturn(true);

        Boolean userNameExists = service.existsByUsername("testuser");
        Assertions.assertThat(userNameExists).isTrue();
    }

    @Test
    void shouldReturnTrueWhenUsernameDoesntExistsOrIsNull() {
        when(repository.existsByUsername(anyString())).thenReturn(false);

        Boolean userNameExists = service.existsByUsername("testuser");
        Assertions.assertThat(userNameExists).isFalse();;
        userNameExists = service.existsByUsername(null);
        Assertions.assertThat(userNameExists).isFalse();
    }

    @Test
    void shouldReturnListOfMoviesWhenUserExists() {
        UserDetailsImpl userImpl = new UserDetailsImpl(1L, "testuser", "testpassword", null);
        Set<Movie> movies = new HashSet<>();
        movies.add(new Movie());
        userToReturnFromRepository.setFavorites(movies);
        when(repository.getReferenceById(anyLong())).thenReturn(userToReturnFromRepository);

        Set<Movie> moviesList = service.findMoviesByUser(userImpl);
        Assertions.assertThat(moviesList).isNotNull();
        Assertions.assertThat(moviesList.size()).isEqualTo(1);
    }

    @Test
    void shouldReturnListOfAllUsersThatHaveFavoritedMovies() {
        Set<User> usersList = new HashSet<>();

        usersList.add(userToReturnFromRepository);

        when(repository.findAllByFavoritesIsNotNull()).thenReturn(usersList);

        List<UserMovieResponse> list = service.findFavoritedByUsers();
        Assertions.assertThat(list).isNotEmpty();
        Assertions.assertThat(list.size()).isEqualTo(1);
    }
}