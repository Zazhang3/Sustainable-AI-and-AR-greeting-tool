package com.tool.greeting_tool.server;

import android.content.Intent;
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
import com.tool.greeting_tool.common.constant.ErrorMessage;
import com.tool.greeting_tool.common.constant.KeySet;
import com.tool.greeting_tool.common.utils.SharedPreferencesUtil;
import com.tool.greeting_tool.common.constant.TAGConstant;
import com.tool.greeting_tool.common.constant.URLConstant;
import com.tool.greeting_tool.pojo.vo.UserLoginVO;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class SignUpController extends AppCompatActivity {

    private EditText usernameEditText;
    private EditText passwordEditText;
    private EditText rePasswordEditText;
    private ImageButton backButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        usernameEditText = findViewById(R.id.id_signup_id);
        passwordEditText = findViewById(R.id.id_signup_password);
        rePasswordEditText = findViewById(R.id.id_signup_password_re);
        backButton = findViewById(R.id.id_back_signup);
        ImageButton SignUp = findViewById(R.id.id_signup_button);

        SignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String username = usernameEditText.getText().toString();
                String password = passwordEditText.getText().toString();
                //TODO
                //set minimum password length
                if(password.equals(rePasswordEditText.getText().toString())){
                    /*Intent intent = new Intent(SignUp.this, MainActivity.class);
                    intent.putExtra(KeySet.UserKey, account);
                    startActivity(intent);*/
                    if(!username.isEmpty()){
                        Intent resultIntent = new Intent();
                        resultIntent.putExtra(KeySet.UserKey, username);
                        setResult(RESULT_OK, resultIntent);
                        finish();
                    }else{
                        //TODO
                        // change error message
                        Toast.makeText(SignUpController.this, ErrorMessage.USERNAME_EMPTY_ERROR, Toast.LENGTH_SHORT).show();
                    }
                }else{
                    Toast.makeText(SignUpController.this, ErrorMessage.INCONSISTENT_PASSWORD, Toast.LENGTH_SHORT).show();
                }
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
     * Sign up with username and password
     * @param username
     * @param password
     */
    private void signUp(String username,String password){
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
                .url(URLConstant.SIGN_UP_URL)
                .post(body)
                .build();

        //send request
        OkHttpClient client = new OkHttpClient();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                Log.e(TAGConstant.SIGN_UP_TAG,"Login failed",e);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(SignUpController.this,"Sign up failed: " + ErrorMessage.NETWORK_ERROR,Toast.LENGTH_SHORT).show();
                    }
                });
            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                final String responseBody = response.body().string();
                Log.d(TAGConstant.SIGN_UP_TAG,"Response: "+ responseBody);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            Gson gson = new Gson();
                            JsonObject jsonResponse = gson.fromJson(responseBody, JsonObject.class);
                            int code = jsonResponse.get("code").getAsInt();
                            if (code == 1) {
                                // login successfully: handle response
                                Long id = jsonResponse.get("data").getAsJsonObject().get("id").getAsLong();
                                String token = jsonResponse.get("data").getAsJsonObject().get("token").getAsString();
                                Toast.makeText(SignUpController.this, "Sign up successfully", Toast.LENGTH_SHORT).show();

                                //save user data
                                SharedPreferencesUtil.saveUserInfo(SignUpController.this,id,username,token);

                                Intent resultIntent = new Intent();
                                resultIntent.putExtra(KeySet.UserKey, username);
                                setResult(RESULT_OK, resultIntent);
                                finish();
                            } else {
                                // login failed
                                String msg = jsonResponse.get("msg").getAsString();
                                Toast.makeText(SignUpController.this, "Sign up failed: " + msg, Toast.LENGTH_SHORT).show();
                            }
                        } catch (Exception e) {
                            Log.e(TAGConstant.SIGN_UP_TAG, "Exception while parsing response", e);
                            Toast.makeText(SignUpController.this, "Sign up failed: " + ErrorMessage.INVALID_RESPONSE, Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });
    }

}