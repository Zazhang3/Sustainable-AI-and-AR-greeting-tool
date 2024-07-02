package com.tool.greeting_tool.ui.IntroPage;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.tool.greeting_tool.R;
import com.tool.greeting_tool.common.constant.ErrorMessage;
import com.tool.greeting_tool.common.constant.KeySet;
import com.tool.greeting_tool.common.constant.TAGConstant;
import com.tool.greeting_tool.common.constant.TempAccountInfoConstant;
import com.tool.greeting_tool.common.constant.URLConstant;
import com.tool.greeting_tool.common.utils.FormatCheckerUtil;
import com.tool.greeting_tool.common.utils.SharedPreferencesUtil;
import com.tool.greeting_tool.pojo.vo.UserEmailVO;
import com.tool.greeting_tool.server.MailSender;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class ReSetPassWord extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_re_set_pass_word);

        EditText usernameEditText = findViewById(R.id.userID_reset);
        EditText emailEditText = findViewById(R.id.re_set_email);
        Button submitButton = findViewById(R.id.ResetButton);

        submitButton.setOnClickListener(v->{
            String username = usernameEditText.getText().toString();
            String email = emailEditText.getText().toString().replaceAll("\\n$", "");


            if (username.isEmpty() || email.isEmpty()) {
                Toast.makeText(ReSetPassWord.this, "Please fill out all fields", Toast.LENGTH_SHORT).show();
                return;
            }
            if (!FormatCheckerUtil.checkEmail(email)) {
                Toast.makeText(ReSetPassWord.this, "Please fill with email!", Toast.LENGTH_SHORT).show();
                return;
            }

            //TODO
            //check username and email and return verificationCode
            userVerification(username,email);

            showSecurityDialog(username,SharedPreferencesUtil.getVerificationCode(ReSetPassWord.this));
        });
    }

    private void showSecurityDialog(String accountName, String verificationCode){
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

            //Check security code here

            if(enterSecurityCode.equals(verificationCode)){
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

    /**
     * verify username and email
     * @param username
     * @param email
     */
    private void userVerification(String username, String email) {
        //generate userVO
        UserEmailVO userEmailVO = new UserEmailVO();
        userEmailVO.setUsername(username);
        userEmailVO.setEmail(email);

        //generate request
        Gson gson = new Gson();
        String json = gson.toJson(userEmailVO);

        RequestBody body = RequestBody.create(
                json,
                MediaType.get("application/json;charset=utf-8"));

        Request request = new Request.Builder()
                .url(URLConstant.VERIFY_USERNAME_AND_EMAIL_URL)
                .post(body)
                .build();

        //send request
        OkHttpClient client = new OkHttpClient();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                Log.e(TAGConstant.VERIFY_EMAIL,"Verify failed",e);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(ReSetPassWord.this,"Verify failed: " + ErrorMessage.NETWORK_ERROR,Toast.LENGTH_SHORT).show();
                    }
                });
            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                final String responseBody = response.body().string();
                Log.d(TAGConstant.SIGN_UP_TAG,"Response: "+ responseBody);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            Gson gson = new Gson();
                            JsonObject jsonResponse = gson.fromJson(responseBody, JsonObject.class);
                            int code = jsonResponse.get("code").getAsInt();
                            if (code == 1) {
                                // Verify successfully: handle response

                                String userVerificationCode = jsonResponse.getAsJsonObject("data").get("verificationCode").getAsString();
                                SharedPreferencesUtil.saveVerificationCode(ReSetPassWord.this,userVerificationCode);
                                SharedPreferencesUtil.saveEmail(ReSetPassWord.this,email);
                                String verificationCode = SharedPreferencesUtil.getVerificationCode(ReSetPassWord.this);

                                //send email
                                MailSender.sendEmail(TempAccountInfoConstant.emailSenderAddress,
                                        TempAccountInfoConstant.emailSenderPassword,
                                        email,
                                        TempAccountInfoConstant.emailSubject,
                                        verificationCode);
                                Toast.makeText(ReSetPassWord.this, "Verify successfully", Toast.LENGTH_SHORT).show();
                            } else {
                                // Verify failed
                                String msg = jsonResponse.get("msg").getAsString();
                                Toast.makeText(ReSetPassWord.this, "Verify failed: " + msg, Toast.LENGTH_SHORT).show();
                            }
                        } catch (Exception e) {
                            Log.e(TAGConstant.SIGN_UP_TAG, "Exception while parsing response", e);
                            Toast.makeText(ReSetPassWord.this, "Verify failed: " + ErrorMessage.INVALID_RESPONSE, Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });
    }


}