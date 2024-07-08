package com.tool.greeting_tool.ui.home;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.google.ar.core.ArCoreApk;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import com.ibm.cloud.sdk.core.security.IamAuthenticator;
import com.ibm.watson.text_to_speech.v1.TextToSpeech;
import com.ibm.watson.text_to_speech.v1.model.SynthesizeOptions;
import com.ibm.watson.text_to_speech.v1.util.WaveUtils;
import com.tool.greeting_tool.WordsSelect;
import com.tool.greeting_tool.common.constant.ErrorMessage;
import com.tool.greeting_tool.common.constant.KeySet;
import com.tool.greeting_tool.common.constant.TAGConstant;
import com.tool.greeting_tool.common.constant.URLConstant;
import com.tool.greeting_tool.common.utils.SharedPreferencesUtil;
import com.tool.greeting_tool.databinding.FragmentHomeBinding;
import com.tool.greeting_tool.pojo.dto.GreetingCard;
import com.tool.greeting_tool.pojo.vo.CardDisplayVO;
import com.tool.greeting_tool.server.LocationHelper;
import com.tool.greeting_tool.server.TextToSpeechHelper;

import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;

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

    private TextToSpeechHelper textToSpeechHelper;

    private static final int REQUEST_CODE_SELECT_1 = 1;
    private static final int REQUEST_CODE_SELECT_2 = 2;

    private static final int REQUEST_WRITE_STORAGE = 112;

    private ArrayList<CardDisplayVO> nearbyGreetingCards = new ArrayList<>();

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        HomeViewModel homeViewModel =
                new ViewModelProvider(this).get(HomeViewModel.class);

        binding = FragmentHomeBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        locationHelper = new LocationHelper(requireContext());

        String postcode_notification = SharedPreferencesUtil.getNotificationMessage(requireContext());
        int messageCount = SharedPreferencesUtil.getMessageCount(requireContext());
        textToSpeechHelper = new TextToSpeechHelper(requireContext());

        if (postcode_notification != null) {
            boolean hasPermission = (ContextCompat.checkSelfPermission(getContext(),
                    Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED);
            if (!hasPermission) {
                System.out.println("no Permission");
                ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, REQUEST_WRITE_STORAGE);
            } else {
                if(SharedPreferencesUtil.isNotificationPosted(getContext())){
                    System.out.println("Goto tts");
                    SharedPreferencesUtil.clearNotificationPostedFlag(getContext());
                    String text = "You have " + messageCount + " Message in " + postcode_notification;
                    textToSpeechHelper.startSynthesizeThread(text);
                    //playAudio();
                }else if(postcode_notification.isEmpty()){
                    System.out.println("postcode is empty");
                }else{
                    System.out.println("Not from Notification");
                }
            }
        }else{
            System.out.println("Didn't get argument");
        }

        ImageButton wordButton = binding.button;
        ImageButton nearByMessage = binding.button2;
        ImageButton sendButton = binding.button3;

        //Button Listener for Preview
        wordButton.setOnClickListener(v->{
            Intent intent = new Intent(getActivity(), WordsSelect.class);
            intent.putExtra(KeySet.SelectedType, "Words");
            intent.putExtra(KeySet.Request, REQUEST_CODE_SELECT_1);
            startActivityForResult(intent, 1);
        });

        //Button Listener for Send
        sendButton.setOnClickListener(v->{
                    Intent intent = new Intent(getActivity(), WordsSelect.class);
                    intent.putExtra(KeySet.SelectedType, "Words");
                    intent.putExtra(KeySet.Request, REQUEST_CODE_SELECT_2);
                    startActivityForResult(intent, 2);
                });

        nearByMessage.setOnClickListener(v->{
            showNearbyMessageWithAR();
           /* locationHelper.getLocation(this, postcode -> {
                if(postcode == null || postcode.isEmpty()){
                    Toast.makeText(getActivity(), ErrorMessage.POSTCODE_NOT_FOUND, Toast.LENGTH_SHORT).show();
                }else{
                    Toast.makeText(getActivity(), postcode, Toast.LENGTH_SHORT).show();
                    getNearbyGreetingCards(postcode);
                    //TODO:open the camera and load the ar model.
                    showNearbyMessageWithAR();
                }

            });*/

        });

        final TextView textView = binding.textHome;
        homeViewModel.getText().observe(getViewLifecycleOwner(), textView::setText);
        return root;
    }

    private void showNearbyMessageWithAR() {
        // Check if AR features are supported
        if (ArCoreApk.getInstance().checkAvailability(getContext()) == ArCoreApk.Availability.SUPPORTED_INSTALLED) {
            // Intent to launch AR Activity
            Intent intent = new Intent(getActivity(), ArActivity.class);
            updateArMessageList();
            intent.putExtra("greetingCards", nearbyGreetingCards);
            startActivity(intent);
        } else {
            Toast.makeText(getContext(), "AR features not supported on this device", Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE_SELECT_1&&data!=null) {
            ArrayList<String> selectedItems = data.getStringArrayListExtra(KeySet.SelectedList);
            //TODO
            //pick user selected items and pass into AR
            showNearbyMessageWithAR();
            //ArrayList<Integer> selectedItems = data.getIntegerArrayListExtra(KeySet.SelectedList);
            //String postcode = data.getStringExtra(KeySet.PostKey);
            //ArrayList<String> SelectedItems = data.getStringArrayListExtra(KeySet.SelectedList);
            //System.out.println(postcode);
            //System.out.println(SelectedItems);
        } else if (requestCode == REQUEST_CODE_SELECT_2&&data!=null) {
            String postcode = data.getStringExtra(KeySet.PostKey);
            ArrayList<String> SelectedItems = data.getStringArrayListExtra(KeySet.SelectedList);
            sendGreetingCard(SelectedItems,postcode);
        }
    }



    /*private void playAudio() {
        if (audioPath != null) {
            mediaPlayer = new MediaPlayer();
            try {
                mediaPlayer.setDataSource(audioPath);
                mediaPlayer.prepare();
                mediaPlayer.start();
                Toast.makeText(getContext(), "Playing Audio", Toast.LENGTH_SHORT).show();

                mediaPlayer.setOnCompletionListener(mp -> {
                    mp.release();
                    mediaPlayer = null;
                });
            } catch (IOException e) {
                e.printStackTrace();
                Toast.makeText(getContext(), "Error playing audio", Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(getContext(), "Audio file path is null", Toast.LENGTH_SHORT).show();
        }
    }

    private void startSynthesizeThread(final String text) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                synthesizeTextToSpeech(text);
            }
        }).start();
    }

    private void synthesizeTextToSpeech(String text) {
        IamAuthenticator authenticator = new IamAuthenticator("5QNOe6RBy9UBI_4kHQ8iTisGsrIAusAxQfIMydNq8O63");
        TextToSpeech textToSpeech = new TextToSpeech(authenticator);
        textToSpeech.setServiceUrl("https://api.eu-gb.text-to-speech.watson.cloud.ibm.com/instances/1766a68d-26f1-439c-a612-370c829aeb42");

        try {
            SynthesizeOptions synthesizeOptions =
                    new SynthesizeOptions.Builder()
                            .text(text)
                            .accept("audio/mp3")
                            .voice("en-US_AllisonV3Voice")
                            .build();

            InputStream inputStream = textToSpeech.synthesize(synthesizeOptions).execute().getResult();
            InputStream in = WaveUtils.reWriteWaveHeader(inputStream);

            File externalFilesDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_MUSIC);
            if (externalFilesDir != null) {
                File outputFile = new File(externalFilesDir, "hello_world_test.mp3");
                System.out.println("find it");

                try (OutputStream out = new FileOutputStream(outputFile)) {
                    byte[] buffer = new byte[1024];
                    int length;
                    while ((length = in.read(buffer)) > 0) {
                        out.write(buffer, 0, length);
                    }

                    Log.i("MainActivity", "Audio file saved at: " + outputFile.getAbsolutePath());
                    audioPath = outputFile.getAbsolutePath();
                    System.out.println("find it" + outputFile.getAbsolutePath());

                    if (isAdded() && getActivity() != null) {
                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                playAudio();
                            }
                        });
                    }
                }
                in.close();
                inputStream.close();
            }else{
                System.out.println("Fail to find dir");
            }
        } catch (IOException e) {
            e.printStackTrace();
            Log.e("MainActivity", "Failed to save audio file: " + e.getMessage());
        }
    }*/

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
        System.out.println(currentPostcode);
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
        for (GreetingCard greetingCard : greetingCards) {
            CardDisplayVO card = new CardDisplayVO(greetingCard.getCardId(),
                    greetingCard.getEmojiId(),greetingCard.getAnimationId());
            nearbyGreetingCards.add(card);
        }
    }

    /**
     * Just for test
     */
    private void updateArMessageList() {

        CardDisplayVO card1 = new CardDisplayVO("getwellsoon", "heart", "staranimation");
        CardDisplayVO card2 = new CardDisplayVO("happynewyear","tongue","staranimation");
        CardDisplayVO card3 = new CardDisplayVO("haveaniceday","lovesmile","staranimation");
        nearbyGreetingCards.add(card1);
        nearbyGreetingCards.add(card2);
        nearbyGreetingCards.add(card3);
    }

    @Override
    public void onPause() {
        textToSpeechHelper.stopAudio();
        super.onPause();
    }

    @Override
    public void onDestroyView() {
        textToSpeechHelper.stopAudio();
        super.onDestroyView();
        binding = null;
    }

}