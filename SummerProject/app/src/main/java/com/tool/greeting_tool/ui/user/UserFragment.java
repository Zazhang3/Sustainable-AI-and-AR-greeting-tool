package com.tool.greeting_tool.ui.user;

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
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.tool.greeting_tool.common.constant.ErrorMessage;
import com.tool.greeting_tool.common.utils.SharedPreferencesUtil;
import com.tool.greeting_tool.common.constant.URLConstant;
import com.tool.greeting_tool.databinding.FragmentUserBinding;
import com.tool.greeting_tool.server.MailSender;
import com.tool.greeting_tool.server.StartPage;
import com.tool.greeting_tool.ui.user.History.HistoryActivity;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class UserFragment extends Fragment {

    private FragmentUserBinding binding;
    private ImageButton History;
    private ImageButton AccountCancel;
    private ImageButton LogoutButton;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        UserViewModel userViewModel =
                new ViewModelProvider(requireActivity()).get(UserViewModel.class);

        binding = FragmentUserBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        final TextView textView = binding.textUser;

        userViewModel.getText().observe(getViewLifecycleOwner(), textView::setText);

        History = binding.history;
        History.setOnClickListener(v->{
            Intent intent = new Intent(getActivity(), HistoryActivity.class);
            startActivity(intent);
        });

        AccountCancel = binding.cancelButton;
        AccountCancel.setOnClickListener(v->{

            cancelAccount();

            //TODO
            Toast.makeText(getActivity(), "Click cancel", Toast.LENGTH_SHORT).show();
        });

        LogoutButton = binding.actionLogout;
        LogoutButton.setOnClickListener(v->{

            //clear user data
            SharedPreferencesUtil.clearSharedPreferences(requireContext());

            Intent intent = new Intent(getActivity(), StartPage.class);
            startActivity(intent);

            requireActivity().finish();
        });

        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    /**
     * cancel user account by userId
     */
    public void cancelAccount(){

        //get user id and then clear local user data
        Long userId = SharedPreferencesUtil.getLong(requireContext());
        String jwtToken = SharedPreferencesUtil.getToken(requireContext());
        SharedPreferencesUtil.clearSharedPreferences(requireContext());

        //set Requestbody
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url(URLConstant.BASIC_USER_URL+"/"+userId)
                .header("Token",jwtToken)
                .delete()
                .build();

        Log.d("RequestDebug", "Authorization Header: " + jwtToken);

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                if (response.isSuccessful()) {
                    // cancel success
                    requireActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(requireContext(), "Cancel account successfully", Toast.LENGTH_SHORT).show();
                        }
                    });
                } else {
                    //fail to cancel
                    requireActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(requireContext(), ErrorMessage.INVALID_RESPONSE, Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }

            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                requireActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(requireContext(), ErrorMessage.NETWORK_ERROR, Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });

    }


}