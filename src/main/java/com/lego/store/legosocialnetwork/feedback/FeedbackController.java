package com.lego.store.legosocialnetwork.feedback;

import com.lego.store.legosocialnetwork.general.LegoCatalog;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/feedback")
@RequiredArgsConstructor
@Tag(name = "Feedback")
public class FeedbackController {

    private final FeedbackService service;

    @PostMapping
    public ResponseEntity<Integer> saveFeedback(
            @Valid @RequestBody FeedbackRequest request,
            Authentication connectedUser)
    {
        return ResponseEntity.ok(service.save(request, connectedUser));
    }

    @GetMapping("/lego/{lego-id}")
    public ResponseEntity<Object> findAllFeedbackByLegoId(
            @PathVariable("lego-id") Integer legoId,
            @RequestParam(name="catalog", defaultValue = "0", required = false) int catalog,
            @RequestParam(name="size", defaultValue = "10", required = false) int size,
            Authentication connectedUser
    )
    {
        return ResponseEntity.ok(service.findAllFeedbackByLegoId(legoId, catalog, size, connectedUser));
    }

    @GetMapping("/lego/{lego-id}")
    public ResponseEntity<LegoCatalog<FeedbackResponse>> findAllFeedbackByLego(
            @PathVariable("lego-id") Integer legoId,
            @RequestParam(name="catalog", defaultValue = "0", required = false) int catalog,
            @RequestParam(name="size", defaultValue = "10", required = false) int size,
            Authentication connectedUser

    )
    {
        return ResponseEntity.ok(service.findAllFeedbackByLegoId(legoId, catalog, size, connectedUser));
    }


}
