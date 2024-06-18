package com.example.sampleapp.ui.user;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.sampleapp.R;
import com.example.sampleapp.databinding.FragmentUserBinding;


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

        userViewModel.getText().observe(getViewLifecycleOwner(), textView::setText);

        setHasOptionsMenu(true);

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