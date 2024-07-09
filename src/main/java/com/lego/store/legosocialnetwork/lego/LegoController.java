package com.lego.store.legosocialnetwork.lego;

import com.lego.store.legosocialnetwork.general.LegoCatalog;
import io.swagger.annotations.Api;
import io.swagger.v3.oas.annotations.Parameter;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/legos")
@RequiredArgsConstructor
@Api(tags = "Lego")
public class LegoController {

    private final LegoService service;

    @PostMapping
    public ResponseEntity<Integer> saveLego(@Valid @RequestBody LegoRequest request, Authentication connectedUser) {
        return ResponseEntity.ok(service.save(request, connectedUser));
    }

    @GetMapping("{book-id}")
    public ResponseEntity<LegoResponse> findLegoById(@PathVariable("book-id") Integer legoId) {
        return ResponseEntity.ok(service.findById(legoId));
    }

    @GetMapping
    public ResponseEntity <LegoCatalog<LegoResponse>> findAllLegos(@RequestParam(name="catalog", defaultValue = "0", required = false) int catalog,
                                                                   @RequestParam(name="size", defaultValue = "10", required = false) int size,
                                                                   Authentication connectedUser)
    {
        return ResponseEntity.ok(service.findAllLegos(catalog, size, connectedUser));
    }

    @GetMapping("/owner")
    public ResponseEntity <LegoCatalog<LegoResponse>> findAllLegosByOwner(@RequestParam(name="catalog", defaultValue = "0", required = false) int catalog,
                                                                         @RequestParam(name="size", defaultValue = "10", required = false) int size,
                                                                         Authentication connectedUser)
    {
        return ResponseEntity.ok(service.findAllLegosByOwner(catalog, size, connectedUser));
    }

    @GetMapping("/borrowed")
    public ResponseEntity <LegoCatalog<BorrowedLegoResponse>> findAllBorrowedLegos(@RequestParam(name="catalog", defaultValue = "0", required = false) int catalog,
                                                                          @RequestParam(name="size", defaultValue = "10", required = false) int size,
                                                                          Authentication connectedUser)
    {
        return ResponseEntity.ok(service.findAllBorrowedBooks(catalog, size, connectedUser));
    }

    @GetMapping("/returned")
    public ResponseEntity <LegoCatalog<BorrowedLegoResponse>> findAllReturnedLegos(@RequestParam(name="catalog", defaultValue = "0", required = false) int catalog,
                                                                                   @RequestParam(name="size", defaultValue = "10", required = false) int size,
                                                                                   Authentication connectedUser)
    {
        return ResponseEntity.ok(service.findAllReturnedBooks(catalog, size, connectedUser));
    }


    @PatchMapping("/shareable/{lego-id}")
    public ResponseEntity<Integer> updateShareableStatus(
            @PathVariable("lego-id") Integer legoId,
            Authentication connectedUser
    ) {
        return ResponseEntity.ok(service.updateShareableStatus(legoId, connectedUser));
    }


    @PatchMapping("/archived/{lego-id}")
    public ResponseEntity<Integer> updateArchivedStatus(
            @PathVariable("lego-id") Integer legoId,
            Authentication connectedUser
    ) {
        return ResponseEntity.ok(service.updateArchivedStatus(legoId, connectedUser));
    }


    @PostMapping("/borrow/{lego-id}")
    public ResponseEntity<Integer> borrowLego(
            @PathVariable("lego-id") Integer legoId,
            Authentication connectedUser
    ) {
        return ResponseEntity.ok(service.borrowLego(legoId, connectedUser));
    }

    @PatchMapping("/borrow/return/{return-id}")
    public ResponseEntity<Integer> returnLego(
            @PathVariable("return-id") Integer returnId,
            Authentication connectedUser
    ) { return ResponseEntity.ok(service.returnBorrowedLego(returnId, connectedUser)); }


    @PatchMapping("/borrow/return/approve/{return-id}")
    public ResponseEntity<Integer> approvedReturnLego(
            @PathVariable("return-id") Integer returnId,
            Authentication connectedUser
    ) { return ResponseEntity.ok(service.approveReturnBorrowedLego(returnId, connectedUser)); }

    @PostMapping(value = "/cover/{book-id}", consumes = {"multipart/form-data"})
    public ResponseEntity<?> uploadCoverPicture(
            @PathVariable("book-id") Integer legoId,
            @Parameter
            @RequestPart("file") MultipartFile file,
            Authentication connectedUser
    )
    {
        service.uploadCoverPicture(file, connectedUser, legoId);
        return ResponseEntity.accepted().build();
    }



}
