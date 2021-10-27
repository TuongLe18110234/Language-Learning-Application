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

import com.example.languages_learning_app.DTO.User;
import com.example.languages_learning_app.R;

import java.util.ArrayList;


public class UserAdapter extends RecyclerView.Adapter<UserAdapter.ViewHolder>{
    Context context;
    ArrayList<User> listUser;

    private RecyclerViewClickListener listener;


    public UserAdapter(Context context, ArrayList<User> listUser, RecyclerViewClickListener listener){
        this.context = context;
        this.listUser = listUser;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_account, parent,false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull UserAdapter.ViewHolder holder, int position) {
        User user = listUser.get(position);

        holder.tvFullName.setText(user.getFullName());
        holder.tvEmail.setText(user.getEmail());
        holder.rbSattus.setChecked(user.isActive());
    }

    @Override
    public int getItemCount() {
        return listUser.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnCreateContextMenuListener, View.OnTouchListener{
        TextView tvFullName, tvEmail;
        RadioButton rbSattus;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            ConstraintLayout itemUser;

            tvFullName = itemView.findViewById(R.id.tvFullName);
            tvEmail = itemView.findViewById(R.id.tvEmail);
            rbSattus = itemView.findViewById(R.id.rbStatusUser);

            rbSattus.setOnTouchListener(this);

            itemView.setOnClickListener(this);

            // Set context menu
            itemUser = itemView.findViewById(R.id.item_language);
            itemUser.setOnCreateContextMenuListener(this);
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
