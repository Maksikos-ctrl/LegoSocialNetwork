package com.lego.store.legosocialnetwork.feedback;

import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.awt.print.Pageable;

public interface FeedbackManager extends JpaRepository<Feedback, Integer> {

    @Query("""
          SELECT feedback
          FROM Feedback feedback
          WHERE feedback.lego.id = :legoId
   
         """)
    Page<Feedback> findAllByLegoId(@Param("legoId")Integer legoId, Pageable catalogable);
}
