package com.askdog.dao.repository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

import static com.google.common.base.CharMatcher.is;

@Repository
public class TokenRepository {

    @Autowired
    private StringRedisTemplate template;

    public String claimToken(String tokenName, Map<String, String> hashValues, long timeout, TimeUnit unit) {
        String token = UUID.randomUUID().toString();
        saveToken(tokenName, hashValues, timeout, unit, token);
        return token;
    }

    public String claimToken(String owner, String tokenName, Map<String, String> hashValues, long timeout, TimeUnit unit) {
        String token = is('-').removeFrom(UUID.randomUUID().toString());
        resetOwnerToken(owner, tokenName, timeout, unit, token);
        saveToken(owner, tokenName, hashValues, timeout, unit, token);
        return token;
    }

    public Map<String, String> redeemToken(String tokenName, String token) {
        String key = generateTokenKey(tokenName, token);
        if (template.hasKey(key)) {
            HashOperations<String, String, String> operations = template.opsForHash();
            // remove owner relationship
            String owner = operations.get(key, "_owner");
            if (owner != null) {
                operations.delete(generateOwnerKey(owner), tokenName);
            }

            Map<String, String> values = operations.entries(key);
            template.delete(key);
            return values;
        }

        return new HashMap<>();
    }

    public Map<String, String> expandToken(String tokenName, String token) {
        if (isTokenValidate(tokenName, token)) {
            String key = generateTokenKey(tokenName, token);
            HashOperations<String, String, String> operations = template.opsForHash();
            return operations.entries(key);
        }

        return null;
    }

    public boolean isTokenValidate(String tokenName, String token) {
        String key = generateTokenKey(tokenName, token);
        if (template.hasKey(key)) {
            Long expire = template.getExpire(key);
            return expire > 0;
        }

        return false;
    }

    private void resetOwnerToken(String owner, String tokenName, long timeout, TimeUnit unit, String token) {
        HashOperations<String, String, String> hashOperations = template.opsForHash();
        String ownerKey = generateOwnerKey(owner);
        String previousToken = hashOperations.get(ownerKey, tokenName);
        template.delete(generateTokenKey(tokenName, previousToken));

        hashOperations.put(ownerKey, tokenName, token);
        template.expire(ownerKey, timeout, unit);
    }

    private void saveToken(String tokenName, Map<String, String> hashValues, long timeout, TimeUnit unit, String token) {
        String key = generateTokenKey(tokenName, token);
        template.opsForHash().putAll(key, hashValues);
        template.expire(key, timeout, unit);
    }

    private void saveToken(String owner, String tokenName, Map<String, String> hashValues, long timeout, TimeUnit unit, String token) {
        String key = generateTokenKey(tokenName, token);
        HashOperations<String, Object, Object> hashOperations = template.opsForHash();
        hashOperations.putAll(key, hashValues);
        hashOperations.put(key, "_owner", owner);
        template.expire(key, timeout, unit);
    }

    private String generateOwnerKey(String owner) {
        return String.format("token:owners:%s", owner);
    }

    private String generateTokenKey(String tokenName, String token) {
        return String.format("token:tokens:%s:%s", tokenName, token);
    }
}
