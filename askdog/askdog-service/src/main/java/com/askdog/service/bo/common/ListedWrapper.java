package com.askdog.service.bo.common;

import java.io.Serializable;
import java.util.List;

public class ListedWrapper<E> implements Serializable {

    private static final long serialVersionUID = -4243242257759618987L;

    private List<E> result;

    public ListedWrapper() {}

    public ListedWrapper(List<E> result) {
        this.result = result;
    }

    public List<E> getResult() {
        return result;
    }

    public void setResult(List<E> result) {
        this.result = result;
    }
}
