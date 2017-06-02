package com.askdog.model.entity.builder;

import com.askdog.model.entity.Agent;
import com.askdog.model.entity.User;
import com.askdog.model.entity.inner.user.UserStatus;
import com.askdog.model.entity.inner.user.UserType;

import java.util.Date;

public final class AgentBuilder {

    private User owner;
    private String address;
    private Date creationTime;

    public static AgentBuilder agentBuilder() {
        return new AgentBuilder();
    }

    public AgentBuilder owner(Long userId) {
        User user = new User();
        user.setId(userId);
        this.owner = user;
        return this;
    }

    public AgentBuilder address(String address) {
        this.address = address;
        return this;
    }

    public AgentBuilder creationTime(Date creationTime) {
        this.creationTime = creationTime;
        return this;
    }

    public Agent build() {
        Agent agent = new Agent();
        agent.setOwner(this.owner);
        agent.setAddress(this.address);
        agent.setCreationTime(this.creationTime == null ? new Date() : this.creationTime);
        return agent;
    }
}
