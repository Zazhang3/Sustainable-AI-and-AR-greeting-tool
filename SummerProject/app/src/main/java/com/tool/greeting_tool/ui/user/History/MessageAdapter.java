package com.tool.greeting_tool.ui.user.History;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.tool.greeting_tool.R;

import java.util.List;

public class MessageAdapter extends ArrayAdapter<History_Message> {
    public MessageAdapter(Context context, List<History_Message> Message_List){
        super(context, 0, Message_List);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        History_Message message = getItem(position);

        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.list_item_message, parent, false);
        }

        TextView messageText = convertView.findViewById(R.id.message_text);
        TextView messageTime = convertView.findViewById(R.id.message_time);
        messageText.setText(message.getMessage());
        messageTime.setText(message.getCreate_time());

        return convertView;
    }
}
