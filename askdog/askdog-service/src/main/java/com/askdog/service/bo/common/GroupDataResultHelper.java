package com.askdog.service.bo.common;

import java.util.List;
import java.util.function.Function;

import static com.google.common.collect.Lists.newArrayList;

public class GroupDataResultHelper {

    @SuppressWarnings("unchecked")
    public static <E> List<GroupedData<E>> group(List<E> data, Function<E, GroupDate> dateGroupTFunction) {

        if (data == null) {
            return null;
        }

        List<GroupedData<E>> result = newArrayList();

        data.forEach(e -> {

            GroupDate groupDate = dateGroupTFunction.apply(e);

            for (GroupedData<E> groupedData : result) {
                if (groupedData.getGroupDate().equals(groupDate)) {
                    groupedData.getGroupData().add(e);
                    return;
                }
            }

            result.add(new GroupedData<E>().setGroupDate(groupDate).setGroupData(newArrayList(e)));
        });

        return result;
    }
}