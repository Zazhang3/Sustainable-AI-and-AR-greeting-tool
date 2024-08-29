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
     * check password
     * @param password : user password
     * @return : boolean
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
     * set postcode format
     */
    private static final String POSTCODE_PATTERN =
            "^([A-Za-z][A-Ha-hJ-Yj-y]?[0-9][A-Za-z0-9]? ?[0-9][A-Za-z]{2}|[Gg][Ii][Rr] ?0[Aa]{2})$";

    private static final Pattern postcodePattern = Pattern.compile(POSTCODE_PATTERN);
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


    private static final String EMAIL_PATTERN = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$";
    private static final Pattern emailPattern = Pattern.compile(EMAIL_PATTERN);

    /**
     * check email
     * @param email : user email
     * @return : boolean
     */
    public static boolean checkEmail(String email){
        if(email == null){
            return false;
        }

        return emailPattern.matcher(email).matches();
    }

    /**
     *
     * @param postcode : postcode need to process
     * @return : string postcode that remove the space
     * e.g. BS1 2BQ -> BS12BQ
     *      BS10 7TW -> BS107TW
     */
    public static String processPostcode(String postcode){
        if (postcode.contains(" ")) {
            return postcode.replace(" ", "");
        }
        return postcode;
    }

    /**
     *
     * @param postcode : postcode need to add space
     * @return : string postcode that add space
     *  Based on the UK postcode, if length of string is 6, add space after third character, otherwise
     *  after forth character
     *  e.g. BS12BQ -> BS1 2BQ
     *       BS107TW -> BS10 7TW
     */
    public static String formatPostcode(String postcode) {
        if (postcode.length() == 6) {
            return postcode.substring(0, 3) + " " + postcode.substring(3);
        } else if (postcode.length() == 7) {
            return postcode.substring(0, 4) + " " + postcode.substring(4);
        }
        return postcode;
    }

}
