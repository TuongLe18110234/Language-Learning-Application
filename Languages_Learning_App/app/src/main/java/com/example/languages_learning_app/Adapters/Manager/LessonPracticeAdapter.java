package com.example.languages_learning_app.Adapters.Manager;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.languages_learning_app.DTO.Lesson;
import com.example.languages_learning_app.R;

import java.util.ArrayList;


public class LessonPracticeAdapter extends RecyclerView.Adapter<LessonPracticeAdapter.ViewHolder>{
    Context context;
    ArrayList<Lesson> listLesson;

    private RecyclerViewClickListener listener;


    public LessonPracticeAdapter(Context context, ArrayList<Lesson> listLesson, RecyclerViewClickListener listener){
        this.context = context;
        this.listLesson = listLesson;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_lesson_practice, parent,false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull LessonPracticeAdapter.ViewHolder holder, int position) {
        Lesson lesson = listLesson.get(position);

        holder.tvLessonName.setText(lesson.getName());
        holder.tvEasyPracticeCount.setText(lesson.getEasyPracticeCount() + " câu");
        holder.tvHardPracticeCount.setText(lesson.getHardPracticeCount() + " câu");
    }

    @Override
    public int getItemCount() {
        return listLesson.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        TextView tvLessonName, tvEasyPracticeCount, tvHardPracticeCount;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            tvLessonName = itemView.findViewById(R.id.tvLessonName);
            tvEasyPracticeCount = itemView.findViewById(R.id.tvEasyPracticeCount);
            tvHardPracticeCount = itemView.findViewById(R.id.tvHardPracticeCount);

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
