package com.tool.greeting_tool.repository;

import android.content.Context;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import dagger.hilt.InstallIn;
import dagger.hilt.android.qualifiers.ApplicationContext;

@Module
@InstallIn(Singleton.class)
public class RepositoryModule {

    @Provides
    public UserRepository provideUserRepository(@ApplicationContext Context context) {
        return new UserRepository(context,);
    }
}
