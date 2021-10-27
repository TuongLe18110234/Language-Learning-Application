package com.example.languages_learning_app.Views.Manager;

import android.os.Bundle;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.example.languages_learning_app.R;
import com.example.languages_learning_app.Views.GeneralMoreFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class ManagerMainActivity extends AppCompatActivity {

    public BottomNavigationView bottomNav;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manager_main);

        getSupportFragmentManager().beginTransaction()
                .replace(R.id.manager_fragment_container, new ManagerLessonFragment()).commit();

        bottomNav = findViewById(R.id.manager_bottom_navigation);
        bottomNav.setOnNavigationItemSelectedListener(navListener);
    }

    private BottomNavigationView.OnNavigationItemSelectedListener navListener =
            new BottomNavigationView.OnNavigationItemSelectedListener() {
                @Override
                public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                    Fragment selectedFragment = null;

                    switch (item.getItemId()) {
                        case R.id.nav_lesson:
                            selectedFragment = new ManagerLessonFragment();
                            break;
                        case R.id.nav_word:
                            selectedFragment = new ManagerWordFragment();
                            break;
                        case R.id.nav_practice:
                            selectedFragment = new ManagerPracticeFragment();
                            break;
                        case R.id.nav_music:
                            selectedFragment = new ManagerMusicFragment();
                            break;
                        case R.id.nav_admin_more:
                            selectedFragment = new GeneralMoreFragment();
                            break;
                    }
                    getSupportFragmentManager().beginTransaction()
                            .replace(R.id.manager_fragment_container, selectedFragment).commit();
                    return true;
                }
            };
}