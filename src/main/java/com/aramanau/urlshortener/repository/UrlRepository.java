package com.aramanau.urlshortener.repository;

import com.aramanau.urlshortener.util.HashUtils;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public class UrlRepository {

    private final StringRedisTemplate stringRedisTemplate;

    public UrlRepository(StringRedisTemplate stringRedisTemplate) {
        this.stringRedisTemplate = stringRedisTemplate;
    }

    private Long generateId() {
        return stringRedisTemplate.opsForValue()
            .increment("long_url:id",1L);
    }

    public Optional<String> findUrlById(Long id) {
        return Optional.ofNullable(
            (String)stringRedisTemplate
                .opsForHash()
                .get("long_urls", "id:" + id)
        );
    }

    public Optional<Long> findIdByUrl(String longUrl) {
        String hash = HashUtils.hash(longUrl);
        return Optional.ofNullable(
            stringRedisTemplate.opsForHash().get("hashed_urls", "hash:" + hash))
            .map(o -> Long.valueOf((String)o));
    }

    public Long save(String longUrl) {
        String hash = HashUtils.hash(longUrl);
        Long id = generateId();
        stringRedisTemplate.opsForHash().put("hashed_urls", "hash:" + hash, id.toString());
        stringRedisTemplate.opsForHash().put("long_urls", "id:" + id, longUrl);
        return id;
    }

}
