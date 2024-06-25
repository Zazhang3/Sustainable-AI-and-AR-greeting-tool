package com.tool.greeting_tool;

import com.tool.greeting_tool.common.constant.ButtonString;
import com.tool.greeting_tool.common.constant.RequestCode;
import com.tool.greeting_tool.common.constant.KeySet;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import java.util.ArrayList;
import java.util.Objects;

public class WordsSelect extends AppCompatActivity {

    private final String[] dataList = new String[]{"Text1", "Text2", "Text3"};
    private final String[] emoji = new String[]{"First", "Second", "Third"};
    private final String[] animation = new String[]{"tes1", "tes2", "tes3"};
    public static final String EXTRA_SELECTION = "com.tool.appname.greeting_tool";

    private ArrayList<String> selectList;
    private String selectType;
    private String selectText;
    private String selectEmoji;
    private int request;
    private String[] items;

    private Toolbar toolbar;


    /**
     * The Activity Class use to ask user to select items
     *
     * @param savedInstanceState If the activity is being re-initialized after
     *     previously being shut down then this Bundle contains the data it most
     *     recently supplied in {@link #onSaveInstanceState}.  <b><i>Note: Otherwise it is null.</i></b>
     *
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_words_select);

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // Optionally set custom title or other properties if needed
        Objects.requireNonNull(getSupportActionBar()).setDisplayShowTitleEnabled(true);

        selectList = getIntent().getStringArrayListExtra(KeySet.SelectedList);
        selectType = getIntent().getStringExtra(KeySet.SelectedType);
        request = getIntent().getIntExtra(KeySet.Request, -1);

        if (selectList == null) {
            selectList = new ArrayList<>();
        }

        if(Objects.equals(selectType, "Words")){
            items = dataList;
        }else if(Objects.equals(selectType, "Emoji")){
            //set toolbar text title to 'Emoji' and set item list to emoji type
            updateToolbarTitle("Select Emoji");
            items = emoji;
        }else if(Objects.equals(selectType, "Animation")){
            updateToolbarTitle("Select Animation");
            items = animation;
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
                selectList.add(selectedItem);
                intent.putExtra(KeySet.SelectedList, selectList);
                intent.putExtra(KeySet.SelectedType, "Emoji");
                intent.putExtra(KeySet.Request, request);
                startActivityForResult(intent, 2);
            }else if(Objects.equals(selectType, "Emoji")){
                Intent intent = new Intent(WordsSelect.this, WordsSelect.class);
                selectList.add(selectedItem);
                intent.putExtra(KeySet.SelectedType, "Animation");
                intent.putExtra(KeySet.SelectedList, selectList);
                intent.putExtra(KeySet.Request, request);
                if(request == 1){
                    startActivityForResult(intent, 2);
                }else{
                    startActivityForResult(intent, 2);
                }
            }else if(Objects.equals(selectType, "Animation")){
                if(request == RequestCode.REQUEST_CODE_SELECT_1){
                    //Situation for Preview
                    selectList.add(selectedItem);
                    showSelectionDialog(selectList);
                }else if(request == RequestCode.REQUEST_CODE_SELECT_2){
                    //Finish Item selection and move to postcode enter
                    //Back to Home page
                    selectList.add(selectedItem);
                    Intent intent = new Intent(WordsSelect.this, Postcode_fill.class);
                    intent.putExtra(KeySet.SelectedList, selectList);
                    startActivityForResult(intent, 2);
                }
            }
        });
    }

    /**
     * Use to update Toolbar title in each selection page
     * @param title
     */
    private void updateToolbarTitle(String title) {
        if (toolbar != null) {
            toolbar.setTitle(title);
        }
    }

    /**
     * Show dialog page after selection and back to Home page in Preview situation
     */
    private void showSelectionDialog(ArrayList<String> selectList) {
        new AlertDialog.Builder(this).setTitle("Your Selection")
                .setMessage("Text: " + selectList.get(0) + "\nEmoji: " + selectList.get(1) + "\nAnimation: " + selectList.get(2))
                .setPositiveButton(ButtonString.positiveSet, (dialog, which) -> {
                    Intent resultIntent = new Intent();
                    resultIntent.putExtra(EXTRA_SELECTION, selectList);
                    setResult(Activity.RESULT_OK, resultIntent);
                    finish();
                })
                .show();
    }

    /** Store the selected result and back to home page
     *
     * @param requestCode The integer request code originally supplied to
     *                    startActivityForResult(), allowing you to identify who this
     *                    result came from.
     * @param resultCode The integer result code returned by the child activity
     *                   through its setResult().
     * @param data An Intent, which can return result data to the caller
     *               (various data can be attached to Intent "extras").
     *
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && data != null && requestCode == RequestCode.REQUEST_CODE_SELECT_1) {
            ArrayList<String> selection = data.getStringArrayListExtra(EXTRA_SELECTION);
            Intent resultIntent = new Intent();
            resultIntent.putExtra(EXTRA_SELECTION, selection);
            setResult(Activity.RESULT_OK, resultIntent);
            finish();
        }else if(requestCode == RequestCode.REQUEST_CODE_SELECT_2 && resultCode == RESULT_OK && data != null){
            String postCode = data.getStringExtra(KeySet.PostKey);
            ArrayList<String> backSelectedList = data.getStringArrayListExtra(KeySet.SelectedList);
            Intent resultIntent = new Intent();
            resultIntent.putExtra(KeySet.PostKey, postCode);
            resultIntent.putExtra(KeySet.SelectedList, backSelectedList);
            setResult(Activity.RESULT_OK, resultIntent);
            finish();
        }
    }
}