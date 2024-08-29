package com.tool.greeting_tool.common.utils;

import android.annotation.SuppressLint;
import android.os.Environment;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.tool.greeting_tool.common.constant.TAGConstant;
import com.tool.greeting_tool.pojo.vo.UserLoginVO;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class JsonUtil {
    private static final String FILE_NAME = "ARGreetingCardsLoginInfo.json";
    private static final String TAG = "JsonUtil";

    /**
     * Transfer info collected by login UI to Json File
     * @param username
     * @param password
     */
    public static void saveLoginInfoToFile(String username, String password) {
        String lastLoginTime = getCurTime();

        KeyStoreUtil.generateKey();

        String encryptUsername = KeyStoreUtil.encryptData(username);
        String encryptPassword = KeyStoreUtil.encryptData(password);

        // Init UserLoginVO
        UserLoginVO loginInfo = new UserLoginVO(encryptUsername, encryptPassword, lastLoginTime);
        // Translate UserLoginVO into jsonString
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        String jsonString = gson.toJson(loginInfo);

        // Get path to Document Dir in Android
        File documentsDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS);
        if (!documentsDir.exists()) {
            documentsDir.mkdirs();
        }

        // Init a new Json File
        File jsonFile = new File(documentsDir, FILE_NAME);

        // Write in JSON File
        try (FileWriter writer = new FileWriter(jsonFile)) {
            writer.write(jsonString);
            Log.d(TAGConstant.LOGIN_TAG, "LoginInfo saved to " + jsonFile.getAbsolutePath());
        } catch (IOException e) {
            e.printStackTrace();
            Log.e(TAGConstant.LOGIN_TAG, "Failed to save LoginInfo", e);
        }
    }

    /**
     * Read Json Login File to be a UserLoginVO Object
     * @return UserLoginVO
     */
    public static UserLoginVO readLoginInfoFile () {
        File documentsDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS);
        File jsonFile = new File(documentsDir, FILE_NAME);

        if (!jsonFile.exists()) {
            Log.e(TAG, "File not exists：" + jsonFile.getAbsolutePath());
            return null;
        }

        try {
            FileReader fileReader = new FileReader(jsonFile);
            Gson gson = new Gson();
            UserLoginVO loginInfo = gson.fromJson(fileReader, UserLoginVO.class);
            fileReader.close();
            Log.d(TAG, "Read from file: " + jsonFile.getAbsolutePath());
            return loginInfo;
        } catch (IOException e) {
            e.printStackTrace();
            Log.e(TAG, "Read file failed", e);
            return null;
        }
    }

    /**
     * Update info in Json File
     * @param newUsername
     * @param newPassword
     */
    public static void updateLoginInfoToFile(String newUsername, String newPassword) {
        File documentsDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS);
        File jsonFile = new File(documentsDir, FILE_NAME);

        if (!jsonFile.exists()) {
            Log.e("JsonFileHandler", "File not exists：" + jsonFile.getAbsolutePath());
            return;
        }

        try {
            // Read Json File
            FileReader fileReader = new FileReader(jsonFile);
            Gson gson = new Gson();
            UserLoginVO loginInfo = gson.fromJson(fileReader, UserLoginVO.class);
            fileReader.close();

            String newLastLoginTime = getCurTime();

            KeyStoreUtil.generateKey();

            String newEncryptUsername = KeyStoreUtil.encryptData(newUsername);
            String newEncryptPassword = KeyStoreUtil.encryptData(newPassword);

            // Modify Java Object
            if (loginInfo != null) {
                loginInfo.setUsername(newEncryptUsername);
                loginInfo.setPassword(newEncryptPassword);
                loginInfo.setLastLoginTime(newLastLoginTime);

                // Transfer new UserLoginVO to Json
                Gson newGson = new GsonBuilder().setPrettyPrinting().create();
                String newJson = newGson.toJson(loginInfo);

                // Write new data to file
                FileWriter fileWriter = new FileWriter(jsonFile);
                fileWriter.write(newJson);
                fileWriter.close();

                Log.d("JsonFileHandler", "File modified:" + jsonFile.getAbsolutePath());
            }
        } catch (IOException e) {
            e.printStackTrace();
            Log.e("JsonFileHandler", "Write or read file failed", e);
        }
    }

    /**
     * Delete Json File
     * @return true or false
     */
    public static boolean deleteLoginInfoJsonFile() {
        File documentsDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS);
        File jsonFile = new File(documentsDir, FILE_NAME);

        if (!jsonFile.exists()) {
            Log.e(TAG, "File not exists：" + jsonFile.getAbsolutePath());
            return false;
        }

        boolean isDeleted = jsonFile.delete();

        if (isDeleted) {
            Log.d(TAG, "File deleted: " + jsonFile.getAbsolutePath());
        } else {
            Log.e(TAG, "Failed to delete file: " + jsonFile.getAbsolutePath());
        }

        return isDeleted;
    }

    /**
     * Check if Json File exists where it should be
     * @return
     */
    public static boolean jsonFileIsExist() {
        File documentsDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS);
        File jsonFile = new File(documentsDir, FILE_NAME);
        return jsonFile.exists();
    }

    /**
     * Get cur time String
     * @return curTime
     */
    public static String getCurTime () {
        long timestamp = System.currentTimeMillis();
        Date date = new Date(timestamp);
        @SuppressLint("SimpleDateFormat")
        SimpleDateFormat formatter = new SimpleDateFormat("MM-dd-yyyy HH:mm:ss");
        return formatter.format(date);
    }
}
