package com.tool.greeting_tool.ui.user;

import android.app.AlertDialog;
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

import com.tool.greeting_tool.MainActivity;
import com.tool.greeting_tool.common.constant.ErrorMessage;
import com.tool.greeting_tool.common.constant.URLConstant;
import com.tool.greeting_tool.common.utils.SharedPreferencesUtil;
import com.tool.greeting_tool.databinding.FragmentUserBinding;
import com.tool.greeting_tool.server.StartPage;
import com.tool.greeting_tool.ui.user.History.HistoryDialogFragment;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class UserFragment extends Fragment {

    private FragmentUserBinding binding;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        UserViewModel userViewModel =
                new ViewModelProvider(requireActivity()).get(UserViewModel.class);

        binding = FragmentUserBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        final TextView textView = binding.textUser;

        String username = SharedPreferencesUtil.getUsername(requireContext());
        System.out.println(username);
        userViewModel.setText(username);

        userViewModel.getText().observe(getViewLifecycleOwner(), textView::setText);

        ImageButton history = binding.history;
        history.setOnClickListener(v->{
            HistoryDialogFragment historyDialog = HistoryDialogFragment.newInstance();
            historyDialog.show(getParentFragmentManager(), "historyDialog");
        });

        ImageButton accountCancel = binding.cancelButton;
        accountCancel.setOnClickListener(v-> showActionConfirm(true));

        ImageButton logoutButton = binding.actionLogout;
        logoutButton.setOnClickListener(v-> showActionConfirm(false));

        return root;
    }

    private void showActionConfirm(boolean isCancel){
        if(isCancel){
            new AlertDialog.Builder(requireContext())
                    .setTitle("Confirm Cancel Account")
                    .setMessage("Are you sure you want to cancel your account?")
                    .setPositiveButton(android.R.string.yes, (dialog, which) -> {
                        // Perform the action to cancel the account
                        cancelAccount();
                    })
                    .setNegativeButton(android.R.string.no, null)
                    .show();
        }else{
            new AlertDialog.Builder(requireContext())
                    .setTitle("Confirm Logout")
                    .setMessage("Are you sure you want to Logout?")
                    .setPositiveButton(android.R.string.yes, (dialog, which) -> {
                        SharedPreferencesUtil.clearSharedPreferences(requireContext());

                        ((MainActivity) requireActivity()).cancelWork();
                        Intent intent = new Intent(getActivity(), StartPage.class);
                        startActivity(intent);

                        requireActivity().finish();
                    })
                    .setNegativeButton(android.R.string.no, null)
                    .show();
        }
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
        long userId = SharedPreferencesUtil.getLong(requireContext());
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
            public void onResponse(@NonNull Call call, @NonNull Response response) {
                if (response.isSuccessful()) {
                    // cancel success
                    requireActivity().runOnUiThread(() -> {
                        Toast.makeText(requireContext(), "Cancel account successfully", Toast.LENGTH_SHORT).show();
                        ((MainActivity) requireActivity()).cancelWork();
                        Intent intent = new Intent(getActivity(), StartPage.class);
                        startActivity(intent);
                        requireActivity().finish();
                    });
                } else {
                    //fail to cancel
                    requireActivity().runOnUiThread(() -> Toast.makeText(requireContext(), ErrorMessage.INVALID_RESPONSE, Toast.LENGTH_SHORT).show());
                }
            }

            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                requireActivity().runOnUiThread(() -> Toast.makeText(requireContext(), ErrorMessage.NETWORK_ERROR, Toast.LENGTH_SHORT).show());
            }
        });

    }


}