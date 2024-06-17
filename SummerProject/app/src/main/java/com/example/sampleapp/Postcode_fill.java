package com.example.sampleapp;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class Postcode_fill extends AppCompatActivity {

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

    //Temporarily use Dialog to show the postcode
    private void showPostCodeDialog(String postCode) {
        new AlertDialog.Builder(this)
                .setMessage("The PostCode you enter: " + postCode)
                .setPositiveButton("OK", (dialog, which) -> {
                    Intent resultIntent = new Intent();
                    //resultIntent.putExtra(EXTRA_SELECTION, selectText + " - " + selectedItem);
                    setResult(RESULT_OK, resultIntent);
                    finish();
                })
                .show();
    }
}