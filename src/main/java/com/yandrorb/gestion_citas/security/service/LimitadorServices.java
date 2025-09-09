package com.yandrorb.gestion_citas.security.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class LimitadorServices {
    private final StringRedisTemplate stringRedisTemplate;
    private final int MAX_CHANGES_PER_WEEK=3;
    private final int MAX_CHANGES_POR_DAY=5;
    private final int MAX_CHANGES_PER_HOUR=2;

    public boolean puedeCambiarUsuario(Long idUsuario){
        String key="username_changes:"+idUsuario;
        String changes=stringRedisTemplate.opsForValue().get(key);
        int currentChanges=changes != null ? Integer.parseInt(changes) : 0;
        return currentChanges < MAX_CHANGES_PER_WEEK;
    }
    public boolean puedeCambiarPassword(Long idUsuario){
        String key="password_changes_daily:"+idUsuario;
        String changes=stringRedisTemplate.opsForValue().get(key);
        int dailyCount=changes != null ? Integer.parseInt(changes) : 0;

        String key2="password_changes_hourly:"+idUsuario;
        String changes2=stringRedisTemplate.opsForValue().get(key2);
        int hourlyCount=changes2 != null ? Integer.parseInt(changes2) : 0;

        return dailyCount < MAX_CHANGES_POR_DAY &&
                hourlyCount < MAX_CHANGES_PER_HOUR;
    }
    public void guadarCambiosUsuario(Long idUsuario){
        String key="username_changes:"+idUsuario;
        Optional<Long> changes=Optional.ofNullable(stringRedisTemplate.opsForValue().increment(key));
        if(changes.isPresent()&& changes.get()==1){
            stringRedisTemplate.expire(key, Duration.ofDays(7));
        }
    }
    public void guadarCambiosPassword(Long idUsuario){
        String key="password_changes_daily:"+idUsuario;
        Optional<Long> changes=Optional.ofNullable(stringRedisTemplate.opsForValue().increment(key));
        if(changes.isPresent()&& changes.get()==1){
            stringRedisTemplate.expire(key, Duration.ofDays(1));
        }

        String key2="password_changes_hourly:"+idUsuario;
        Optional<Long> changes2=Optional.ofNullable(stringRedisTemplate.opsForValue().increment(key2));
        if(changes2.isPresent()&& changes2.get()==1){
            stringRedisTemplate.expire(key, Duration.ofHours(1));
        }
    }
}
