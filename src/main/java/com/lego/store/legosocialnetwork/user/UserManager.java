package com.lego.store.legosocialnetwork.user;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserManager extends JpaRepository<User, Integer> {
    Optional<User> findByEmail(String email);
}
