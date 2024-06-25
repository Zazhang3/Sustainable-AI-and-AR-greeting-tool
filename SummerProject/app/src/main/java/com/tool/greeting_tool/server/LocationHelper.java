package com.tool.greeting_tool.server;

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

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import java.io.IOException;
import java.util.List;
import java.util.Locale;


public class LocationHelper {
    FusedLocationProviderClient fusedLocationProviderClient;
    Context context;

    public interface LocationCallback{
        void onLocationResult(String postcode);
    }

    public LocationHelper(Context context){
        this.context = context;
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(context);
    }

    public void getLocationPermission(Activity activity){
        if(ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION)!=PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(activity, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
        }
    }

    public void getLastLocation(LocationCallback callback){
        if(ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION)==PackageManager.PERMISSION_GRANTED){
            fusedLocationProviderClient.getLastLocation().addOnCompleteListener(new OnCompleteListener<Location>() {
                @Override
                public void onComplete(@NonNull Task<Location> task) {
                    if (task.isSuccessful() && task.getResult() != null){
                        Location location = task.getResult();
                        double latitude = location.getLatitude();
                        double longitude = location.getLongitude();
                        getPostCode(callback, latitude, longitude);
                    }
                }
            });
        }
    }

    private void getPostCode(LocationCallback callback, double latitude, double longitude){
        Geocoder geocoder = new Geocoder(context, Locale.UK);
        try{
            List<Address> addressList = geocoder.getFromLocation(latitude, longitude, 1);
            if(addressList != null && !addressList.isEmpty()){
                Address address = addressList.get(0);
                String postCode = address.getPostalCode();
                callback.onLocationResult(postCode);
            }else{
                Toast.makeText(context, "Unable to get PostCode", Toast.LENGTH_SHORT).show();
            }
        }catch(IOException e){
            e.printStackTrace();
        }
    }

}
