package com.tool.greeting_tool.common.utils;

import android.content.Context;
import android.content.SharedPreferences;

public class SharedPreferencesUtil {
    private static final String userPrefs = "user_prefs";

    private static final String notificationPrefs = "notification_prefs";

    private static final String KEY_NOTIFICATION_POSTED = "posted_message";

    private static final String KEY_MESSAGE_COUNT = "message_count";

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

    public static String getUsername(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences("user_prefs", Context.MODE_PRIVATE);
        return sharedPreferences.getString("username", null);
    }

    public static void saveNotificationMessage(Context context, String message, Integer count) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(notificationPrefs, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("postcode", message);
        editor.putInt(KEY_MESSAGE_COUNT, count);
        editor.apply();
    }

    public static String getNotificationMessage(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(notificationPrefs, Context.MODE_PRIVATE);
        return sharedPreferences.getString("postcode", "");
    }

    public static void saveMessageCount(Context context, Integer count){
        SharedPreferences sharedPreferences = context.getSharedPreferences(notificationPrefs, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt(KEY_MESSAGE_COUNT, count);
        editor.apply();
    }

    public static Integer getMessageCount(Context context){
        SharedPreferences sharedPreferences = context.getSharedPreferences(notificationPrefs, Context.MODE_PRIVATE);
        return sharedPreferences.getInt(KEY_MESSAGE_COUNT, 0);
    }

    public static void setNotificationPostedFlag(Context context, boolean isPosted) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(notificationPrefs, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean(KEY_NOTIFICATION_POSTED, isPosted);
        editor.apply();
    }

    public static boolean isNotificationPosted(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(notificationPrefs, Context.MODE_PRIVATE);
        return sharedPreferences.getBoolean(KEY_NOTIFICATION_POSTED, false);
    }

    public static void clearNotificationPostedFlag(Context context) {
        setNotificationPostedFlag(context, false);
    }
}
