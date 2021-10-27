package com.example.languages_learning_app.Adapters.Trainee;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.languages_learning_app.DTO.Score;
import com.example.languages_learning_app.R;

import java.util.ArrayList;


public class ScorePracticeAdapter extends RecyclerView.Adapter<ScorePracticeAdapter.ViewHolder> {
    Context context;
    ArrayList<Score> scores;

    private RecyclerViewClickListener listener;


    public ScorePracticeAdapter(Context context, ArrayList<Score> scores, RecyclerViewClickListener listener){
        this.context = context;
        this.scores = scores;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_lesson_practice, parent,false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ScorePracticeAdapter.ViewHolder holder, int position) {
        Score score = scores.get(position);

        holder.tvLessonName.setText(score.getLessonName());
        holder.tvEasyPracticeCount.setText(score.getPracticeEasyPercentile() + " %");
        holder.tvHardPracticeCount.setText(score.getPracticeHardPercentile() + " %");
    }

    @Override
    public int getItemCount() {
        return scores.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        TextView tvLessonName, tvEasyPracticeCount, tvHardPracticeCount;
        TextView tvEasyPractice, tvHardPractice;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            tvLessonName = itemView.findViewById(R.id.tvLessonName);
            tvEasyPracticeCount = itemView.findViewById(R.id.tvEasyPracticeCount);
            tvHardPracticeCount = itemView.findViewById(R.id.tvHardPracticeCount);

            tvEasyPractice = itemView.findViewById(R.id.tvEasyPractice);
            tvHardPractice = itemView.findViewById(R.id.tvHardPractice);

            tvEasyPractice.setOnClickListener(this);
            tvHardPractice.setOnClickListener(this);
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
