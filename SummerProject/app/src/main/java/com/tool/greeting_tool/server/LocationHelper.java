package com.tool.greeting_tool.server;

import com.tool.greeting_tool.common.constant.RequestCode;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * Class use to ask location permission and get current location
 * by latitude and longitude
 */

public class LocationHelper {
    FusedLocationProviderClient fusedLocationProviderClient;
    Context context;

    public interface LocationCallback{
        void onLocationResult(double latitude, double longitude);
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
    public void getLocation(Activity activity, LocationCallback callback){
        if(ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION)!=PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(activity, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, RequestCode.REQUEST_LOCATION_PERMISSION);
        }else{
            getLastLocation(callback);
        }
    }

    public void getLocation(Fragment fragment, LocationCallback callback){
        if(ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION)!=PackageManager.PERMISSION_GRANTED){
            fragment.requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, RequestCode.REQUEST_LOCATION_PERMISSION);
        }else{
            getLastLocation(callback);
        }
    }

    /**
     * No need for this method yet
     * Use in situation that User denied the permission
     * @param requestCode
     * @param grantResults
     * @param callback
     */
    public void onRequestResult(int requestCode, int[] grantResults, LocationCallback callback){
        if(requestCode == RequestCode.REQUEST_LOCATION_PERMISSION){
            if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                getLastLocation(callback);
            }
            else{
                Toast.makeText(context, "Location permission denied", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void getLastLocation(LocationCallback callback){
        if(ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION)==PackageManager.PERMISSION_GRANTED){
            fusedLocationProviderClient.getLastLocation().addOnCompleteListener(new OnCompleteListener<Location>() {
                @Override
                public void onComplete(@NonNull Task<Location> task) {
                    if (task.isSuccessful() && task.getResult() != null){
                        Location location = task.getResult();
                        double latitude = location.getLatitude();
                        double longitude = location.getLongitude();
                        callback.onLocationResult(latitude, longitude);
                    }
                }
            });
        }
    }
}
