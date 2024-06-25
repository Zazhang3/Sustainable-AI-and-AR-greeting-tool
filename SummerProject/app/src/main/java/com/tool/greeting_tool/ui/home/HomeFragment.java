package com.tool.greeting_tool.ui.home;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
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

import com.tool.greeting_tool.MainActivity;
import com.tool.greeting_tool.Postcode_fill;
import com.tool.greeting_tool.WordsSelect;
import com.tool.greeting_tool.common.constant.ErrorMessage;
import com.tool.greeting_tool.common.constant.KeySet;
import com.tool.greeting_tool.databinding.FragmentHomeBinding;
import com.tool.greeting_tool.server.GeocodingServer;
import com.tool.greeting_tool.server.LocationHelper;

import java.security.Key;
import java.util.ArrayList;
import java.util.List;

public class HomeFragment extends Fragment {

    private FragmentHomeBinding binding;
    private LocationHelper locationHelper;
    private GeocodingServer geocodingServer;

    private double backLatitude;
    private double backLongitude;

    private String selectType;
    private static final int REQUEST_CODE_SELECT_1 = 1;
    private static final int REQUEST_CODE_SELECT_2 = 2;

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
            startActivityForResult(intent, REQUEST_CODE_SELECT_1);
        });

        //Button Listener for Send
        sendButton.setOnClickListener(v->{
                    Intent intent = new Intent(getActivity(), WordsSelect.class);
                    intent.putExtra(KeySet.SelectedType, "Words");
                    intent.putExtra(KeySet.Request, REQUEST_CODE_SELECT_2);
                    startActivityForResult(intent, REQUEST_CODE_SELECT_2);
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
            //TODO
            //get nearbymessage
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
            System.out.println(latitude);
            return geocodingServer.getAddress(latitude, longitude);
        }

        @Override
        protected void onPostExecute(String postcode) {
            super.onPostExecute(postcode);
            if(postcode!=null){
                //TODO
                //the callback postcode is the current location postcode
                Toast.makeText(requireContext(), postcode, Toast.LENGTH_SHORT).show();
            }else{
                Toast.makeText(requireContext(), ErrorMessage.POSTCODE_NOT_FOUND, Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE_SELECT_1) {
            System.out.println();
        } else if (requestCode == REQUEST_CODE_SELECT_2) {
            //TODO
            //These are the items need for back_end
            String postCode = data.getStringExtra(KeySet.PostKey);
            ArrayList<String> SelectedItems = data.getStringArrayListExtra(KeySet.SelectedList);
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

}