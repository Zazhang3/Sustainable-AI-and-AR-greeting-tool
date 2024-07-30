package com.tool.greeting_tool.server;

import android.Manifest;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Build;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;
import androidx.core.content.ContextCompat;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

import com.tool.greeting_tool.MainActivity;
import com.tool.greeting_tool.R;
import com.tool.greeting_tool.common.constant.URLConstant;
import com.tool.greeting_tool.common.utils.FormatCheckerUtil;
import com.tool.greeting_tool.common.utils.SharedPreferencesUtil;
import com.tool.greeting_tool.ui.home.HomeFragment;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class NotificationWorker extends Worker {

    private static final String CHANNEL_ID = "location_update_channel";
    private final LocationHelper locationHelper;
    private final Context context;

    private final TextToSpeechHelper textToSpeechHelper;
    private final ExecutorService executorService;

    public NotificationWorker(@NonNull Context context, @NonNull WorkerParameters workerParams) {
        super(context, workerParams);
        this.context = context;
        this.executorService = Executors.newSingleThreadExecutor();
        locationHelper = new LocationHelper(context);
        textToSpeechHelper = new TextToSpeechHelper(context);
    }

    @NonNull
    @Override
    public Result doWork() {
        System.out.println("Goto doWork");
        if (!shouldSkipInitialExecution()) {
            if (ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                locationHelper.getLastLocation(postcode -> executorService.execute(() -> {
                    int count;
                    OkHttpClient client = new OkHttpClient();
                    Request request = new Request.Builder()
                            .url(URLConstant.COUNT_BY_POSTCODE + "/" + postcode)
                            .get()
                            .build();

                    try (Response response = client.newCall(request).execute()) {
                        if (response.isSuccessful() && response.body() != null) {
                            String responseData = response.body().string();
                            JSONObject jsonObject = new JSONObject(responseData);
                            count = jsonObject.getInt("data");
                        } else {
                            count = -1;
                        }
                    } catch (IOException | JSONException e) {
                        throw new RuntimeException(e);
                    }

                    //send notification
                    System.out.println("goto sending");
                    if(SharedPreferencesUtil.getNotificationSender(context)){
                        sendNotification(postcode, count);
                    }
                }));
                return Result.success();
            } else {
                return Result.failure();
            }
        } else {
            System.out.println("have skip");
        }

        return Result.failure();
    }

    private boolean shouldSkipInitialExecution() {
        if (!SharedPreferencesUtil.getFirstSkip(context)) {
            SharedPreferencesUtil.setFirstSkip(context, true);
            return true;
        }

        return false;
    }

    private void sendNotification(String postcode, int count) {
        System.out.println("Start sending");
        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

        postcode = FormatCheckerUtil.formatPostcode(postcode);

        String text = "You have " + count + " Message in " + postcode;
        textToSpeechHelper.startSynthesizeThread(text);

        //SharedPreferencesUtil.saveNotificationMessage(context, postcode, count);
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
