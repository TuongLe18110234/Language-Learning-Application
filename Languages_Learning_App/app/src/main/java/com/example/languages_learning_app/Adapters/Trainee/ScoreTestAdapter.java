package com.example.languages_learning_app.Adapters.Trainee;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.languages_learning_app.Common.Common;
import com.example.languages_learning_app.DTO.Score;
import com.example.languages_learning_app.R;

import java.util.ArrayList;


public class ScoreTestAdapter extends RecyclerView.Adapter<ScoreTestAdapter.ViewHolder> {
    Context context;
    ArrayList<Score> scores;

    private RecyclerViewClickListener listener;


    public ScoreTestAdapter(Context context, ArrayList<Score> scores, RecyclerViewClickListener listener){
        this.context = context;
        this.scores = scores;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_lesson_test, parent,false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ScoreTestAdapter.ViewHolder holder, int position) {
        Score score = scores.get(position);

        holder.tvLessonName.setText(score.getLessonName());
        holder.tvWriting.setText("Việt-" + Common.language.getBriefName());
        holder.tvSelection.setText(Common.language.getBriefName() + "-Việt");

        holder.tvWritingScore.setText(score.getWritingPercentile() + "%");
        holder.tvSelectionScore.setText(score.getSelectionPercentile() + "%");
        holder.tvAudioScore.setText(score.getAudioPercentile() + "%");
    }

    @Override
    public int getItemCount() {
        return scores.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        TextView tvLessonName, tvWriting, tvSelection, tvAudio;
        TextView tvWritingScore, tvSelectionScore, tvAudioScore;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            tvLessonName = itemView.findViewById(R.id.tvLessonName);

            tvWriting = itemView.findViewById(R.id.tvWriting);
            tvSelection = itemView.findViewById(R.id.tvSelection);
            tvAudio = itemView.findViewById(R.id.tvAudio);

            tvWritingScore = itemView.findViewById(R.id.tvWritingScore);
            tvSelectionScore = itemView.findViewById(R.id.tvSelectionScore);
            tvAudioScore = itemView.findViewById(R.id.tvAudioScore);

            tvWriting.setOnClickListener(this);
            tvSelection.setOnClickListener(this);
            tvAudio.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            listener.onClick(v, getAdapterPosition());
        }
    }

    public interface RecyclerViewClickListener{
        void onClick(View v, int position);
    }
}
