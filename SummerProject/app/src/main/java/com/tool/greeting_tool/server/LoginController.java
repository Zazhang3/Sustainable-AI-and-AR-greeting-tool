package com.tool.greeting_tool.server;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.tool.greeting_tool.R;
import com.tool.greeting_tool.common.ErrorMessage;
import com.tool.greeting_tool.common.KeySet;
import com.tool.greeting_tool.common.SharedPreferencesUtil;
import com.tool.greeting_tool.common.TAGConstant;
import com.tool.greeting_tool.common.URLConstant;
import com.tool.greeting_tool.pojo.vo.UserLoginVO;

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
    private ImageButton backButton;

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
        backButton = findViewById(R.id.navigateButton_login);
        ImageButton loginButton = findViewById(R.id.signin_button);

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String username = usernameEditText.getText().toString();
                String password = passwordEditText.getText().toString();
                //login logic
                login(username,password);
            }
        });

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }


    /**
     * login connection
     * @param username
     * @param password
     */
    private void login(String username,String password){
        //generate userVO
        UserLoginVO userLoginVO = new UserLoginVO();
        userLoginVO.setUsername(username);
        userLoginVO.setPassword(password);

        //generate request
        Gson gson = new Gson();
        String json = gson.toJson(userLoginVO);

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
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(LoginController.this,"Login failed " + ErrorMessage.NETWORK_ERROR,Toast.LENGTH_SHORT).show();
                    }
                });
            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                final String responseBody = response.body().string();
                Log.d(TAGConstant.LOGIN_TAG,"Response: "+ responseBody);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            Gson gson = new Gson();
                            JsonObject jsonResponse = gson.fromJson(responseBody, JsonObject.class);
                            int code = jsonResponse.get("code").getAsInt();
                            if (code == 1) {
                                // login successfully: handle response
                                Long userId = jsonResponse.get("data").getAsJsonObject().get("id").getAsLong();
                                String token = jsonResponse.get("data").getAsJsonObject().get("token").getAsString();
                                Toast.makeText(LoginController.this, "Login successful", Toast.LENGTH_SHORT).show();

                                System.out.println(token);
                                //save user data
                                SharedPreferencesUtil.saveUserInfo(LoginController.this,userId,username,token);

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
                    }
                });
            }
        });
    }

}