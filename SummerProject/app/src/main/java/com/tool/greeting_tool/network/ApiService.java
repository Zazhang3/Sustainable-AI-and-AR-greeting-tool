package com.tool.greeting_tool.network;

import com.google.gson.JsonObject;
import com.tool.greeting_tool.pojo.vo.UserVO;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface ApiService {
    @POST("login")
    Call<JsonObject> loginUser(@Body UserVO user);

}
