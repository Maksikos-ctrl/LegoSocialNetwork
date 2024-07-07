package com.lego.store.legosocialnetwork.auth;

import com.lego.store.legosocialnetwork.email.EmailManager;
import com.lego.store.legosocialnetwork.email.EmailTemplateName;
import com.lego.store.legosocialnetwork.role.RoleManager;
import com.lego.store.legosocialnetwork.security.JwtService;
import com.lego.store.legosocialnetwork.user.Token;
import com.lego.store.legosocialnetwork.user.TokenManager;
import com.lego.store.legosocialnetwork.user.User;
import com.lego.store.legosocialnetwork.user.UserManager;
import jakarta.mail.MessagingException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.management.RuntimeErrorException;
import java.security.SecureRandom;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AuthenticationService {

    private final RoleManager roleManager;
    private final PasswordEncoder passwordEncoder;
    private final UserManager userManager;
    private final TokenManager tokenManager;
    private final EmailManager emailManager;
    private final AuthenticationManager authManager;
    private final JwtService jwtService;

    @Value("${application.mailing.frontend.activation-url}")
    private String activationUrl;

    public void signup(SignUpRequest request) throws MessagingException {
        var userRole = roleManager.findByName("USER")
                //TODO: fix better exception handling
                .orElseThrow(() -> new IllegalStateException("Role not found"));
        var user = User.builder()
                .firstName(request.getFirstName())
                .lastName(request.getLastName())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .accountLocked(false)
                .enabled(false)
                .roles(List.of(userRole))
                .build();

        userManager.save(user);
        sendValidationEmail(user);
    }

    private void sendValidationEmail(User user) throws MessagingException {

        var newToken = generateAndSaveActivationToken(user);

        emailManager.sendEmail(
                user.getEmail(),
                user.fullName(),
                EmailTemplateName.ACTIVATE_ACCOUNT,
                activationUrl,
                newToken,
                "Account Activation"
        );

    }

    private String generateAndSaveActivationToken(User user) {
        // generate a token
        String generatedToken = generateActivationCode(6);
        var token = Token.builder()
                .token(generatedToken)
                .createdAt(LocalDateTime.now())
                .expiresAt(LocalDateTime.now().plusMinutes(15))
                .user(user)
                .build();

        tokenManager.save(token);
        return generatedToken;
    }

    /**
     * This method generates an activation code of a specified length.
     * The activation code consists of alphanumeric characters (0-9, A).
     *
     * @param length the length of the activation code to be generated
     * @return a string representing the generated activation code
     */
     private String generateActivationCode(int length) {
        String chars = "0123456789";
        StringBuilder sb = new StringBuilder();
        SecureRandom secureRandom = new SecureRandom();
        for (int i = 0; i < length; i++) {
            int randomIndex = secureRandom.nextInt(chars.length());
            sb.append(chars.charAt(randomIndex));
        }
        return sb.toString();
    }

    public AuthenticationResponse auth(AuthenticationRequest request) {
         var auth = authManager.authenticate(
                 new UsernamePasswordAuthenticationToken(
                      request.getEmail(), request.getPassword()
                 )
         );
         var claims = new HashMap<String, Object>();
         var user = ((User)auth.getPrincipal());
         claims.put("fullName", user.fullName());
         var jwtToken = jwtService.generateToken(claims, user);
         return AuthenticationResponse.builder()
                .token(jwtToken)
                .build();
    }

    //@Transactional
    public void confirm(String token) throws MessagingException {

         Token savedToken = tokenManager.findByToken(token)
                 //TODO: fix better exception handling
                 .orElseThrow(() -> new IllegalStateException("Token not found"));
         if (LocalDateTime.now().isAfter(savedToken.getExpiresAt())) {
             sendValidationEmail(savedToken.getUser());
             throw new RuntimeException("Token expired, new token sent");
         };
         var user = userManager.findById(savedToken.getUser().getId()).orElseThrow(() -> new UsernameNotFoundException("User not found"));
         user.setEnabled(true);
         userManager.save(user);
         savedToken.setValidatedAt(LocalDateTime.now());
         tokenManager.save(savedToken);
    }
}
