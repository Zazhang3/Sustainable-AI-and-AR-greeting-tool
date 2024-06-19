package com.tool.greeting_tool.server;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.tool.greeting_tool.MainActivity;
import com.tool.greeting_tool.R;
import com.tool.greeting_tool.common.ErrorMessage;
import com.tool.greeting_tool.common.KeySet;

public class SignUp extends AppCompatActivity {

    private EditText Account;
    private EditText Password;
    private EditText Password_re;
    private ImageButton backButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        Account = findViewById(R.id.id_signup_id);
        Password = findViewById(R.id.id_signup_password);
        Password_re = findViewById(R.id.id_signup_password_re);
        backButton = findViewById(R.id.id_back_signup);
        ImageButton SignUp = findViewById(R.id.id_signup_button);

        SignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String account = Account.getText().toString();
                if(Password.getText().toString().equals(Password_re.getText().toString())){
                    /*Intent intent = new Intent(SignUp.this, MainActivity.class);
                    intent.putExtra(KeySet.UserKey, account);
                    startActivity(intent);*/
                    Intent resultIntent = new Intent();
                    resultIntent.putExtra(KeySet.UserKey, account);
                    setResult(RESULT_OK, resultIntent);
                    finish();
                }else{
                    Toast.makeText(SignUp.this, ErrorMessage.User_Not_Found, Toast.LENGTH_SHORT).show();
                }
            }
        });

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }
}