package com.tool.greeting_tool.ui.user;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.tool.greeting_tool.R;
import com.tool.greeting_tool.databinding.FragmentUserBinding;
import com.tool.greeting_tool.server.StartPage;
import com.tool.greeting_tool.ui.user.History.HistoryActivity;
import com.tool.greeting_tool.ui.user.History.History_Message;


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

        setHasOptionsMenu(true);

        History = binding.history;
        History.setOnClickListener(v->{
            Intent intent = new Intent(getActivity(), HistoryActivity.class);
            startActivity(intent);
        });

        AccountCancel = binding.cancelButton;
        AccountCancel.setOnClickListener(v->{
            //TODO
            //add account cancellation logic here
            Toast.makeText(getActivity(), "Click cancel", Toast.LENGTH_SHORT).show();
        });

        LogoutButton = binding.actionLogout;
        LogoutButton.setOnClickListener(v->{
            //TODO
            //add account logout logic here
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

    //Create optionsMenu
    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        inflater.inflate(R.menu.select_menu, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_cancel_account) {
            // Handle settings action
            Toast.makeText(getActivity(), "Click cancel", Toast.LENGTH_SHORT).show();
            return true;
        } else if (id == R.id.action_logout) {
            // Handle about action
            Toast.makeText(getActivity(), "Click logout", Toast.LENGTH_SHORT).show();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}