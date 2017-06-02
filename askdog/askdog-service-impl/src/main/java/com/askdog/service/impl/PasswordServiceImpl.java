package com.askdog.service.impl;

import com.askdog.dao.repository.IdentifyingCodeRepository;
import com.askdog.dao.repository.UserRepository;
import com.askdog.model.entity.User;
import com.askdog.service.PasswordService;
import com.askdog.service.bo.ChangePassword;
import com.askdog.service.exception.IllegalArgumentException;
import com.askdog.service.exception.NotFoundException;
import com.askdog.service.exception.ServiceException;
import com.askdog.service.impl.mail.PasswordTokenMail;
import org.elasticsearch.common.Strings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Nonnull;
import java.util.Date;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

import static com.askdog.common.RegexPattern.REGEX_MAIL;
import static com.askdog.common.RegexPattern.REGEX_PHONE;
import static com.askdog.common.utils.Conditions.checkState;
import static com.askdog.model.entity.inner.user.UserStatus.MAIL_CONFIRMED;
import static com.askdog.model.entity.inner.user.UserStatus.PHONE_CONFIRMED;
import static com.askdog.service.exception.IllegalArgumentException.Error.INVALID_IDENTIFYING_CODE;
import static com.askdog.service.exception.IllegalArgumentException.Error.INVALID_ORIGIN_PASSWORD;
import static com.askdog.service.exception.NotFoundException.Error.USER;
import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.collect.ImmutableMap.of;

@Service
@RestController
public class PasswordServiceImpl implements PasswordService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private PasswordTokenMail passwordTokenMail;

    @Autowired
    private IdentifyingCodeRepository identifyingCodeRepository;


    @Override
    public void sendRecoverPasswordMail(@Nonnull @RequestParam("mail") String mail) throws ServiceException {
        checkArgument(mail.matches(REGEX_MAIL), "invalid mail address");
        Optional<User> foundUser = userRepository.findByEmail(mail);
        if (!foundUser.isPresent()) {
            throw new NotFoundException(USER);
        }
        passwordTokenMail.send(foundUser.get());
    }

    @Override
    public void sendRecoverPasswordPhone(@Nonnull @RequestParam("phone") String phone) throws ServiceException {
        checkArgument(phone.matches(REGEX_PHONE), "invalid phone number");
        Optional<User> foundUser = userRepository.findByPhoneNumber(phone);
        if (!foundUser.isPresent()) {
            throw new NotFoundException(USER);
        }
    }

    @Override
    public void sendIdentifyingCode(@Nonnull @RequestParam("phone") String phone) throws ServiceException {
        checkArgument(phone.matches(REGEX_PHONE), "invalid phone number");
        Optional<User> foundUser = userRepository.findByPhoneNumber(phone);
        if (!foundUser.isPresent()) {
            throw new NotFoundException(USER);
        }
        Map<String, String> values = of(
                "phone", phone,
                "issue_time", String.valueOf(new Date().getTime())
        );
        identifyingCodeRepository.claimIdentifyingCode(phone, "recover", values, 5, TimeUnit.MINUTES);
    }

    @Override
    public void validateIdentifyingCode(@Nonnull @RequestParam("phone") String phone, @Nonnull @RequestParam("code") String code) throws ServiceException {
        checkArgument(phone.matches(REGEX_PHONE), "invalid phone number");
        checkState(identifyingCodeRepository.isIdentifyingCodeValidate("recover", phone, code), () -> new IllegalArgumentException(INVALID_IDENTIFYING_CODE));
    }

    @Override
    public void updatePhonePassword(@Nonnull @RequestParam("phone") String phone,
                                    @RequestParam("newPassword") @Nonnull String newPassword,
                                    @Nonnull @RequestParam("code") String code) throws ServiceException {
        checkArgument(phone.matches(REGEX_PHONE), "invalid phone number");
        checkState(identifyingCodeRepository.isIdentifyingCodeValidate("recover", phone, code), () -> new IllegalArgumentException(INVALID_IDENTIFYING_CODE));
        User user = userRepository.findByPhoneNumber(phone).orElseThrow(() -> new NotFoundException(USER));
        user.setPassword(passwordEncoder.encode(newPassword));
        user.addStatus(PHONE_CONFIRMED);
        userRepository.save(user);
        identifyingCodeRepository.redeemIdentifyingCode("recover", phone, code);
    }

    @Override
    public boolean validatePasswordToken(@Nonnull @RequestParam("token") String token) {
        return passwordTokenMail.isTokenValidate(token);
    }

    @Override
    public void updateMailPassword(@Nonnull @RequestParam("token") String token,
                                   @RequestParam("mail") @Nonnull String mail,
                                   @Nonnull @RequestParam("newPassword") String newPassword) throws ServiceException {
        passwordTokenMail.redeemToken(mail, token);
        User user = userRepository.findByEmail(mail).orElseThrow(() -> new NotFoundException(USER));
        user.setPassword(passwordEncoder.encode(newPassword));
        user.addStatus(MAIL_CONFIRMED);
        userRepository.save(user);
    }

    @Override
    public void changePassword(@PathVariable("userId") Long userId, @RequestBody ChangePassword changePassword) throws ServiceException {
        User user = userRepository.findById(userId).orElseThrow(() -> new NotFoundException(USER));

        if (!Strings.isEmpty(user.getPassword()) && !passwordEncoder.matches(changePassword.getOriginPassword(), user.getPassword())) {
            throw new IllegalArgumentException(INVALID_ORIGIN_PASSWORD);
        }
        user.setPassword(passwordEncoder.encode(changePassword.getNewPassword()));
        userRepository.save(user);
    }
}
