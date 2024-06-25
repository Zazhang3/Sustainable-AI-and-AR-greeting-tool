package com.tool.greeting_tool.ui.home;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import com.tool.greeting_tool.MainActivity;
import com.tool.greeting_tool.Postcode_fill;
import com.tool.greeting_tool.WordsSelect;
import com.tool.greeting_tool.common.constant.ErrorMessage;
import com.tool.greeting_tool.common.constant.KeySet;
import com.tool.greeting_tool.common.constant.TAGConstant;
import com.tool.greeting_tool.common.constant.URLConstant;
import com.tool.greeting_tool.common.utils.SharedPreferencesUtil;
import com.tool.greeting_tool.databinding.FragmentHomeBinding;
import com.tool.greeting_tool.pojo.dto.GreetingCard;
import com.tool.greeting_tool.server.GeocodingServer;
import com.tool.greeting_tool.server.LocationHelper;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.security.Key;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class HomeFragment extends Fragment {

    private FragmentHomeBinding binding;
    private LocationHelper locationHelper;
    private GeocodingServer geocodingServer;

    private double backLatitude;
    private double backLongitude;

    private String backPostCode;

    private String selectType;
    private static final int REQUEST_CODE_SELECT_1 = 1;
    private static final int REQUEST_CODE_SELECT_2 = 2;
    private ArrayList<GreetingCard> nearbyGreetingCards = new ArrayList<>();

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        HomeViewModel homeViewModel =
                new ViewModelProvider(this).get(HomeViewModel.class);

        binding = FragmentHomeBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        locationHelper = new LocationHelper(requireContext());
        geocodingServer = new GeocodingServer();

        ImageButton wordButton = binding.button;
        ImageButton nearByMessage = binding.button2;
        ImageButton sendButton = binding.button3;

        //Button Listener for Preview
        wordButton.setOnClickListener(v->{
            Intent intent = new Intent(getActivity(), WordsSelect.class);
            intent.putExtra(KeySet.SelectedType, "Words");
            intent.putExtra(KeySet.Request, REQUEST_CODE_SELECT_1);
            startActivityForResult(intent, 2);
        });

        //Button Listener for Send
        sendButton.setOnClickListener(v->{
                    Intent intent = new Intent(getActivity(), WordsSelect.class);
                    intent.putExtra(KeySet.SelectedType, "Words");
                    intent.putExtra(KeySet.Request, REQUEST_CODE_SELECT_2);
                    startActivityForResult(intent, 2);
                });

        nearByMessage.setOnClickListener(v->{
            locationHelper.getLocation(this, new LocationHelper.LocationCallback() {
                @Override
                public void onLocationResult(double latitude, double longitude) {
                    backLongitude = longitude;
                    backLatitude = latitude;
                    new FetchAddressTask().execute(backLatitude, backLongitude);
                }
            });
            //getNearbyGreetingCards("BS2 0BU");
        });

        final TextView textView = binding.textHome;
        homeViewModel.getText().observe(getViewLifecycleOwner(), textView::setText);
        return root;
    }

    private class FetchAddressTask extends AsyncTask<Double, Void, String>{
        @Override
        protected String doInBackground(Double... params) {
            double latitude = params[0];
            double longitude = params[1];
            return geocodingServer.getAddress(latitude, longitude);
        }

        @Override
        protected void onPostExecute(String postcode) {
            super.onPostExecute(postcode);
            if(postcode!=null){
                //TODO Now move the getNearbyGreetingCards method here
                getNearbyGreetingCards(postcode);
                //Toast.makeText(requireContext(), postcode, Toast.LENGTH_SHORT).show();
            }else{
                Toast.makeText(requireContext(), ErrorMessage.POSTCODE_NOT_FOUND, Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE_SELECT_1) {
            String postcode = data.getStringExtra(KeySet.PostKey);
            ArrayList<String> SelectedItems = data.getStringArrayListExtra(KeySet.SelectedList);
            System.out.println(postcode);
            System.out.println(SelectedItems);
        } else if (requestCode == REQUEST_CODE_SELECT_2) {
            //TODO
            //These are the items need for back_end
            String postcode = data.getStringExtra(KeySet.PostKey);
            ArrayList<String> SelectedItems = data.getStringArrayListExtra(KeySet.SelectedList);
            sendGreetingCard(SelectedItems,postcode);
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    /**
     * Help user to send card
     * @param selectionList
     * @param postcode
     */
    private void sendGreetingCard(ArrayList<String> selectionList, String postcode) {

        //Just for test, disable it while it can receive message
        /*GreetingCard greetingCard = new GreetingCard();
        greetingCard.setText("Happy Birthday!");
        greetingCard.setEmoji("Laugh");
        greetingCard.setAnimation("rotate");
        greetingCard.setPostcode("BS2 0BU");
        greetingCard.setUser_id(SharedPreferencesUtil.getLong(requireContext()));*/

        GreetingCard greetingCard = new GreetingCard();
        greetingCard.setText(selectionList.get(0));
        greetingCard.setEmoji(selectionList.get(1));
        greetingCard.setAnimation(selectionList.get(2));
        greetingCard.setPostcode(postcode);
        greetingCard.setUser_id(SharedPreferencesUtil.getLong(requireContext()));

        Gson gson = new Gson();
        String json = gson.toJson(greetingCard);
        String jwtToken = SharedPreferencesUtil.getToken(requireContext());

        RequestBody body = RequestBody.create(json,
                MediaType.get("application/json; charset=utf-8"));

        Request request = new Request.Builder()
                .url(URLConstant.SEND_CARD_URL)
                .header("Token",jwtToken)
                .post(body)
                .build();

        OkHttpClient client = new OkHttpClient();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                Log.e(TAGConstant.SEND_CARD_TAG,"send card failed",e);
                requireActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(requireContext(), "Failed to send card", Toast.LENGTH_SHORT).show();
                    }
                });
            }



            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                if (response.isSuccessful()) {
                    getActivity().runOnUiThread(() -> Toast.makeText(getActivity(), "Greeting card sent successfully", Toast.LENGTH_SHORT).show());
                } else {
                    getActivity().runOnUiThread(() -> Toast.makeText(getActivity(), "Failed to send greeting card", Toast.LENGTH_SHORT).show());
                }
            }

        });


    }

    /**
     * User get nearby greeting card via their current postcode
     * @param currentPostcode
     */
    private void getNearbyGreetingCards(String currentPostcode) {
        String jwtToken = SharedPreferencesUtil.getToken(requireContext());

        // Create empty JSON body
        Gson gson = new Gson();
        JsonObject jsonObject = new JsonObject();
        String json = gson.toJson(jsonObject);

        RequestBody body = RequestBody.create(json, MediaType.get("application/json; charset=utf-8"));

        Request request = new Request.Builder()
                .url(URLConstant.GET_NEARBY_CARD_URL + "/" + currentPostcode)
                .header("Token", jwtToken)
                .post(body)
                .build();

        //send request
        OkHttpClient client = new OkHttpClient();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                final String responseBody = response.body().string();
                Log.d(TAGConstant.NEARBY_CARD_TAG,"Response: "+ responseBody);
                getActivity().runOnUiThread(() -> {
                    try {
                        JsonObject jsonResponse = new Gson().fromJson(responseBody, JsonObject.class);
                        int code = jsonResponse.get("code").getAsInt();
                        if (code == 1) {
                            ArrayList<GreetingCard> greetingCards = new Gson().fromJson(
                                    jsonResponse.get("data").getAsJsonArray(),
                                    new TypeToken<ArrayList<GreetingCard>>(){}.getType()
                            );
                            updateMessageList(greetingCards);
                        } else {
                            String msg = jsonResponse.get("msg").getAsString();
                            Toast.makeText(requireContext(), "Error: " + msg, Toast.LENGTH_SHORT).show();
                        }
                    } catch (Exception e) {
                        Log.e(TAGConstant.NEARBY_CARD_TAG, "Exception while parsing response", e);
                        Toast.makeText(requireContext(), "Invalid response", Toast.LENGTH_SHORT).show();
                    }
                });



            }

            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                Log.e(TAGConstant.NEARBY_CARD_TAG,"Get card failed",e);
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(requireContext(),"Get nearby card failed " + ErrorMessage.NETWORK_ERROR,Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });

    }

    /**
     * Represent nearby message
     * @param greetingCards
     */
    private void updateMessageList(ArrayList<GreetingCard> greetingCards) {
        nearbyGreetingCards.clear();
        int id = 0;
        for (GreetingCard greetingCard : greetingCards) {
            nearbyGreetingCards.add(greetingCard);
        }
    }


}