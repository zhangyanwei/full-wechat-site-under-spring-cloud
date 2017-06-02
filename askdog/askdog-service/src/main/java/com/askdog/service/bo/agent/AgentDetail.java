package com.askdog.service.bo.agent;

import com.askdog.common.Out;
import com.askdog.service.bo.BasicUser;
import com.fasterxml.jackson.annotation.JsonFormat;

import java.util.Date;

import static com.fasterxml.jackson.annotation.JsonFormat.Shape.STRING;

public class AgentDetail implements Out<AgentDetail, com.askdog.service.bo.AgentDetail> {
    private static final long serialVersionUID = -4071253088960929958L;

    @JsonFormat(shape = STRING)
    private Long id;
    private BasicUser owner;
    private String address;
    private Date creationTime;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public BasicUser getOwner() {
        return owner;
    }

    public void setOwner(BasicUser owner) {
        this.owner = owner;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public Date getCreationTime() {
        return creationTime;
    }

    public void setCreationTime(Date creationTime) {
        this.creationTime = creationTime;
    }

    @Override public AgentDetail from(com.askdog.service.bo.AgentDetail agentDetail) {
        this.id = agentDetail.getId();
        this.owner = agentDetail.getOwner().toBasic();
        this.address = agentDetail.getAddress();
        this.creationTime = agentDetail.getCreationTime();
        return this;
    }
}
