package com.tool.greeting_tool.ui.user.History;

import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

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
import java.util.Objects;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * The new version of history
 * show a dialog within user frgment
 */
public class HistoryDialogFragment extends DialogFragment {

    private MessageAdapter messageAdapter;
    private ProgressBar progressBar;
    private List<History_Message> MessageList;
    private Long currentUserId;

    public static HistoryDialogFragment newInstance() {
        return new HistoryDialogFragment();
    }

    /**
     *
     * @param savedInstanceState The last saved instance state of the Fragment,
     * or null if this is a freshly created Fragment.
     *
     * @return
     */
    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        LayoutInflater inflater = requireActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.dialog_history, null);

        currentUserId = SharedPreferencesUtil.getLong(requireContext());
        ListView listView = view.findViewById(R.id.history);
        progressBar = view.findViewById(R.id.progressBar_history);
        MessageList = new ArrayList<>();

        messageAdapter = new MessageAdapter(getActivity(), MessageList);
        listView.setAdapter(messageAdapter);

        listView.setOnItemClickListener((parent, view1, position, id) -> {
            History_Message message = messageAdapter.getItem(position);
            if (message != null) {
                Long deletedId = message.getCardId();
                showDeleteDialog(message);
                deleteMessage(deletedId);
            }
        });

        getGreetingCards(currentUserId);

        builder.setView(view)
                .setTitle("Message History")
                .setNegativeButton("Close", (dialog, id) -> dialog.dismiss());

        return builder.create();
    }

    /**
     *
     * @param message
     */
    private void showDeleteDialog(History_Message message) {
        new AlertDialog.Builder(getActivity())
                .setTitle(MessageConstant.Delete)
                .setMessage(MessageConstant.DeleteMessage)
                .setPositiveButton(android.R.string.yes, (dialog, which) -> {
                    MessageList.remove(message);
                    messageAdapter.notifyDataSetChanged();
                    Toast.makeText(getActivity(), MessageConstant.Delete, Toast.LENGTH_SHORT).show();
                })
                .setNegativeButton(android.R.string.no, null)
                .show();
    }

    /**
     * Use to help user to check their history sent card
     * @param userId
     */

    private void getGreetingCards(Long userId) {
        progressBar.setVisibility(View.VISIBLE);
        String jwtToken = SharedPreferencesUtil.getToken(getActivity());

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
                            Toast.makeText(getActivity(), "Error: " + msg, Toast.LENGTH_SHORT).show();
                        }
                    } catch (Exception e) {
                        Log.e(TAGConstant.HISTORY_CARD_TAG, "Exception while parsing response", e);
                        Toast.makeText(getActivity(), "Invalid response", Toast.LENGTH_SHORT).show();
                    }finally{
                        progressBar.setVisibility(View.GONE);
                    }
                });



            }

            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                Log.e(TAGConstant.HISTORY_CARD_TAG,"Get card failed",e);
                if (isAdded() && getActivity() != null) {
                    getActivity().runOnUiThread(() -> {
                        progressBar.setVisibility(View.GONE);
                        Toast.makeText(getActivity(), "Get history card failed " + ErrorMessage.NETWORK_ERROR, Toast.LENGTH_SHORT).show();
                    });
                }
            }
        });

    }
    private void updateMessageList(ArrayList<GreetingCard> greetingCards) {
        MessageList.clear();
        int id = 0;
        for (GreetingCard card : greetingCards) {
            MessageList.add(new History_Message(id++, card.getCardId(), card.getId()));
        }
        progressBar.setVisibility(View.GONE);
        messageAdapter.notifyDataSetChanged();
    }

    /**
     * user delete a greeting card
     * @param deletedId
     */
    private void deleteMessage(Long deletedId) {
        String jwtToken = SharedPreferencesUtil.getToken(getActivity());

        Request request = new Request.Builder()
                .url(URLConstant.GET_DELETE_CARD_URL + "/" + deletedId)
                .header("Token", jwtToken)
                .delete()
                .build();

        //send request
        OkHttpClient client = new OkHttpClient();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                if (response.isSuccessful()) {
                    // cancel success
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(getActivity(), "Delete card successfully", Toast.LENGTH_SHORT).show();
                        }
                    });
                } else {
                    //fail to cancel
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(getActivity(), ErrorMessage.INVALID_RESPONSE, Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }

            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(getActivity(), ErrorMessage.NETWORK_ERROR, Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });


    }
}
