package com.tool.greeting_tool.network;

import com.tool.greeting_tool.common.constant.URLConstant;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import dagger.hilt.InstallIn;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

@Module
@InstallIn(Singleton.class)
public class NetworkClient {
    @Provides
    @Singleton
    public Retrofit provideRetrofit() {
        return new Retrofit.Builder()
                .baseUrl(URLConstant.BASIC_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
    }

    @Provides
    @Singleton
    public ApiService provideLoginService (Retrofit retrofit) {
        return retrofit.create(ApiService.class);
    }
}
