package com.lego.store.legosocialnetwork.user;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface TokenManager  extends JpaRepository<Token, Integer> {

    Optional<Token> findByToken(String token);

}
