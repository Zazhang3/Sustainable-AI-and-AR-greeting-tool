package com.tool.greeting_tool.ui.user.History;

import android.app.AlertDialog;
import android.os.Bundle;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.tool.greeting_tool.R;
import com.tool.greeting_tool.common.constant.MessageConstant;

import java.util.ArrayList;
import java.util.List;

public class HistoryActivity extends AppCompatActivity {
    private MessageAdapter messageAdapter;
    private List<History_Message> MessageList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);

        //TODO
        //history check
        ListView listView = findViewById(R.id.history);

        MessageList = new ArrayList<>();
        MessageList.add(new History_Message(0, "First One"));
        MessageList.add(new History_Message(1, "Second One"));
        MessageList.add(new History_Message(2, "Third One"));
        MessageList.add(new History_Message(3, "Fourth One"));

        messageAdapter = new MessageAdapter(this, MessageList);
        listView.setAdapter(messageAdapter);

        listView.setOnItemClickListener((parent, view, position, id) -> {
            History_Message message = messageAdapter.getItem(position);
            if (message != null) {
                showDeleteDialog(message);
            }
        });
    }

    private void showDeleteDialog(History_Message message) {
        new AlertDialog.Builder(this)
                .setTitle(MessageConstant.Delete)
                .setMessage(MessageConstant.DeleteMessage)
                .setPositiveButton(android.R.string.yes, (dialog, which) -> {
                    MessageList.remove(message);
                    messageAdapter.notifyDataSetChanged();
                    Toast.makeText(HistoryActivity.this, MessageConstant.Delete, Toast.LENGTH_SHORT).show();
                })
                .setNegativeButton(android.R.string.no, null)
                .show();
    }
}