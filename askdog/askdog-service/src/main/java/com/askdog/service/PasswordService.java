package com.askdog.service;

import com.askdog.service.bo.ChangePassword;
import com.askdog.service.exception.ServiceException;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.annotation.Nonnull;

import static com.askdog.service.utils.MediaType.APPLICATION_JSON_UTF8_VALUE;
import static org.springframework.web.bind.annotation.RequestMethod.*;

@FeignClient("service")
@RequestMapping("/service/password")
public interface PasswordService {

    @RequestMapping(value = "/recover", method = POST)
    void sendRecoverPasswordMail(@Nonnull @RequestParam("mail") String mail) throws ServiceException;

    @RequestMapping(value = "/recover/phone", method = POST)
    void sendRecoverPasswordPhone(@Nonnull @RequestParam("phone") String phone) throws ServiceException;

    @RequestMapping(value = "/recover/code", method = POST)
    void sendIdentifyingCode(@Nonnull @RequestParam("phone") String phone) throws ServiceException;

    @RequestMapping(value = "/code/validation", method = POST)
    void validateIdentifyingCode(@Nonnull @RequestParam("phone") String phone, @Nonnull @RequestParam("code") String code) throws ServiceException;

    @RequestMapping(value = "/phone", method = PUT)
    void updatePhonePassword(@Nonnull @RequestParam("phone") String phone,
                             @RequestParam("newPassword") @Nonnull String newPassword,
                             @Nonnull @RequestParam("code") String code) throws ServiceException;

    @RequestMapping(value = "/token/validation", method = GET, produces = APPLICATION_JSON_UTF8_VALUE)
    boolean validatePasswordToken(@Nonnull @RequestParam("token") String token);

    @RequestMapping(method = PUT)
    void updateMailPassword(@Nonnull @RequestParam("token") String token,
                            @RequestParam("mail") @Nonnull String mail,
                            @Nonnull @RequestParam("newPassword") String newPassword) throws ServiceException;

    @RequestMapping(value = "/change/{userId}", method = PUT)
    void changePassword(@PathVariable("userId") Long userId, @RequestBody ChangePassword changePassword) throws ServiceException;
}
