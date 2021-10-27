package com.example.languages_learning_app.Adapters;

import android.content.Context;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.example.languages_learning_app.Common.Common;
import com.example.languages_learning_app.DTO.Language;
import com.example.languages_learning_app.R;

import java.util.ArrayList;


public class LanguageAdapter extends RecyclerView.Adapter<LanguageAdapter.ViewHolder>{
    Context context;
    ArrayList<Language> listLanguage;
    int imageLanguage;

    private RecyclerViewClickListener listener;


    public LanguageAdapter(Context context, ArrayList<Language> listLanguage, RecyclerViewClickListener listener){
        this.context = context;
        this.listLanguage = listLanguage;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_language, parent,false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull LanguageAdapter.ViewHolder holder, int position) {
        Language language = listLanguage.get(position);

        imageLanguage = Common.getFlagLanguage(language.getName());

        holder.imageView.setImageResource(imageLanguage);
        holder.tvLanguage.setText(language.getDisplayName());
        holder.rbIsChecked.setChecked(language.isStatus());
    }

    @Override
    public int getItemCount() {
        return listLanguage.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnCreateContextMenuListener, View.OnTouchListener{
        ImageView imageView;
        TextView tvLanguage;
        RadioButton rbIsChecked;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            ConstraintLayout itemLanguage;

            imageView = itemView.findViewById(R.id.imageView);
            tvLanguage = itemView.findViewById(R.id.tvLanguage);
            rbIsChecked = itemView.findViewById(R.id.rbIsActivedLanguage);

            rbIsChecked.setOnTouchListener(this);

            itemView.setOnClickListener(this);

            // Set context menu
            itemLanguage = itemView.findViewById(R.id.item_language);
            itemLanguage.setOnCreateContextMenuListener(this);
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
