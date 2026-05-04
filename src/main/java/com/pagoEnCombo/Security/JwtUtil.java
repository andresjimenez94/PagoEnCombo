package com.pagoEnCombo.Security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;

import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

@Component
public class JwtUtil {

    private static final String SECRET_KEY = "F354708EB96596C253BCDDEF01A512C9704E6DF508C8A31B3B62411B8E23E148";  // Cambia esta clave por una clave más segura en producción

    private static Set<String> blacklistedTokens = new HashSet<>();

    // Generar el token JWT con nombre de usuario y rol
    public String generateToken(String username, String role) {
        
        Key key = Keys.hmacShaKeyFor(SECRET_KEY.getBytes());  // Aquí es donde generas la clave
        String token = Jwts.builder()
                .setSubject(username)
                .claim("role", role)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60))  // Expiración en 1 hora
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();

        return token;
    }

    // Revocar un token (agregarlo a la lista negra)
    public void revokeToken(String token) {
        blacklistedTokens.add(token);
    }

    // Extraer el nombre de usuario desde el JWT
    public String extractUsername(String token) {
        try {
            return Jwts.parserBuilder()
                    .setSigningKey(SECRET_KEY.getBytes())  // La clave secreta para verificar la firma
                    .build()
                    .parseClaimsJws(token)  // Parsear el JWT
                    .getBody()
                    .getSubject();  // Extraer el nombre de usuario (subject)
        } catch (JwtException | IllegalArgumentException e) {
            throw new JwtException("Error al extraer el nombre de usuario del token", e);
        }
    }

    public boolean validateToken(String token, String username) {
        if (blacklistedTokens.contains(token)) {
            return false;
        }
        try {
    
            String extractedUsername = extractUsername(token); 
            boolean isExpired = isTokenExpired(token);  
            // Verificar si el nombre de usuario extraído coincide con el proporcionado
            return (extractedUsername.equals(username) && !isExpired);

        } catch (ExpiredJwtException e) {
            // Token expirado
            return false;
        } catch (MalformedJwtException e) {
            // Token mal formado
            return false;
        } catch (IllegalArgumentException e) {
            // Firma inválida
            return false;
        } catch (Exception e) {
            // Manejo genérico de otros errores
            return false;
        }
    }

    // Verificar si el token ha expirado
    private boolean isTokenExpired(String token) {
        Date expirationDate = parseClaims(token).getExpiration();
        return expirationDate.before(new Date());  // Verificar si la fecha de expiración ya ha pasado
    }

    // Parsear el token para obtener las claims (información) del token
    private Claims parseClaims(String token) {
        try {
            return Jwts.parserBuilder()
                    .setSigningKey(SECRET_KEY.getBytes())  // La clave secreta para verificar la firma
                    .build()
                    .parseClaimsJws(token)  // Parsear el JWT
                    .getBody();  // Obtener las claims (información)
        } catch (JwtException e) {
            throw new JwtException("Error al parsear el token", e);
        }
    }
}