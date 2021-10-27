package com.example.languages_learning_app.Views;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.languages_learning_app.Common.Common;
import com.example.languages_learning_app.DAO.UserDAO;
import com.example.languages_learning_app.DTO.User;
import com.example.languages_learning_app.R;
import com.google.android.material.textfield.TextInputEditText;

import de.hdodenhof.circleimageview.CircleImageView;

public class GeneralProfileActivity extends AppCompatActivity {

    private TextInputEditText tetName, tetEmail, tetPhone;
    private Button btUpdate;
    CircleImageView civAvatar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_general_profile);

        tetName = (TextInputEditText) findViewById(R.id.tetName);
        tetEmail = (TextInputEditText) findViewById(R.id.tetEmail);
        tetPhone = (TextInputEditText) findViewById(R.id.tetPhone);

        civAvatar = findViewById(R.id.imgProfile);
        if(Common.language !=null){
            int imageLanguage = Common.getFlagLanguage(Common.language.getName());
            civAvatar.setImageResource(imageLanguage);
        } else {
            civAvatar.setImageResource(R.drawable.bg_logo);
        }

        fillInUserInfo(Common.user);

        btUpdate = (Button) findViewById(R.id.btUpdate);
        btUpdate.setOnClickListener((View v) -> {
            updateUserProfile();
        });
    }

    private void fillInUserInfo(User user){
        if(user != null){
            tetName.setText(user.getFullName());
            tetEmail.setText(user.getEmail());
            tetPhone.setText(user.getPhone());
        }
    }

    private void updateUserProfile(){
        String name = tetName.getText().toString();
        String phone = tetPhone.getText().toString();

        if (name.isEmpty()){
            tetName.setError("Name is required!");
            tetName.requestFocus();
            return;
        }

        if (phone.isEmpty()){
            tetPhone.setError("Phone is required!");
            tetPhone.requestFocus();
            return;
        }

        User user = Common.user;

        try {
            user.setFullName(name);
            user.setPhone(phone);
            UserDAO.getInstance().setUserValue(user);

            Toast.makeText(getApplicationContext(), "Update profile successfully!", Toast.LENGTH_SHORT).show();
        }
        catch (Exception e){
            Toast.makeText(getApplicationContext(), "Sorry. Update failed!", Toast.LENGTH_SHORT).show();
        }
    }
}