package com.example.languages_learning_app.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.example.languages_learning_app.DTO.Lesson;
import com.example.languages_learning_app.R;

import java.util.ArrayList;


public class LessonVocabAdapter extends RecyclerView.Adapter<LessonVocabAdapter.ViewHolder>{

    private static final int MANAGER_MODE = 0;
    private static final int TRAINEE_MODE = 1;

    Context context;
    ArrayList<Lesson> listLesson;
    int mode;

    private RecyclerViewClickListener listener;


    public LessonVocabAdapter(Context context, ArrayList<Lesson> listLesson, int mode, RecyclerViewClickListener listener){
        this.context = context;
        this.listLesson = listLesson;
        this.mode = mode;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_vocab_lesson, parent,false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull LessonVocabAdapter.ViewHolder holder, int position) {
        Lesson lesson = listLesson.get(position);

        holder.tvLessonName.setText(lesson.getName());
        holder.tvLessonDescription.setText(lesson.getDescription());
        if(mode == MANAGER_MODE) {
            holder.tvWordCount.setText(String.valueOf(lesson.getWordCount()));
        } else if(mode == TRAINEE_MODE) {
            holder.tvWordCount.setText(String.valueOf(lesson.getActiveWordCount()));
        }
    }

    @Override
    public int getItemCount() {
        return listLesson.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        TextView tvLessonName, tvLessonDescription, tvWordCount;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            ConstraintLayout itemLesson;

            tvLessonName = itemView.findViewById(R.id.txtLessonName);
            tvLessonDescription = itemView.findViewById(R.id.txtLessonDescription);
            tvWordCount = itemView.findViewById(R.id.txtActiveWordCount);

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            listener.onClick(itemView, getAdapterPosition());
        }
    }

    public interface RecyclerViewClickListener{
        void onClick(View v, int position);
    }
}
