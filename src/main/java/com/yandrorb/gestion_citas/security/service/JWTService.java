package com.yandrorb.gestion_citas.security.service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.Base64;
import java.util.Date;
import java.util.Map;
import java.util.function.Function;

@Service
public class JWTService {
    @Value("${jwt.secret:s9v8X2pR!fL7dQz4mE#hY6wT@bN3uV1k}")
    private String SECRET_KEY;

    @Value("${jwt.expiration:86400000}")
    private long jwtExpiration;

    @Value("${JWT_EXPIRATION_REFRESH:86400000}")
    private long jwtExpirationRefresh;

    /*Convierte la llave maestra en bytes Base64 y la transforma en un objeto Key valido para HS256.
    * Sirve para firmar los tokens */
    private Key getSigningKey() {
        byte[] keyBytes = Base64.getEncoder().encode(SECRET_KEY.getBytes());
        return Keys.hmacShaKeyFor(keyBytes);
    }
    /*
    * Extrae todos los datos del token como usuario, fecha de expiracion,etc
    * */
    private Claims extractAllClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(getSigningKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }
    /*
    * Extrae un dato especifico del token, primeramente se apoya extrayendo todos los datos y
    * luego con la funcion generica de tipo Claims se extrae el dato especifico
    * */
    public <T> T extractClaim(String token, Function <Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }
    /*
    * Extrae el usuario del token apoyandose de la funcion extractClaim
    * */
    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }
    /*
    * Extrae la fecha de expiracion apoyandose de la funcion extractClaim
    * */
    public Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }
    /*
    * Verifica si el token no ha expirado, extrayendo primeramente la fecha y
    * luego verificandola con la fecha actual
    * */
    private boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }
    /*
    * Genera un token con los datos del userDetails y con la posibilidad de agregar datos extras al token
    * caso contrario pasarle un Map<String,Object> vacio
    * */

    public String generateToken(Map<String, Object> extraClaims, UserDetails userDetails){
        return Jwts.builder()
                .setClaims(extraClaims)
                .setSubject(userDetails.getUsername())
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis()+jwtExpiration))
                .signWith(getSigningKey(), SignatureAlgorithm.HS256)
                .compact();
    }
    /*
    * Genera un token de larga duración, sirve para generar token de corta duracion. Su uso sería
    * Se verifica el token de larga duracion y si es valido se genera el token de corta duracion con generateToken
    * */
    public String generateRefreshToken(UserDetails userDetails){
        return Jwts.builder()
                .setSubject(userDetails.getUsername())
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis()+jwtExpirationRefresh))
                .signWith(getSigningKey(),SignatureAlgorithm.HS256)
                .compact();
    }
    /*
    * Verifica si el token es valido verificando si el usuario registrado en el token y con el de userDetails
    * coinciden y verificando si aun no ha expirado
    *  */
    public boolean isTokenValid(String token, UserDetails userDetails){
        final String username = extractUsername(token);
        return (username.equals(userDetails.getUsername()) && !isTokenExpired(token));
    }
    /*
    * Verifica el token parseando el token con la firma establecida en el sistema y si no lanza excepcion
    * se da por valido
    * */
    public boolean isValidateToken(String token){
        try{
            Jwts.parserBuilder().setSigningKey(getSigningKey())
                    .build().parseClaimsJws(token);
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}
