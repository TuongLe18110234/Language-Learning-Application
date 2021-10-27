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

import com.example.languages_learning_app.DTO.Practice;
import com.example.languages_learning_app.R;

import java.util.ArrayList;


public class PracticeAdapter extends RecyclerView.Adapter<PracticeAdapter.ViewHolder>{
    Context context;
    ArrayList<Practice> practices;

    private RecyclerViewClickListener listener;


    public PracticeAdapter(Context context, ArrayList<Practice> practices, RecyclerViewClickListener listener){
        this.context = context;
        this.practices = practices;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_practice, parent,false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PracticeAdapter.ViewHolder holder, int position) {
        Practice practice = practices.get(position);

        holder.tvCorrectAnswer.setText(practice.getCorrectAnswer());
        holder.tvPracticeSentence.setText(practice.getSentence());
        holder.rbStatus.setChecked(practice.getStatus());
    }

    @Override
    public int getItemCount() {
        return practices.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnCreateContextMenuListener, View.OnTouchListener{
        TextView tvCorrectAnswer, tvPracticeSentence;
        RadioButton rbStatus;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            ConstraintLayout itemPractice;

            tvCorrectAnswer = itemView.findViewById(R.id.tvCorrectAnswer);
            tvPracticeSentence = itemView.findViewById(R.id.tvPracticeSentence);
            rbStatus = itemView.findViewById(R.id.rbStatusPractice);

            rbStatus.setOnTouchListener(this);

            itemView.setOnClickListener(this);

            // Set context menu
            itemPractice = itemView.findViewById(R.id.item_manager_practice);
            itemPractice.setOnCreateContextMenuListener(this);
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
