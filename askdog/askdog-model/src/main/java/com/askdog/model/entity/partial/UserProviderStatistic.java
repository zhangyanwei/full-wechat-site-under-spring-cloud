package com.askdog.model.entity.partial;

import com.askdog.model.entity.inner.user.UserProvider;

import java.io.Serializable;

import static com.askdog.model.entity.inner.user.UserProvider.ASKDOG;
import static com.askdog.model.entity.inner.user.UserProvider.valueOf;
import static com.google.common.base.Strings.isNullOrEmpty;

public class UserProviderStatistic implements Serializable {

    private static final long serialVersionUID = 3257772453339304787L;

    private UserProvider provider;
    private Long count;

    public UserProviderStatistic() {}

    public UserProviderStatistic(String provider, Long count) {
        this.provider = isNullOrEmpty(provider) ? ASKDOG : valueOf(provider);
        this.count = count;
    }

    public UserProvider getProvider() {
        return provider;
    }

    public void setProvider(UserProvider provider) {
        this.provider = provider;
    }

    public Long getCount() {
        return count;
    }

    public void setCount(Long count) {
        this.count = count;
    }
}
