package com.tool.greeting_tool.ui.user.History;

import android.app.AlertDialog;
import android.os.Bundle;
import android.util.Log;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import com.tool.greeting_tool.R;
import com.tool.greeting_tool.common.constant.ErrorMessage;
import com.tool.greeting_tool.common.constant.MessageConstant;
import com.tool.greeting_tool.common.constant.TAGConstant;
import com.tool.greeting_tool.common.constant.URLConstant;
import com.tool.greeting_tool.common.utils.SharedPreferencesUtil;
import com.tool.greeting_tool.pojo.dto.GreetingCard;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class HistoryActivity extends AppCompatActivity {
    private MessageAdapter messageAdapter;
    private List<History_Message> MessageList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);

        Long currentUserId = SharedPreferencesUtil.getLong(this);
        ListView listView = findViewById(R.id.history);

        MessageList = new ArrayList<>();

        messageAdapter = new MessageAdapter(this, MessageList);
        listView.setAdapter(messageAdapter);

        listView.setOnItemClickListener((parent, view, position, id) -> {
            History_Message message = messageAdapter.getItem(position);
            if (message != null) {
                showDeleteDialog(message);
            }
        });

        getGreetingCards(currentUserId);
    }

    private void showDeleteDialog(History_Message message) {
        new AlertDialog.Builder(this)
                .setTitle(MessageConstant.Delete)
                .setMessage(MessageConstant.DeleteMessage)
                .setPositiveButton(android.R.string.yes, (dialog, which) -> {
                    MessageList.remove(message);
                    messageAdapter.notifyDataSetChanged();
                    Toast.makeText(HistoryActivity.this, MessageConstant.Delete, Toast.LENGTH_SHORT).show();
                })
                .setNegativeButton(android.R.string.no, null)
                .show();
    }

    /**
     * Use to help user to check their history sent card
     * @param userId
     */

    private void getGreetingCards(Long userId) {
        String jwtToken = SharedPreferencesUtil.getToken(this);

        // Create empty JSON body
        Gson gson = new Gson();
        JsonObject jsonObject = new JsonObject();
        String json = gson.toJson(jsonObject);

        RequestBody body = RequestBody.create(json, MediaType.get("application/json; charset=utf-8"));

        Request request = new Request.Builder()
                .url(URLConstant.GET_HISTORY_CARD_URL + "/" + userId)
                .header("Token", jwtToken)
                .post(body)
                .build();

        //send request
        OkHttpClient client = new OkHttpClient();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                final String responseBody = response.body().string();
                Log.d(TAGConstant.HISTORY_CARD_TAG,"Response: "+ responseBody);
                runOnUiThread(() -> {
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
                            Toast.makeText(HistoryActivity.this, "Error: " + msg, Toast.LENGTH_SHORT).show();
                        }
                    } catch (Exception e) {
                        Log.e(TAGConstant.HISTORY_CARD_TAG, "Exception while parsing response", e);
                        Toast.makeText(HistoryActivity.this, "Invalid response", Toast.LENGTH_SHORT).show();
                    }
                });



            }

            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                Log.e(TAGConstant.HISTORY_CARD_TAG,"Get card failed",e);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(HistoryActivity.this,"Get history card failed " + ErrorMessage.NETWORK_ERROR,Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });

    }
    private void updateMessageList(ArrayList<GreetingCard> greetingCards) {
        MessageList.clear();
        int id = 0;
        for (GreetingCard card : greetingCards) {
            MessageList.add(new History_Message(id++, card.getCardId()));
        }
        messageAdapter.notifyDataSetChanged();
    }

}