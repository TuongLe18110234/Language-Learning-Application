package com.example.languages_learning_app.Adapters.Manager;

import android.content.Context;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.example.languages_learning_app.DTO.YouTubeVideo;
import com.example.languages_learning_app.R;

import java.util.ArrayList;


public class YoutubeVideoAdapter extends RecyclerView.Adapter<YoutubeVideoAdapter.ViewHolder>{
    Context context;
    ArrayList<YouTubeVideo> listVideos;

    private RecyclerViewClickListener listener;


    public YoutubeVideoAdapter(Context context, ArrayList<YouTubeVideo> listVideos, RecyclerViewClickListener listener){
        this.context = context;
        this.listVideos = listVideos;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_video_youtube, parent,false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull YoutubeVideoAdapter.ViewHolder holder, int position) {
        YouTubeVideo video = listVideos.get(position);

        holder.tvSong.setText(video.getSongName());
        holder.tvSinger.setText(video.getSinger());
    }

    @Override
    public int getItemCount() {
        return listVideos.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnCreateContextMenuListener {
        TextView tvSong, tvSinger;
        ImageView imgPlay;
        RelativeLayout itemVideo;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            ConstraintLayout itemLanguage;

            tvSong = itemView.findViewById(R.id.txtVideoName);
            tvSinger = itemView.findViewById(R.id.txtSinger);
            imgPlay = itemView.findViewById(R.id.imgPlay);

            itemView.setOnClickListener(this);

            // Set context menu
            itemVideo = itemView.findViewById(R.id.item_video_youtube);
            itemVideo.setOnCreateContextMenuListener(this);
        }

        @Override
        public void onClick(View v) {
            listener.onClick(itemView, getAdapterPosition());
        }

        @Override
        public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
            listener.onCreateContextMenu(menu, getAdapterPosition());
        }
    }

    public interface RecyclerViewClickListener {
        void onClick(View v, int position);

        void onCreateContextMenu(ContextMenu menu, int position);
    }
}
