package com.askdog.web.api.generic;

import com.askdog.service.TokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Nonnull;
import java.util.Map;

@RestController
@RequestMapping("/api/token")
public class TokenApi {

    @Autowired
    private TokenService tokenService;

    @Nonnull
    @RequestMapping(value = "/{name}/{token}", method = RequestMethod.GET)
    public Map<String, String> tokenDetail(@PathVariable("name") String tokenName, @PathVariable("token") String token) {
        return tokenService.tokenDetail(tokenName, token);
    }

}
