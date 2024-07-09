package com.lego.store.legosocialnetwork.history;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface LegoTransactionHistoryManager extends JpaRepository<LegoTransactionHistory, Integer> {

        @Query("""
             SELECT history
             FROM LegoTransactionHistory history
             WHERE history.user = :userId
        """)


    Page<LegoTransactionHistory> findAllBorrowedLegos(Pageable catalogable, Integer userId);

         @Query("""
             SELECT history
             FROM LegoTransactionHistory history
             WHERE history.lego.id = :userId
         """)

    Page<LegoTransactionHistory> findAllReturnedLegos(Pageable catalogable, Integer userId);

    @Query("""
            SELECT\s
            (COUNT(*) > 0) AS isBorrowed
            FROM LegoTransactionHistory legoTransactionHistory
            WHERE legoTransactionHistory.user = :userId
            AND legoTransactionHistory.lego.id = :legoId
            AND legoTransactionHistory.returned = false
            AND legoTransactionHistory.returnApproved = false
       \s""")
    boolean isAlreadyBorrowedByUser(Integer legoId, Integer userId);

    @Query("""
               SELECT transaction
               FROM LegoTransactionHistory transaction
               WHERE transaction.user = :userId
               AND transaction.lego.id = :legoId
               AND transaction.returned = false
               AND transaction.returnApproved = true
          """)
    Optional<LegoTransactionHistory> findByLegoIdAndUserId(Integer legoId, Integer userId);

    @Query("""
               SELECT transaction
               FROM LegoTransactionHistory transaction
               WHERE transaction.lego.owner = :legoId
               AND transaction.user = :userId
               AND transaction.returned = true
               AND transaction.returnApproved = true
          """)
    Optional<LegoTransactionHistory> findByLegoIdAndOwnerId(Integer legoId, Integer id);
}
