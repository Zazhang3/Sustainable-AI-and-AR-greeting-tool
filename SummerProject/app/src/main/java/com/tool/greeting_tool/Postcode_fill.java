package com.tool.greeting_tool;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.widget.EditText;

import com.tool.greeting_tool.common.constant.ButtonString;
import com.tool.greeting_tool.common.constant.KeySet;
import com.tool.greeting_tool.common.constant.MessageConstant;
import com.tool.greeting_tool.server.LocationHelper;

import androidx.appcompat.app.AppCompatActivity;
import android.widget.ImageButton;

import java.util.ArrayList;

public class Postcode_fill extends AppCompatActivity{
    private LocationHelper locationHelper;

    /**
     *
     * @param savedInstanceState If the activity is being re-initialized after
     *     previously being shut down then this Bundle contains the data it most
     *     recently supplied in {@link #onSaveInstanceState}.  <b><i>Note: Otherwise it is null.</i></b>
     *
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_postcode_fill);

        Intent intent = getIntent();
        ArrayList<String> selectList = intent.getStringArrayListExtra(KeySet.SelectedList);

        EditText postCode = findViewById(R.id.postcode);
        ImageButton submitButton = findViewById(R.id.submit_postcode_button);

        submitButton.setOnClickListener(v -> {
            String postcode = postCode.getText().toString().trim();
            //TODO
            //Check submit postcode
            showPostCodeDialog(postcode, selectList);

        });
    }

    /**
     * Temporarily use Dialog to show the postcode
     * @param postCode
     */
    private void showPostCodeDialog(String postCode, ArrayList<String> selectList) {
        new AlertDialog.Builder(this)
                .setMessage(MessageConstant.PostCodeMessage + postCode)
                .setPositiveButton(ButtonString.positiveSet, (dialog, which) -> {
                    Intent resultIntent = new Intent();
                    resultIntent.putExtra(KeySet.PostKey, postCode);
                    resultIntent.putExtra(KeySet.SelectedList, selectList);
                    setResult(Activity.RESULT_OK, resultIntent);
                    finish();
                })
                .show();
    }

}