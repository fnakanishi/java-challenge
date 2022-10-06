package com.javachallenge.basico.repository;

import com.javachallenge.basico.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
}
