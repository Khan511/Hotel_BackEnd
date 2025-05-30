package com.naji.gjensidige.sHotel.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.naji.gjensidige.sHotel.model.User;

public interface UserRepository extends JpaRepository<User, Long> {

    boolean existsByEmail(String email);
    // boolean existByEmail(String email);

    void deleteByEmail(String email);

    Optional<User> getUserByEmail(String email);
}
