package com.tool.greeting_tool.common.constant;

public class URLConstant {

    public static final String BASIC_URL = "http:/18.130.196.224:8080/";
    public static final String BASIC_USER_URL = BASIC_URL+"user";

    public static final String BASIC_GREETING_CARD_URL = BASIC_URL+"greeting_card";
    public static final String LOGIN_URL = BASIC_USER_URL+"/login";
    public static final String SIGN_UP_URL = BASIC_USER_URL+"/register";
    public static final String UPDATE_URL = BASIC_USER_URL+"/update";

    public static final String VERIFY_USERNAME_AND_EMAIL_URL = BASIC_USER_URL+"/verification";
    public static final String LOGOUT_URL = BASIC_USER_URL+"/logout";

    public static final String GET_HISTORY_CARD_URL = BASIC_GREETING_CARD_URL+"/user";
    public static final String SEND_CARD_URL = BASIC_GREETING_CARD_URL;
    public static final String GET_NEARBY_CARD_URL = BASIC_GREETING_CARD_URL+"/postcode";
    public static final String GET_DELETE_CARD_URL = BASIC_GREETING_CARD_URL;

    public static final String COUNT_BY_POSTCODE = BASIC_GREETING_CARD_URL+"/count";
}
