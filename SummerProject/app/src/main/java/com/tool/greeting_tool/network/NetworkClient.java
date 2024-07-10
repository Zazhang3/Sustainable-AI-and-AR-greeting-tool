package com.tool.greeting_tool.network;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import dagger.hilt.InstallIn;

@Module
@InstallIn(ApplicationComponemt.class)
public class NetworkClient {
    @Provides
    @Singleton
}
