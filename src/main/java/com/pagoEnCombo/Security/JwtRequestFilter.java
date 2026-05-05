package com.pagoEnCombo.Security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.util.Map;
import java.util.HashMap;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import org.springframework.lang.NonNull;


@Component
public class JwtRequestFilter extends OncePerRequestFilter {

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private JwtUserDetailsService jwtUserDetailsService;

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain chain)
            throws ServletException, IOException {

        String path = request.getRequestURI();

        // ✅ ENDPOINTS PÚBLICOS (SIN JWT)
        if (
            path.equals("/api/usuario/authenticate") ||
            path.equals("/api/version")
        ) {
            chain.doFilter(request, response);
            return;
        }

        String authHeader = request.getHeader("Authorization");

        // ✅ SOLO intentar autenticar si hay Bearer
        if (authHeader != null && authHeader.startsWith("Bearer ")) {

            String jwt = authHeader.substring(7);
            String username = null;

            try {
                username = jwtUtil.extractUsername(jwt);
            } catch (Exception e) {
                // Token inválido → no autenticar
                // (Spring Security decidirá)
            }

            if (username != null
                && SecurityContextHolder.getContext().getAuthentication() == null
                && jwtUtil.validateToken(jwt, username)) {

                var userDetails =
                        jwtUserDetailsService.loadUserByUsername(username);

                var authentication =
                        new UsernamePasswordAuthenticationToken(
                                userDetails,
                                null,
                                userDetails.getAuthorities()
                        );

                SecurityContextHolder.getContext()
                        .setAuthentication(authentication);
            }
        }

        // ✅ SIEMPRE continuar la cadena
        chain.doFilter(request, response);
    }
}
