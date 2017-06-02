package com.askdog.web.vo;

import com.askdog.common.In;
import com.askdog.model.entity.User;
import com.askdog.model.entity.inner.user.UserType;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

import static com.askdog.common.RegexPattern.*;

public class MailUser implements In<User> {

    @NotNull
    @Pattern(regexp = REGEX_NICK_NAME)
    private String nickname;

    @NotNull
    @Pattern(regexp = REGEX_USER_PASSWORD)
    private String password;

    @Pattern(regexp = REGEX_MAIL)
    private String mail;

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getMail() {
        return mail;
    }

    public void setMail(String mail) {
        this.mail = mail;
    }

    @Override
    public User convert() {
        User user = new User();
        user.setNickname(this.nickname);
        user.setPassword(this.password);
        user.setEmail(this.mail);
        user.setType(UserType.REGISTERED);
        return user;
    }
}
