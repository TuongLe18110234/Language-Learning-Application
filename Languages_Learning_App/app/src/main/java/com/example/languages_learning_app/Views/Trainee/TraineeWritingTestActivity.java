package com.example.languages_learning_app.Views.Trainee;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.languages_learning_app.Common.Common;
import com.example.languages_learning_app.DAO.ScoreDAO;
import com.example.languages_learning_app.DTO.Score;
import com.example.languages_learning_app.DTO.Vocabulary;
import com.example.languages_learning_app.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Random;

public class TraineeWritingTestActivity extends AppCompatActivity {
    ImageView ivShowResult;
    TextView tvMeaning, tvCorrectAnswer;
    EditText edtEnterWord;
    Button btNextQuestion;

    Score score;
    ArrayList<Vocabulary> vocabularies;
    Boolean NEED_TO_ADD_VOCABULARY, IS_CHECK_ANSWER;
    int totalQuestion, totalCorrectAnswer;
    int indexQuestion;
    String chooseAnswer, correctAnswer;

    DatabaseReference mDataBase;

    MediaPlayer mpCorrect, mpIncorrect;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trainee_writing_test);

        NEED_TO_ADD_VOCABULARY = true;
        IS_CHECK_ANSWER = true;

        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        if (bundle != null) {
            score = (Score) bundle.getSerializable("score");
        }

        vocabularies = new ArrayList<>();
        totalCorrectAnswer = 0;
        indexQuestion = 0;
        mpCorrect = MediaPlayer.create(this, R.raw.audio_correct);
        mpIncorrect = MediaPlayer.create(this, R.raw.audio_incorrect);

        mapAndSetView();
        getListVocabulary();
    }

    private void mapAndSetView() {
        tvMeaning = findViewById(R.id.tvMeaning);

        edtEnterWord = findViewById(R.id.edtEnterWord);

        ivShowResult = findViewById(R.id.ivShowResult);
        tvCorrectAnswer = findViewById(R.id.tvCorrectAnswer);

        btNextQuestion = findViewById(R.id.btNextQuestion);
        btNextQuestion.setOnClickListener((View v) -> {
            if(IS_CHECK_ANSWER){
                checkAnswer();
            } else {
                showNextQuestion();
            }
        });

        setToolbar("Việt - " + Common.language.getBriefName());

    }

    private void showNextQuestion() {
        IS_CHECK_ANSWER = true;
        btNextQuestion.setText("Kiểm tra");
        edtEnterWord.setText("");
        ivShowResult.setVisibility(View.GONE);
        tvCorrectAnswer.setVisibility(View.GONE);

        if (indexQuestion == totalQuestion) {
            donePractice();
            return;
        }

        Vocabulary vocabulary = vocabularies.get(indexQuestion);

        tvMeaning.setText(vocabulary.getMeaning());
        correctAnswer = vocabulary.getWord().toLowerCase();

        indexQuestion++;
    }



    private void checkAnswer() {
        IS_CHECK_ANSWER = false;
        chooseAnswer = edtEnterWord.getText().toString().trim().toLowerCase();

        btNextQuestion.setText("Câu tiếp theo");
        ivShowResult.setVisibility(View.VISIBLE);

        if (chooseAnswer.equals(correctAnswer)) {
            totalCorrectAnswer++;
            mpCorrect.start();
            ivShowResult.setImageResource(R.drawable.bg_correct);
        } else {
            mpIncorrect.start();

            tvCorrectAnswer.setText(correctAnswer);
            tvCorrectAnswer.setVisibility(View.VISIBLE);
            ivShowResult.setImageResource(R.drawable.bg_incorrect);
        }
    }

    private void donePractice() {

        int percentile = (totalCorrectAnswer * 100) / totalQuestion;

        score.setWritingScore(totalCorrectAnswer);
        score.setWritingPercentile(percentile);

        ScoreDAO.getInstance().setValue(score);
        openDialogNotification();
    }

    private void openDialogNotification() {
        final Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.diaglog_notification);
        dialog.setCancelable(false);

        Window window = dialog.getWindow();
        if (window == null) {
            return;
        }

        window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
        window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        WindowManager.LayoutParams windowAttributes = window.getAttributes();
        windowAttributes.gravity = Gravity.CENTER;
        window.setAttributes(windowAttributes);

        TextView tvClose = dialog.findViewById(R.id.tvClose);
        TextView tvMessage = dialog.findViewById(R.id.tvMessage);
        tvMessage.setText("Hoàn thành kiểm tra với " + totalCorrectAnswer + "/" + totalQuestion + " câu");
        tvClose.setOnClickListener((View v) -> {
            dialog.dismiss();
            finish();
        });

        dialog.show();
    }

    private void getListVocabulary() {
        // Get data from firebase
        mDataBase = FirebaseDatabase.getInstance().getReference("Vocabularies").child(Common.language.getId());
        Query query = mDataBase.orderByChild("lessonId").equalTo(score.getLessonId());

        ValueEventListener valueEventListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                if (NEED_TO_ADD_VOCABULARY) {
                    vocabularies.clear();
                    for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                        Vocabulary vocabulary = dataSnapshot.getValue(Vocabulary.class);

                        if(vocabulary.isActive()){
                            vocabularies.add(vocabulary);
                        }
                    }
                    NEED_TO_ADD_VOCABULARY = false;

                    if (vocabularies.size() >= 3) {
                        totalQuestion = vocabularies.size();
                        showNextQuestion();
                    } else {
                        Toast.makeText(TraineeWritingTestActivity.this, "Không đủ từ vựng kiểm tra", Toast.LENGTH_SHORT).show();
                        finish();
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull @NotNull DatabaseError error) {

            }
        };
        query.addValueEventListener(valueEventListener);
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