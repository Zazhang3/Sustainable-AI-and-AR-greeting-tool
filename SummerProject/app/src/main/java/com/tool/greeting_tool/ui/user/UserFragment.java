package com.tool.greeting_tool.ui.user;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.google.android.material.switchmaterial.SwitchMaterial;
import com.tool.greeting_tool.MainActivity;
import com.tool.greeting_tool.R;
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

        ImageButton notificationButton = binding.notificationIcon;
        notificationButton.setOnClickListener(v-> showNotificationDialog());

        return root;
    }

    private void showNotificationDialog(){
        Dialog dialog = new Dialog(requireContext());
        dialog.setContentView(R.layout.dialog_notification_switch);

        SwitchMaterial switchMaterial = dialog.findViewById(R.id.switchNotification);
        boolean isTick = SharedPreferencesUtil.getNotificationSender(requireContext());
        switchMaterial.setChecked(isTick);

        switchMaterial.setOnCheckedChangeListener(((buttonView, isChecked) -> {
            //((MainActivity) requireActivity()).cancelWork();
            SharedPreferencesUtil.clearNotificationPostedFlag(requireContext());
            SharedPreferencesUtil.setFirstSkip(requireContext(), false);
            SharedPreferencesUtil.setNotificationSender(requireContext(), isChecked);
        }));

        dialog.show();
    }

    private void showActionConfirm(boolean isCancel){
        // Inflate the custom layouts
        LayoutInflater inflater = getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.custom_dialog, null);
        View titleView = inflater.inflate(R.layout.custom_dialog_title, null);

        TextView messageTextView = dialogView.findViewById(R.id.dialog_message);
        TextView titleTextView = titleView.findViewById(R.id.custom_title);

        AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());

        if (isCancel) {
            messageTextView.setText("Are you sure you want to cancel your account?");
            titleTextView.setText("Confirm Cancel Account");
            builder.setCustomTitle(titleView)
                    .setView(dialogView)
                    .setPositiveButton(android.R.string.yes, (dialog, which) -> {
                        // Perform the action to cancel the account
                        cancelAccount();
                    })
                    .setNegativeButton(android.R.string.no, null);
        } else {
            messageTextView.setText("Are you sure you want to Logout?");
            titleTextView.setText("Confirm Logout");
            builder.setCustomTitle(titleView)
                    .setView(dialogView)
                    .setPositiveButton(android.R.string.yes, (dialog, which) -> {
                        SharedPreferencesUtil.clearSharedPreferences(requireContext());

                        ((MainActivity) requireActivity()).cancelWork();
                        Intent intent = new Intent(getActivity(), StartPage.class);
                        startActivity(intent);

                        requireActivity().finish();
                    })
                    .setNegativeButton(android.R.string.no, null);
        }

        AlertDialog dialog = builder.create();
        dialog.show();

        dialog.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(Color.parseColor("#FAC307")); // 黄色
        dialog.getButton(AlertDialog.BUTTON_NEGATIVE).setTextColor(Color.parseColor("#F89E85"));
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