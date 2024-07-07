package com.lego.store.legosocialnetwork.security;

import com.lego.store.legosocialnetwork.user.UserManager;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserDetailsServiceImplement implements UserDetailsService {

    private final UserManager manager;

    @Override
    @Transactional
    public UserDetails loadUserByUsername(String userEmail) throws UsernameNotFoundException {
        return manager.findByEmail(userEmail)
                .orElseThrow(() -> new UsernameNotFoundException("Email was not found"));
    }
}
