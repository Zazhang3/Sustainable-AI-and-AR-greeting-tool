package com.tool.greeting_tool.ui.IntroPage;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.tool.greeting_tool.R;
import com.tool.greeting_tool.common.constant.KeySet;

public class ReSetPassWord extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_re_set_pass_word);

        EditText userID = findViewById(R.id.userID_reset);
        EditText email = findViewById(R.id.textEmail);
        Button submitButton = findViewById(R.id.ResetButton);
        String accountName = userID.getText().toString();
        String emailString = email.getText().toString();

        submitButton.setOnClickListener(v->{
            //TODO
            //send email to check here
            Intent intent = new Intent(this, SetPasswordActivity.class);
            intent.putExtra(KeySet.UserKey, accountName);
            startActivityForResult(intent, 2);
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == RESULT_OK){
            Intent resultIntent = new Intent();
            setResult(Activity.RESULT_OK, resultIntent);
            finish();
        }
    }
}