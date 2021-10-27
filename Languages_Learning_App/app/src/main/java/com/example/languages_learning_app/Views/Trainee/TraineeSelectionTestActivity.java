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

public class TraineeSelectionTestActivity extends AppCompatActivity {
    ImageView ivShowResult;
    TextView tvWord, tvAnswer_1, tvAnswer_2, tvAnswer_3, tvCorrectAnswer;
    CardView cvAnswer_1, cvAnswer_2, cvAnswer_3;
    Button btNextQuestion;

    Score score;
    ArrayList<Vocabulary> vocabularies;
    Boolean NEED_TO_ADD_VOCABULARY;
    int totalQuestion, totalCorrectAnswer;
    int indexQuestion, indexVocabulary;
    String chooseAnswer, correctAnswer;

    DatabaseReference mDataBase;

    MediaPlayer mpCorrect, mpIncorrect;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trainee_selection_test);

        NEED_TO_ADD_VOCABULARY = true;

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
        tvWord = findViewById(R.id.tvWord);

        tvAnswer_1 = findViewById(R.id.tvAnswer_1);
        tvAnswer_2 = findViewById(R.id.tvAnswer_2);
        tvAnswer_3 = findViewById(R.id.tvAnswer_3);

        ivShowResult = findViewById(R.id.ivShowResult);
        tvCorrectAnswer = findViewById(R.id.tvCorrectAnswer);

        cvAnswer_1 = findViewById(R.id.cvAnswer_1);
        cvAnswer_1.setOnClickListener((View v) -> {
            chooseAnswer = tvAnswer_1.getText().toString();
            checkAnswer(cvAnswer_1);
        });

        cvAnswer_2 = findViewById(R.id.cvAnswer_2);
        cvAnswer_2.setOnClickListener((View v) -> {
            chooseAnswer = tvAnswer_2.getText().toString();
            checkAnswer(cvAnswer_2);
        });

        cvAnswer_3 = findViewById(R.id.cvAnswer_3);
        cvAnswer_3.setOnClickListener((View v) -> {
            chooseAnswer = tvAnswer_3.getText().toString();
            checkAnswer(cvAnswer_3);
        });

        btNextQuestion = findViewById(R.id.btNextQuestion);
        btNextQuestion.setOnClickListener((View v) -> {
            // Do something
            showNextQuestion();
        });

        setToolbar(Common.language.getBriefName() + " - Việt");

    }

    private void showNextQuestion() {
        cvAnswer_1.setEnabled(true);
        cvAnswer_1.setCardBackgroundColor(Color.WHITE);
        cvAnswer_2.setEnabled(true);
        cvAnswer_2.setCardBackgroundColor(Color.WHITE);
        cvAnswer_3.setEnabled(true);
        cvAnswer_3.setCardBackgroundColor(Color.WHITE);
        btNextQuestion.setEnabled(false);
        ivShowResult.setVisibility(View.GONE);
        tvCorrectAnswer.setVisibility(View.GONE);

        if (indexQuestion == totalQuestion) {
            donePractice();
            return;
        }

        Vocabulary vocabulary = vocabularies.get(indexQuestion);

        tvWord.setText(vocabulary.getWord());
        correctAnswer = vocabulary.getMeaning();

        chooseOtherVocabulary();

        indexQuestion++;
    }

    private void chooseOtherVocabulary() {
        indexVocabulary = indexQuestion;
        String answer_1, answer_2, answer_3;
        answer_1 = correctAnswer;

        while (vocabularies.get(indexVocabulary).getMeaning().equals(answer_1)) {
            if (indexVocabulary == vocabularies.size() - 1) {
                indexVocabulary = -1;
            }
            indexVocabulary++;
        }
        answer_2 = vocabularies.get(indexVocabulary).getMeaning();

        while (vocabularies.get(indexVocabulary).getMeaning().equals(answer_1)
                || vocabularies.get(indexVocabulary).getMeaning().equals(answer_2)) {
            if (indexVocabulary == vocabularies.size() - 1) {
                indexVocabulary = -1;
            }
            indexVocabulary++;
        }
        answer_3 = vocabularies.get(indexVocabulary).getMeaning();

        Random random = new Random();
        int type = random.nextInt(6);

        switch (type) {
            case 0:
                setAnswer(answer_1, answer_2, answer_3);
                break;
            case 1:
                setAnswer(answer_1, answer_3, answer_2);
                break;
            case 2:
                setAnswer(answer_2, answer_1, answer_3);
                break;
            case 3:
                setAnswer(answer_2, answer_3, answer_1);
                break;
            case 4:
                setAnswer(answer_3, answer_1, answer_2);
                break;
            case 5:
                setAnswer(answer_3, answer_2, answer_1);
                break;
        }
    }

    private void setAnswer(String answer_1, String answer_2, String answer_3) {
        tvAnswer_1.setText(answer_1);
        tvAnswer_2.setText(answer_2);
        tvAnswer_3.setText(answer_3);
    }

    private void checkAnswer(CardView cvAnswer) {
        cvAnswer_1.setEnabled(false);
        cvAnswer_2.setEnabled(false);
        cvAnswer_3.setEnabled(false);
        btNextQuestion.setEnabled(true);
        ivShowResult.setVisibility(View.VISIBLE);

        if (chooseAnswer.equals(correctAnswer)) {
            totalCorrectAnswer++;

            mpCorrect.start();

            cvAnswer.setCardBackgroundColor(Color.CYAN);
            ivShowResult.setImageResource(R.drawable.bg_correct);
        } else {
            mpIncorrect.start();

            cvAnswer.setCardBackgroundColor(Color.RED);
            tvCorrectAnswer.setText(correctAnswer);
            tvCorrectAnswer.setVisibility(View.VISIBLE);
            ivShowResult.setImageResource(R.drawable.bg_incorrect);
        }
    }

    private void donePractice() {
        btNextQuestion.setEnabled(false);

        int percentile = (totalCorrectAnswer * 100) / totalQuestion;

        score.setSelectionScore(totalCorrectAnswer);
        score.setSelectionPercentile(percentile);

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

                        if(vocabulary.isActive()) {
                            vocabularies.add(vocabulary);
                        }
                    }
                    NEED_TO_ADD_VOCABULARY = false;

                    if (vocabularies.size() >= 3) {
                        totalQuestion = vocabularies.size();
                        showNextQuestion();
                    } else {
                        Toast.makeText(TraineeSelectionTestActivity.this, "Không đủ từ vựng kiểm tra", Toast.LENGTH_SHORT).show();
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
