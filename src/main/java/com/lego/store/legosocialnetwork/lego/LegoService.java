package com.lego.store.legosocialnetwork.lego;

import com.lego.store.legosocialnetwork.general.LegoCatalog;
import com.lego.store.legosocialnetwork.history.LegoTransactionHistory;
import com.lego.store.legosocialnetwork.history.LegoTransactionHistoryManager;
import com.lego.store.legosocialnetwork.lego.exception.OperationNotPermittedException;
import com.lego.store.legosocialnetwork.user.User;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class LegoService {

    private final LegoManager legoManager;
    private final LegoTransactionHistoryManager transactionHistoryManager;
    private final LegoMapper legoMapper;

    public Integer save(LegoRequest request, Authentication connectedUser) {
        User user = (User) connectedUser.getPrincipal();
        Lego lego = legoMapper.toLego(request);
        lego.setOwner(user);
        return Math.toIntExact(legoManager.save(lego).getId());
    }

    public LegoResponse findById(Integer legoId) {
        return legoManager.findById(legoId)
                .map(legoMapper::toLegoResponse)
                .orElseThrow(() -> new EntityNotFoundException("Lego not found with this id : " + legoId));
    }

    public LegoCatalog<LegoResponse> findAllLegos(int catalog, int size, Authentication connectedUser) {
        User user = (User) connectedUser.getPrincipal();
        Pageable catalogable = PageRequest.of(catalog, size, Sort.by("createdDate").descending());
        Page<Lego> legos = legoManager.findAllDisplayableLegos(catalogable, user.getId());
        List<LegoResponse> legoResponses = legos.stream()
                .map(legoMapper::toLegoResponse)
                .toList();
        return new LegoCatalog<>(
                legoResponses,
                legos.getTotalPages(),
                (int) legos.getTotalElements(),
                legos.getNumber(),
                legos.getSize(),
                legos.isFirst(),
                legos.isLast()
        );
    }

    public LegoCatalog<LegoResponse> findAllLegosByOwner(int catalog, int size, Authentication connectedUser) {
        User user = (User) connectedUser.getPrincipal();
        Pageable catalogable = PageRequest.of(catalog, size, Sort.by("createdDate").descending());
        Page<Lego> legos = legoManager.findAll(LegoSpecification.withOwnerId(user.getId()), catalogable);
        List<LegoResponse> legoResponses = legos.stream()
                .map(legoMapper::toLegoResponse)
                .toList();
        return new LegoCatalog<>(
                legoResponses,
                legos.getTotalPages(),
                (int) legos.getTotalElements(),
                legos.getNumber(),
                legos.getSize(),
                legos.isFirst(),
                legos.isLast()
        );
    }

    public LegoCatalog<BorrowedLegoResponse> findAllBorrowedBooks(int catalog, int size, Authentication connectedUser) {
        User user = (User) connectedUser.getPrincipal();
        Pageable catalogable = PageRequest.of(catalog, size, Sort.by("createdDate").descending());
        Page<LegoTransactionHistory> allBorrowedLegos = transactionHistoryManager.findAllBorrowedLegos(catalogable,  user.getId());
        List<BorrowedLegoResponse> legoResponse = allBorrowedLegos.stream()
                .map(legoMapper::toBorrowedLegoResponse)
                .toList();
        return new LegoCatalog<>(
                legoResponse,
                allBorrowedLegos.getTotalPages(),
                (int) allBorrowedLegos.getTotalElements(),
                allBorrowedLegos.getNumber(),
                allBorrowedLegos.getSize(),
                allBorrowedLegos.isFirst(),
                allBorrowedLegos.isLast()
        );
    }

    public LegoCatalog<BorrowedLegoResponse> findAllReturnedBooks(int catalog, int size, Authentication connectedUser) {
        User user = (User) connectedUser.getPrincipal();
        Pageable catalogable = PageRequest.of(catalog, size, Sort.by("createdDate").descending());
        Page<LegoTransactionHistory> allBorrowedLegos = transactionHistoryManager.findAllReturnedLegos(catalogable,  user.getId());
        List<BorrowedLegoResponse> legoResponse = allBorrowedLegos.stream()
                .map(legoMapper::toBorrowedLegoResponse)
                .toList();
        return new LegoCatalog<>(
                legoResponse,
                allBorrowedLegos.getTotalPages(),
                (int) allBorrowedLegos.getTotalElements(),
                allBorrowedLegos.getNumber(),
                allBorrowedLegos.getSize(),
                allBorrowedLegos.isFirst(),
                allBorrowedLegos.isLast()
        );
    }

    public Integer updateShareableStatus(Integer legoId, Authentication connectedUser) {
        Lego lego = legoManager.findById(legoId)
                .orElseThrow(() -> new EntityNotFoundException("Lego not found with this id : " + legoId));
        User user = (User) connectedUser.getPrincipal();
        if (!lego.getOwner().getLegoPacks().equals(user.getId())) {
            throw new OperationNotPermittedException("You are not allowed to update this lego");
        }
        lego.setShareable(!lego.isShareable());
        legoManager.save(lego);
        return legoId;
    }

    public Integer updateArchivedStatus(Integer legoId, Authentication connectedUser) {
        Lego lego = legoManager.findById(legoId)
                .orElseThrow(() -> new EntityNotFoundException("Lego not found with this id : " + legoId));
        User user = (User) connectedUser.getPrincipal();
        if (!lego.getOwner().getLegoPacks().equals(user.getId())) {
            throw new OperationNotPermittedException("You are not allowed to update this lego");
        }
        lego.setArchived(!lego.isArchived());
        legoManager.save(lego);
        return legoId;
    }
}
