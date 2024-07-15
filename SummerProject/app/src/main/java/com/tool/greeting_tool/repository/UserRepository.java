package com.tool.greeting_tool.repository;

import android.content.Context;

import com.tool.greeting_tool.common.utils.SharedPreferencesUtil;
import com.tool.greeting_tool.network.ApiService;
import com.tool.greeting_tool.pojo.vo.UserLoginVO;

import javax.inject.Inject;
import javax.security.auth.callback.Callback;

import retrofit2.Call;


public class UserRepository {
    private Context context;
    private ApiService apiService;

    @Inject
    public UserRepository (Context context, ApiService apiService) {
        this.context = context;
        this.apiService = apiService;
    }

    public void saveUserInfo(long userId, String username, String token) {
        SharedPreferencesUtil.saveUserInfo(context, userId, username, token);
    }

    public void UserLogin(UserLoginVO userLoginVO, Callback callback) {
        Call<UserLoginVO> call = apiService.loginUser(userLoginVO)
    }

}
