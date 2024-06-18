package com.example.sampleapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.Objects;

public class Login extends AppCompatActivity {
    private EditText Account;
    private EditText Password;
    private Button Login;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        Account = findViewById(R.id.account_num);
        Password = findViewById(R.id.password);
        Login = findViewById(R.id.signButton);

        Objects.requireNonNull(getSupportActionBar()).setDisplayShowTitleEnabled(false);
        Login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String account = Account.getText().toString();
                String passWord = Password.getText().toString();
                if(account.equals("Admin")&&passWord.equals("123456")){
                    Intent intent = new Intent(Login.this, MainActivity.class);
                    intent.putExtra("userName", account);
                    startActivity(intent);
                    finish();
                }else{
                    Toast.makeText(Login.this, "Invalid user name", Toast.LENGTH_SHORT).show();
                }
            }
        });


    }
}