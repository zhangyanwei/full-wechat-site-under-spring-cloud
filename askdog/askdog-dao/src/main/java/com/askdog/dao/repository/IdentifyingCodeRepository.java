package com.askdog.dao.repository;

import com.askdog.sms.client.impl.PasswordIdentifyingCodeSms;
import com.askdog.sms.client.impl.RegistrationIdentifyingCodeSms;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.Map;
import java.util.concurrent.TimeUnit;

// TODO should merge to token repository.
@Repository
public class IdentifyingCodeRepository {

    @Autowired
    private StringRedisTemplate template;

    @Autowired
    private RegistrationIdentifyingCodeSms registrationIdentifyingCodeSms;

    @Autowired
    private PasswordIdentifyingCodeSms passwordIdentifyingCodeSms;

    public void claimIdentifyingCode(String phone, String identifyingCodeName, Map<String, String> hashValues, long timeout, TimeUnit unit) {
        if(!isExceedDuration(phone, identifyingCodeName)){
            return;
        }
        String identifyingCode = String.valueOf((int) ((Math.random() * 9 + 1) * 100000));
        resetOwnerIdentifyingCode(phone, identifyingCodeName, timeout, unit, identifyingCode);
        saveIdentifyingCode(phone, identifyingCodeName, hashValues, timeout, unit, identifyingCode);
        switch (identifyingCodeName) {
            case "registration":
                registrationIdentifyingCodeSms.send(phone, identifyingCode);
                break;
            case "recover":
                passwordIdentifyingCodeSms.send(phone, identifyingCode);
                break;
        }
    }

    public void redeemIdentifyingCode(String identifyingCodeName, String phone, String identifyingCode) {
        String key = generateIdentifyingCodeKey(identifyingCodeName, phone, identifyingCode);
        if (template.hasKey(key)) {
            HashOperations<String, String, String> operations = template.opsForHash();
            String owner = operations.get(key, "_owner");
            if (owner != null) {
                operations.delete(generateOwnerKey(owner), identifyingCodeName);
            }
            template.delete(key);
        }
    }

    public boolean isIdentifyingCodeValidate(String identifyingCodeName, String phone, String identifyingCode) {
        String key = generateIdentifyingCodeKey(identifyingCodeName, phone, identifyingCode);
        if (template.hasKey(key)) {
            Long expire = template.getExpire(key);
            return expire > 0;
        }
        return false;
    }

    private boolean isExceedDuration(String phone, String identifyingCodeName) {
        HashOperations<String, String, String> hashOperations = template.opsForHash();
        String ownerKey = generateOwnerKey(phone);
        String previousIdentifyingCode = hashOperations.get(ownerKey, identifyingCodeName);

        String key = generateIdentifyingCodeKey(identifyingCodeName, phone, previousIdentifyingCode);
        HashOperations<String, Object, Object> hashOperationsForValidate = template.opsForHash();
        Map<Object, Object> map = hashOperationsForValidate.entries(key);
        if (!map.isEmpty()) {
            long duration = new Date().getTime() - (Long.parseLong(map.get("issue_time").toString()));
            if (duration < 60 * 1000) {
                return false;
            }
        }
        return true;
    }
    private void resetOwnerIdentifyingCode(String phone, String identifyingCodeName, long timeout, TimeUnit unit, String identifyingCode) {
        HashOperations<String, String, String> hashOperations = template.opsForHash();
        String ownerKey = generateOwnerKey(phone);

        String previousIdentifyingCode = hashOperations.get(ownerKey, identifyingCodeName);
        template.delete(generateIdentifyingCodeKey(identifyingCodeName, phone, previousIdentifyingCode));
        hashOperations.put(ownerKey, identifyingCodeName, identifyingCode);
        template.expire(ownerKey, timeout, unit);
    }

    private void saveIdentifyingCode(String phone, String identifyingCodeName, Map<String, String> hashValues, long timeout, TimeUnit unit, String identifyingCode) {
        String key = generateIdentifyingCodeKey(identifyingCodeName, phone, identifyingCode);
        HashOperations<String, Object, Object> hashOperations = template.opsForHash();
        hashOperations.putAll(key, hashValues);
        hashOperations.put(key, "_owner", phone);
        template.expire(key, timeout, unit);
    }

    private String generateOwnerKey(String phone) {
        return String.format("identifyingCode:owners:%s", phone);
    }

    private String generateIdentifyingCodeKey(String identifyingCodeName, String phone, String identifyingCode) {
        return String.format("identifyingCode:identifyingCodes:%s:%s:%s", identifyingCodeName, phone, identifyingCode);
    }
}
