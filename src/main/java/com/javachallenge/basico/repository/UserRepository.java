package com.javachallenge.basico.repository;

import com.javachallenge.basico.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Set;

public interface UserRepository extends JpaRepository<User, Long> {
    User findByUsername(String username);

    Boolean existsByUsername(String username);

    Set<User> findAllByFavoritesIsNotNull();
}
