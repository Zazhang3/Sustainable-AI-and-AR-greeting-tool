package com.tool.greeting_tool.ui.IntroPage;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
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
import com.tool.greeting_tool.common.constant.URLConstant;
import com.tool.greeting_tool.common.utils.SharedPreferencesUtil;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class ReSetPassWord extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_re_set_pass_word);

        EditText userIdEditText = findViewById(R.id.userID_reset);
        EditText emailEditText = findViewById(R.id.textEmail);
        Button submitButton = findViewById(R.id.ResetButton);

        submitButton.setOnClickListener(v->{
            //TODO
            String username = userIdEditText.getText().toString();
            String emailAddress = emailEditText.getText().toString();
            getEmail(username);
            if(!emailAddress.isEmpty()  && SharedPreferencesUtil.getEmail(ReSetPassWord.this).equals(emailAddress)) {
                Intent intent = new Intent(this, SetPasswordActivity.class);
                intent.putExtra(KeySet.UserKey, username);
                startActivityForResult(intent, 2);
            }else {
                Toast.makeText(ReSetPassWord.this,ErrorMessage.INCORRECT_EMAIL_ADDRESS ,Toast.LENGTH_SHORT).show();
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
     * get email by username
     * @param username
     */
    private void getEmail(String username){

        //generate request
        Request request = new Request.Builder()
                .url(URLConstant.BASIC_USER_URL+"/"+username)
                .get()
                .build();

        //send request
        OkHttpClient client = new OkHttpClient();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                Log.e(TAGConstant.GET_EMAIL_BY_USERNAME,"Get email failed",e);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(ReSetPassWord.this,"Fail to get email: " + ErrorMessage.NETWORK_ERROR,Toast.LENGTH_SHORT).show();
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
                                // get email successfully: handle response
                                String returnEmail = jsonResponse.get("data").getAsString();
                                //save email
                                SharedPreferencesUtil.saveUserEmail(ReSetPassWord.this,returnEmail);

                                Toast.makeText(ReSetPassWord.this, "Succeed to get email", Toast.LENGTH_SHORT).show();
                            } else {
                                // get email failed
                                String msg = jsonResponse.get("msg").getAsString();
                                Toast.makeText(ReSetPassWord.this, "Fail to get email: " + msg, Toast.LENGTH_SHORT).show();
                            }
                        } catch (Exception e) {
                            Log.e(TAGConstant.SIGN_UP_TAG, "Exception while parsing response", e);
                            Toast.makeText(ReSetPassWord.this, "Fail to get email: " + ErrorMessage.INVALID_RESPONSE, Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });
    }
}