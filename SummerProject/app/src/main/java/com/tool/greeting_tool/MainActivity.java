package com.tool.greeting_tool;

import static android.app.NotificationManager.IMPORTANCE_HIGH;

import android.os.Bundle;

import com.tool.greeting_tool.common.constant.KeySet;
import com.tool.greeting_tool.server.NotificationGenerater;
import com.tool.greeting_tool.server.NotificationWorker;
import com.tool.greeting_tool.ui.user.UserViewModel;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.work.PeriodicWorkRequest;
import androidx.work.WorkManager;

import com.tool.greeting_tool.databinding.ActivityMainBinding;

import java.util.concurrent.TimeUnit;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;
    private UserViewModel userViewModel;
    private NotificationGenerater notificationGenerater;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        String userName = getIntent().getStringExtra(KeySet.UserKey);

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
        //NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
        NavigationUI.setupWithNavController(binding.navView, navController);

        //Objects.requireNonNull(getSupportActionBar()).setDisplayShowTitleEnabled(false);
        scheduleWork();
    }

    private void scheduleWork() {
        //because the background limit, the minimum interval is 15min
        //It will set a background workManager to repeat the LocationWorker action each 15min
        PeriodicWorkRequest locationWorkRequest = new PeriodicWorkRequest.Builder(NotificationWorker.class, 15, TimeUnit.MINUTES)
                .build();

        WorkManager.getInstance(this).enqueue(locationWorkRequest);
    }

}