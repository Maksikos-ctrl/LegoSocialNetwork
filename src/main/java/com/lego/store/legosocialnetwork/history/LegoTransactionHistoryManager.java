package com.lego.store.legosocialnetwork.history;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface LegoTransactionHistoryManager extends JpaRepository<LegoTransactionHistory, Integer> {

        @Query("""
             SELECT history
             FROM LegoTransactionHistory history
             WHERE history.user.id = :userId
        """)


        Page<LegoTransactionHistory> findAllBorrowedLegos(Pageable catalogable, Integer userId);

         @Query("""
             SELECT history
             FROM LegoTransactionHistory history
             WHERE history.lego.owner.id = :userId
         """)

        Page<LegoTransactionHistory> findAllReturnedLegos(Pageable catalogable, Integer userId);
}
