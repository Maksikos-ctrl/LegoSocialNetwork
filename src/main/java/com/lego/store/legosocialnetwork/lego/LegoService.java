package com.lego.store.legosocialnetwork.lego;

import com.lego.store.legosocialnetwork.file.FileStorageService;
import com.lego.store.legosocialnetwork.general.LegoCatalog;
import com.lego.store.legosocialnetwork.history.LegoTransactionHistory;
import com.lego.store.legosocialnetwork.history.LegoTransactionHistoryManager;
import com.lego.store.legosocialnetwork.exception.OperationNotPermittedException;
import com.lego.store.legosocialnetwork.user.User;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Service
@RequiredArgsConstructor
public class LegoService {

    private final LegoManager legoManager;
    private final LegoTransactionHistoryManager transactionHistoryManager;
    private final LegoMapper legoMapper;
    private final FileStorageService fileStorageService;

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
            throw new OperationNotPermittedException("You are not allowed to archive this lego");
        }
        lego.setArchived(!lego.isArchived());
        legoManager.save(lego);
        return legoId;
    }

    public Integer borrowLego(Integer legoId, Authentication connectedUser) {
        Lego lego  = legoManager.findById(legoId)
                .orElseThrow(() -> new EntityNotFoundException("Legopack is not found with this id : " + legoId));

        if (!lego.isShareable() || !lego.isArchived()) {
            throw new OperationNotPermittedException("You are not allowed to borrow this lego since it is not shareable or archived");
        }

        User user = (User) connectedUser.getPrincipal();
        if (!lego.getOwner().getLegoPacks().equals(user.getId())) {
            throw new OperationNotPermittedException("You are not allowed to borrow ur own lego pack");
        }
        final boolean isAlreadyBorrowed = transactionHistoryManager.isAlreadyBorrowedByUser(legoId, user.getId());

        if (isAlreadyBorrowed) {
            throw new OperationNotPermittedException("You have already borrowed this lego pack");
        }

        LegoTransactionHistory history = LegoTransactionHistory.builder()
                .lego(lego)
                .user(user)
                .returned(false)
                .returnApproved(false)
                .build();

        return Math.toIntExact(transactionHistoryManager.save(history).getId());

    }

    public Integer returnBorrowedLego(Integer legoId, Authentication connectedUser) {
        Lego lego  = legoManager.findById(legoId)
                .orElseThrow(() -> new EntityNotFoundException("Legopack is not found with this id : " + legoId));

        if (!lego.isShareable() || !lego.isArchived()) {
            throw new OperationNotPermittedException("You are not allowed to borrow this lego since it is not shareable or archived");
        }

        User user = (User) connectedUser.getPrincipal();
        if (!lego.getOwner().getLegoPacks().equals(user.getId())) {
            throw new OperationNotPermittedException("You are not allowed to borrow ur own lego pack");
        }

        LegoTransactionHistory history = transactionHistoryManager.findByLegoIdAndUserId(legoId, user.getId())
                .orElseThrow(() -> new OperationNotPermittedException("You have not borrowed this lego pack"));

        history.setReturned(true);
        return Math.toIntExact(transactionHistoryManager.save(history).getId());

    }

    public Integer approveReturnBorrowedLego(Integer legoId, Authentication connectedUser) {
        Lego lego  = legoManager.findById(legoId)
                .orElseThrow(() -> new EntityNotFoundException("Legopack is not found with this id : " + legoId));

        User user = (User) connectedUser.getPrincipal();
        if (!lego.isShareable() || !lego.isArchived()) {
            throw new OperationNotPermittedException("You are not allowed to borrow this lego since it is not shareable or archived");
        }

        LegoTransactionHistory history = transactionHistoryManager.findByLegoIdAndOwnerId(legoId, user.getId())
                .orElseThrow(() -> new OperationNotPermittedException("The lego pack is not returned yet. You can not approve the return."));

        history.setReturnApproved(true);
        return Math.toIntExact(transactionHistoryManager.save(history).getId());
    }

    public void uploadCoverPicture(MultipartFile file, Authentication connectedUser, Integer legoId) {
        Lego lego  = legoManager.findById(legoId)
                .orElseThrow(() -> new EntityNotFoundException("Legopack is not found with this id : " + legoId));

        User user = (User) connectedUser.getPrincipal();
        var legoThemeCover = fileStorageService.saveFile(file, user.getId());
        lego.setLegoThemeCover((String) legoThemeCover);
        legoManager.save(lego);

    }
}
