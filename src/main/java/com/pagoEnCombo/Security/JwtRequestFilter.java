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
    protected void doFilterInternal(@NonNull HttpServletRequest request,@NonNull HttpServletResponse response,@NonNull FilterChain chain)
            throws ServletException, IOException {
    
        String authorizationHeader = request.getHeader("Authorization");
        String jwt = null;
        String username = null;

        if (request.getRequestURI().equals("/api/usuario/authenticate") || authorizationHeader == null || !authorizationHeader.startsWith("Bearer ")) {
            chain.doFilter(request, response); // Permite que la solicitud continúe sin validar el JWT
            return;
        }
    
        // Verifica si el encabezado "Authorization" contiene un token
        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            jwt = authorizationHeader.substring(7);  // Extraer el token JWT
            username = jwtUtil.extractUsername(jwt);  // Extraer el nombre de usuario desde el token
        }
    
        if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
    
            try {
                if (jwtUtil.validateToken(jwt, username)) {
                    var userDetails = jwtUserDetailsService.loadUserByUsername(username);
                    var authentication = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
                    SecurityContextHolder.getContext().setAuthentication(authentication);
                } else {
                    sendErrorResponse(response, "JWT token is not valid", HttpStatus.UNAUTHORIZED);
                    return;  // Detener la ejecución si el token no es válido
                }
            } catch (Exception e) {
                sendErrorResponse(response, "Invalid JWT token: " + e.getMessage(), HttpStatus.UNAUTHORIZED);
                return;  // Detener la ejecución si hubo un error
            }
        }
    
        // Continuar con el siguiente filtro o el controlador
        chain.doFilter(request, response); 
    }

private void sendErrorResponse(HttpServletResponse response, String message, HttpStatus status) throws IOException {
    response.setStatus(status.value());  // Establece el código de estado HTTP (401, etc.)
    response.setContentType("application/json");  // Define el tipo de contenido como JSON
    Map<String, String> errorResponse = new HashMap<>();
    errorResponse.put("error", message);  // Agrega el mensaje de error en el cuerpo
    String jsonResponse = new ObjectMapper().writeValueAsString(errorResponse);  // Convierte el mapa a JSON
    response.getWriter().write(jsonResponse);  // Escribe el cuerpo de la respuesta
}
}