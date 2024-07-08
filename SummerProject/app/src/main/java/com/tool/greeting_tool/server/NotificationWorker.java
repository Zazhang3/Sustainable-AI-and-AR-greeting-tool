package com.tool.greeting_tool.server;

import static androidx.core.content.ContentProviderCompat.requireContext;

import android.Manifest;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Build;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;
import androidx.core.content.ContextCompat;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import com.tool.greeting_tool.MainActivity;
import com.tool.greeting_tool.R;
import com.tool.greeting_tool.common.constant.ErrorMessage;
import com.tool.greeting_tool.common.constant.KeySet;
import com.tool.greeting_tool.common.constant.TAGConstant;
import com.tool.greeting_tool.common.constant.URLConstant;
import com.tool.greeting_tool.common.utils.SharedPreferencesUtil;
import com.tool.greeting_tool.pojo.dto.GreetingCard;
import com.tool.greeting_tool.ui.home.HomeFragment;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.ArrayList;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class NotificationWorker extends Worker {

    private static final String CHANNEL_ID = "location_update_channel";
    private LocationHelper locationHelper;
    private Context context;

    public NotificationWorker(@NonNull Context context, @NonNull WorkerParameters workerParams) {
        super(context, workerParams);
        this.context = context;
        locationHelper = new LocationHelper(context);
    }

    @NonNull
    @Override
    public Result doWork() {
        System.out.println("Goto doWork");
        if (!shouldSkipInitialExecution()){
            if (ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                locationHelper.getLastLocation(new LocationHelper.PostcodeCallback() {
                    @Override
                    public void onPostcodeResult(String postcode) {
                        //because the background limit, must have background location permission
                        //TODO
                        //Add sending action to back-end here and get back integer value
                        int count = 2;
                        System.out.println("goto sending");
                        sendNotification(postcode, count);
                    }
                });
                return Result.success();
            } else {
                return Result.failure();
            }
        }else{
            System.out.println("have skip");
        }

        return Result.failure();
    }

    private boolean shouldSkipInitialExecution() {
        SharedPreferences sharedPreferences = context.getSharedPreferences("notification_prefs", Context.MODE_PRIVATE);
        boolean isInitialExecutionHandled = sharedPreferences.getBoolean("initial_execution_handled", false);

        if (!isInitialExecutionHandled) {
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putBoolean("initial_execution_handled", true);
            editor.apply();
            return true;
        }

        return false;
    }

    private void requestNewLocationData() {
        // Request a new location update if needed
    }

    private void sendNotification(String postcode, int count) {
        System.out.println("Start sending");
        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

        SharedPreferencesUtil.saveNotificationMessage(context, postcode, count);
        SharedPreferencesUtil.setNotificationPostedFlag(context, true);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = "Location Update Channel";
            String description = "Channel for location update notifications";
            int importance = NotificationManager.IMPORTANCE_HIGH;
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, name, importance);
            channel.setDescription(description);

            if (notificationManager != null) {
                notificationManager.createNotificationChannel(channel);
            }
        }

        Intent intent = new Intent(context, MainActivity.class);
        //intent.putExtra(KeySet.NotificationKey, postcode);
        intent.putExtra("source", "notification");
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_IMMUTABLE);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, CHANNEL_ID)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle("Greeting Card Update")
                .setContentText("Your current postcode " + postcode + " have " + count + " message ")
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setContentIntent(pendingIntent)
                .setAutoCancel(true);

        if (notificationManager != null) {
            notificationManager.notify(1, builder.build());
            System.out.println("finish sending");
        }else{
            System.out.println("notificationManager is null");
        }
    }

}
