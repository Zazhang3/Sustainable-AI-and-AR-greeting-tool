package com.tool.greeting_tool.ui.IntroPage;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
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
import com.tool.greeting_tool.common.constant.TAGConstant;
import com.tool.greeting_tool.common.constant.URLConstant;
import com.tool.greeting_tool.common.utils.SharedPreferencesUtil;
import com.tool.greeting_tool.pojo.vo.UserVO;
import com.tool.greeting_tool.server.SignUpController;
import com.tool.greeting_tool.server.StartPage;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class SetPasswordActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_password);

        Intent data = getIntent();
        EditText reset_Password = findViewById(R.id.re_set_password);
        EditText reset_Password_Re = findViewById(R.id.re_set_password_re);
        String username = data.getStringExtra(KeySet.UserKey);


        ImageButton submitButton = findViewById(R.id.reset_password_button);
        submitButton.setOnClickListener(v->{
            //TODO
            //Whether to send request
            String email = SharedPreferencesUtil.getEmail(SetPasswordActivity.this);
            String password = reset_Password.getText().toString();
            String password_Re = reset_Password_Re.getText().toString();
            if(!password.isEmpty()&& password.equals(password_Re)){

                updateUserInfo(username,password,email);

                Intent resultIntent = new Intent();
                setResult(Activity.RESULT_OK, resultIntent);
                finish();
            }else{

            }

        });
    }

    /**
     * update user data
     * @param username
     * @param password
     * @param email
     */
    private void updateUserInfo(String username, String password, String email) {
        //generate userVO
        UserVO userVO = new UserVO();
        userVO.setUsername(username);
        userVO.setPassword(password);
        userVO.setEmail(email);

        //generate request
        Gson gson = new Gson();
        String json = gson.toJson(userVO);

        RequestBody body = RequestBody.create(
                json,
                MediaType.get("application/json;charset=utf-8"));

        Request request = new Request.Builder()
                .url(URLConstant.UPDATE_URL)
                .put(body)
                .build();

        //send request
        OkHttpClient client = new OkHttpClient();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                Log.e(TAGConstant.UPDATE_USER_DATA,"Update failed",e);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(SetPasswordActivity.this,"Update failed: " + ErrorMessage.NETWORK_ERROR,Toast.LENGTH_SHORT).show();
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
                                // Update successfully: handle response
                                Toast.makeText(SetPasswordActivity.this, "Update successfully", Toast.LENGTH_SHORT).show();
                            } else {
                                // login failed
                                String msg = jsonResponse.get("msg").getAsString();
                                Toast.makeText(SetPasswordActivity.this, "Update failed: " + msg, Toast.LENGTH_SHORT).show();
                            }
                        } catch (Exception e) {
                            Log.e(TAGConstant.SIGN_UP_TAG, "Exception while parsing response", e);
                            Toast.makeText(SetPasswordActivity.this, "Update failed: " + ErrorMessage.INVALID_RESPONSE, Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });
    }


}