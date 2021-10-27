package com.example.languages_learning_app.Views.Manager;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.languages_learning_app.Adapters.Manager.YoutubeVideoAdapter;
import com.example.languages_learning_app.Common.Common;
import com.example.languages_learning_app.DAO.YoutubeVideoDAO;
import com.example.languages_learning_app.DTO.YouTubeVideo;
import com.example.languages_learning_app.R;
import com.example.languages_learning_app.Views.GeneralPlayVideoActivity;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.Serializable;
import java.util.ArrayList;

public class ManagerMusicFragment extends Fragment {

    // Constants
    private static final int INSERT_MODE = 0;
    private static final int UPDATE_MODE = 1;

    // Database
    DatabaseReference mDatabase;
    ArrayList<YouTubeVideo> listVideo;

    // Recycler View
    RecyclerView recyclerView;
    YoutubeVideoAdapter adapter;
    private YoutubeVideoAdapter.RecyclerViewClickListener listener;

    // Form dialog
    private Dialog dialog;
    private EditText edVideoName, edSinger, edVideoId, edLyric;
    private Button btnSave, btnClose;

    // Add video
    FloatingActionButton fabAdd;

    public ManagerMusicFragment() {
        // Required empty public constructor
    }

    public static ManagerMusicFragment newInstance() {
        ManagerMusicFragment fragment = new ManagerMusicFragment();

        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_manager_music, container, false);

        setToolbarWithoutBack(root);
        setOnClickListener();
        setRecyclerView(root);

        fabAdd = root.findViewById(R.id.fabAdd);
        fabAdd.setOnClickListener((View v) -> {
            openDialog(INSERT_MODE, new YouTubeVideo());
        });

        return root;
    }

    private void setOnClickListener() {
        listener = new YoutubeVideoAdapter.RecyclerViewClickListener() {
            @Override
            public void onClick(View v, int position) {
                YouTubeVideo video = listVideo.get(position);

                Bundle bundle = new Bundle();
                bundle.putSerializable("video", (Serializable) video);

                Intent intent = new Intent(getActivity(), GeneralPlayVideoActivity.class);
                intent.putExtras(bundle);
                startActivity(intent);
            }

            @Override
            public void onCreateContextMenu(ContextMenu menu, int position) {
                // Add context menu
                menu.add(position,0,0,"Chỉnh sửa");
                menu.add(position,1,1,"Xóa");
            }
        };
    }

    @Override
    public boolean onContextItemSelected(@NonNull MenuItem item) {
        int position = item.getGroupId();
        YouTubeVideo video = listVideo.get(position);

        switch (item.getItemId()){
            case 0:
                // Open dialog with update mode when selecting update
                openDialog(UPDATE_MODE, video);
                return true;
            case 1:
                // Delete song when selecting delete);
                if(YoutubeVideoDAO.getInstance().delete(video.getId())){
                    Toast.makeText(getContext(), "Xóa bài hát thành công!", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getContext(), "Đã xảy ra lỗi. Vui lòng thử lại!", Toast.LENGTH_SHORT).show();
                }
                return true;
        }
        return super.onContextItemSelected(item);
    }

    /**
     * Set layout manager, adapter for recycler view
     * @param root: this fragment
     */
    private void setRecyclerView(View root){
        recyclerView = root.findViewById(R.id.rvListSongs);
        listVideo = new ArrayList<>();

        adapter = new YoutubeVideoAdapter(getContext(), listVideo, listener);

        GridLayoutManager gridLayoutManager = new GridLayoutManager(getContext(), 1, GridLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(gridLayoutManager);

        recyclerView.setAdapter(adapter);

        // Get data from firebase
        // Data is list lessons
        mDatabase = FirebaseDatabase.getInstance().getReference("Videos");
        mDatabase.child(Common.language.getId()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                listVideo.clear();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()){
                    YouTubeVideo video = dataSnapshot.getValue(YouTubeVideo.class);
                    listVideo.add(video);
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    // Toolbar on the top of screen
    private void setToolbarWithoutBack(View root) {
        // Set name for activity
        TextView txtToolbarName = root.findViewById(R.id.activity_name);
        txtToolbarName.setText("Youtube");
    }

    private void openDialog(int mode, YouTubeVideo video) {
        // Create view of full screen layout
        View view = getLayoutInflater().inflate(R.layout.dialog_fullscreen_save_videos, null);
        dialog = new Dialog(getContext(), android.R.style.Theme_DeviceDefault_Light_NoActionBar);
        dialog.setContentView(view);

        // Mapping
        edVideoName = dialog.findViewById(R.id.edVideoName);
        edSinger = dialog.findViewById(R.id.edSinger);
        edVideoId = dialog.findViewById(R.id.edVideoId);
        edLyric = dialog.findViewById(R.id.edLyric);
        btnSave = dialog.findViewById(R.id.btnSave);
        btnClose = dialog.findViewById(R.id.btnClose);

        // Toolbar on top of the dialog
        setToolbar();

        if(mode == UPDATE_MODE) {
            edVideoName.setText(video.getSongName());
            edSinger.setText(video.getSinger());
            edVideoId.setText(video.getVideoId());
            edLyric.setText(video.getLyric());
        }

        btnSave.setOnClickListener((View v) -> {
            video.setSongName(edVideoName.getText().toString());
            video.setSinger(edSinger.getText().toString());
            video.setVideoId(edVideoId.getText().toString());
            video.setLyric(edLyric.getText().toString());

            mDatabase = FirebaseDatabase.getInstance().getReference("Videos").child(Common.language.getId());
            if(mode == INSERT_MODE){
                String id = mDatabase.push().getKey();
                video.setId(id);
            }
            try {
                mDatabase.child(video.getId()).setValue(video);
                Toast.makeText(getContext(), "Lưu thành công!", Toast.LENGTH_SHORT).show();
                dialog.dismiss();
            } catch (Exception e) {
                Toast.makeText(getContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
            }

        });

        btnClose.setOnClickListener((View v) -> {
            dialog.dismiss();
        });

        dialog.show();
    }

    // Toolbar on the top of screen
    private void setToolbar() {
        // Finish activity when clicking on back item
        ImageView backIcon = dialog.findViewById(R.id.left_icon);
        backIcon.setOnClickListener((View v) -> {
            dialog.dismiss();
        });

        // Set name for activity
        TextView txtToolbarName = dialog.findViewById(R.id.activity_name);
        txtToolbarName.setText("Thông tin video");
    }
}