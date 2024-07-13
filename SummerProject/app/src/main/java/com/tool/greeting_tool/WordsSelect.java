package com.tool.greeting_tool;

import com.tool.greeting_tool.common.constant.ButtonString;
import com.tool.greeting_tool.common.constant.RequestCode;
import com.tool.greeting_tool.common.constant.KeySet;
import com.tool.greeting_tool.common.utils.AssetManager;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.widget.TextViewCompat;

import java.util.ArrayList;
import java.util.Objects;


public class WordsSelect extends AppCompatActivity {
    public static final String EXTRA_SELECTION = "com.tool.appname.greeting_tool";

    private ArrayList<String> selectList;
    private String selectType;
    private int request;
    private int[] items;

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
        //request = getIntent().getIntExtra(KeySet.Request, -1);

        if (selectList == null) {
            selectList = new ArrayList<>();
        }

        ListView listView = findViewById(R.id.words);

        if(Objects.equals(selectType, "Words")){
            items = AssetManager.getDataList();
        }else if(Objects.equals(selectType, "Emoji")){
            //set toolbar text title to 'Emoji' and set item list to emoji type
            updateToolbarTitle("Select Emoji");
            items = AssetManager.getEmojiList();
        }else if(Objects.equals(selectType, "Animation")){
            updateToolbarTitle("Select Animation");
            items = AssetManager.getAnimationList();
        }

        ImageAdapter theAdapter = new ImageAdapter(WordsSelect.this, items);
        listView.setAdapter(theAdapter);
        //Adapter to setup ListView
        //Now Adapter Generics type set to string, Will update to a Bundle to store more data

        //ListView listView = findViewById(R.id.words);
        //listView.setAdapter(theAdapter);

        //Item selection logic
        listView.setOnItemClickListener((parent, view, position, id) -> {
            int selectedItem = items[position];
            Intent intent = new Intent(WordsSelect.this, WordsSelect.class);
            if (Objects.equals(selectType, "Words")) {
                String text = AssetManager.mapResourceIdToText(selectedItem);
                selectList.add(text);
                intent.putExtra(KeySet.SelectedList, selectList);
                intent.putExtra(KeySet.SelectedType, "Emoji");
                //intent.putExtra(KeySet.Request, request);
                startActivityForResult(intent, 1);
            } else if (Objects.equals(selectType, "Emoji")) {
                String emoji = AssetManager.mapResourceIdToEmoji(selectedItem);
                selectList.add(emoji);
                intent.putExtra(KeySet.SelectedType, "Animation");
                intent.putExtra(KeySet.SelectedList, selectList);
                startActivityForResult(intent, 1);
                //intent.putExtra(KeySet.Request, request);
                /*if (request == 1) {
                    startActivityForResult(intent, 1);
                } else {
                    startActivityForResult(intent, 2);
                }*/
            } else if (Objects.equals(selectType, "Animation")) {
                String animation = AssetManager.mapResourceIdToAnimation(selectedItem);
                selectList.add(animation);

                showSelectionDialog(selectList);
                /*if (request == RequestCode.REQUEST_CODE_SELECT_1) {
                    showSelectionDialog(selectList);
                } else if (request == RequestCode.REQUEST_CODE_SELECT_2) {
                    intent = new Intent(WordsSelect.this, Postcode_fill.class);
                    intent.putExtra(KeySet.SelectedList, selectList);
                    startActivityForResult(intent, 2);
                }*/
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
        String text = selectList.get(0);
        String emoji = selectList.get(1);
        String animation = selectList.get(2);

        LayoutInflater inflater = getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.dialog_custom, null);

        TextView titleTextView = dialogView.findViewById(R.id.dialog_title);
        TextView messageTextView = dialogView.findViewById(R.id.dialog_message);

        titleTextView.setText("Your Selection");
        messageTextView.setText("Text: " + text + "\nEmoji: " + emoji + "\nAnimation: " + animation);

        AlertDialog dialog = new AlertDialog.Builder(this)
                    .setView(dialogView)
                    .setPositiveButton("send", (dialogInterface, which) -> {
                        /*Intent intent = new Intent(WordsSelect.this, Postcode_fill.class);
                        intent.putExtra(KeySet.SelectedList, selectList);
                        startActivityForResult(intent, 1);*/
                        Intent resultIntent = new Intent();
                        resultIntent.putExtra(KeySet.SelectedList, selectList);
                        resultIntent.putExtra(KeySet.IsSend, 1);
                        setResult(Activity.RESULT_OK, resultIntent);
                        finish();
                    })
                    .setNeutralButton("preview", (dialogInterface, which)->{
                        Intent resultIntent = new Intent();
                        resultIntent.putExtra(KeySet.SelectedList, selectList);
                        resultIntent.putExtra(KeySet.IsSend, 0);
                        //resultIntent.putExtra(KeySet.Request, RequestCode.REQUEST_CODE_SELECT_1);
                        setResult(Activity.RESULT_OK, resultIntent);
                        finish();
                    })
                    .setNegativeButton("cancel", (dialogInterface, which)->{
                        dialogInterface.dismiss();
                    })
                    .create();

        dialog.setOnShowListener(dialogInterface -> {
            //Modify other button format
            Button positiveButton = dialog.getButton(AlertDialog.BUTTON_POSITIVE);
            Button neutralButton = dialog.getButton(AlertDialog.BUTTON_NEUTRAL);
            Button negativeButton = dialog.getButton(AlertDialog.BUTTON_NEGATIVE);

            TextViewCompat.setTextAppearance(positiveButton, R.style.CustomButton_pre_se);
            TextViewCompat.setTextAppearance(neutralButton, R.style.CustomButton_pre_se);
            TextViewCompat.setTextAppearance(negativeButton, R.style.CustomButton_cancel);
        });

        dialog.show();
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
            if (resultCode == RESULT_OK && data != null) {
                ArrayList<String> selection = data.getStringArrayListExtra(KeySet.SelectedList);
                int sendState = data.getIntExtra(KeySet.IsSend, -1);
                Intent resultIntent = new Intent();
                resultIntent.putExtra(KeySet.SelectedList, selection);
                resultIntent.putExtra(KeySet.IsSend, sendState);
                setResult(Activity.RESULT_OK, resultIntent);
                finish();
                /*if (requestCode == RequestCode.REQUEST_CODE_SELECT_1) {
                    ArrayList<String> selection = data.getStringArrayListExtra(KeySet.SelectedList);
                    System.out.println(selection);
                    Intent resultIntent = new Intent();
                    resultIntent.putExtra(KeySet.SelectedList, selection);
                    resultIntent.putExtra(KeySet.Request, RequestCode.REQUEST_CODE_SELECT_1);
                    setResult(Activity.RESULT_OK, resultIntent);
                    finish();
                } else if (requestCode == RequestCode.REQUEST_CODE_SELECT_2) {
                    String postCode = data.getStringExtra(KeySet.PostKey);
                    ArrayList<String> backSelectedList = data.getStringArrayListExtra(KeySet.SelectedList);
                    Intent resultIntent = new Intent();
                    resultIntent.putExtra(KeySet.PostKey, postCode);
                    resultIntent.putExtra(KeySet.SelectedList, backSelectedList);
                    resultIntent.putExtra(KeySet.Request, RequestCode.REQUEST_CODE_SELECT_2);
                    setResult(Activity.RESULT_OK, resultIntent);
                    finish();
                }*/
            }
        }
}