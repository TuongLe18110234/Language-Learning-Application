package com.example.languages_learning_app.Views.Admin;

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

import com.example.languages_learning_app.Adapters.LanguageAdapter;
import com.example.languages_learning_app.Common.Common;
import com.example.languages_learning_app.DAO.LanguageDAO;
import com.example.languages_learning_app.DTO.Language;
import com.example.languages_learning_app.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class AdminLanguageFragment extends Fragment implements View.OnClickListener{
    RecyclerView recyclerView;
    ArrayList<Language> listLanguage;
    LanguageAdapter languageAdapter;
    EditText edtName, edtVietnamese, edtBriefName;
    Button btSetLanguage, btClose;
    AlertDialog alertDialog;
    DatabaseReference mDatabase;
    FloatingActionButton btOpenDialog;

    private LanguageAdapter.RecyclerViewClickListener listener;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_admin_language, container, false);

        setOnClickListener();
        setRecyclerView(root);

        btOpenDialog = (FloatingActionButton) root.findViewById(R.id.btOpenLanguageDialog);
        btOpenDialog.setOnClickListener(this);

        return root;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btOpenLanguageDialog:
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
        View view = getLayoutInflater().inflate(R.layout.dialog_language, null);
        alertDialog = builder.create();
        alertDialog.setView(view);
        alertDialog.show();

        btClose = view.findViewById(R.id.btClose);
        btClose.setOnClickListener(this);

        btSetLanguage = view.findViewById(R.id.btSetLanguage);

        edtName = view.findViewById(R.id.edtLanguageName);
        edtVietnamese = view.findViewById(R.id.edtVietnameseName);
        edtBriefName = view.findViewById(R.id.edtBriefName);

        if (mode == Common.mode.update || mode == mode.read){
            Language language  = listLanguage.get(position);
            edtName.setText(language.getName());
            edtVietnamese.setText(language.getDisplayName());
            edtBriefName.setText(language.getBriefName());

            if(mode == Common.mode.update){
                btSetLanguage.setText("Update");
            }
            if (mode == Common.mode.read){
                btSetLanguage.setVisibility(View.GONE);
            }
        }

        btSetLanguage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = edtName.getText().toString().trim();
                String vietnamese = edtVietnamese.getText().toString().trim();
                String briefName = edtBriefName.getText().toString().trim();

                if (name.isEmpty()){
                    edtName.setError("Enter English Name");
                    return;
                }
                if (vietnamese.isEmpty()){
                    edtVietnamese.setError("Enter Vietnamese Name");
                    return;
                }
                if (briefName.isEmpty()){
                    edtBriefName.setError("Enter Brief Name");
                    return;
                }

                if(mode == Common.mode.create){
                    for(int i=0;i<listLanguage.size();i++){
                        if(name.equals(listLanguage.get(i).getName())){
                            edtName.setError("English Name is duplicated");
                            return;
                        }
                    }
                    Language language = new Language(name, vietnamese, briefName, R.drawable.flag_of_english);
                    LanguageDAO.getInstance().setLanguageValue(language);
                }
                if (mode == Common.mode.update){
                    Language language = listLanguage.get(position);

                    if (!name.equals(language.getName())){
                        for(int i=0;i<listLanguage.size();i++){
                            if(name.equals(listLanguage.get(i).getName())){
                                edtName.setError("English Name is duplicated");
                                return;
                            }
                        }
                    }

                    LanguageDAO.getInstance().deleteLanguage(language.getId());

                    language.setName(name);
                    language.setDisplayName(vietnamese);
                    language.setBriefName(briefName);

                    LanguageDAO.getInstance().setLanguageValue(language);
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
                Language language = listLanguage.get(position);
                if(LanguageDAO.getInstance().deleteLanguage(language.getId())){
                    Toast.makeText(this.getContext(), "Xóa ngôn ngữ thành công!", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(this.getContext(), "Lỗi khi xóa ngôn ngữ!", Toast.LENGTH_SHORT).show();
            }
                return true;
        }
        return super.onContextItemSelected(item);
    }

    private void setOnClickListener() {
        listener = new LanguageAdapter.RecyclerViewClickListener() {
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
                    Language language = listLanguage.get(position);
                    LanguageDAO.getInstance().changeStatusLanguage(language);
                }
            }
        };
    }

    private void setRecyclerView(View root){
        recyclerView = root.findViewById(R.id.rvLanguages);

        listLanguage = new ArrayList<>();

        languageAdapter = new LanguageAdapter(getContext(), listLanguage, listener);

        GridLayoutManager gridLayoutManager = new GridLayoutManager(getContext(), 1, GridLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(gridLayoutManager);

        recyclerView.setAdapter(languageAdapter);

        // Get data from firebase
        mDatabase = FirebaseDatabase.getInstance().getReference("Languages");
        mDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                listLanguage.clear();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()){
                    Language language = dataSnapshot.getValue(Language.class);
                    listLanguage.add(language);
                }
                languageAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
    }
}