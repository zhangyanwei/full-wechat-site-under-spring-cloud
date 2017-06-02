package com.askdog.service;

import com.askdog.model.entity.inner.user.UserProvider;
import com.askdog.service.bo.BasicExternalUser;
import com.askdog.service.exception.ServiceException;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.annotation.Nonnull;
import javax.validation.Valid;
import java.util.Optional;

import static com.askdog.service.utils.MediaType.APPLICATION_JSON_UTF8_VALUE;
import static org.springframework.web.bind.annotation.RequestMethod.*;

@FeignClient("service")
@RequestMapping("/service/external_user")
public interface ExternalUserService {

    @RequestMapping(value = "/", method = POST, produces = APPLICATION_JSON_UTF8_VALUE)
    @Nonnull BasicExternalUser save(@Valid @RequestBody BasicExternalUser externalUser) throws ServiceException;

    @RequestMapping(value = "/", method = PUT, produces = APPLICATION_JSON_UTF8_VALUE)
    void update(@Valid @RequestBody BasicExternalUser externalUser) throws ServiceException;

    @RequestMapping(value = "/{externalUserId}", method = GET, produces = APPLICATION_JSON_UTF8_VALUE)
    @Nonnull Optional<BasicExternalUser> findByExternalUserIdAndProvider(@Nonnull @PathVariable("externalUserId") String externalUserId, @Nonnull @RequestParam("provider") UserProvider provider) throws ServiceException;

    @RequestMapping(method = POST, produces = APPLICATION_JSON_UTF8_VALUE)
    @Nonnull Optional<BasicExternalUser> findByExternalUserId(@Nonnull @RequestParam("externalUserId") String externalUserId) throws ServiceException;

    @RequestMapping(value = "/provider/{provider}/bind_user/{userId}", produces = APPLICATION_JSON_UTF8_VALUE)
    @Nonnull Optional<BasicExternalUser> findByUserIdAndProvider(@Nonnull @PathVariable("userId") Long userId, @Nonnull @PathVariable("provider") UserProvider provider) throws ServiceException;
}
