package com.damdung.banking.service;

import com.damdung.banking.entity.IdempotencyEntity;
import com.damdung.banking.repository.IIdempotencyRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDateTime;

@Service
public class IdempotencyService {

    @Autowired
    @Qualifier("idempotencyTemplate")
    private RedisTemplate<String,String> redisTemplate;
    @Autowired
    private IIdempotencyRepository idempotencyRepository;

    private static final String PREFIX = "idem:";
    private static final Duration TTL = Duration.ofMinutes(3);

    public Boolean tryAcquire(String key) {
        Boolean accquire = redisTemplate.opsForValue().setIfAbsent(PREFIX + key, "PROCESSING", TTL);

        if(Boolean.FALSE.equals(accquire)) {
            return false;
        }

        IdempotencyEntity idempotencyEntity = new IdempotencyEntity();
        idempotencyEntity.setIdempotencyID(key);
        idempotencyEntity.setStatus(IdempotencyEntity.Status.PROCESSING);
        idempotencyEntity.setCreatedAt(LocalDateTime.now());

        idempotencyRepository.save(idempotencyEntity);

        return true;
    }

    public void markSuccess(String key, String response) {

        idempotencyRepository.findById(key).ifPresent(entity -> {
            entity.setStatus(IdempotencyEntity.Status.SUCCESS);
            entity.setResponseBody(response);
            idempotencyRepository.save(entity);
        });

        redisTemplate.opsForValue()
                .set(PREFIX + key, "SUCCESS", TTL);
    }

    public void markFailed(String key, String response) {

        idempotencyRepository.findById(key).ifPresent(entity -> {
            entity.setStatus(IdempotencyEntity.Status.FAILED);
            entity.setResponseBody(response);
            idempotencyRepository.save(entity);
        });

        redisTemplate.opsForValue()
                .set(PREFIX + key, "FAILED", TTL);
    }

    public String getOldResponse(String key) {

        return idempotencyRepository.findById(key)
                .filter(e -> e.getStatus() == IdempotencyEntity.Status.SUCCESS)
                .map(IdempotencyEntity::getResponseBody)
                .orElse(null);
    }
}
