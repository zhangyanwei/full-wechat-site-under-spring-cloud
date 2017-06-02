package com.askdog.model.entity;

import com.askdog.model.converter.RoleSetConverter;
import com.askdog.model.converter.UserStatusSetConverter;
import com.askdog.model.converter.UserTagsSetConverter;
import com.askdog.model.common.State;
import com.askdog.model.entity.inner.user.UserStatus;
import com.askdog.model.entity.inner.user.UserTag;
import com.askdog.model.entity.inner.user.UserType;
import com.askdog.model.entity.partial.TimeBasedStatistics;
import com.askdog.model.entity.partial.UserProviderStatistic;
import com.askdog.model.security.Authority;
import com.askdog.model.validation.Group.Create;
import com.askdog.model.validation.UserIdentifier;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import java.util.Date;
import java.util.EnumSet;

import static com.askdog.common.RegexPattern.*;
import static com.askdog.model.common.State.OK;
import static javax.persistence.EnumType.STRING;

// http://stackoverflow.com/questions/13012584/jpa-how-to-convert-a-native-query-result-set-to-pojo-class-collection
@Entity
@Table(name = "mc_user")
@UserIdentifier(groups = Create.class)
@NamedNativeQueries({
        @NamedNativeQuery(
                name = "UserProviderDistribution",
                query = "SELECT join_u_eu.provider as provider, count(*) as count " +
                        "FROM (" +
                        "   SELECT * FROM mc_user u LEFT JOIN mc_external_user eu ON u.id = eu.bind_user) " +
                        "   AS join_u_eu " +
                        "GROUP BY join_u_eu.provider",
                resultSetMapping = "UserProviderDistribution"
        )
})
@SqlResultSetMappings({
        @SqlResultSetMapping(
                name = "UserProviderDistribution",
                classes = {
                        @ConstructorResult(targetClass = UserProviderStatistic.class, columns = {
                                @ColumnResult(name = "provider", type = String.class),
                                @ColumnResult(name = "count", type = Long.class)
                        })
                }
        ),
        @SqlResultSetMapping(
                name = "UserRegistrationInWeek",
                classes = {
                        @ConstructorResult(
                                targetClass = TimeBasedStatistics.class,
                                columns = {
                                    @ColumnResult(name = "day", type = Date.class),
                                    @ColumnResult(name = "user_count", type = Long.class)
                        })
                }
        )
})
public class User extends Base {

    private static final long serialVersionUID = 3685618184666644069L;

    @NotNull(groups = Create.class)
    @Pattern(regexp = REGEX_NICK_NAME)
    @Column(name = "nickname")
    private String nickname;

    @Pattern(regexp = REGEX_USER_NAME)
    @Column(name = "name", unique = true)
    private String name;

    @NotNull(groups = Create.class)
    @Column(name = "password")
    private String password;

    @Pattern(regexp = REGEX_PHONE)
    @Column(name = "phone_number", unique = true)
    private String phoneNumber;

    @Pattern(regexp = REGEX_MAIL)
    @Column(name = "email", unique = true)
    private String email;

    @NotNull(groups = Create.class)
    @Enumerated(EnumType.STRING)
    @Column(name = "type", nullable = false)
    private UserType type;

    @Convert(converter = UserStatusSetConverter.class)
    @Column(name = "user_statuses")
    private EnumSet<UserStatus> userStatuses;

    @Convert(converter = RoleSetConverter.class)
    @Column(name = "user_authorities")
    private EnumSet<Authority.Role> authorities;

    @Convert(converter = UserTagsSetConverter.class)
    @Column(name = "user_tags")
    private EnumSet<UserTag> userTags;

    @Column(name = "last_access_time")
    private Date lastAccessTime;

    @Column(name = "registration_time")
    private Date registrationTime;

    @Column(name = "avatar")
    private Long avatar;

    @NotNull
    @Enumerated(STRING)
    private State state = OK;

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public UserType getType() {
        return type;
    }

    public void setType(UserType type) {
        this.type = type;
    }

    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

    public EnumSet<UserStatus> getUserStatuses() {
        return userStatuses;
    }

    public void setUserStatuses(EnumSet<UserStatus> userStatuses) {
        this.userStatuses = userStatuses;
    }

    public EnumSet<Authority.Role> getAuthorities() {
        return authorities;
    }

    public void setAuthorities(EnumSet<Authority.Role> authorities) {
        this.authorities = authorities;
    }

    public EnumSet<UserTag> getUserTags() {
        return userTags;
    }

    public void setUserTags(EnumSet<UserTag> userTags) {
        this.userTags = userTags;
    }

    public Date getLastAccessTime() {
        return lastAccessTime;
    }

    public void setLastAccessTime(Date lastAccessTime) {
        this.lastAccessTime = lastAccessTime;
    }

    public Date getRegistrationTime() {
        return registrationTime;
    }

    public void setRegistrationTime(Date registrationTime) {
        this.registrationTime = registrationTime;
    }

    public void addStatus(UserStatus userStatus) {
        if (userStatuses == null) {
            userStatuses = EnumSet.noneOf(UserStatus.class);
        }

        userStatuses.add(userStatus);
    }

    public void addTags(UserTag userTag) {
        if (userTags == null) {
            userTags = EnumSet.noneOf(UserTag.class);
        }

        userTags.add(userTag);
    }

    public Long getAvatar() {
        return avatar;
    }

    public void setAvatar(Long avatar) {
        this.avatar = avatar;
    }

    public State getState() {
        return state;
    }

    public void setState(State state) {
        this.state = state;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}