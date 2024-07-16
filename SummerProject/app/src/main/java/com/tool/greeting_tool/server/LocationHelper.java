package com.tool.greeting_tool.server;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.tool.greeting_tool.common.constant.RequestCode;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Handler;
import android.os.Looper;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.tool.greeting_tool.common.utils.FormatCheckerUtil;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Class use to ask location permission and get current location
 * by latitude and longitude
 */

public class LocationHelper {
    FusedLocationProviderClient fusedLocationProviderClient;
    Context context;

    private static final String BASE_URL = "https://maps.googleapis.com/maps/api/geocode/json";
    private static final String API_KEY = "AIzaSyB3FX_zTEwt-SJkW_62J_kAaL2Yu18v630";

    public interface PostcodeCallback{
        void onPostcodeResult(String postcode);
    }

    public LocationHelper(Context context){
        this.context = context;
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(context);
    }

    /**
     * The Activity version
     * compared with Fragement version
     * @param activity
     * @param callback
     */
    public void getLocation(Activity activity, PostcodeCallback callback){
        if(ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION)!=PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(activity, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, RequestCode.REQUEST_LOCATION_PERMISSION);
        }else{
            getLastLocation(callback);
        }
    }

    public void getLocation(Fragment fragment){
        if(ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION)!=PackageManager.PERMISSION_GRANTED){
            fragment.requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, RequestCode.REQUEST_LOCATION_PERMISSION);
        }
    }

    /**
     * No need for this method yet
     * Use in situation that User denied the permission
     * @param requestCode
     * @param grantResults
     * @param callback
     */
    public void onRequestResult(int requestCode, int[] grantResults, PostcodeCallback callback){
        if(requestCode == RequestCode.REQUEST_LOCATION_PERMISSION){
            if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                getLastLocation(callback);
            }
            else{
                Toast.makeText(context, "Location permission denied", Toast.LENGTH_SHORT).show();
            }
        }
    }

    /**
     * This is the only method use to get postcode
     * @param callback
     */

    public void getLastLocation(PostcodeCallback callback){
        if(ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION)==PackageManager.PERMISSION_GRANTED){
            fusedLocationProviderClient.getLastLocation().addOnCompleteListener(new OnCompleteListener<Location>() {
                @Override
                public void onComplete(@NonNull Task<Location> task) {
                    if (task.isSuccessful() && task.getResult() != null){
                        Location location = task.getResult();
                        fetchPostcode(location.getLatitude(), location.getLongitude(), callback);
                        //callback.onLocationResult(latitude, longitude);
                    }
                }
            });
        }
    }

    /**
     *
     * @param latitude
     * @param longitude
     * @param callback
     */
    private void fetchPostcode(double latitude, double longitude, PostcodeCallback callback){
        OkHttpClient client = new OkHttpClient();
        String url = BASE_URL + "?latlng=" + latitude + "," + longitude + "&key=" + API_KEY;

        Request request = new Request.Builder()
                .url(url)
                .build();

        new Thread(new Runnable() {
            @Override
            public void run() {
                try(Response response = client.newCall(request).execute()){
                    if (response.isSuccessful() && response.body() != null) {
                        String responseBody = response.body().string();
                        JsonObject json = new Gson().fromJson(responseBody, JsonObject.class);
                        if ("OK".equals(json.get("status").getAsString())) {
                            JsonArray results = json.getAsJsonArray("results");
                            for (JsonElement result : results) {
                                JsonArray addressComponents = result.getAsJsonObject().getAsJsonArray("address_components");
                                for (JsonElement component : addressComponents) {
                                    JsonArray types = component.getAsJsonObject().getAsJsonArray("types");
                                    for (JsonElement type : types) {
                                        if ("postal_code".equals(type.getAsString())) {
                                            String postcode = FormatCheckerUtil.processPostcode(component.getAsJsonObject().get("long_name").getAsString());
                                            new Handler(Looper.getMainLooper()).post(() ->
                                                    callback.onPostcodeResult(postcode)
                                            );
                                            return;
                                        }
                                    }
                                }
                            }
                        }
                    }
                }catch(IOException e){
                    e.printStackTrace();
                }
            }
        }).start();
    }
}
