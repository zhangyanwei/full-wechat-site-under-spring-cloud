package com.askdog.service.bo.admin.operation;

import java.util.Date;

public class OperationState {
    private String id;
    private Date OperationTime;
    private Double OperationScore;
    private String index;
    private String type;
    private boolean modified;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getIndex() {
        return index;
    }

    public void setIndex(String index) {
        this.index = index;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Date getOperationTime() {
        return OperationTime;
    }

    public void setOperationTime(Date operationTime) {
        OperationTime = operationTime;
    }

    public Double getOperationScore() {
        return OperationScore;
    }

    public void setOperationScore(Double operationScore) {
        OperationScore = operationScore;
    }

    public boolean isModified() {
        return modified;
    }

    public void setModified(boolean modified) {
        this.modified = modified;
    }

}
