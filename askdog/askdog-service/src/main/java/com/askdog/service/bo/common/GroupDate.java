package com.askdog.service.bo.common;

public class GroupDate {

    private int y;
    private int m;
    private int d;

    public int getY() {
        return y;
    }

    public GroupDate setY(int y) {
        this.y = y;
        return this;
    }

    public int getM() {
        return m;
    }

    public GroupDate setM(int m) {
        this.m = m;
        return this;
    }

    public int getD() {
        return d;
    }

    public GroupDate setD(int d) {
        this.d = d;
        return this;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (obj == this) {
            return true;
        }
        if (!(obj instanceof GroupDate)) {
            return false;
        }
        GroupDate te = (GroupDate) obj;
        if (te.getY() == y && te.getM() == m && te.getD() == d) {
            return true;
        }
        return false;
    }
}