package com.askdog.service.impl;

import com.askdog.dao.repository.IdentifyingCodeRepository;
import com.askdog.dao.repository.UserRepository;
import com.askdog.model.data.UserAttribute;
import com.askdog.model.entity.User;
import com.askdog.model.entity.inner.user.UserTag;
import com.askdog.model.validation.Group.Create;
import com.askdog.service.UserService;
import com.askdog.service.bo.BasicInnerUser;
import com.askdog.service.bo.SimpleUserCreation;
import com.askdog.service.bo.UserDetail;
import com.askdog.service.exception.ConflictException;
import com.askdog.service.exception.IllegalArgumentException;
import com.askdog.service.exception.NotFoundException;
import com.askdog.service.impl.cell.UserCell;
import com.askdog.service.impl.event.TriggerEvent;
import com.askdog.service.impl.event.TriggerEvent.TriggerEventItem;
import com.askdog.service.impl.mail.RegistrationTokenMail;
import com.askdog.service.impl.storage.StorageRecorder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Nonnull;
import javax.validation.Valid;
import java.util.Date;
import java.util.EnumSet;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

import static com.askdog.common.RegexPattern.*;
import static com.askdog.common.utils.Conditions.checkState;
import static com.askdog.model.common.EventType.*;
import static com.askdog.model.entity.inner.user.UserStatus.*;
import static com.askdog.model.entity.inner.user.UserType.REGISTERED;
import static com.askdog.service.exception.ConflictException.Error.*;
import static com.askdog.service.exception.IllegalArgumentException.Error.INVALID_IDENTIFYING_CODE;
import static com.askdog.service.exception.NotFoundException.Error.USER;
import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Strings.isNullOrEmpty;
import static com.google.common.collect.ImmutableMap.of;
import static java.util.EnumSet.of;

@Service
@RestController
@Transactional
public class UserServiceImpl implements UserService {

    private static final Logger logger = LoggerFactory.getLogger(UserServiceImpl.class);

    @Autowired
    private UserCell userCell;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private RegistrationTokenMail registrationTokenMail;
    @Autowired
    private StorageRecorder storageRecorder;
    @Autowired
    private IdentifyingCodeRepository identifyingCodeRepository;

    @Override
    public UserDetail findDetail(@PathVariable("userId") long userId) {
        UserDetail userDetail = userCell.findDetail(userId);
        userDetail.setAttributes(userCell.getUserUserAttribute(userId));
        if (null == userDetail.getPhone()) {
            userDetail.setPhone(userDetail.getBasePhone());
        }
        return userDetail;
    }

    @Nonnull
    @Override
    public Optional<BasicInnerUser> findById(@PathVariable("userId") long userId) {
        Optional<User> foundUser = userRepository.findById(userId);
        if (foundUser.isPresent()) {
            return Optional.of(userCell.buildInnerUser(foundUser.get()));
        }

        return Optional.empty();
    }

    @Nonnull
    @Override
    public Optional<BasicInnerUser> findByEmail(@RequestParam("email") String mail) {
        checkArgument(mail.matches(REGEX_MAIL), "mail address not right or not support");
        Optional<User> foundUser = userRepository.findByEmail(mail);
        if (foundUser.isPresent()) {
            return Optional.of(userCell.buildInnerUser(foundUser.get()));
        }

        return Optional.empty();
    }

    @Nonnull
    @Override
    public Optional<BasicInnerUser> findByPhone(@RequestParam("phone") String phone) {
        checkArgument(phone.matches(REGEX_PHONE), "phone number not right or not support");
        Optional<User> foundUser = userRepository.findByPhoneNumber(phone);
        if (foundUser.isPresent()) {
            return Optional.of(userCell.buildInnerUser(foundUser.get()));
        }

        return Optional.empty();
    }

    @Nonnull
    @Override
    public Optional<BasicInnerUser> findByName(@RequestParam("name") String name) {
        checkArgument(name.matches(REGEX_USER_NAME), "user name not right or not support");
        Optional<User> foundUser = userRepository.findByName(name);
        if (foundUser.isPresent()) {
            return Optional.of(userCell.buildInnerUser(foundUser.get()));
        }

        return Optional.empty();
    }

    @Override
    public void updateLastAccessTime(@PathVariable("userId") long userId, @RequestBody Date time) {
        User user = userCell.findExists(userId);
        user.setLastAccessTime(time);
        userRepository.save(user);
    }

    @Override
    public UserDetail createSimpleUser(@Valid @RequestBody SimpleUserCreation simpleUserCreation) {
        String username = simpleUserCreation.getUsername();
        String password = simpleUserCreation.getPassword();

        checkState(!userRepository.findByName(username).isPresent(), () -> new ConflictException(USER_USERNAME));

        User user = new User();
        user.setName(username);
        user.setNickname(simpleUserCreation.getNickname());
        user.setPassword(passwordEncoder.encode(password));
        user.setType(REGISTERED);
        user.setUserStatuses(of(MANAGED));
        User savedUser = userRepository.save(user);
        return findDetail(savedUser.getId());
    }

