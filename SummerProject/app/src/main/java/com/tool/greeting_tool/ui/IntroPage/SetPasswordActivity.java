package com.tool.greeting_tool.ui.IntroPage;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

import com.tool.greeting_tool.R;
import com.tool.greeting_tool.common.constant.KeySet;
import com.tool.greeting_tool.server.StartPage;

public class SetPasswordActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_password);

        Intent data = getIntent();
        EditText Reset_password = findViewById(R.id.re_set_password);
        String ID = data.getStringExtra(KeySet.UserKey);

        //TODO
        //Check and update reset_password

        Button submitButton = findViewById(R.id.reset_password_button);
        submitButton.setOnClickListener(v->{
            Intent resultIntent = new Intent();
            setResult(Activity.RESULT_OK, resultIntent);
            finish();
        });
    }
}