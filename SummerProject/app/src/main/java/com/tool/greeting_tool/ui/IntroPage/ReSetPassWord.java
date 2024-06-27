package com.tool.greeting_tool.ui.IntroPage;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.tool.greeting_tool.R;
import com.tool.greeting_tool.common.constant.ErrorMessage;
import com.tool.greeting_tool.common.constant.KeySet;

public class ReSetPassWord extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_re_set_pass_word);

        EditText userID = findViewById(R.id.userID_reset);
        EditText email = findViewById(R.id.re_set_email);
        Button submitButton = findViewById(R.id.ResetButton);

        submitButton.setOnClickListener(v->{
            String accountName = userID.getText().toString();
            String emailString = email.getText().toString();

            if (accountName.isEmpty() || emailString.isEmpty()) {
                Toast.makeText(ReSetPassWord.this, "Please fill out all fields", Toast.LENGTH_SHORT).show();
                return;
            }

            //TODO
            //Add Email format check

            //TODO
            //send email message to user's email

            showSecurityDialog(accountName);
        });
    }

    private void showSecurityDialog(String accountName){
        LayoutInflater inflater = LayoutInflater.from(this);
        View dialogView = inflater.inflate(R.layout.dialog_security_code, null);

        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
        dialogBuilder.setView(dialogView);
        AlertDialog dialog = dialogBuilder.create();
        dialog.show();

        EditText securityCode = dialogView.findViewById(R.id.security_code_input);
        Button submitSecurityCodeButton = dialogView.findViewById(R.id.submit_security_code_button);

        submitSecurityCodeButton.setOnClickListener(v->{
            String enterSecurityCode = securityCode.getText().toString();

            //TODO
            //Check security code here, set 12345 to check for now
            if(enterSecurityCode.equals("12345")){
                Intent intent = new Intent(this, SetPasswordActivity.class);
                intent.putExtra(KeySet.UserKey, accountName);
                startActivityForResult(intent, 2);
                dialog.dismiss();
            }else{
                Toast.makeText(ReSetPassWord.this, ErrorMessage.INCORRECT_SECURITY_CODE, Toast.LENGTH_SHORT).show();
            }
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