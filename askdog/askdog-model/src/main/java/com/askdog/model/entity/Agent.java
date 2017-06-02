package com.askdog.model.entity;

import com.askdog.model.common.State;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

import java.util.Date;

import static com.askdog.model.common.State.OK;
import static javax.persistence.EnumType.STRING;
import static javax.persistence.FetchType.LAZY;

@Entity
@Table(name = "mc_agent")
public class Agent extends Base {
    @NotNull
    @ManyToOne(optional = false, fetch = LAZY)
    @JoinColumn(name = "owner", nullable = false, updatable = false)
    private User owner;

    @Column(name = "address")
    private String address;

    @Column(name = "creation_time")
    private Date creationTime;

    @NotNull
    @Enumerated(STRING)
    private State state = OK;

    public User getOwner() {
        return owner;
    }

    public void setOwner(User owner) {
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

    public State getState() {
        return state;
    }

    public void setState(State state) {
        this.state = state;
    }
}
