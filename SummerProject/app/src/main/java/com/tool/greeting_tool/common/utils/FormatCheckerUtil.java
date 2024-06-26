package com.tool.greeting_tool.common.utils;

import java.util.regex.Pattern;

public class FormatCheckerUtil {
    /**
     * check password format
     */
    private static final String PASSWORD_PATTERN =
            "^(?=.*[0-9])"        // At least one number
                    +   "(?=.*[a-z])"         // At least one lowercase letter
                    +   "(?=.*[A-Z])"        // At least one uppercase letter
                    +   "(?=.*[@#$%^&+=])"    // At least one special character
                    +   "(?=\\S+$).{8,}$";      // No spaces and at least eight digits

    private static final Pattern pattern = Pattern.compile(PASSWORD_PATTERN);

    public static boolean checkPassword(String password) {
            // check whether is null
        if (password == null) {
            return false;
        }

        // check format
        return pattern.matcher(password).matches();
    }

}
