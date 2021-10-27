package com.example.languages_learning_app.Views.Trainee;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.languages_learning_app.R;
import com.mannan.translateapi.Language;
import com.mannan.translateapi.TranslateAPI;

public class TraineeTranslateActivity extends AppCompatActivity {

    EditText edtSentence;
    TextView tvMeaning;
    Button btTranslate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trainee_translate);

        setToolbar("Dịch câu");

        edtSentence = findViewById(R.id.edtSentence);
        tvMeaning = findViewById(R.id.tvMeaning);
        btTranslate = findViewById(R.id.btTranslate);
        btTranslate.setOnClickListener((View v)->{
            doTranslate();
        });

    }

    private void doTranslate(){
        String sentence = edtSentence.getText().toString();

        TranslateAPI translateAPI = new TranslateAPI(
                Language.AUTO_DETECT,
                Language.VIETNAMESE,
                sentence
        );

        translateAPI.setTranslateListener(new TranslateAPI.TranslateListener() {
            @Override
            public void onSuccess(String translatedText) {
                tvMeaning.setText(translatedText);
            }

            @Override
            public void onFailure(String ErrorText) {

            }
        });
    }

    // Toolbar on the top of screen
    private void setToolbar(String name) {
        // Finish activity when clicking on back item
        ImageView backIcon = findViewById(R.id.left_icon);
        backIcon.setOnClickListener((View v) -> {
            this.finish();
        });

        // Set name for activity
        TextView txtToolbarName = findViewById(R.id.activity_name);
        txtToolbarName.setText(name);
    }
}