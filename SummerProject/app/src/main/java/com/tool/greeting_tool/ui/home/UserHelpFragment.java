package com.tool.greeting_tool.ui.home;

import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.viewpager2.widget.ViewPager2;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import com.tool.greeting_tool.R;
import com.tool.greeting_tool.server.UserHelpPagerAdapter;

public class UserHelpFragment extends DialogFragment {

    private static final int[] IMAGE_RES_IDS = {
            R.drawable.goodday, R.drawable.happynewyear, R.drawable.hello, R.drawable.back,
            R.drawable.biting, R.drawable.enjoyyourday, R.drawable.allwell, R.drawable.butterfly,
            R.drawable.allwell, R.drawable.butterfly
    };

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.dialog_userhelp, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        ViewPager2 viewPager = view.findViewById(R.id.viewPager);
        UserHelpPagerAdapter adapter = new UserHelpPagerAdapter(requireContext(), IMAGE_RES_IDS);
        viewPager.setAdapter(adapter);

        ImageButton closeButton = view.findViewById(R.id.buttonClose);
        closeButton.setOnClickListener(v -> dismiss());
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(DialogFragment.STYLE_NORMAL, R.style.CustomDialog);
    }
}
