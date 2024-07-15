package com.tool.greeting_tool.ui.login;

import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import com.google.gson.JsonObject;
import com.tool.greeting_tool.network.ApiService;
import com.tool.greeting_tool.pojo.vo.UserLoginVO;
import com.tool.greeting_tool.repository.UserRepository;
import java.io.IOException;
import javax.inject.Inject;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginViewModel extends ViewModel {
    private final MutableLiveData<String> loginStatus = new MutableLiveData<>();
    private final ApiService apiService;
    private UserRepository userRepository;

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
            public void onResponse(@NonNull Call<JsonObject> call, @NonNull Response<JsonObject> response) {
                if (response.isSuccessful() && response.body() !=null) {
                    JsonObject jsonResponse = response.body();
                    int code = jsonResponse.get("code").getAsInt();
                    if (code == 1) {
                        // login successfully: handle response
                        Long userId = jsonResponse.get("data").getAsJsonObject().get("id").getAsLong();
                        String token = jsonResponse.get("data").getAsJsonObject().get("token").getAsString();
                        //save user data
                        userRepository.saveUserInfo(userId,username,token);
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
