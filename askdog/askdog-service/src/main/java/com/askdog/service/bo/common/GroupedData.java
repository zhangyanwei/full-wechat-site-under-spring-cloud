package com.askdog.service.bo.common;

import java.util.List;

public class GroupedData<E> {

    private GroupDate groupDate;
    private List<E> groupData;

    public GroupDate getGroupDate() {
        return groupDate;
    }

    public GroupedData<E> setGroupDate(GroupDate groupDate) {
        this.groupDate = groupDate;
        return this;
    }

    public List<E> getGroupData() {
        return groupData;
    }

    public GroupedData<E> setGroupData(List<E> groupData) {
        this.groupData = groupData;
        return this;
    }
}