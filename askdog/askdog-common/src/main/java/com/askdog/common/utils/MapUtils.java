package com.askdog.common.utils;

import com.google.common.collect.Maps;
import org.springframework.util.Assert;

import java.util.Arrays;
import java.util.Map;

public class MapUtils {

    private static final String PATH_SEPARATOR = "\\.";

    public static <V> V getPathValue(Map<String, Object> map, String path) {
        Assert.notNull(map);
        Assert.hasLength(path);
        return getPathValue(map, path.split(PATH_SEPARATOR));
    }

    public static <T> T setPathValue(Map<String, Object> map, String path, T value) {
        return setValue(map, path.split(PATH_SEPARATOR), value);
    }

    @SuppressWarnings("unchecked")
    public static <V> V getPathValue(Map<String, Object> map, String[] path) {
        Assert.notEmpty(path);

        if (map == null) {
            return null;
        }

        if (path.length == 1) {
            return (V) map.get(path[0]);
        }

        return getPathValue((Map<String, Object>) map.get(path[0]), Arrays.copyOfRange(path, 1, path.length));
    }

    @SuppressWarnings("unchecked")
    public static <T> T setValue(Map<String, Object> obj, String[] path, T value) {
        Assert.notNull(obj);
        Assert.notEmpty(path);

        if (path.length == 1) {
            obj.put(path[0], value);
            return value;
        }

        if (!obj.containsKey(path[0])) {
            obj.put(path[0], Maps.newHashMap());
        } else if (path.length == 2 && obj.get(path[0]) instanceof String) {
            obj.put(path[0],value);
            return value;
        }

        return setValue((Map<String, Object>) obj.get(path[0]), Arrays.copyOfRange(path, 1, path.length), value);
    }

}
