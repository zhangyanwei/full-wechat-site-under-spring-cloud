package com.askdog.service.impl.mail;

import com.askdog.service.exception.ServiceException;

public interface TokenMail extends Mail {
    void redeemToken(String mail, String token) throws ServiceException;
    String redeemToken(String token) throws ServiceException;
    boolean isTokenValidate(String token);
}
