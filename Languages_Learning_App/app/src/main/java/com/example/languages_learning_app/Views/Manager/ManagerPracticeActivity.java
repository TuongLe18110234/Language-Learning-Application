package com.example.languages_learning_app.Views.Manager;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.languages_learning_app.Adapters.PracticeAdapter;
import com.example.languages_learning_app.Common.Common;
import com.example.languages_learning_app.DAO.LessonDAO;
import com.example.languages_learning_app.DAO.PracticeDAO;
import com.example.languages_learning_app.DTO.Lesson;
import com.example.languages_learning_app.DTO.Practice;
import com.example.languages_learning_app.DTO.Vocabulary;
import com.example.languages_learning_app.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class ManagerPracticeActivity extends AppCompatActivity {
    final int MODE_ADD = 0;
    final int MODE_UPDATE = 1;

    RecyclerView recyclerView;
    PracticeAdapter adapter;
    TextView tvEasyPracticeCount, tvHardPracticeCount, tvTypePractice, tvTitle;
    EditText edtSentence;
    Spinner spCorrectAnswer;
    FloatingActionButton btOpenDialog;

    int numOfEasy, numOfHard;

    int PRACTICE_TYPE;
    Lesson lesson;
    ArrayList<Practice> practices;
    ArrayList<String> wordNames;

    DatabaseReference mDataBase;
    
    PracticeAdapter.RecyclerViewClickListener listener;
    
    

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manager_practice);

        PRACTICE_TYPE = Common.PRACTICE_TYPE_EASY;

        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        if (bundle!=null){
            lesson = (Lesson) bundle.getSerializable("lesson");
        }

        wordNames = new ArrayList<>();
        tvEasyPracticeCount = findViewById(R.id.tvEasyPracticeCount);
        tvHardPracticeCount = findViewById(R.id.tvHardPracticeCount);
        tvTypePractice = findViewById(R.id.tvTypePractice);

        setToolbar(lesson.getName());
        setOnClickListener();
        getWordNames();
        setRecyclerView();

        CardView cvEasyPracticeCount = findViewById(R.id.cvEasyPracticeCount);
        cvEasyPracticeCount.setOnClickListener((View v) -> {
            PRACTICE_TYPE = Common.PRACTICE_TYPE_EASY;
            tvTypePractice.setText("Câu dễ");
            getDataFromFirebase(PRACTICE_TYPE);
        });

        CardView cvHardPracticeCount = findViewById(R.id.cvHardPracticeCount);
        cvHardPracticeCount.setOnClickListener((View v) -> {
            PRACTICE_TYPE = Common.PRACTICE_TYPE_HARD;
            tvTypePractice.setText("Câu khó");
            getDataFromFirebase(PRACTICE_TYPE);
        });

        btOpenDialog = findViewById(R.id.fabAdd);
        btOpenDialog.setOnClickListener((View v) -> {
            openDialog(MODE_ADD, -1);
        });
    }

    private void openDialog(int mode, int position) {
        if(wordNames.size() == 0){
            Toast.makeText(ManagerPracticeActivity.this, "Không có từ vựng để thêm luyện tập!", Toast.LENGTH_SHORT).show();
            return;
        }

        final Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_add_practice);

        Window window = dialog.getWindow();
        if(window == null) {
            return;
        }

        window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
        window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        WindowManager.LayoutParams windowAttributes = window.getAttributes();
        windowAttributes.gravity = Gravity.CENTER;
        window.setAttributes(windowAttributes);

        tvTitle = dialog.findViewById(R.id.tvTitle);
        edtSentence = dialog.findViewById(R.id.edtSentence);
        spCorrectAnswer = dialog.findViewById(R.id.spCorrectAnswer);

        // Set data for spinner
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, wordNames);
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spCorrectAnswer.setAdapter(arrayAdapter);

        Button btnSave = dialog.findViewById(R.id.btSave);
        Button btnClose = dialog.findViewById(R.id.btClose);

        // If mode is update
        if(mode == MODE_UPDATE){
            Practice practice = practices.get(position);

            tvTitle.setText("Cập nhật câu hỏi luyện tập");
            edtSentence.setText(practice.getSentence());

            if(arrayAdapter.getPosition(practice.getCorrectAnswer()) >= 0){
                spCorrectAnswer.setSelection(arrayAdapter.getPosition(practice.getCorrectAnswer()));
            } else {
                spCorrectAnswer.setSelection(0);
            }
        }

        btnClose.setOnClickListener((View v) -> {
            dialog.dismiss();
        });

        btnSave.setOnClickListener((View v) -> {
            savePractice(mode, position);
            dialog.dismiss();
        });

        dialog.show();
    }

    private void savePractice(int mode, int position){
        String sentence = edtSentence.getText().toString();
        String correctAnswer = spCorrectAnswer.getSelectedItem().toString();

        if(sentence.trim().isEmpty()){
            edtSentence.setError("Câu hỏi không được để trống!");
            edtSentence.requestFocus();
            return;
        }

        if (correctAnswer.trim().isEmpty()){
            edtSentence.setError("Chưa có đáp án được chọn!");
            edtSentence.requestFocus();
            return;
        }

        if(mode == MODE_ADD){
            Practice practice = new Practice(lesson.getId(), sentence, correctAnswer, PRACTICE_TYPE);
            PracticeDAO.getInstance().setValue(practice);
            Toast.makeText(ManagerPracticeActivity.this, "Thêm câu hỏi luyện tập thành công!", Toast.LENGTH_SHORT).show();
        } else if(mode == MODE_UPDATE){
            Practice practice = practices.get(position);
            practice.setSentence(sentence);
            practice.setCorrectAnswer(correctAnswer);

            PracticeDAO.getInstance().setValue(practice);
            Toast.makeText(ManagerPracticeActivity.this, "Cập nhật câu hỏi luyện tập thành công!", Toast.LENGTH_SHORT).show();
        }
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

    private void setOnClickListener() {
        listener = new PracticeAdapter.RecyclerViewClickListener() {
            @Override
            public void onClick(View v, int position) {
                openDialog(MODE_UPDATE, position);
            }

            @Override
            public void onCreateContextMenu(ContextMenu menu, int position) {
                menu.add(position,0,0,"Cập nhật câu hỏi");
                menu.add(position,1,1,"Xóa câu hỏi");
            }

            @Override
            public void onTouch(View v, int position) {
                if(position>=0) {
                    Practice practice = practices.get(position);
                    PracticeDAO.getInstance().changeStatus(practice);
                }
            }
        };
    }

    @Override
    public boolean onContextItemSelected(@NonNull MenuItem item) {
        int position = item.getGroupId();
        switch (item.getItemId()){
            case 0:
                openDialog(MODE_UPDATE, position);
                return true;
            case 1:
                Practice practice = practices.get(position);
                if(PracticeDAO.getInstance().delete(practice.getId())){
                    Toast.makeText(this, "Xóa câu hỏi luyện tập thành công!", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(this, "Lỗi khi xóa câu hỏi luyện tập!", Toast.LENGTH_SHORT).show();
                }
                return true;
        }
        return super.onContextItemSelected(item);
    }

    private void getWordNames(){
        // Get data from firebase
        mDataBase = FirebaseDatabase.getInstance().getReference("Vocabularies").child(Common.language.getId());
        Query query = mDataBase.orderByChild("lessonId").equalTo(lesson.getId());
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                practices.clear();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()){
                    Vocabulary vocabulary = dataSnapshot.getValue(Vocabulary.class);
                    wordNames.add(vocabulary.getWord());
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void setRecyclerView(){
        recyclerView = findViewById(R.id.rvPractice);

        practices = new ArrayList<>();
        getDataFromFirebase(PRACTICE_TYPE);

        adapter = new PracticeAdapter(this, practices, listener);

        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 1, GridLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(gridLayoutManager);

        recyclerView.setAdapter(adapter);
    }

    private void getDataFromFirebase(int type){
        // Get data from firebase
        mDataBase = FirebaseDatabase.getInstance().getReference("Practices").child(Common.language.getId());
        Query query = mDataBase.orderByChild("lessonId").equalTo(lesson.getId());
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                numOfEasy = 0;
                numOfHard = 0;
                practices.clear();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()){
                    Practice practice = dataSnapshot.getValue(Practice.class);

                    if (practice.getType() == PRACTICE_TYPE) {
                        practices.add(practice);
                    }

                    if (practice.getType() == Common.PRACTICE_TYPE_EASY){
                        numOfEasy++;
                    } else if(practice.getType() == Common.PRACTICE_TYPE_HARD) {
                        numOfHard++;
                    }
                }
                tvEasyPracticeCount.setText(numOfEasy + " CÂU");
                tvHardPracticeCount.setText(numOfHard + " CÂU");
                adapter.notifyDataSetChanged();
                LessonDAO.getInstance().updateNumOfPractice(lesson.getId(), numOfEasy, numOfHard);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}