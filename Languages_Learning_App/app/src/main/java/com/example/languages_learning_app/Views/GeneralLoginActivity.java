package com.example.languages_learning_app.Views;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.languages_learning_app.Common.Common;
import com.example.languages_learning_app.DTO.User;
import com.example.languages_learning_app.R;
import com.example.languages_learning_app.Views.Admin.AdminMainActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class GeneralLoginActivity extends AppCompatActivity implements View.OnClickListener {

    private TextView register, fogotPassword;
    private EditText etEmail, etPassword;
    private Button signIn;
    private RadioButton rbAdmin, rbManager, rbTrainee;
    private ProgressBar progressBar;

    CheckBox checkBox;
    SharedPreferences sharedPreferences;

    private FirebaseAuth mAuth;
    DatabaseReference mDatabase;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_general_login);

        mapAndSetView();
        rememberUser();
    }
    
    private void mapAndSetView(){
        etEmail = (EditText) findViewById(R.id.etEmail);
        etPassword = (EditText) findViewById(R.id.etPassword);

        progressBar = (ProgressBar) findViewById(R.id.progressBar);

        rbAdmin = (RadioButton) findViewById(R.id.rbAdmin);
        rbManager = (RadioButton) findViewById(R.id.rbManager);
        rbTrainee = (RadioButton) findViewById(R.id.rbTrainee);

        register = (TextView) findViewById((R.id.tvRegister));
        register.setOnClickListener(this);

        signIn = (Button) findViewById(R.id.btLogin);
        signIn.setOnClickListener(this);

        fogotPassword = (TextView) findViewById(R.id.tvForgotPassword);
        fogotPassword.setOnClickListener(this);

        //Shared Preferences
        sharedPreferences = getSharedPreferences("dataLogin", MODE_PRIVATE);
        checkBox = findViewById(R.id.checkBox);

        mAuth = FirebaseAuth.getInstance();
    }

    public void rememberUser(){
        etEmail.setText(sharedPreferences.getString("email",""));
        etPassword.setText(sharedPreferences.getString("password",""));
        checkBox.setChecked(sharedPreferences.getBoolean("checked",false));
    }

    private void addRememberMe(String emailLogin, String passLogin){
        SharedPreferences.Editor editor = sharedPreferences.edit();
        if (checkBox.isChecked()) {
            editor.putString("email", emailLogin);
            editor.putString("password", passLogin);
            editor.putBoolean("checked", true);
        } else {
            editor.remove("email");
            editor.remove("password");
            editor.remove("checked");
        }
        editor.commit();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.tvRegister:
                startActivity(new Intent(this, GeneralRegisterUserActivity.class));
                break;
            case R.id.btLogin:
                userLogin();
                break;
            case R.id.tvForgotPassword:
                startActivity(new Intent(this, GeneralFogotPasswordActivity.class));
                break;
        }
    }

    private void userLogin() {
        String email = etEmail.getText().toString().trim();
        String password = etPassword.getText().toString().trim();

        if (email.isEmpty()){
            etEmail.setError("Chưa nhập Email!");
            etEmail.requestFocus();
            return;
        }

        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            etEmail.setError("Email không hợp lệ!");
            etEmail.requestFocus();
            return;
        }

        if(password.isEmpty()) {
            etPassword.setError("Chưa nhập mật khẩu!");
            etPassword.requestFocus();
            return;
        }

        if(password.length() < 8){
            etPassword.setError("Mật khẩu tối thiểu 8 ký tự!");
            etPassword.requestFocus();
            return;
        }

        progressBar.setVisibility(View.VISIBLE);

        //Add email to auth
        mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    // Validate Email
                    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                    if(user.isEmailVerified()){
                        chooseRole(user.getUid());
                        addRememberMe(email, password);
                    }
                    else{
                        user.sendEmailVerification();
                        Toast.makeText(GeneralLoginActivity.this, "Kiểm tra Email để kích hoạt tài khoản", Toast.LENGTH_LONG).show();
                    }
                }
                else {
                    Toast.makeText(GeneralLoginActivity.this, "Sai thông tin đăng nhập", Toast.LENGTH_LONG).show();
                }
                progressBar.setVisibility(View.GONE);
            }
        });
    }

    public void chooseRole(String userId){
        mDatabase = FirebaseDatabase.getInstance().getReference();
        mDatabase.child("Users").child(userId).get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                if (!task.isSuccessful()) {
                }
                else {
                    Common.user = task.getResult().getValue(User.class);
                    
                    if(!Common.user.isActive()){
                        Toast.makeText(GeneralLoginActivity.this, "Tài khoản của bạn đang tạm khóa!", Toast.LENGTH_LONG).show();
                        return;
                    }

                    if(Common.user != null){
                        if (Common.user.getRole().equals(Common.RoleAdmin)) {
                            if (rbAdmin.isChecked()) {
                                Common.role = Common.RoleAdmin;
                                startActivity(new Intent(GeneralLoginActivity.this, AdminMainActivity.class));
                                return;
                            }

                            if (rbManager.isChecked()){
                                Common.role = Common.RoleManager;
                                startActivity(new Intent(GeneralLoginActivity.this, GeneralChooseLanguageActivity.class));
                                return;
                            }

                            if (rbTrainee.isChecked()){
                                Common.role = Common.RoleTrainee;
                                startActivity(new Intent(GeneralLoginActivity.this, GeneralChooseLanguageActivity.class));
                                return;
                            }
                        }
                        if (Common.user.getRole().equals(Common.RoleManager)) {
                            if (rbManager.isChecked()){
                                Common.role = Common.RoleManager;
                                startActivity(new Intent(GeneralLoginActivity.this, GeneralChooseLanguageActivity.class));
                                return;
                            }
                            if (rbTrainee.isChecked()){
                                Common.role = Common.RoleTrainee;
                                startActivity(new Intent(GeneralLoginActivity.this, GeneralChooseLanguageActivity.class));
                                return;
                            }
                        }

                        if (Common.user.getRole().equals(Common.RoleTrainee)) {
                            if (rbTrainee.isChecked()){
                                Common.role = Common.RoleTrainee;
                                startActivity(new Intent(GeneralLoginActivity.this, GeneralChooseLanguageActivity.class));
                                return;
                            }
                        }
                        Toast.makeText(GeneralLoginActivity.this, "Truy cập sai quyền!", Toast.LENGTH_LONG).show();
                    }
                    Toast.makeText(GeneralLoginActivity.this, "Tài khoản của bạn không tồn tại!", Toast.LENGTH_LONG).show();
                }
            }
        });
    }
}