package com.askdog.service.impl;

import com.askdog.dao.repository.TokenRepository;
import com.askdog.service.TokenService;
import com.askdog.service.exception.NotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Nonnull;
import java.util.Map;

import static com.askdog.service.exception.NotFoundException.Error.TOKEN;

@RestController
public class TokenServiceImpl implements TokenService {

    @Autowired
    private TokenRepository tokenRepository;

    @Nonnull
    @Override
    public Map<String, String> tokenDetail(@PathVariable("name") String tokenName, @PathVariable("token") String token) {
        Map<String, String> details = tokenRepository.expandToken(tokenName, token);
        if (details == null) {
            throw new NotFoundException(TOKEN);
        }

        return details;
    }
}
