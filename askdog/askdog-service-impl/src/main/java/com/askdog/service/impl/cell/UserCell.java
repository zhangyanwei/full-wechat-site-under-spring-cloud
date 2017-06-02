package com.askdog.service.impl.cell;

import com.askdog.dao.repository.ExternalUserRepository;
import com.askdog.dao.repository.UserRepository;
import com.askdog.dao.repository.mongo.OriginalNotificationRepository;
import com.askdog.dao.repository.mongo.UserAttributeRepository;
import com.askdog.model.data.UserAttribute;
import com.askdog.model.entity.User;
import com.askdog.model.entity.inner.user.UserType;
import com.askdog.service.bo.BasicInnerUser;
import com.askdog.service.bo.DecoratedUser;
import com.askdog.service.bo.UserDetail;
import com.askdog.service.exception.NotFoundException;
import com.askdog.service.impl.cache.annotation.user.UserAttributeCache;
import com.askdog.service.impl.cache.annotation.user.UserAttributeCacheRefresh;
import com.askdog.service.impl.cache.annotation.user.UserBasicCache;
import com.askdog.service.impl.cache.annotation.user.UserBasicCacheRefresh;
import com.askdog.service.impl.event.TriggerEvent;
import com.askdog.service.impl.event.TriggerEvent.TriggerEventItem;
import com.askdog.service.impl.storage.StorageRecorder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Nonnull;

import static com.askdog.model.common.EventType.UPDATE_USER;
import static com.askdog.model.security.Authority.Role.ADMIN;
import static com.askdog.service.exception.NotFoundException.Error.USER;
import static org.elasticsearch.common.Strings.isNullOrEmpty;

@Component
@Transactional
public class UserCell {

    @Autowired private UserRepository userRepository;
    @Autowired private UserAttributeRepository userAttributeRepository;
    @Autowired private ExternalUserRepository externalUserRepository;
    @Autowired private OriginalNotificationRepository originalNotificationRepository;
    @Autowired private StorageRecorder storageRecorder;

    public User findExists(long id) {
        return userRepository.findById(id).orElseThrow(() -> new NotFoundException(USER));
    }

    @Nonnull
    @UserBasicCache
    public UserDetail findDetail(long userId) {
        User user = findExists(userId);
        UserDetail userDetail = new UserDetail().from(innerDecorate(user));
        userDetail.setAdmin(user.getAuthorities() != null && user.getAuthorities().contains(ADMIN));
        return userDetail;
    }

    @UserBasicCacheRefresh
    public UserDetail refreshBasicCache(long userId) {
        return findDetail(userId);
    }

    @UserAttributeCache
    public UserAttribute getUserUserAttribute(long userId) {
        return userAttributeRepository.findByUserId(userId).orElseGet(() -> {
            UserAttribute userAttribute = new UserAttribute();
            userAttribute.setUserId(userId);
            return userAttribute;
        });
    }

    @UserAttributeCacheRefresh
    @TriggerEvent({@TriggerEventItem(performer = "userId", eventType = UPDATE_USER, target = "userId")})
    public UserAttribute updateUserAttribute(long userId, @Nonnull UserAttribute attribute) {
        attribute.setUserId(userId);

        userAttributeRepository.findByUserId(userId).ifPresent(
                userAttribute -> attribute.setId(userAttribute.getId())
        );

        return userAttributeRepository.save(attribute);
    }

    @Deprecated
    /* see findDetail
     */
    public boolean isAdmin(Long userId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new NotFoundException(USER));
        return user.getAuthorities() != null && user.getAuthorities().contains(ADMIN);
    }

    public BasicInnerUser buildInnerUser(User user) {
        DecoratedUser decoratedUser = innerDecorate(user);
        BasicInnerUser basic = new BasicInnerUser();
        basic.setId(decoratedUser.getId());
        basic.setUsername(decoratedUser.getName());
        basic.setNickname(decoratedUser.getNickname());
        basic.setPassword(decoratedUser.getPassword());
        basic.setMail(decoratedUser.getEmail());
        basic.setPhone(decoratedUser.getPhoneNumber());
        basic.setType(decoratedUser.getType());
        basic.setUserStatuses(decoratedUser.getUserStatuses());
        basic.setAuthorities(decoratedUser.getAuthorities());
        basic.setAvatar(decoratedUser.getAvatarUrl());
        basic.setNoticeCount(originalNotificationRepository.countByRecipientAndReadFalse(decoratedUser.getId()));
        return basic;
    }

    private DecoratedUser innerDecorate(User user) {
        DecoratedUser decoratedUser = new DecoratedUser(user);
        if (user.getType() == UserType.EXTERNAL) {
            externalUserRepository.findFirstByUser_IdOrderByRegistrationTimeAsc(user.getId()).ifPresent(externalUser -> {
                decoratedUser.setNickname(isNullOrEmpty(user.getNickname()) ? externalUser.getNickname() : user.getNickname());
                decoratedUser.setAvatarUrl(externalUser.getAvatar());
            });
        } else {
            decoratedUser.setNickname(user.getNickname());
            Long avatar = user.getAvatar();
            if (avatar != null) {
                decoratedUser.setAvatarUrl(storageRecorder.getResource(avatar).getDescription().getResourceUrl());
            }
        }
        return decoratedUser;
    }
}
