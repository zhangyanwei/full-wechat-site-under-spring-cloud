package com.askdog.model.entity;

import com.askdog.model.converter.EmployeeRoleonverter;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.Date;
import java.util.EnumSet;

@Entity
@Table(name = "mc_store_employee")
public class StoreEmployee extends Base {

    private static final long serialVersionUID = 3199891470768658093L;

    @NotNull
    @ManyToOne(optional = false)
    @JoinColumn(name = "store_id")
    private Store store;

    @NotNull
    @ManyToOne(optional = false)
    @JoinColumn(name = "user_id")
    private User user;

    @Size(max = 100)
    private String note;

    @NotNull
    @Column(name = "roles")
    @Convert(converter = EmployeeRoleonverter.class)
    private EnumSet<EmployeeRole> roles;

    @Column(name = "creation_time")
    private Date creationTime;

    public Store getStore() {
        return store;
    }

    public void setStore(Store store) {
        this.store = store;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public EnumSet<EmployeeRole> getRoles() {
        return roles;
    }

    public void setRoles(EnumSet<EmployeeRole> roles) {
        this.roles = roles;
    }

    public Date getCreationTime() {
        return creationTime;
    }

    public void setCreationTime(Date creationTime) {
        this.creationTime = creationTime;
    }

    public enum EmployeeRole {
        CASHIER
    }
}
