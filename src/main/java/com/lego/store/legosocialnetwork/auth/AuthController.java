package com.lego.store.legosocialnetwork.auth;

import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.mail.MessagingException;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("auth")
@RequiredArgsConstructor
@Tag(name = "Authentication")
public class AuthController {

    private final AuthenticationService service;

    @PostMapping("/signup")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public ResponseEntity<?> signup(
            @RequestBody @Valid SignUpRequest request
    ) throws MessagingException {
        service.signup(request);
        return ResponseEntity.accepted().build();
    }


    @PostMapping("/auth")
    public ResponseEntity<AuthenticationResponse> auth(@RequestBody @Valid AuthenticationRequest request ) {
        return ResponseEntity.ok(service.auth(request));
    }

    @GetMapping("/activate-account")
    public void confirm(@RequestParam String token) throws MessagingException {
        service.confirm(token);
    }


}
