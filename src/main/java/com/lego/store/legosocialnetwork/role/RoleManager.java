package com.lego.store.legosocialnetwork.role;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RoleManager extends JpaRepository<Role, Integer> {

    Optional<Role> findByName(String role);


}
