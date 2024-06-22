package com.tool.greeting_tool;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;

import com.tool.greeting_tool.common.ButtonString;
import com.tool.greeting_tool.common.KeySet;
import com.tool.greeting_tool.common.MessageConstant;

import androidx.appcompat.app.AppCompatActivity;

public class Postcode_fill extends AppCompatActivity {

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

        EditText postCode = findViewById(R.id.postcode);
        Button submitButton = findViewById(R.id.submit_postcode_button);

        submitButton.setOnClickListener(v -> {
            String postcode = postCode.getText().toString().trim();
            showPostCodeDialog(postcode);

            /*Intent resultIntent = new Intent();
            resultIntent.putExtra("POSTCODE", postcode);
            setResult(RESULT_OK, resultIntent);
            finish();*/
        });
    }

    /**
     * Temporarily use Dialog to show the postcode
     * @param postCode
     */
    private void showPostCodeDialog(String postCode) {
        new AlertDialog.Builder(this)
                .setMessage(MessageConstant.PostCodeMessage + postCode)
                .setPositiveButton(ButtonString.positiveSet, (dialog, which) -> {
                    Intent resultIntent = new Intent();
                    resultIntent.putExtra(KeySet.PostKey, postCode);
                    //resultIntent.putExtra(EXTRA_SELECTION, selectText + " - " + selectedItem);
                    setResult(RESULT_OK, resultIntent);
                    finish();
                })
                .show();
    }
}