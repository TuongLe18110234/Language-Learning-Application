package com.example.languages_learning_app.Views.Manager;

import android.app.Dialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.languages_learning_app.Adapters.Manager.VocabularyAdapter;
import com.example.languages_learning_app.Common.Common;
import com.example.languages_learning_app.DAO.LessonDAO;
import com.example.languages_learning_app.DAO.VocabularyDAO;
import com.example.languages_learning_app.DTO.Lesson;
import com.example.languages_learning_app.DTO.Vocabulary;
import com.example.languages_learning_app.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class ManagerVocabularyActivity extends AppCompatActivity {

    // Const
    private static final int PICK_IMAGE_REQUEST = 1;
    private static final int INSERT_MODE = 1;
    private static final int UPDATE_MODE = 2;
    private static Dialog dialog;

    // Flag
    private boolean chooseFileFlag = false;

    // Variables used for recycler view
    private RecyclerView recyclerView;
    private ArrayList<Vocabulary> vocabularies;
    private VocabularyAdapter adapter;
    private VocabularyAdapter.RecyclerViewClickListener listener;

    // Variables used for mapping views
    private FloatingActionButton fabAddVocab;
    private ProgressBar progressBar;
    private TextView txtTittle;
    private EditText edWord, edMeaning;
    private ImageView imgVocab;

    // Variables used for connecting with Firebase
    private StorageReference mStorage;
    private DatabaseReference mDataBase;
    private Uri mImageUri;
    private StorageTask mUploadTask;

    // Current lesson - it means vocabs showed in the activity are on this lesson
    private Lesson lesson;
    private Vocabulary vocab;
    //private String phonetic;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manager_vocabulary);

        // Get data from intent (data is current lesson)
        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        if (bundle != null) {
            lesson = (Lesson) bundle.getSerializable("lesson");
        }

        // Firebase storage and database
        mStorage = FirebaseStorage.getInstance().getReference("Vocabularies");
        mDataBase = FirebaseDatabase.getInstance().getReference("Vocabularies/" + Common.language.getId());

        // Tool bar on the top on screen
        setToolbar();

        // For recycler view
        setOnClickListener();
        setRecyclerView();

        // Mapping float action button
        fabAddVocab = findViewById(R.id.fabAdd);
        fabAddVocab.setOnClickListener((View v) -> {
            // Open dialog to insert new word when clicking on float action button
            openDialog(INSERT_MODE, -1);
        });
    }

    // Toolbar on the top of screen
    private void setToolbar() {
        // Finish activity when clicking on back item
        ImageView backIcon = findViewById(R.id.left_icon);
        backIcon.setOnClickListener((View v) -> {
            this.finish();
        });

        // Set name for activity
        TextView txtToolbarName = findViewById(R.id.activity_name);
        txtToolbarName.setText(lesson.getName());
    }

    /**
     * Open dialog to enter vocabulary's info
     * @param mode: insert or update
     * @param position: position of selected vocab (equal -1 if mode is insert)
     */
    private void openDialog(int mode, int position ) {
        dialog = new Dialog(this);

        // Set some attributes for dialog
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_add_vocabulary);

        Window window = dialog.getWindow();
        if(window == null) {
            return;
        }

        window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
        window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        WindowManager.LayoutParams windowAttributes = window.getAttributes();
        windowAttributes.gravity = Gravity.CENTER;
        window.setAttributes(windowAttributes);

        // Mapping
        txtTittle = dialog.findViewById(R.id.txtTittle);
        edWord = dialog.findViewById(R.id.edWord);
        edMeaning = dialog.findViewById(R.id.edMeaning);
        progressBar = dialog.findViewById(R.id.progressBar);

        // Open file chooser when clicking on the image
        imgVocab = dialog.findViewById(R.id.imgVocab);
        imgVocab.setOnClickListener((View v) -> {
            openFileChooser();
        });

        // Button save and cancel
        Button btnSave = dialog.findViewById(R.id.btnSave);
        Button btnCancel = dialog.findViewById(R.id.btnClose);

        // Update mode: get vocabulary in list with position
        if(mode == UPDATE_MODE) {
            Vocabulary vocabulary = vocabularies.get(position);

            // Set some information of vocab to dialog
            txtTittle.setText("Chỉnh sửa từ vựng");
            edWord.setText(vocabulary.getWord());
            edMeaning.setText(vocabulary.getMeaning());
            Picasso.get().load(vocabulary.getImageUrl())
                    .fit()
                    .centerCrop()
                    .into(imgVocab);
        }

        // Dismiss dialog when clicking cancel
        btnCancel.setOnClickListener((View v) -> {
            dialog.dismiss();
        });

        // Save vocab
        btnSave.setOnClickListener((View v) -> {
            if(mUploadTask != null && mUploadTask.isInProgress()) {
                // Show notice if user click on button when task is not successful
                Toast.makeText(this, "Đang lưu...", Toast.LENGTH_SHORT).show();
            } else {
                // Call save with mode and position
                saveVocab(mode, position);
            }
        });

        dialog.show();
    }

    // Open file chooser
    private void openFileChooser(){
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(intent.ACTION_GET_CONTENT);
        startActivityForResult(intent, PICK_IMAGE_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Get image's uri and show it into image view
        if(requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            mImageUri = data.getData();

            Picasso.get().load(mImageUri).into(imgVocab);
            chooseFileFlag = true;
        }
    }

    // Get file extension
    private String getFileExtension(Uri uri) {
        ContentResolver contentResolver = getContentResolver();
        MimeTypeMap mimeTypeMap = MimeTypeMap.getSingleton();
        return mimeTypeMap.getExtensionFromMimeType(contentResolver.getType(uri));
    }

    /**
     * Save vocabulary to database
     * @param mode: insert or update
     * @param position: position of selected vocab (equal -1 if mode is insert)
     */
    private void saveVocab(int mode, int position) {

        String vocabWord = edWord.getText().toString().trim();
        String vocabMeaning = edMeaning.getText().toString().trim();

        if(vocabWord.equals("")) {
            edWord.setError("Từ vựng không được để trống!");
            return;
        }

        if(vocabMeaning.equals("")) {
            edMeaning.setError("Nghĩa không được để trống!");
        }

        if(mode == UPDATE_MODE) {
            // Set new value for vocab
            vocab = vocabularies.get(position);

            vocab.setWord(vocabWord);
            vocab.setMeaning(vocabMeaning);

            // If user don't change image -> only save on runtime database
            if(chooseFileFlag == false) {
                //mDataBase.child(vocab.getId()).setValue(vocab);
                VocabularyDAO.getInstance().saveWordWithPhonetic(vocab);
                Toast.makeText(ManagerVocabularyActivity.this, "Lưu thành công!", Toast.LENGTH_SHORT).show();
                dialog.dismiss();
                return;
            }
        } else {
            // Get key and save new vocab with id equals this key
            String vocabId = mDataBase.push().getKey();
            // Create a vocabulary object using builder pattern
            vocab = new Vocabulary.VocabularyBuilder(vocabId, vocabWord, vocabMeaning)
                    .setLessonId(lesson.getId())
                    .build();
        }

        if(mImageUri != null) {
            // This comment will save file with file extension.
            // But I don't use it because I want file name is as same as vocab id
            // It's easy for when I upload or delete a word --> I will delete file with name = vocab id
            // StorageReference fileReference = mStorage.child(vocab.getId() + "." + getFileExtension(mImageUri));
            StorageReference fileReference = mStorage.child(vocab.getId());

            // Show progress bar when start task
            progressBar.setVisibility(View.VISIBLE);

            mUploadTask = fileReference.putFile(mImageUri)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            fileReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {
                                    // Set image url
                                    vocab.setImageUrl(uri.toString());
                                    // Save vocab into Firebase Database
                                    // mDataBase.child(vocab.getId()).setValue(vocab);
                                    VocabularyDAO.getInstance().saveWordWithPhonetic(vocab);

                                    // If mode is insert -> increase word count 1
                                    // if (mode == INSERT_MODE) {
                                    //     lesson.setWordCount(lesson.getWordCount() + 1);
                                    //     LessonDAO.getInstance().setLessonValue(lesson);
                                    // }

                                    // Hide progress bar when task finish
                                    dialog.dismiss();
                                    chooseFileFlag = false;
                                    progressBar.setVisibility(View.GONE);
                                    Toast.makeText(ManagerVocabularyActivity.this, "Lưu thành công!", Toast.LENGTH_SHORT).show();
                                }
                            });
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            // Hide progress bar when task fail
                            progressBar.setVisibility(View.GONE);
                            chooseFileFlag = false;
                            Toast.makeText(ManagerVocabularyActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                            dialog.dismiss();
                        }
                    });
        } else {
            Toast.makeText(this, "Chưa chọn ảnh", Toast.LENGTH_SHORT).show();
        }
    }

    private void setOnClickListener() {
        listener = new VocabularyAdapter.RecyclerViewClickListener() {
            @Override
            public void onClick(View v, int position) {
                // Open vocabulary dialog when clicking on an item in recycler view
                openDialog(UPDATE_MODE, position);
            }

            @Override
            public void onCreateContextMenu(ContextMenu menu, int position) {
                // Add context menu
                menu.add(position,0,0,"Chỉnh sửa");
                menu.add(position,1,1,"Xóa");
            }

            @Override
            public void onTouch(View v, int position) {
                // Change status of vocabulary when check/uncheck
                if(position >= 0) {
                    Vocabulary vocabulary = vocabularies.get(position);
                    VocabularyDAO.getInstance().changeStatus(vocabulary);
                }
            }
        };
    }

    @Override
    public boolean onContextItemSelected(@NonNull MenuItem item) {
        int position = item.getGroupId();
        switch (item.getItemId()){
            case 0:
                // Open dialog with update mode when selecting update
                openDialog(UPDATE_MODE, position);
                return true;
            case 1:
                // Delete vocabulary when selecting delete
                Vocabulary vocabulary = vocabularies.get(position);
                if(VocabularyDAO.getInstance().delete(vocabulary.getId())){
                    // Delete file in storage with have name = vocab id
                    StorageReference fileReference = mStorage.child(vocabulary.getId());
                    fileReference.delete();

                    // Decrease word count 1
                    // lesson.setWordCount(lesson.getWordCount() - 1);
                    // LessonDAO.getInstance().setLessonValue(lesson);

                    Toast.makeText(this, "Xóa từ vựng thành công!", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(this, "Đã xảy ra lỗi. Vui lòng thử lại!", Toast.LENGTH_SHORT).show();
                }
                return true;
        }
        return super.onContextItemSelected(item);
    }

    private void setRecyclerView(){
        vocabularies = new ArrayList<>();
        recyclerView = findViewById(R.id.rvListVocabularies);
        adapter = new VocabularyAdapter(this, vocabularies, listener);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 2, GridLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(gridLayoutManager);

        recyclerView.setAdapter(adapter);

        // Get data from firebase
        // Data is list vocabularies in current lesson
        Query query = mDataBase.orderByChild("lessonId").equalTo(lesson.getId());
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                vocabularies.clear();
                int activeCount = 0;
                for (DataSnapshot dataSnapshot : snapshot.getChildren()){
                    Vocabulary vocabulary = dataSnapshot.getValue(Vocabulary.class);
                    vocabularies.add(vocabulary);

                    if(vocabulary.isActive()) {
                        activeCount ++;
                    }
                }

                lesson.setWordCount(vocabularies.size());
                lesson.setActiveWordCount(activeCount);
                LessonDAO.getInstance().setLessonValue(lesson);

                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}