package com.tool.greeting_tool;


import android.os.Bundle;
import android.view.View;

import com.tool.greeting_tool.common.KeySet;
import com.tool.greeting_tool.server.NotificationHelper;
import com.tool.greeting_tool.ui.user.UserViewModel;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.tool.greeting_tool.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;
    private UserViewModel userViewModel;
    private NotificationHelper notificationHelper;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        String userName = getIntent().getStringExtra(KeySet.UserKey);
        // Initialize NotificationHelper
        notificationHelper = new NotificationHelper(this);
        // Initialize UserViewModel
        userViewModel = new ViewModelProvider(this).get(UserViewModel.class);
        // Set the username in UserViewModel
        userViewModel.setText(userName);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        BottomNavigationView navView = findViewById(R.id.nav_view);
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.

        //Set ActionBar that show on the left top of each page
        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
                R.id.navigation_home, R.id.navigation_user)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_activity_main);
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
        NavigationUI.setupWithNavController(binding.navView, navController);

        //Objects.requireNonNull(getSupportActionBar()).setDisplayShowTitleEnabled(false);

    }

    public void sendNotification(View view) {
        notificationHelper.sendNotification("Notification!", "You have a new E-card!");
    }

    public void cancelNotification(View view) {
        notificationHelper.cancelNotification(1);
    }

}