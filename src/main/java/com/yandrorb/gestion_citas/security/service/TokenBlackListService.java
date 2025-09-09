package com.yandrorb.gestion_citas.security.service;

import io.jsonwebtoken.Claims;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.time.Duration;

@Service
@RequiredArgsConstructor
public class TokenBlackListService {
    private final StringRedisTemplate stringRedisTemplate;
    private final JWTService jwtService;

    public void invalidarTokenUsuario(String username) {
        String tokenKey="blacklist:token:"+username;
        String timestamp=String.valueOf(System.currentTimeMillis());
        stringRedisTemplate.opsForValue().set(tokenKey,timestamp,Duration.ofDays(7));
    }
    public boolean esTokenValido(String token) {
        String username=jwtService.extractUsername(token);
        long issueAt=jwtService.extractClaim(token, Claims::getIssuedAt).getTime();
        String invalidtoken=stringRedisTemplate.opsForValue().get("blacklist:token:"+username);
        if(invalidtoken!=null){
            long invalidatetoken=Long.parseLong(invalidtoken);
            return issueAt<invalidatetoken;
        }
        return false;
    }
}