    @Override
    @TriggerEvent({@TriggerEventItem(performer = "returnValue.id", eventType = CREATE_USER, target = "returnValue.id")})
    public UserDetail createEmailUser(@Nonnull @Validated(Create.class) @RequestBody User user) {
        checkState(!userRepository.existsByEmail(user.getEmail()), () -> new ConflictException(USER_MAIL));
        user.setRegistrationTime(new Date());
        // TODO the password encode should be done in client ?
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        User savedUser = userRepository.save(user);
        registrationTokenMail.send(savedUser);
        return findDetail(savedUser.getId());
    }

    @Override
    @TriggerEvent({@TriggerEventItem(performer = "returnValue.id", eventType = CREATE_USER, target = "returnValue.id")})
    public UserDetail createPhoneUser(@Nonnull @RequestParam("identifyingCode") String identifyingCode, @Nonnull @Validated(Create.class) @RequestBody User user) {
        checkState(!userRepository.existsByPhone(user.getPhoneNumber()), () -> new ConflictException(USER_PHONE));
        checkState(identifyingCodeRepository.isIdentifyingCodeValidate("registration", user.getPhoneNumber(), identifyingCode), () -> new IllegalArgumentException(INVALID_IDENTIFYING_CODE));
        user.setRegistrationTime(new Date());
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.addStatus(PHONE_CONFIRMED);
        User savedUser = userRepository.save(user);
        identifyingCodeRepository.redeemIdentifyingCode("registration", user.getPhoneNumber(), identifyingCode);
        return findDetail(savedUser.getId());
    }

    @Override
    public void generateIdentifyingCode(@Nonnull @RequestParam("phone") String phone) {
        checkState(!userRepository.existsByPhone(phone), () -> new ConflictException(USER_PHONE));
        Map<String, String> values = of(
                "phone", phone,
                "issue_time", String.valueOf(new Date().getTime())
        );
        identifyingCodeRepository.claimIdentifyingCode(phone, "registration", values, 5, TimeUnit.MINUTES);
    }

    @Override
    public boolean validateRegistrationToken(@RequestParam("token") String token) {
        return registrationTokenMail.isTokenValidate(token);
    }

    @Override
    @TriggerEvent({@TriggerEventItem(performer = "userId", eventType = UPDATE_USER, target = "userId")})
    public void updateAvatar(@PathVariable("userId") Long userId, @PathVariable("linkId") Long linkId) {
        User user = userCell.findExists(userId);
        storageRecorder.assertValid(linkId);
        user.setAvatar(linkId);
        userRepository.save(user);
        userCell.refreshBasicCache(userId);
    }

    @Override
    @TriggerEvent({@TriggerEventItem(performer = "userId", eventType = UPDATE_USER, target = "userId")})
    public void updateTags(@PathVariable("userId") long userId, @Nonnull @RequestBody EnumSet<UserTag> tags) {
        User user = userCell.findExists(userId);
        user.setUserTags(tags);
        userRepository.save(user);
        userCell.refreshBasicCache(userId);
    }

    @Override
    @TriggerEvent({@TriggerEventItem(eventType = CONFIRM_USER, target = "returnValue.id")})
    public UserDetail confirmRegistration(@Nonnull @RequestParam("token") String token) {
        String mail = registrationTokenMail.redeemToken(token);
        User user = userRepository.findByEmail(mail).orElseThrow(() -> new NotFoundException(USER));
        user.addStatus(MAIL_CONFIRMED);
        User savedUser = userRepository.save(user);
        return userCell.findDetail(savedUser.getId());
    }

    @Override
    @TriggerEvent({@TriggerEventItem(performer = "userId", eventType = UPDATE_USER, target = "userId")})
    public UserDetail updateUserAttribute(@PathVariable("userId") long userId,
                                          @Nonnull @RequestBody UserAttribute userAttribute) {

        if (!isNullOrEmpty(userAttribute.getRealName())) {
            User user = userCell.findExists(userId);
            user.setNickname(userAttribute.getRealName());
            userRepository.save(user);
            userCell.refreshBasicCache(userId);
        }

        UserAttribute savedUserAttribute = userCell.getUserUserAttribute(userId);
        userAttribute.setId(savedUserAttribute.getId());
        userAttribute.setUserId(savedUserAttribute.getUserId());
        userCell.updateUserAttribute(userId, userAttribute);
        return findDetail(userId);
    }

    @Override
    public boolean isExists(@PathVariable("userId") long userId) {
        return userRepository.exists(userId);
    }

    @Override
    public Page<User> getUsersByAccessTime(@RequestParam("date") Date date, @RequestBody Pageable pageable) {
        return userRepository.findByLastAccessTimeAfter(date, pageable);
    }

}