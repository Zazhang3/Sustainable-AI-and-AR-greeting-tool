package com.tool.greeting_tool.server;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageButton;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.tool.greeting_tool.MainActivity;
import com.tool.greeting_tool.R;
import com.tool.greeting_tool.common.constant.KeySet;
import com.tool.greeting_tool.common.constant.RequestCode;
import com.tool.greeting_tool.ui.IntroPage.ReSetPassWord;

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
            startActivityForResult(intent, RequestCode.REQUEST_LOGIN);
        });

        signup.setOnClickListener(v -> {
            Intent intent = new Intent(this, SignUpController.class);
            startActivityForResult(intent, RequestCode.REQUEST_SIGNUP);
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == RESULT_OK && data!=null){
            if(data.getStringExtra(KeySet.ReSetKey)==null){
                String account = data.getStringExtra(KeySet.UserKey);
                Intent intent = new Intent(this, MainActivity.class);
                //intent.putExtra(KeySet.UserKey, account);
                //intent.putExtra("source", "login");
                startActivity(intent);
                finish();
            }
        }
    }
}