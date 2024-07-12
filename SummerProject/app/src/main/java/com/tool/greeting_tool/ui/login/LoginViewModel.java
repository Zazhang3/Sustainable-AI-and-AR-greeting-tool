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

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

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

    public void login(String username, String password) {
        UserLoginVO user = new UserLoginVO(username,password);
        apiService.loginUser(user).enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                if (response.isSuccessful() && response.body() !=null) {
                    JsonObject jsonResponse = response.body();
                    int code = jsonResponse.get("code").getAsInt();
                    if (code == 1) {
                        // login successfully: handle response
                        Long userId = jsonResponse.get("data").getAsJsonObject().get("id").getAsLong();
                        String token = jsonResponse.get("data").getAsJsonObject().get("token").getAsString();
                        //save user data
                        SharedPreferencesUtil.saveUserInfo(LoginViewModel.this,userId,username,token);
                        loginStatus.postValue("Login success");
                    } else {
                        loginStatus.postValue("Login failed:"+jsonResponse.get("msg").getAsString());
                    }
                } else {
                    loginStatus.postValue("Login failed: Error in response");
                }
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable throwable) {
                loginStatus.postValue("Login failed:" + throwable.getMessage());
            }
        });
    }








}
