package com.tool.greeting_tool.common.utils;

import java.util.regex.Pattern;

public class FormatCheckerUtil {
    /**
     * set password format
     */
    private static final String PASSWORD_PATTERN =
            "^(?=.*[0-9])"        // At least one number
                    +   "(?=.*[a-z])"         // At least one lowercase letter
                    +   "(?=.*[A-Z])"        // At least one uppercase letter
                    +   "(?=.*[@#$%^&+=!?])"    // At least one special character
                    +   "(?=\\S+$).{8,}$";      // No spaces and at least eight digits
    private static final Pattern passwordPattern = Pattern.compile(PASSWORD_PATTERN);

    /**
     * set postcode format
     */
    private static final String POSTCODE_PATTERN =
            "^([A-Za-z][A-Ha-hJ-Yj-y]?[0-9][A-Za-z0-9]? ?[0-9][A-Za-z]{2}|[Gg][Ii][Rr] ?0[Aa]{2})$";

    private static final Pattern postcodePattern = Pattern.compile(POSTCODE_PATTERN);

    /**
     * check password
     * @param password
     * @return
     */
    public static boolean checkPassword(String password) {
        // check whether is null
        if (password == null) {
            return false;
        }
        // check format
        return passwordPattern.matcher(password).matches();
    }

    /**
     * check postcode
     * @param postcode :local postcode
     * @return :boolean
     */
    public static boolean checkPostcode(String postcode){
        if(postcode == null){
            return false;
        }

        return postcodePattern.matcher(postcode).matches();
    }



}
