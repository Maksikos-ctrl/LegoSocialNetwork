package com.lego.store.legosocialnetwork.lego;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.security.core.Authentication;

public interface LegoManager extends JpaRepository<Lego, Integer>, JpaSpecificationExecutor<Lego> {


    @Query("""
            SELECT lego
            FROM Lego lego
            WHERE lego.archived = false
            AND lego.shareable = true
            AND lego.owner != :userId
            """)
    Page<Lego> findAllDisplayableLegos(Pageable catalogable, Integer userId);


}
