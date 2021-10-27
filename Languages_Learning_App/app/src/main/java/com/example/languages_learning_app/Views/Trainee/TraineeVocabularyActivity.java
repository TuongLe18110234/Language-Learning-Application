package com.example.languages_learning_app.Views.Trainee;

import android.app.ActionBar;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.languages_learning_app.Common.Common;
import com.example.languages_learning_app.DTO.Lesson;
import com.example.languages_learning_app.DTO.Vocabulary;
import com.example.languages_learning_app.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Random;

public class TraineeVocabularyActivity extends AppCompatActivity {

    private Lesson lesson;
    private ArrayList<Vocabulary> vocabularies = new ArrayList<>();
    private DatabaseReference mDataBase;

    Button btnShowFlashcard, btnResult;
    Dialog dialog;
    ImageView imgVocab, imgLeft, imgRight, imgClose, imgHeadphones;
    TextView txtWord, txtPronunciation, txtMeaning;

    private Vocabulary currentVocab;
    private TextToSpeech mTTS;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trainee_vocabulary);

        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        if(bundle != null) {
            lesson = (Lesson) bundle.getSerializable("lesson");
        }

        getListVocabularies();
        setTextToSpeech();
        setDialog();

        btnShowFlashcard = findViewById(R.id.btnShowDialog);
        btnShowFlashcard.setOnClickListener((View v) -> {
            currentVocab = getRandomVocab();
            showDialog(currentVocab);
        });
    }

    private void getListVocabularies() {
        // Get data from firebase
        // Data is list vocabularies in current lesson
        mDataBase = FirebaseDatabase.getInstance().getReference("Vocabularies").child(Common.language.getId());
        Query query = mDataBase.orderByChild("lessonId").equalTo(lesson.getId());
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                vocabularies.clear();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()){
                    Vocabulary vocabulary = dataSnapshot.getValue(Vocabulary.class);
                    if(vocabulary.isActive()) {
                        vocabularies.add(vocabulary);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void setTextToSpeech() {
        mTTS = new TextToSpeech(this, new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                if (status == TextToSpeech.SUCCESS) {
                    int result = mTTS.setLanguage(Common.getLocale(Common.language.getName()));

                    if (result == TextToSpeech.LANG_MISSING_DATA || result == TextToSpeech.LANG_NOT_SUPPORTED) {
                        Log.e("TTS", "Language not supported");
                    }
                } else {
                    Log.e("TTS", "Initialization failed");
                }
            }
        });
    }

    private void speak(String word) {
        float pitch = 1;
        float speed = 1;
        mTTS.setPitch(pitch);
        mTTS.setSpeechRate(speed);

        mTTS.speak(word, TextToSpeech.QUEUE_FLUSH, null);
    }

    @Override
    public void onDestroy() {
        if (mTTS != null) {
            mTTS.stop();
            mTTS.shutdown();
        }

        super.onDestroy();
    }

    private Vocabulary getRandomVocab(){
        Random random = new Random();
        int index = random.nextInt(vocabularies.size());
        return vocabularies.get(index);
    }

    private void setDialog() {
        // Set some attributes for dialog
        dialog = new Dialog(this);
        dialog.setContentView(R.layout.diaglog_flash_card);
        dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        Window window = dialog.getWindow();
        window.setGravity(Gravity.CENTER);
        window.getAttributes().windowAnimations = R.style.DialogAnimationUpDown;
        dialog.setCancelable(false);
        window.setLayout(ActionBar.LayoutParams.WRAP_CONTENT, ActionBar.LayoutParams.WRAP_CONTENT);

        // Mapping
        imgVocab = dialog.findViewById(R.id.imgVocab);
        imgLeft = dialog.findViewById(R.id.imgLeft);
        imgRight = dialog.findViewById(R.id.imgRight);
        imgClose = dialog.findViewById(R.id.imgClose);
        imgHeadphones = dialog.findViewById(R.id.imgHeadphone);
        txtWord = dialog.findViewById(R.id.txtWord);
        txtPronunciation = dialog.findViewById(R.id.txtPronunciation);
        txtMeaning = dialog.findViewById(R.id.txtMeaning);
        btnResult = dialog.findViewById(R.id.btnResult);

        imgClose.setOnClickListener((View v) -> {
            dialog.cancel();
        });

        imgHeadphones.setOnClickListener((View v) -> {
            speak(currentVocab.getWord());
        });

        imgLeft.setOnClickListener((View v) -> {
            dialog.dismiss();
            currentVocab = getRandomVocab();
            showDialog(currentVocab);
        });

        imgRight.setOnClickListener((View v) -> {
            dialog.dismiss();
            currentVocab = getRandomVocab();
            showDialog(currentVocab);
        });

        btnResult.setOnClickListener((View v) -> {
            txtWord.setText(currentVocab.getWord());
            txtPronunciation.setText(currentVocab.getPronunciation());
            txtMeaning.setText(currentVocab.getMeaning());
        });
    }

    private void showDialog(Vocabulary vocabulary) {

        Random random = new Random();
        int mode = random.nextInt(2);

        if(mode == 0) {
            txtWord.setText(vocabulary.getWord());
            txtPronunciation.setText(vocabulary.getPronunciation());
            txtMeaning.setText("???");
        } else if (mode == 1) {
            txtWord.setText("???");
            txtPronunciation.setText("");
            txtMeaning.setText(vocabulary.getMeaning());
        }

        Picasso.get().load(vocabulary.getImageUrl())
                .fit()
                .centerCrop()
                .into(imgVocab);

        dialog.show();
    }

}