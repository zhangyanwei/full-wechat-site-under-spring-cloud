package com.askdog.wechat.utils;

import com.google.common.hash.Hashing;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

import static com.google.common.base.Joiner.on;
import static com.google.common.base.Strings.isNullOrEmpty;
import static com.google.common.collect.Ordering.natural;
import static java.nio.charset.StandardCharsets.UTF_8;

public final class WxMessageSecurity {

    public static String nextRandomString() {
        long val = java.util.UUID.randomUUID().getLeastSignificantBits();
        long hi = 1L << (12 * 4);
        return Long.toHexString(hi | (val & (hi - 1))).substring(1);
    }

    public static String sign(Map<String, ?> map, String signKey) {
        List<String> pairs = natural().sortedCopy(map.keySet()).stream()
                .filter(key -> !isNullOrEmpty(String.valueOf(map.get(key))))
                .filter(key -> !Objects.equals(key, "sign"))
                .map(key -> key + "=" + map.get(key))
                .collect(Collectors.toList());

        String joined = on("&").join(pairs);
        return Hashing.md5().hashString(joined + "&key=" + signKey, UTF_8).toString().toUpperCase();
    }

}
