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

import de.hdodenhof.circleimageview.CircleImageView;


public class ScoreRankAdapter extends RecyclerView.Adapter<ScoreRankAdapter.ViewHolder> {
    Context context;
    ArrayList<Score> scores;
    int imageLanguage;

    public ScoreRankAdapter(Context context, ArrayList<Score> scores){
        this.context = context;
        this.scores = scores;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_final_score, parent,false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ScoreRankAdapter.ViewHolder holder, int position) {
        Score score = scores.get(position);

        holder.tvRank.setText(String.valueOf(position + 1));
        holder.tvTraineeName.setText(score.getTraineeName());
        holder.tvTotalScore.setText(String.valueOf(score.getTotalScore()));

        imageLanguage = Common.getFlagLanguage(Common.language.getName());
        holder.imgAvatar.setImageResource(imageLanguage);
    }

    @Override
    public int getItemCount() {
        return scores.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder{
        TextView tvRank, tvTraineeName, tvTotalScore;
        CircleImageView imgAvatar;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            tvRank = itemView.findViewById(R.id.tvRank);
            tvTraineeName = itemView.findViewById(R.id.tvTraineeName);
            tvTotalScore = itemView.findViewById(R.id.tvTotalScore);
            imgAvatar = itemView.findViewById(R.id.imgAvatar);
        }
    }
}
