package com.askdog.operation.web.vo;


import com.askdog.service.search.SearchRequest;
import com.google.common.collect.Maps;

import java.util.Map;

public class OperationSearchParam {

    private int from;
    private int size;
    private String key;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    private String type;

    public int getFrom() {
        return from;
    }

    public void setFrom(int from) {
        this.from = from;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public SearchRequest convert() {
        SearchRequest searchRequest = new SearchRequest();
        Map<String,Object> requestParam = Maps.newHashMap();
        requestParam.put("from",from);
        requestParam.put("size",size);
        requestParam.put("key",key);
        searchRequest.setType(type);
        searchRequest.setParams(requestParam);
        return searchRequest;
    }

}
