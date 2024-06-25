package com.tool.greeting_tool.server;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import java.io.IOException;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * The Class help to get address by latitude and longitude
 * Use Google API and have 100 check Limits
 * Back a Json file contains lots of address
 */

public class GeocodingServer {
    private static final String BASE_URL = "https://maps.googleapis.com/maps/api/geocode/json";
    private static final String API_KEY = "AIzaSyDIalYY9nM3RgrPWJAcolhEoeCgfvog01c";

    public String getAddress(double latitude, double longitude){
        OkHttpClient client = new OkHttpClient();
        String url = BASE_URL + "?latlng=" + latitude + "," + longitude + "&key=" + API_KEY;

        Request request = new Request.Builder()
                .url(url)
                .build();

        try(Response response = client.newCall(request).execute()){
            /*if(response.isSuccessful()){
                String ResponseBody = response.body().string();
                JsonObject object = new Gson().fromJson(ResponseBody, JsonObject.class);
                if ("OK".equals(object.get("status").getAsString())) {
                    System.out.println(object);
                    return object.getAsJsonArray("results").get(0)
                            .getAsJsonObject()
                            .get("formatted_address")
                            .getAsString();
                }
            }*/
            //TODO
            //Here is the Json file getter
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
                                    return component.getAsJsonObject().get("long_name").getAsString();
                                }
                            }
                        }
                    }
                }
            }
        }catch(IOException e){
            e.printStackTrace();
        }
        return null;
    }
}
