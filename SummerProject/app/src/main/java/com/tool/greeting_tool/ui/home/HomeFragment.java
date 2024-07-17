package com.tool.greeting_tool.ui.home;

import static android.app.Activity.RESULT_OK;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.viewpager2.widget.ViewPager2;

import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;
import com.google.ar.core.ArCoreApk;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import com.tbuonomo.viewpagerdotsindicator.DotsIndicator;
import com.tbuonomo.viewpagerdotsindicator.SpringDotsIndicator;
import com.tool.greeting_tool.Postcode_fill;
import com.tool.greeting_tool.R;
import com.tool.greeting_tool.WordsSelect;
import com.tool.greeting_tool.common.constant.ErrorMessage;
import com.tool.greeting_tool.common.constant.KeySet;
import com.tool.greeting_tool.common.constant.TAGConstant;
import com.tool.greeting_tool.common.constant.URLConstant;
import com.tool.greeting_tool.common.utils.SharedPreferencesUtil;
import com.tool.greeting_tool.databinding.FragmentHomeBinding;
import com.tool.greeting_tool.pojo.dto.GreetingCard;
import com.tool.greeting_tool.pojo.vo.CardDisplayVO;
import com.tool.greeting_tool.server.AudioPlayer;
import com.tool.greeting_tool.server.LocationHelper;
import com.tool.greeting_tool.server.UserHelpAdapter;

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

/** @noinspection ALL*/
public class HomeFragment extends Fragment {

    private FragmentHomeBinding binding;
    private LocationHelper locationHelper;
    private AudioPlayer audioPlayer;
    private Dialog dialog;

    private final ArrayList<CardDisplayVO> nearbyGreetingCards = new ArrayList<>();

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        HomeViewModel homeViewModel =
                new ViewModelProvider(this).get(HomeViewModel.class);

        binding = FragmentHomeBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        locationHelper = new LocationHelper(requireContext());

        audioPlayer = new AudioPlayer(requireContext());
        String audioPath = SharedPreferencesUtil.getAudioPath(requireContext());
        audioPlayer.setAudioPath(audioPath);

        if (audioPath != null) {
            if(SharedPreferencesUtil.isNotificationPosted(requireContext())){
                System.out.println("Goto tts");
                SharedPreferencesUtil.clearNotificationPostedFlag(getContext());
                audioPlayer.playAudio();
            }else if(audioPath.isEmpty()){
                System.out.println("audioPath is empty");
            }else{
                System.out.println("Not from Notification");
            }
        }else{
            System.out.println("Didn't get argument");
        }

        ImageButton wordButton = binding.button;
        ImageButton nearByMessage = binding.button2;
        ImageButton helpButton = binding.helpButton;

        //Button Listener for Preview
        wordButton.setOnClickListener(v->{
            Intent intent = new Intent(getActivity(), WordsSelect.class);
            intent.putExtra(KeySet.SelectedType, "Words");
            //intent.putExtra(KeySet.Request, REQUEST_CODE_SELECT_1);
            startActivityForResult(intent, 1);
        });


        //Button Listener for Nearby message
        nearByMessage.setOnClickListener(v->{
            //showNearbyMessageWithAR();
            locationHelper.getLocation(this);
            locationHelper.getLastLocation(this::getNearbyGreetingCards);

        });

        helpButton.setOnClickListener(v-> showImageDialog());

