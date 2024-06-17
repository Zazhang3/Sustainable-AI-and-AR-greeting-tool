package com.example.sampleapp;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.provider.UserDictionary;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AlertDialogLayout;
import androidx.appcompat.widget.Toolbar;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.ArrayList;
import java.util.Objects;

public class WordsSelect extends AppCompatActivity {

    private final String[] dataList = new String[]{"Text1", "Text2", "Text3"};
    private final String[] emoji = new String[]{"First", "Second", "Third"};
    public static final String EXTRA_SELECTION = "com.example.appname.SampleApp";

    private static final int REQUEST_CODE_SELECT_1 = 1;
    private static final int REQUEST_CODE_SELECT_2 = 2;

    private String selectType;
    private String selectText;
    private int request;
    private String[] items;

    private Toolbar toolbar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_words_select);

        //Use custom toolbar to replace built-in actionBar
        //Check words_select.xml
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // Optionally set custom title or other properties if needed
        Objects.requireNonNull(getSupportActionBar()).setDisplayShowTitleEnabled(true);

        selectType = getIntent().getStringExtra("selectType");
        selectText = getIntent().getStringExtra("selectText");
        request = getIntent().getIntExtra("request", -1);

        if(Objects.equals(selectType, "Words")){
            items = dataList;
        }else if(Objects.equals(selectType, "Emoji")){
            //set toolbar text title to 'Emoji' and set item list to emoji type
            updateToolbarTitle("Select Emoji");
            items = emoji;
        }

        //Adapter to setup ListView
        //Now Adapter Generics type set to string, Will update to a Bundle to store more data
        ArrayAdapter<String> theAdapter = new ArrayAdapter<String>(WordsSelect.this,
                android.R.layout.simple_list_item_1, items);

        ListView ListView = findViewById(R.id.words);
        ListView.setAdapter(theAdapter);

        //Item selection logic
        //Temporarily only have two selections and will add last selection
        ListView.setOnItemClickListener((parent, view, position, id) -> {
            String selectedItem = items[position];
            if(Objects.equals(selectType, "Words")){
                //Store User's first Selection in an Intent and send to next
                Intent intent = new Intent(WordsSelect.this, WordsSelect.class);
                intent.putExtra("selectType", "Emoji");
                intent.putExtra("selectText", selectedItem);
                intent.putExtra("request", request);
                startActivityForResult(intent, 1);
            }else if(Objects.equals(selectType, "Emoji")){
                if(request == REQUEST_CODE_SELECT_1){
                    //Situation for Preview
                    showSelectionDialog(selectText, selectedItem);
                }else if(request == REQUEST_CODE_SELECT_2){
                    //Finish Item selection and move to postcode enter
                    //Back to Home page
                    Intent resultIntent = new Intent();
                    resultIntent.putExtra(EXTRA_SELECTION, selectedItem);
                    setResult(RESULT_OK, resultIntent);
                    finish();
                }
            }
        });
    }

    //Use to update Toolbar title in each selection page
    private void updateToolbarTitle(String title) {
        if (toolbar != null) {
            toolbar.setTitle(title);
        }
    }

    //Show dialog page after selection and back to Home page in Preview situation
    private void showSelectionDialog(String selectText, String selectedItem) {
        new AlertDialog.Builder(this).setTitle("Your Selection")
                .setMessage("Text: " + selectText + "\nEmoji: " + selectedItem)
                .setPositiveButton("OK", (dialog, which) -> {
                    Intent resultIntent = new Intent();
                    resultIntent.putExtra(EXTRA_SELECTION, selectText + " - " + selectedItem);
                    setResult(RESULT_OK, resultIntent);
                    finish();
                })
                .show();
    }

    //Store the selected result and back to home page
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && data != null) {
            String selection = data.getStringExtra(EXTRA_SELECTION);
            Intent resultIntent = new Intent();
            resultIntent.putExtra(EXTRA_SELECTION, selection);
            setResult(RESULT_OK, resultIntent);
            finish();
        }
    }
}