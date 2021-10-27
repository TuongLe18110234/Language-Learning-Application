package com.example.languages_learning_app.Adapters.Manager;

import android.content.Context;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.languages_learning_app.DTO.Vocabulary;
import com.example.languages_learning_app.R;

import java.util.ArrayList;


public class VocabularyAdapter extends RecyclerView.Adapter<VocabularyAdapter.ViewHolder>{
    Context context;
    ArrayList<Vocabulary> vocabularies;

    private RecyclerViewClickListener listener;


    public VocabularyAdapter(Context context, ArrayList<Vocabulary> vocabularies, RecyclerViewClickListener listener){
        this.context = context;
        this.vocabularies = vocabularies;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_vocabulary, parent,false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Vocabulary vocabulary = vocabularies.get(position);

        holder.tvWord.setText(vocabulary.getWord());
        holder.cbActive.setChecked(vocabulary.isActive());
    }

    @Override
    public int getItemCount() {
        return vocabularies.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnCreateContextMenuListener, View.OnTouchListener{
        TextView tvWord;
        CheckBox cbActive;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            LinearLayout itemVocab;

            tvWord = itemView.findViewById(R.id.txtWord);
            cbActive = itemView.findViewById(R.id.cbActive);

            cbActive.setOnTouchListener(this);
            itemView.setOnClickListener(this);

            // Set context menu
            itemVocab = itemView.findViewById(R.id.item_vocabulary);
            itemVocab.setOnCreateContextMenuListener(this);
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
            listener.onTouch(v, getAdapterPosition());
            return true;
        }
    }

    public interface RecyclerViewClickListener{
        void onClick(View v, int position);

        void onCreateContextMenu(ContextMenu menu, int position);

        void onTouch(View v, int position);
    }
}
