package com.lego.store.legosocialnetwork.lego;

import com.lego.store.legosocialnetwork.general.LegoCatalog;
import io.swagger.annotations.Api;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

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




}
