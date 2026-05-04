package com.pagoEnCombo.Security;

import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

@Service
public class JwtUserDetailsService implements UserDetailsService {

    @Override
    public UserDetails loadUserByUsername(String username) {
        // Aquí deberías cargar al usuario desde tu base de datos
        return User.builder()
                .username("admin")
                .password("{noop}admin")  // Esto es solo un ejemplo, en producción usa bcrypt
                .roles("USER")
                .build();
    }

}
