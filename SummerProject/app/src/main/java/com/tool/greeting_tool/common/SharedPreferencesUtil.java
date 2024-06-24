package com.tool.greeting_tool.common;

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
        editor.clear(); // 清空所有数据
        editor.apply(); // 提交更改
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

}
