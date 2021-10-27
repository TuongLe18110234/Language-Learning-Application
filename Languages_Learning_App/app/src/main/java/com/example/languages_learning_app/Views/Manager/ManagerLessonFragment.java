package com.example.languages_learning_app.Views.Manager;

import android.app.AlertDialog;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.languages_learning_app.Adapters.LessonAdapter;
import com.example.languages_learning_app.Common.Common;
import com.example.languages_learning_app.DAO.LessonDAO;
import com.example.languages_learning_app.DTO.Lesson;
import com.example.languages_learning_app.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class ManagerLessonFragment extends Fragment implements View.OnClickListener{

    RecyclerView recyclerView;
    ArrayList<Lesson> listLesson;
    LessonAdapter lessonAdapter;
    EditText edtName, edtDescription;
    Button btSetLesson, btClose;
    AlertDialog alertDialog;
    DatabaseReference mDatabase;
    FloatingActionButton btOpenDialog;

    private LessonAdapter.RecyclerViewClickListener listener;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_manager_lesson, container, false);

        setOnClickListener();
        setRecyclerView(root);

        btOpenDialog = (FloatingActionButton) root.findViewById(R.id.btOpenLessonDialog);
        btOpenDialog.setOnClickListener(this);

        return root;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btOpenLessonDialog:
                openDialog(Common.mode.create, -1);
                break;
            case R.id.btClose:
                alertDialog.cancel();
                break;
        }
    }

    private void openDialog(Common.mode mode, int position) {
        // Show alert dialog
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        View view = getLayoutInflater().inflate(R.layout.dialog_lesson, null);
        alertDialog = builder.create();
        alertDialog.setView(view);
        alertDialog.show();

        btClose = view.findViewById(R.id.btClose);
        btClose.setOnClickListener(this);

        btSetLesson = view.findViewById(R.id.btSetLesson);

        edtName = view.findViewById(R.id.edtLessonName);
        edtDescription = view.findViewById(R.id.edtDescription);

        if (mode == Common.mode.update || mode == mode.read){
            Lesson lesson  = listLesson.get(position);
            edtName.setText(lesson.getName());
            edtDescription.setText(lesson.getDescription());

            if(mode == Common.mode.update){
                btSetLesson.setText("Update");
            }
            if (mode == Common.mode.read){
                btSetLesson.setVisibility(View.GONE);
            }
        }

        btSetLesson.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = edtName.getText().toString().trim();
                String description = edtDescription.getText().toString().trim();

                if (name.isEmpty()){
                    edtName.setError("Enter Lesson Name");
                    return;
                }
                if (description.isEmpty()){
                    edtDescription.setError("Enter Description");
                    return;
                }

                if(mode == Common.mode.create){
                    for(int i=0;i<listLesson.size();i++){
                        if(name.equals(listLesson.get(i).getName())){
                            edtName.setError("English Name is duplicated");
                            return;
                        }
                    }
                    Lesson lesson = new Lesson(Common.language.getId(), name, description);
                    LessonDAO.getInstance().setLessonValue(lesson);
                }
                if (mode == Common.mode.update){
                    Lesson lesson = listLesson.get(position);

                    if (!name.equals(lesson.getName())){
                        for(int i=0;i<listLesson.size();i++){
                            if(name.equals(listLesson.get(i).getName())){
                                edtName.setError("English Name is duplicated");
                                return;
                            }
                        }
                    }

                    LessonDAO.getInstance().deleteLesson(lesson.getId());

                    lesson.setName(name);
                    lesson.setDescription(description);

                    LessonDAO.getInstance().setLessonValue(lesson);
                }
                alertDialog.cancel();
            }
        });
    }

    @Override
    public boolean onContextItemSelected(@NonNull MenuItem item) {
        int position = item.getGroupId();
        switch (item.getItemId()){
            case 0:
                openDialog(Common.mode.update, position);
                return true;
            case 1:
                Lesson lesson = listLesson.get(position);
                if(LessonDAO.getInstance().deleteLesson(lesson.getId())){
                    Toast.makeText(this.getContext(), "Xóa bài học thành công!", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(this.getContext(), "Lỗi khi xóa bài học!", Toast.LENGTH_SHORT).show();
                }
                return true;
        }
        return super.onContextItemSelected(item);
    }

    private void setOnClickListener() {
        listener = new LessonAdapter.RecyclerViewClickListener() {
            @Override
            public void onClick(View v, int position) {
                openDialog(Common.mode.read, position);
            }

            @Override
            public void onCreateContextMenu(ContextMenu menu, int position) {
                menu.add(position,0,0,"Edit");
                menu.add(position,1,1,"Delete");
            }

            @Override
            public void onTouch(View v, int position) {
                if(position >= 0) {
                    Lesson lesson = listLesson.get(position);
                    LessonDAO.getInstance().changeStatusLesson(lesson);
                }
            }
        };
    }

    private void setRecyclerView(View root){
        recyclerView = root.findViewById(R.id.rvLesson);

        listLesson = new ArrayList<>();

        lessonAdapter = new LessonAdapter(getContext(), listLesson, listener);

        GridLayoutManager gridLayoutManager = new GridLayoutManager(getContext(), 1, GridLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(gridLayoutManager);

        recyclerView.setAdapter(lessonAdapter);

        // Get data from firebase
        mDatabase = FirebaseDatabase.getInstance().getReference("Lessons");
        mDatabase.child(Common.language.getId()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                listLesson.clear();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()){
                    Lesson lesson = dataSnapshot.getValue(Lesson.class);
                    listLesson.add(lesson);
                }
                lessonAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}