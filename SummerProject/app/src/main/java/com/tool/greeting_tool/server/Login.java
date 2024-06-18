package com.tool.greeting_tool.server;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.tool.greeting_tool.MainActivity;
import com.tool.greeting_tool.R;
import com.tool.greeting_tool.common.ErrorMessage;
import com.tool.greeting_tool.common.KeySet;

public class Login extends AppCompatActivity {
    private EditText Account;
    private EditText Password;
    private Button Login;

    /**
     * Use to ask user enter the ID and Password
     *
     * @param savedInstanceState If the activity is being re-initialized after
     *     previously being shut down then this Bundle contains the data it most
     *     recently supplied in {@link #onSaveInstanceState}.  <b><i>Note: Otherwise it is null.</i></b>
     *
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        Account = findViewById(R.id.account_num);
        Password = findViewById(R.id.password);
        Login = findViewById(R.id.signButton);

        Login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String account = Account.getText().toString();
                String passWord = Password.getText().toString();
                if(account.equals("Admin")&&passWord.equals("123456")){
                    Intent intent = new Intent(Login.this, MainActivity.class);
                    intent.putExtra(KeySet.UserKey, account);
                    startActivity(intent);
                    finish();
                }else{
                    Toast.makeText(Login.this, ErrorMessage.User_Not_Found, Toast.LENGTH_SHORT).show();
                }
            }
        });


    }
}