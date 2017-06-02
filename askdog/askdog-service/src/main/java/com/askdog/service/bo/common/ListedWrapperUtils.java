package com.askdog.service.bo.common;

import java.util.List;
import java.util.function.Function;

import static com.google.common.collect.Lists.newArrayList;
import static java.util.stream.Collectors.toList;

public class ListedWrapperUtils {

    public static <T, E> ListedWrapper<E> reListedPage(List<T> page, Function<T, E> convert) {
        List<E> result = newArrayList();
        if (page != null) {
            result.addAll(page.stream().map(convert).collect(toList()));
        }
        return new ListedWrapper<>(result);
    }

}
