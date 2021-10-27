package com.example.languages_learning_app.Adapters;

import android.content.Context;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.example.languages_learning_app.DTO.Lesson;
import com.example.languages_learning_app.R;

import java.util.ArrayList;


public class LessonAdapter extends RecyclerView.Adapter<LessonAdapter.ViewHolder>{
    Context context;
    ArrayList<Lesson> listLesson;

    private RecyclerViewClickListener listener;


    public LessonAdapter(Context context, ArrayList<Lesson> listLesson, RecyclerViewClickListener listener){
        this.context = context;
        this.listLesson = listLesson;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_lesson, parent,false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull LessonAdapter.ViewHolder holder, int position) {
        Lesson lesson = listLesson.get(position);

        holder.tvLessonName.setText(lesson.getName());
        holder.tvDescription.setText(lesson.getDescription());
        holder.rbIsChecked.setChecked(lesson.isStatus());
    }

    @Override
    public int getItemCount() {
        return listLesson.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnCreateContextMenuListener, View.OnTouchListener{
        TextView tvLessonName, tvDescription;
        RadioButton rbIsChecked;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            ConstraintLayout itemLesson;

            tvLessonName = itemView.findViewById(R.id.tvLessonName);
            tvDescription = itemView.findViewById(R.id.tvDescription);
            rbIsChecked = itemView.findViewById(R.id.rbStatusLesson);

            rbIsChecked.setOnTouchListener(this);

            itemView.setOnClickListener(this);

            // Set context menu
            itemLesson = itemView.findViewById(R.id.item_lesson);
            itemLesson.setOnCreateContextMenuListener(this);
        }

        @Override
        public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
            listener.onCreateContextMenu(menu, getAdapterPosition());
        }

        @Override
        public void onClick(View v) {
            listener.onClick(itemView, getAdapterPosition());
        }

        @Override
        public boolean onTouch(View v, MotionEvent event) {
            listener.onTouch(itemView, getAdapterPosition());
            return true;
        }
    }

    public interface RecyclerViewClickListener{
        void onClick(View v, int position);

        void onCreateContextMenu(ContextMenu menu, int position);

        void onTouch(View v, int position);
    }
}
