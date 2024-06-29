package com.tool.greeting_tool.common.utils;

import android.content.Context;
import android.content.SharedPreferences;

public class SharedPreferencesUtil {
    private static final String userPrefs = "user_prefs";

    /**
     * clear user data
     * @param context
     */
    public static void clearSharedPreferences(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(userPrefs, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.clear();
        editor.apply();
    }

    /**
     * save user data
     * @param context
     * @param userId
     * @param username
     * @param token
     */
    public static void saveUserInfo(Context context,Long userId, String username, String token) {
        SharedPreferences sharedPreferences = context.getSharedPreferences("user_prefs", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putLong("userId", userId);
        editor.putString("username", username);
        editor.putString("token", token);
        editor.apply();
    }

    /**
     * save verificationCode
     * @param context
     * @param email
     */
    public static void saveEmail(Context context,String email) {
        SharedPreferences sharedPreferences = context.getSharedPreferences("user_prefs", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("email", email);
        editor.apply();
    }

    /**
     * save verificationCode
     * @param context
     * @param verificationCode
     */
    public static void saveVerificationCode(Context context,String verificationCode) {
        SharedPreferences sharedPreferences = context.getSharedPreferences("user_prefs", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("verificationCode", verificationCode);
        editor.apply();
    }
    /**
     * get user id
     * @param context
     * @return
     */
    public static long getLong(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(userPrefs, Context.MODE_PRIVATE);
        return sharedPreferences.getLong("userId", 0);
    }

    /**
     * get token
     * @param context
     * @return
     */
    public static String getToken(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(userPrefs, Context.MODE_PRIVATE);
        return sharedPreferences.getString("token","");
    }

    /**
     * get email
     * @param context
     * @return
     */
    public static String getEmail(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(userPrefs, Context.MODE_PRIVATE);
        return sharedPreferences.getString("email","");
    }

    /**
     * get verificationCode
     * @param context
     * @return
     */
    public static String getVerificationCode(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(userPrefs, Context.MODE_PRIVATE);
        return sharedPreferences.getString("verificationCode","");
    }

}
