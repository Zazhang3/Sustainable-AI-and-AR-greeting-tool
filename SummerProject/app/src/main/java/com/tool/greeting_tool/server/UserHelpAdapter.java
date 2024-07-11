package com.tool.greeting_tool.server;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.tool.greeting_tool.R;
import com.tool.greeting_tool.common.utils.AssetManager;

public class UserHelpAdapter extends RecyclerView.Adapter<UserHelpAdapter.ViewHolder> {

    private final int[] images;
    private final LayoutInflater inflater;

    public UserHelpAdapter(Context context) {
        images = AssetManager.getHelpimageList();
        this.inflater = LayoutInflater.from(context);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.userhelp_page, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.imageView.setImageResource(images[position]);
    }

    @Override
    public int getItemCount() {
        return images.length;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.help_image);
        }
    }
}

