package com.askdog.common;

public interface RegexPattern {

    // TODO user name could be chinese, japanese, or other language. not do not allowed the special characters.
    String REGEX_NICK_NAME = "^[^-!$%^&*()_+|~=`{}\\[\\]:/;<>?,.@#'\"\\\\]{1,20}$";
    String REGEX_USER_NAME = "^[a-zA-Z][\\w.]{3,18}$"; // length is 4 ~ 19
    String REGEX_USER_PASSWORD = "^[\\s\\S]{6,20}$";
    String REGEX_PHONE = "^(1[3578][0-9]{9}|(\\d{3,4}-)\\d{7,8}(-\\d{1,4})?)$";
    String REGEX_MAIL = "^[^@\\s]+@(?:[^@\\s.]+)(?:\\.[^@\\s.]+)+$";
    String REGEX_TEMPLATE_NAME = "^[\\w-]+$";
    String REGEX_IDENTIFYING_CODE = "^\\d{6}$";
}