        final TextView textView = binding.textHome;
        homeViewModel.getText().observe(getViewLifecycleOwner(), textView::setText);
        return root;
    }

    private void showNearbyMessageWithAR() {
        // Check if AR features are supported
        if (ArCoreApk.getInstance().checkAvailability(getContext()) == ArCoreApk.Availability.SUPPORTED_INSTALLED) {
            // Intent to launch AR Activity
            Intent intent = new Intent(getActivity(), ArActivity.class);
            intent.putExtra("greetingCards", nearbyGreetingCards);

            startActivity(intent);

        } else {
            Toast.makeText(getContext(), "AR features not supported on this device", Toast.LENGTH_LONG).show();
        }
    }

    private void showNoMessageDialog(){
        //TODO
        //update format
        AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
        builder.setMessage("There are no message near you !")
                .setPositiveButton("OK", (dialog, which) -> dialog.dismiss())
                .create()
                .show();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == RESULT_OK && data != null){
            int sendState = data.getIntExtra(KeySet.IsSend, -1);
            if(sendState == 1){
                Intent intent = new Intent(getActivity(), Postcode_fill.class);
                ArrayList<String> selectedItems = data.getStringArrayListExtra(KeySet.SelectedList);
                intent.putExtra(KeySet.SelectedList, selectedItems);
                startActivityForResult(intent, 1);
            }else if(sendState == 0){
                ArrayList<String> selectedItems = data.getStringArrayListExtra(KeySet.SelectedList);
                assert selectedItems != null;
                CardDisplayVO greetingCard = new CardDisplayVO(selectedItems.get(0),selectedItems.get(1), selectedItems.get(2));
                ArrayList<CardDisplayVO> previewCard = new ArrayList<>();
                previewCard.add(greetingCard);
                Intent intent = new Intent(getActivity(), ArActivity.class);
                intent.putExtra("greetingCards", previewCard);
                startActivity(intent);
            }else if(sendState == 2){
                String postcode = data.getStringExtra(KeySet.PostKey);
                ArrayList<String> SelectedItems = data.getStringArrayListExtra(KeySet.SelectedList);
                if(SelectedItems != null){
                    sendGreetingCard(SelectedItems,postcode);
                }
            }
        }
    }

    private void showImageDialog() {
        dialog = new Dialog(requireContext());
        dialog.setContentView(R.layout.dialog_userhelp);

        ViewPager2 viewPager = dialog.findViewById(R.id.viewPager);
        SpringDotsIndicator springDotsIndicator = dialog.findViewById(R.id.spring_dots_indicator);

        UserHelpAdapter userHelpAdapter = new UserHelpAdapter(requireContext());
        viewPager.setAdapter(userHelpAdapter);

        springDotsIndicator.setViewPager2(viewPager);

        dialog.show();
    }


    /**
     * Help user to send card
     * @param selectionList : the selection list
     * @param postcode : the postcode that will send to
     */
    private void sendGreetingCard(ArrayList<String> selectionList, String postcode) {
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
                requireActivity().runOnUiThread(() -> Toast.makeText(requireContext(), "Failed to send card", Toast.LENGTH_SHORT).show());
            }



            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) {
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
     * @param currentPostcode : the current postcode that user at
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
                getActivity().runOnUiThread(() -> Toast.makeText(requireContext(),"Get nearby card failed " + ErrorMessage.NETWORK_ERROR,Toast.LENGTH_SHORT).show());
            }
        });

    }

    /**
     * Represent nearby message
     * @param greetingCards : the greeting card list that user send
     */
    private void updateMessageList(ArrayList<GreetingCard> greetingCards) {
        nearbyGreetingCards.clear();
        for (GreetingCard greetingCard : greetingCards) {
            CardDisplayVO card = new CardDisplayVO(greetingCard.getCardId(),
                    greetingCard.getEmojiId(),greetingCard.getAnimationId());
            nearbyGreetingCards.add(card);
        }
        if(nearbyGreetingCards.isEmpty()){
            showNoMessageDialog();
        }else{
            showNearbyMessageWithAR();
        }
    }

    @Override
    public void onPause() {
        //textToSpeechHelper.stopAudio();
        audioPlayer.stopAudio();
        super.onPause();
    }

    @Override
    public void onDestroyView() {
        //textToSpeechHelper.stopAudio();
        audioPlayer.stopAudio();
        if (dialog != null && dialog.isShowing()) {
            dialog.dismiss();
        }
        super.onDestroyView();
        binding = null;
    }

}