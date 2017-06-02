package com.askdog.model.entity;

import com.askdog.model.common.State;
import com.askdog.model.entity.partial.TimeBasedStatistics;
import com.askdog.model.validation.Group;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.Date;

import static com.askdog.model.common.State.OK;
import static javax.persistence.EnumType.STRING;
import static javax.persistence.FetchType.LAZY;

@Entity
@Table(name = "mc_store")
@SqlResultSetMappings({
        @SqlResultSetMapping(
                name = "StoreCreationInWeek",
                classes = {
                        @ConstructorResult(
                                targetClass = TimeBasedStatistics.class,
                                columns = {
                                    @ColumnResult(name = "day", type = Date.class),
                                    @ColumnResult(name = "store_count", type = Long.class)
                        })
                }
        )
})
public class Store extends Base {

    private static final long serialVersionUID = 2153916938613308497L;

    @NotNull(groups = Group.Create.class)
    @Size(min = 1, max = 24)
    @Column(name = "name", length = 24)
    private String name;

    @Size(max = 200)
    @Column(name = "description", length = 200)
    private String description;

    @NotNull(groups = Group.Create.class)
    @Size(min = 1, max = 50)
    @Column(name = "address", length = 50)
    private String address;

    //@Pattern(regexp = REGEX_PHONE)
    @Column(name = "phone")
    private String phone;

    @Column(name = "cover")
    private Long cover;

    @Column(name = "creation_time")
    private Date creationTime;

    @NotNull
    @ManyToOne(optional = false, fetch = LAZY)
    @JoinColumn(name = "owner", nullable = false, updatable = false)
    private User owner;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "agent_id")
    private Agent agent;

    @NotNull
    @Enumerated(STRING)
    private State state = OK;

    @Column(name = "contacts_name")
    private String contactsName;

    @Column(name = "contacts_phone")
    private String contactsPhone;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public Long getCover() {
        return cover;
    }

    public void setCover(Long cover) {
        this.cover = cover;
    }

    public Date getCreationTime() {
        return creationTime;
    }

    public void setCreationTime(Date creationTime) {
        this.creationTime = creationTime;
    }

    public User getOwner() {
        return owner;
    }

    public void setOwner(User owner) {
        this.owner = owner;
    }

    public Agent getAgent() {
        return agent;
    }

    public void setAgent(Agent agent) {
        this.agent = agent;
    }

    public State getState() {
        return state;
    }

    public void setState(State state) {
        this.state = state;
    }

    public String getContactsName() {
        return contactsName;
    }

    public void setContactsName(String contactsName) {
        this.contactsName = contactsName;
    }

    public String getContactsPhone() {
        return contactsPhone;
    }

    public void setContactsPhone(String contactsPhone) {
        this.contactsPhone = contactsPhone;
    }

}
