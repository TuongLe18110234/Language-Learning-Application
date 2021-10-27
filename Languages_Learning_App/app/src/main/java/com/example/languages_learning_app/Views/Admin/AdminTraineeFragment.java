package com.example.languages_learning_app.Views.Admin;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.languages_learning_app.Adapters.UserAdapter;
import com.example.languages_learning_app.Common.Common;
import com.example.languages_learning_app.DAO.UserDAO;
import com.example.languages_learning_app.DTO.User;
import com.example.languages_learning_app.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class AdminTraineeFragment extends Fragment{
    RecyclerView recyclerView;
    ArrayList<User> listUser;
    UserAdapter userAdapter;
    DatabaseReference mDatabase;

    private UserAdapter.RecyclerViewClickListener listener;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_admin_trainee, container, false);

        setOnClickListener();
        setRecyclerView(root);

        return root;
    }

    private void setOnClickListener() {
        listener = new UserAdapter.RecyclerViewClickListener() {
            @Override
            public void onClick(View v, int position) {
            }

            @Override
            public void onCreateContextMenu(ContextMenu menu, int position) {
            }

            @Override
            public void onTouch(View v, int position) {
                if(position >= 0) {
                    User user = listUser.get(position);
                    UserDAO.getInstance().changeStatusUser(user.getUserId(), user.isActive());
                }
            }
        };
    }

    private void setRecyclerView(View root){
        recyclerView = root.findViewById(R.id.rvTrainee);

        listUser = new ArrayList<>();

        userAdapter = new UserAdapter(getContext(), listUser, listener);

        GridLayoutManager gridLayoutManager = new GridLayoutManager(getContext(), 1, GridLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(gridLayoutManager);

        recyclerView.setAdapter(userAdapter);

        // Get data from firebase
        mDatabase = FirebaseDatabase.getInstance().getReference("Users");
        Query myTopPostsQuery = mDatabase.orderByChild("fullName");
        myTopPostsQuery.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                listUser.clear();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()){
                    User user = dataSnapshot.getValue(User.class);
                    if(user.getRole().equals(Common.RoleTrainee)){
                        listUser.add(user);
                    }
                }
                userAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
    }
}