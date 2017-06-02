package com.askdog.model.entity;

import com.askdog.common.utils.IDGenerator;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Objects;

import static com.askdog.model.validation.Group.Delete;
import static com.askdog.model.validation.Group.Edit;

@MappedSuperclass
@DynamicUpdate
public abstract class Base implements Serializable {

    private static final long serialVersionUID = 5422411418045572855L;

    @Id
    @Column(name = "id", unique = true, nullable = false, insertable = false, updatable = false)
    private Long id = IDGenerator.next();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Base)) return false;
        Base base = (Base) o;
        return Objects.equals(getId(), base.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId());
    }
}