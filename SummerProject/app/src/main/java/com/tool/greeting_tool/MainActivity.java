package com.tool.greeting_tool;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.NavigationUI;
import androidx.work.ExistingPeriodicWorkPolicy;
import androidx.work.OneTimeWorkRequest;
import androidx.work.PeriodicWorkRequest;
import androidx.work.WorkManager;

import com.tool.greeting_tool.common.utils.SharedPreferencesUtil;
import com.tool.greeting_tool.databinding.ActivityMainBinding;
import com.tool.greeting_tool.server.NotificationWorker;

import java.util.concurrent.TimeUnit;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        com.tool.greeting_tool.databinding.ActivityMainBinding binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        //BottomNavigationView navView = findViewById(R.id.nav_view);
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.

        //Set ActionBar that show on the left top of each page
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_activity_main);
        //NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
        NavigationUI.setupWithNavController(binding.navView, navController);

        scheduleWork();
        //scheduleOneTimeWork();
    }

    /**
     * the method use to test notification, it will make a notification once login into home page
     */
    private void scheduleOneTimeWork() {
        System.out.println("Start one-time work schedule");
        OneTimeWorkRequest oneTimeWorkRequest = new OneTimeWorkRequest.Builder(NotificationWorker.class)
                .build();

        WorkManager.getInstance(this).enqueue(oneTimeWorkRequest);
        System.out.println("Finish one-time work schedule");
    }

    /**
     * the method use to schedule notification sending, each 15min will send a notification
     */
    private void scheduleWork() {
        //because the background limit, the minimum interval is 15min
        //It will set a background workManager to repeat the LocationWorker action each 15min
        System.out.println("Start schedule");
        PeriodicWorkRequest locationWorkRequest = new PeriodicWorkRequest.Builder(NotificationWorker.class, 15, TimeUnit.MINUTES)
                //.setInitialDelay(15, TimeUnit.MINUTES)
                .build();

        //WorkManager.getInstance(this).enqueue(locationWorkRequest);
        WorkManager.getInstance(this).enqueueUniquePeriodicWork(
                "LocationWork",
                ExistingPeriodicWorkPolicy.KEEP, // Ensures only one work request is active at a time
                locationWorkRequest);

        System.out.println("Finish schedule");
    }

    public void cancelWork() {
        WorkManager.getInstance(this).cancelUniqueWork("LocationWork");
        System.out.println("Periodic work canceled");
    }

    @Override
    protected void onDestroy() {
        SharedPreferencesUtil.setFirstSkip(this, false);
        super.onDestroy();
    }
}