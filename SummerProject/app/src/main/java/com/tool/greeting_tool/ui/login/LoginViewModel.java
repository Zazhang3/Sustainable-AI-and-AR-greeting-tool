package com.tool.greeting_tool.ui.login;

import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.tool.greeting_tool.common.constant.ErrorMessage;
import com.tool.greeting_tool.common.constant.KeySet;
import com.tool.greeting_tool.common.constant.TAGConstant;
import com.tool.greeting_tool.common.constant.URLConstant;
import com.tool.greeting_tool.common.utils.SharedPreferencesUtil;
import com.tool.greeting_tool.network.ApiService;
import com.tool.greeting_tool.pojo.vo.UserLoginVO;
import com.tool.greeting_tool.pojo.vo.UserVO;

import java.io.IOException;

import javax.inject.Inject;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class LoginViewModel extends ViewModel {
    private final MutableLiveData<String> loginStatus = new MutableLiveData<>();
    private ApiService apiService;

    @Inject
    public LoginViewModel (ApiService apiService) {
        this.apiService = apiService;
    }

    public LiveData<String> getLoginStatus() {
        return loginStatus;
    }

    public void login(String username,String password){
        //generate userLoginVO
        UserLoginVO userLoginVO = new UserLoginVO(username,password);

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
                        Toast.makeText(LoginActivity.this,"Login failed " + ErrorMessage.NETWORK_ERROR,Toast.LENGTH_SHORT).show();
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
                                Toast.makeText(LoginActivity.this, "Login successful", Toast.LENGTH_SHORT).show();

                                System.out.println(token);
                                //save user data
                                SharedPreferencesUtil.saveUserInfo(LoginActivity.this,userId,username,token);

                                Intent resultIntent = new Intent();
                                resultIntent.putExtra(KeySet.UserKey, username);
                                setResult(RESULT_OK, resultIntent);
                                finish();
                            } else {
                                // login failed
                                String msg = jsonResponse.get("msg").getAsString();
                                Toast.makeText(LoginActivity.this, "Login failed: " + msg, Toast.LENGTH_SHORT).show();
                            }
                        } catch (Exception e) {
                            Log.e(TAGConstant.LOGIN_TAG, "Exception while parsing response", e);
                            Toast.makeText(LoginActivity.this, "Login failed " + ErrorMessage.INVALID_RESPONSE, Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });
    }








}
