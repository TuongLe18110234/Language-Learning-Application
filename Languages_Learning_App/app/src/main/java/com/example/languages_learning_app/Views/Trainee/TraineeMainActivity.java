package com.example.languages_learning_app.Views.Trainee;

import android.os.Bundle;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.example.languages_learning_app.R;
import com.example.languages_learning_app.Views.GeneralMoreFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class TraineeMainActivity extends AppCompatActivity {

    public BottomNavigationView bottomNav;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trainee_main);
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment_container, new TraineeHomeFragment()).commit();

        bottomNav = findViewById(R.id.bottom_navigation);
        bottomNav.setOnNavigationItemSelectedListener(navListener);
    }

    private BottomNavigationView.OnNavigationItemSelectedListener navListener =
            new BottomNavigationView.OnNavigationItemSelectedListener() {
                @Override
                public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                    Fragment selectedFragment = null;

                    switch (item.getItemId()) {
                        case R.id.nav_home:
                            selectedFragment = new TraineeHomeFragment();
                            break;
                        case R.id.nav_flashcard:
                            selectedFragment = new TraineeWordFragment();
                            break;
                        case R.id.nav_test:
                            selectedFragment = new TraineeTestFragment();
                            break;
                        case R.id.nav_practice:
                            selectedFragment = new TraineePracticeFragment();
                            break;
                        case R.id.nav_more:
                            selectedFragment = new GeneralMoreFragment();
                            break;
                    }
                        getSupportFragmentManager().beginTransaction()
                                .replace(R.id.fragment_container, selectedFragment).commit();
                        return true;
                    }
            };
}