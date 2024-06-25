package com.tool.greeting_tool.server;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageButton;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.tool.greeting_tool.MainActivity;
import com.tool.greeting_tool.R;
import com.tool.greeting_tool.common.Constant.KeySet;

public class StartPage extends AppCompatActivity {
    private ImageButton login;
    private ImageButton signup;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start_page);

        login = findViewById(R.id.loginButton);
        signup = findViewById(R.id.signupButton);


        login.setOnClickListener(v -> {
            Intent intent = new Intent(this, LoginController.class);
            startActivityForResult(intent, 1);
        });

        signup.setOnClickListener(v -> {
            Intent intent = new Intent(this, SignUpController.class);
            startActivityForResult(intent, 2);
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == RESULT_OK && data!=null){
            String account = data.getStringExtra(KeySet.UserKey);
            Intent intent = new Intent(this, MainActivity.class);
            intent.putExtra(KeySet.UserKey, account);
            startActivity(intent);
            finish();
        }
    }
}