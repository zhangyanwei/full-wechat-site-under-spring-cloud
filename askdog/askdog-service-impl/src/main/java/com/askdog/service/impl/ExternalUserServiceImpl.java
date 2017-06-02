package com.askdog.service.impl;

import com.askdog.dao.repository.ExternalUserRepository;
import com.askdog.model.entity.ExternalUser;
import com.askdog.model.entity.User;
import com.askdog.model.entity.inner.user.UserProvider;
import com.askdog.model.entity.inner.user.UserType;
import com.askdog.service.ExternalUserService;
import com.askdog.service.bo.BasicExternalUser;
import com.askdog.service.exception.ServiceException;
import com.askdog.service.impl.cell.UserCell;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Nonnull;
import javax.validation.Valid;
import java.util.Date;
import java.util.Optional;

import static com.askdog.model.entity.inner.user.UserProvider.WECHAT;
import static com.askdog.model.entity.inner.user.UserProvider.WECHAT_CONNECT;
import static com.google.common.base.Preconditions.checkArgument;
import static org.springframework.util.StringUtils.isEmpty;

@Service
@RestController
public class ExternalUserServiceImpl implements ExternalUserService {

    private static final Logger logger = LoggerFactory.getLogger(ExternalUserServiceImpl.class);

    @Autowired
    private ExternalUserRepository externalUserRepository;

    @Autowired
    private UserCell userCell;

    @Nonnull
    @Override
    public BasicExternalUser save(@Valid @RequestBody BasicExternalUser basicExternalUser) throws ServiceException {
        User user = null;
        if (basicExternalUser.getProvider() == WECHAT || basicExternalUser.getProvider() == WECHAT_CONNECT) {
            UserProvider relatedProvider = basicExternalUser.getProvider() == WECHAT ? WECHAT_CONNECT : WECHAT;
            Optional<ExternalUser> foundRelatedExternalUser = externalUserRepository.findByExternalUserIdAndProvider(basicExternalUser.getExternalUserId(), relatedProvider);
            if (foundRelatedExternalUser.isPresent()) {
                user = foundRelatedExternalUser.get().getUser();
            }
        }

        if (user == null) {
            user = new User();
            user.setType(UserType.EXTERNAL);
        }

        ExternalUser externalUser = new ExternalUser();
        externalUser.setExternalUserId(basicExternalUser.getExternalUserId());
        externalUser.setNickname(basicExternalUser.getNickname());
        externalUser.setAvatar(basicExternalUser.getAvatar());
        externalUser.setProvider(basicExternalUser.getProvider());
        externalUser.setDetails(basicExternalUser.getDetails());
        externalUser.setUser(user);
        externalUser.setRegistrationTime(new Date());
        ExternalUser savedExternalUser = externalUserRepository.save(externalUser);
        return buildExternalUser(savedExternalUser);
    }

    @Override
    public void update(@Valid @RequestBody BasicExternalUser externalUser) throws ServiceException {
        String externalUserId = externalUser.getExternalUserId();
        Optional<ExternalUser> saved = externalUserRepository.findByExternalUserIdAndProvider(externalUserId, externalUser.getProvider());
        saved.ifPresent(u -> {
            u.setAvatar(externalUser.getAvatar());
            u.setDetails(externalUser.getDetails());
            externalUserRepository.save(u);
        });
    }

    @Nonnull
    @Override
    public Optional<BasicExternalUser> findByExternalUserIdAndProvider(@Nonnull @PathVariable("externalUserId") String externalUserId, @Nonnull @RequestParam("provider") UserProvider provider) throws ServiceException {
        checkArgument(!isEmpty(externalUserId));
        checkArgument(!isEmpty(provider));
        Optional<ExternalUser> foundExternalUser = externalUserRepository.findByExternalUserIdAndProvider(externalUserId, provider);
        if (foundExternalUser.isPresent()) {
            return Optional.of(buildExternalUser(foundExternalUser.get()));
        }

        return Optional.empty();
    }

    @Nonnull
    @Override
    public Optional<BasicExternalUser> findByExternalUserId(@Nonnull @RequestParam("externalUserId") String externalUserId) throws ServiceException {
        checkArgument(!isEmpty(externalUserId));
        Optional<ExternalUser> foundExternalUser = externalUserRepository.findByExternalUserId(externalUserId);
        if (foundExternalUser.isPresent()) {
            return Optional.of(buildExternalUser(foundExternalUser.get()));
        }
        return Optional.empty();
    }

    @Nonnull
    @Override
    public Optional<BasicExternalUser> findByUserIdAndProvider(@Nonnull @PathVariable("userId") Long userId, @Nonnull @PathVariable("provider") UserProvider provider) throws ServiceException {
        checkArgument(!isEmpty(userId) || !isEmpty(provider));
        Optional<ExternalUser> foundExternalUser = externalUserRepository.findByUser_IdAndProvider(userId, provider);
        if (foundExternalUser.isPresent()) {
            return Optional.of(buildExternalUser(foundExternalUser.get()));
        }
        return Optional.empty();
    }

    private BasicExternalUser buildExternalUser(ExternalUser externalUser) throws ServiceException {
        BasicExternalUser basicExternalUser = new BasicExternalUser();
        basicExternalUser.setExternalUserId(externalUser.getExternalUserId());
        basicExternalUser.setNickname(externalUser.getNickname());
        basicExternalUser.setAvatar(externalUser.getAvatar());
        basicExternalUser.setProvider(externalUser.getProvider());
        basicExternalUser.setDetails(externalUser.getDetails());
        basicExternalUser.setInnerUser(userCell.buildInnerUser(externalUser.getUser()));
        return basicExternalUser;
    }
}
