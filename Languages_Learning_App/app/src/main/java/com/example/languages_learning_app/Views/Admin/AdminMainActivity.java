package com.example.languages_learning_app.Views.Admin;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.os.Bundle;
import android.view.MenuItem;

import com.example.languages_learning_app.R;
import com.example.languages_learning_app.Views.GeneralMoreFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class AdminMainActivity extends AppCompatActivity {

    public BottomNavigationView bottomNav;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_main);

        getSupportFragmentManager().beginTransaction()
                .replace(R.id.admin_fragment_container, new AdminLanguageFragment()).commit();

        bottomNav = findViewById(R.id.admin_bottom_navigation);
        bottomNav.setOnNavigationItemSelectedListener(navListener);
    }

    private BottomNavigationView.OnNavigationItemSelectedListener navListener =
            new BottomNavigationView.OnNavigationItemSelectedListener() {
                @Override
                public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                    Fragment selectedFragment = null;

                    switch (item.getItemId()) {
                        case R.id.nav_language:
                            selectedFragment = new AdminLanguageFragment();
                            break;
                        case R.id.nav_manager:
                            selectedFragment = new AdminManagerFragment();
                            break;
                        case R.id.nav_trainee:
                            selectedFragment = new AdminTraineeFragment();
                            break;
                        case R.id.nav_admin_more:
                            selectedFragment = new GeneralMoreFragment();
                            break;
                    }
                    getSupportFragmentManager().beginTransaction()
                            .replace(R.id.admin_fragment_container, selectedFragment).commit();
                    return true;
                }
            };
}