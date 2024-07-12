package com.tool.greeting_tool.server;

import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;

import com.tool.greeting_tool.R;

import java.util.List;

public class UserHelpPagerAdapter extends PagerAdapter {
    private List<View> views;
    public UserHelpPagerAdapter(List<View> views) {
        this.views = views;
    }
    @Override
    public int getCount() {
        return views.size();
    }
    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view == object;
    }
    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        View view = views.get(position);
        container.addView(view);
        return view;
    }
    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView(views.get(position));
    }
}
