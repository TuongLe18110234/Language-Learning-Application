package com.example.languages_learning_app.Views;

import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.languages_learning_app.Common.Common;
import com.example.languages_learning_app.DTO.User;
import com.example.languages_learning_app.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

public class GeneralRegisterUserActivity extends AppCompatActivity implements View.OnClickListener  {

    private Button registerUser;
    private ImageView ivBack;
    private EditText editTextFullName, editTextPhone, editTextEmail, editTextPassword;
    private ProgressBar progressBar;

    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_general_register_user);

        mAuth = FirebaseAuth.getInstance();

        registerUser = (Button) findViewById(R.id.btRegisterUser);
        registerUser.setOnClickListener(this);

        ivBack = (ImageView) findViewById(R.id.ivBack);
        ivBack.setOnClickListener(this);

        editTextFullName = (EditText) findViewById(R.id.etFullName);
        editTextPhone = (EditText) findViewById(R.id.etPhone);
        editTextEmail = (EditText) findViewById(R.id.etEmail);
        editTextPassword = (EditText) findViewById(R.id.etPassword);

        progressBar = (ProgressBar) findViewById(R.id.progressBar);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btRegisterUser:
                registerUser();
                break;
            case R.id.ivBack:
                finish();
                break;
        }
    }

    private void registerUser() {
        String email = editTextEmail.getText().toString().trim();
        String phone = editTextPhone.getText().toString().trim();
        String password = editTextPassword.getText().toString().trim();
        String fullName = editTextFullName.getText().toString().trim();

        if(fullName.isEmpty()){
            editTextFullName.setError("Ch??a nh???p t??n!");
            editTextFullName.requestFocus();
            return;
        }

        if(phone.isEmpty()){
            editTextPhone.setError("Ch??a nh???p s??? ??i???n tho???i!");
            editTextPhone.requestFocus();
            return;
        }

        if(email.isEmpty()){
            editTextEmail.setError("Ch??a nh???p Email!");
            editTextEmail.requestFocus();
            return;
        }

        if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            editTextEmail.setError("Email kh??ng h???p l???!");
            editTextEmail.requestFocus();
            return;
        }

        if (password.isEmpty()){
            editTextPassword.setError("Ch??a nh???p m???t kh???u!");
            editTextPassword.requestFocus();
            return;
        }

        if(password.length()<8){
            editTextPassword.setError("M???t kh???u t???i ti???u 8 k?? t???!");
            editTextPassword.requestFocus();
            return;
        }

        progressBar.setVisibility(View.VISIBLE);
        // T???o user trong auth
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){
                            String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
                            User user = new User.UserBuilder(Common.RoleTrainee, false)
                                    .setUserId(userId)
                                    .setFullName(fullName)
                                    .setEmail(email)
                                    .setPhone(phone)
                                    .setActive(true)
                                    .build();
                            FirebaseDatabase.getInstance().getReference("Users")
                                    .child(userId)
                                    .setValue(user).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if(task.isSuccessful()){
                                        Toast.makeText(GeneralRegisterUserActivity.this, "????ng k?? th??nh c??ng", Toast.LENGTH_LONG).show();
                                        progressBar.setVisibility(View.GONE);
                                    }
                                    else{
                                        Toast.makeText(GeneralRegisterUserActivity.this, "L???i ????ng k??!", Toast.LENGTH_LONG).show();
                                        progressBar.setVisibility(View.GONE);
                                    }
                                }
                            });
                        }
                        else {
                            Toast.makeText(GeneralRegisterUserActivity.this, "L???i ????ng k??!", Toast.LENGTH_LONG).show();
                            progressBar.setVisibility(View.GONE);
                        }
                    }
                });
    }
}