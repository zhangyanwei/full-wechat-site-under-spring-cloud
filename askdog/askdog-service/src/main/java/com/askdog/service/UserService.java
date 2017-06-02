package com.askdog.service;

import com.askdog.model.data.UserAttribute;
import com.askdog.model.entity.User;
import com.askdog.model.entity.inner.user.UserTag;
import com.askdog.service.bo.BasicInnerUser;
import com.askdog.service.bo.SimpleUserCreation;
import com.askdog.service.bo.UserDetail;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.annotation.Nonnull;
import javax.validation.Valid;
import java.util.Date;
import java.util.EnumSet;
import java.util.Optional;

import static com.askdog.service.utils.MediaType.APPLICATION_JSON_UTF8_VALUE;
import static org.springframework.web.bind.annotation.RequestMethod.*;

@FeignClient("service")
@RequestMapping("/service/users")
public interface UserService {

    @Nonnull
    @RequestMapping(value = "/{userId}", method = GET, produces = APPLICATION_JSON_UTF8_VALUE)
    Optional<BasicInnerUser> findById(@PathVariable("userId") long userId);

    @Nonnull
    @RequestMapping(value = "/mail", method = GET, produces = APPLICATION_JSON_UTF8_VALUE)
    Optional<BasicInnerUser> findByEmail(@RequestParam("email") String mail);

    @Nonnull
    @RequestMapping(value = "/phone", method = GET, produces = APPLICATION_JSON_UTF8_VALUE)
    Optional<BasicInnerUser> findByPhone(@RequestParam("phone") String phone);

    @Nonnull
    @RequestMapping(value = "/name", method = GET, produces = APPLICATION_JSON_UTF8_VALUE)
    Optional<BasicInnerUser> findByName(@RequestParam("name") String name);

    @RequestMapping(value = "/simple", method = POST)
    UserDetail createSimpleUser(@Valid @RequestBody SimpleUserCreation simpleUserCreation);

    @RequestMapping(value = "/", method = POST, consumes = APPLICATION_JSON_UTF8_VALUE, produces = APPLICATION_JSON_UTF8_VALUE)
    UserDetail createEmailUser(@Nonnull @RequestBody User user);

    @RequestMapping(value = "/phone", method = POST, consumes = APPLICATION_JSON_UTF8_VALUE, produces = APPLICATION_JSON_UTF8_VALUE)
    UserDetail createPhoneUser(@Nonnull @RequestParam("identifyingCode") String identifyingCode, @Nonnull @RequestBody User user);

    @RequestMapping(value = "/phone/code", method = POST, consumes = APPLICATION_JSON_UTF8_VALUE, produces = APPLICATION_JSON_UTF8_VALUE)
    void generateIdentifyingCode(@Nonnull @RequestParam("phone") String phone);

    @RequestMapping(value = "/{userId}/tags", method = PUT)
    void updateTags(@PathVariable("userId") long userId, @Nonnull @RequestBody EnumSet<UserTag> tags);

    @RequestMapping(value = "/token/validation", method = GET, produces = APPLICATION_JSON_UTF8_VALUE)
    boolean validateRegistrationToken(@RequestParam("token") String token);

    @RequestMapping(value = "/{userId}/last_access_time", method = PUT)
    void updateLastAccessTime(@PathVariable("userId") long userId, @RequestBody Date time);

    @RequestMapping(value = "/{userId}/avatar/{linkId}", method = PUT)
    void updateAvatar(@PathVariable("userId") Long userId, @PathVariable("linkId") Long linkId);

    @RequestMapping(value = "/confirm", method = PUT, produces = APPLICATION_JSON_UTF8_VALUE)
    UserDetail confirmRegistration(@Nonnull @RequestParam("token") String token);

    @RequestMapping(value = "/{userId}/detail", method = GET, produces = APPLICATION_JSON_UTF8_VALUE)
    UserDetail findDetail(@PathVariable("userId") long userId);

    @RequestMapping(value = "/{userId}/detail", method = PUT, produces = APPLICATION_JSON_UTF8_VALUE)
    UserDetail updateUserAttribute(@PathVariable("userId") long userId, @Nonnull @RequestBody UserAttribute userAttribute);

    @RequestMapping("/{userId}/is_exists")
    boolean isExists(@PathVariable("userId") long userId);

    @RequestMapping(value = "/recorder", method = POST)
    Page<User> getUsersByAccessTime(@RequestParam("date") Date date, @RequestBody Pageable pageable);
}