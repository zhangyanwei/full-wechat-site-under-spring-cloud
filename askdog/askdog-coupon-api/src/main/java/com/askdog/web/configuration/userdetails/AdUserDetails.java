package com.askdog.web.configuration.userdetails;

import com.askdog.model.entity.inner.user.UserType;
import com.askdog.service.bo.BasicExternalUser;
import com.askdog.service.bo.BasicInnerUser;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import static com.askdog.model.entity.inner.user.UserStatus.*;

public class AdUserDetails implements UserDetails {

    private static final long serialVersionUID = -7966053625918135698L;

    private BasicExternalUser externalUser;
    private BasicInnerUser user;

    public AdUserDetails(BasicInnerUser user) {
        this.user = user;
    }

    public AdUserDetails(BasicExternalUser user) {
        this.externalUser = user;
        this.user = user.getInnerUser();
    }

    public BasicInnerUser getUser() {
        return this.user;
    }

    public Long getId() {
        return this.user.getId();
    }

    public BasicExternalUser getExternalUser() {
        return this.externalUser;
    }

    public Map<String, String> getDetails() {
        if (this.externalUser != null) {
            Map<String, String> details = this.externalUser.getDetails();
            if (details != null) {
                return details;
            }
        }

        return new HashMap<>();
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return AuthorityUtils.createAuthorityList(user.authorities());
    }

    @Override
    public String getUsername() {
        return String.valueOf(user.getId());
    }

    @Override
    public String getPassword() {
        return user.getPassword();
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return user.hasStatus(MAIL_CONFIRMED)
                || user.hasStatus(PHONE_CONFIRMED)
                || user.hasStatus(MANAGED)
                || user.getType() == UserType.EXTERNAL;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    @Override
    public boolean equals(Object obj) {

        if (obj == null) {
            return false;

        } else if (!(obj instanceof AdUserDetails)) {
            return false;
        }

        return this.getUsername().equals(((AdUserDetails) obj).getUsername());
    }
}