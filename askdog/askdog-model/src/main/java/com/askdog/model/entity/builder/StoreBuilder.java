package com.askdog.model.entity.builder;

import com.askdog.model.entity.Agent;
import com.askdog.model.entity.Store;
import com.askdog.model.entity.User;
import com.askdog.model.security.Authority;
import org.springframework.beans.factory.annotation.Autowired;

import javax.persistence.Column;
import javax.validation.constraints.NotNull;
import java.util.Date;
import java.util.EnumSet;
import java.util.HashSet;
import java.util.Iterator;

import static com.askdog.model.security.Authority.Role.STORE_ADMIN;

public final class StoreBuilder {

    private String name;
    private String description;
    private String address;
    private String phone;
    private Long cover;
    private Date creationTime;
    private User owner;
    private Agent agent;
    private String contactsName;
    private String contactsPhone;

    public static StoreBuilder storeBuilder() {
        return new StoreBuilder();
    }

    public StoreBuilder name(String name) {
        this.name = name;
        return this;
    }

    public StoreBuilder description(String description) {
        this.description = description;
        return this;
    }

    public StoreBuilder address(String address) {
        this.address = address;
        return this;
    }

    public StoreBuilder phone(String phone) {
        this.phone = phone;
        return this;
    }

    public StoreBuilder cover(Long cover) {
        this.cover = cover;
        return this;
    }

    public StoreBuilder creationTime(Date creationTime) {
        this.creationTime = creationTime;
        return this;
    }

    public StoreBuilder owner(User user) {
        if (null == user.getAuthorities()) {
            user.setAuthorities(EnumSet.of(STORE_ADMIN));
        } else {
            user.getAuthorities().add(STORE_ADMIN);
        }
        this.owner = user;
        return this;
    }

    public StoreBuilder agent(Agent agent) {
        this.agent = agent;
        return this;
    }


    public StoreBuilder contactsName(String contactsName) {
        this.contactsName = contactsName;
        return this;
    }

    public StoreBuilder contactsPhone(String contactsPhone) {
        this.contactsPhone = contactsPhone;
        return this;
    }

    public Store build() {
        Store store = new Store();
        store.setName(this.name);
        store.setDescription(this.description);
        store.setAddress(this.address);
        store.setPhone(this.phone);
        store.setCover(this.cover);
        store.setCreationTime(this.creationTime == null ? new Date() : this.creationTime);
        store.setOwner(this.owner);
        store.setAgent(this.agent);
        store.setContactsName(this.contactsName);
        store.setContactsPhone(this.contactsPhone);
        return store;
    }
}
