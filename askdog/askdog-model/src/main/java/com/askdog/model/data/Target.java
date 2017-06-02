package com.askdog.model.data;

import com.askdog.model.data.inner.EntityType;

import javax.validation.constraints.NotNull;

public abstract class Target extends Base {

    private static final long serialVersionUID = 8768452665831831951L;

    @NotNull
    private Long targetId;

    @NotNull
    private EntityType targetType;

    public Long getTargetId() {
        return targetId;
    }

    public void setTargetId(Long targetId) {
        this.targetId = targetId;
    }

    public EntityType getTargetType() {
        return targetType;
    }

    public void setTargetType(EntityType targetType) {
        this.targetType = targetType;
    }
}
