package com.tool.greeting_tool.server;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.tool.greeting_tool.R;
import com.tool.greeting_tool.common.constant.ErrorMessage;
import com.tool.greeting_tool.common.constant.KeySet;
import com.tool.greeting_tool.common.constant.TAGConstant;
import com.tool.greeting_tool.common.constant.URLConstant;
import com.tool.greeting_tool.common.utils.JsonUtil;
import com.tool.greeting_tool.common.utils.SharedPreferencesUtil;
import com.tool.greeting_tool.pojo.vo.UserLoginVO;
import com.tool.greeting_tool.pojo.vo.UserVO;
import com.tool.greeting_tool.ui.IntroPage.ReSetPassWord;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class LoginController extends AppCompatActivity {

    private EditText usernameEditText;
    private EditText passwordEditText;

    /**
     * Use to ask user enter the ID and Password
     *
     * @param savedInstanceState If the activity is being re-initialized after
     *     previously being shut down then this Bundle contains the data it most
     *     recently supplied in {@link #onSaveInstanceState}.  <b><i>Note: Otherwise it is null.</i></b>
     *
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        usernameEditText = findViewById(R.id.account_num);
        passwordEditText = findViewById(R.id.password);
        ImageButton backButton = findViewById(R.id.navigateButton_login);
        ImageButton loginButton = findViewById(R.id.signin_button);
        ImageButton forgetPassWord = findViewById(R.id.test_button);

        if (JsonUtil.jsonFileIsExist()) {
            showAutoLoginDialog();
        }

        loginButton.setOnClickListener(v -> {
            String username = usernameEditText.getText().toString();
            String password = passwordEditText.getText().toString();
            //login logic
            login(username,password);
        });

        forgetPassWord.setOnClickListener(v->{
            Intent intent = new Intent(this, ReSetPassWord.class);
            intent.putExtra(KeySet.ReSetKey, KeySet.ReSetKey);
            startActivityForResult(intent, 1);
        });

        backButton.setOnClickListener(v -> onBackPressed());
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Intent resultIntent = new Intent();
        //resultIntent.putExtra(KeySet.UserKey, username);
        resultIntent.putExtra(KeySet.ReSetKey, KeySet.ReSetKey);
        setResult(RESULT_OK, resultIntent);
        finish();
    }

    /**
     * login connection
     * @param username : username
     * @param password : password
     */
    private void login(String username,String password){
        if (username.equals("testuser") && password.equals("testpass")) {
            // Mock response for successful login
            Toast.makeText(LoginController.this, "Test Login Successful", Toast.LENGTH_SHORT).show();

            // Simulating successful login response
            Long userId = 123456789L; // Sample user ID
            String token = "sampleToken123456"; // Sample token

            // Save user data as if from a successful response
            SharedPreferencesUtil.saveUserInfo(LoginController.this, userId, username, token);

            // Return to the main activity with success result
            Intent resultIntent = new Intent();
            resultIntent.putExtra(KeySet.UserKey, username);
            setResult(RESULT_OK, resultIntent);
            finish();
            return; // Skip the rest of the login process
        }
        //generate userVO
        UserVO userVO = new UserVO();
        userVO.setUsername(username);
        userVO.setPassword(password);

        //generate request
        Gson gson = new Gson();
        String json = gson.toJson(userVO);

        RequestBody body = RequestBody.create(
                json,
                MediaType.get("application/json;charset=utf-8"));

        Request request = new Request.Builder()
                .url(URLConstant.LOGIN_URL)
                .post(body)
                .build();

        //send request
        OkHttpClient client = new OkHttpClient();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                Log.e(TAGConstant.LOGIN_TAG,"Login failed",e);
                runOnUiThread(() -> Toast.makeText(LoginController.this,"Login failed " + ErrorMessage.NETWORK_ERROR,Toast.LENGTH_SHORT).show());
            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                final String responseBody = response.body().string();
                Log.d(TAGConstant.LOGIN_TAG,"Response: "+ responseBody);
                runOnUiThread(() -> {
                    try {
                        Gson gson1 = new Gson();
                        JsonObject jsonResponse = gson1.fromJson(responseBody, JsonObject.class);
                        int code = jsonResponse.get("code").getAsInt();
                        if (code == 1) {
                            // login successfully: handle response
                            Long userId = jsonResponse.get("data").getAsJsonObject().get("id").getAsLong();
                            String token = jsonResponse.get("data").getAsJsonObject().get("token").getAsString();
                            Toast.makeText(LoginController.this, "Login successful", Toast.LENGTH_SHORT).show();

                            System.out.println(token);
                            //save user data
                            SharedPreferencesUtil.saveUserInfo(LoginController.this,userId,username,token);

                            // save user data for continuously login
                            if (!JsonUtil.jsonFileIsExist()) {
                                JsonUtil.saveLoginInfoToFile(username, password);
                            } else {
                                JsonUtil.updateLoginInfoToFile(username, password);
                            }

                            Intent resultIntent = new Intent();
                            resultIntent.putExtra(KeySet.UserKey, username);
                            setResult(RESULT_OK, resultIntent);
                            finish();
                        } else {
                            // login failed
                            String msg = jsonResponse.get("msg").getAsString();
                            Toast.makeText(LoginController.this, "Login failed: " + msg, Toast.LENGTH_SHORT).show();
                        }
                    } catch (Exception e) {
                        Log.e(TAGConstant.LOGIN_TAG, "Exception while parsing response", e);
                        Toast.makeText(LoginController.this, "Login failed " + ErrorMessage.INVALID_RESPONSE, Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
    }

    private void showAutoLoginDialog() {
        new AlertDialog.Builder(this)
                .setTitle("Auto Login")
                .setMessage("Saved login information detected, do you want to log in automatically?")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        UserLoginVO userLoginInfo = JsonUtil.readLoginInfoFile();
                        if (userLoginInfo != null) {
                            String username = userLoginInfo.getUsername();
                            String password = userLoginInfo.getPassword();
                            // Fill username and password automate
                            usernameEditText.setText(username);
                            passwordEditText.setText(password);
                            // Login
                            login(username, password);
                        } else {
                            Log.e("MainActivity", "Failed to read login info.");
                        }
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // Clean username and password in login window
                        usernameEditText.setText("");
                        passwordEditText.setText("");
                    }
                })
                .show();
    }
}