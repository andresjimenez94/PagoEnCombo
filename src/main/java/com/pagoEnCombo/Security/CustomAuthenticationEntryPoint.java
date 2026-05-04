package com.pagoEnCombo.Security;

import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Component;


import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
public class CustomAuthenticationEntryPoint implements AuthenticationEntryPoint {

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException)
            throws IOException {
        // Configura el código de estado HTTP (ejemplo: 401 Forbidden)
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);

        // Puedes establecer el tipo de respuesta y devolver un mensaje JSON
        response.setContentType("application/json");
        response.getWriter().write("{\"mensaje\": \"Access Denied: " + authException.getMessage() + "\"}");
    }
}
