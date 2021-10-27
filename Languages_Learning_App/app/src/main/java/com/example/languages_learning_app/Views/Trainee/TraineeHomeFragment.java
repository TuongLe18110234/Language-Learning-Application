package com.example.languages_learning_app.Views.Trainee;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import com.example.languages_learning_app.Common.Common;
import com.example.languages_learning_app.R;
import com.example.languages_learning_app.Views.GeneralChooseLanguageActivity;

public class TraineeHomeFragment extends Fragment {
    ImageView contry_flag;
    TextView languageTitle;

    CardView cvFlashcard, cvSongs, cvPractice, cvTest, cvTranslate, cvRank;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_trainee_home, container, false);

        setData(view);

        contry_flag.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getContext(), GeneralChooseLanguageActivity.class));
            }
        });

        cvFlashcard = (CardView) view.findViewById(R.id.cvFlashcard);
        cvFlashcard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((TraineeMainActivity)getActivity()).bottomNav.setSelectedItemId(R.id.nav_flashcard);
            }
        });

        cvSongs = (CardView) view.findViewById(R.id.cvSongs);
        cvSongs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getContext(), TraineeSongActivity.class));
            }
        });

        cvTest = (CardView) view.findViewById(R.id.cvTest);
        cvTest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((TraineeMainActivity)getActivity()).bottomNav.setSelectedItemId(R.id.nav_test);
            }
        });

        cvPractice = (CardView) view.findViewById(R.id.cvPractice);
        cvPractice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((TraineeMainActivity)getActivity()).bottomNav.setSelectedItemId(R.id.nav_practice);
            }
        });

        cvTranslate = (CardView) view.findViewById(R.id.cvTranslate);
        cvTranslate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getContext(), TraineeTranslateActivity.class));
            }
        });

        cvRank = (CardView) view.findViewById(R.id.cvStory);
        cvRank.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getContext(), TraineeRankActivity.class));
            }
        });

        return view;
    }

    private void setData(View view) {
        languageTitle = (TextView) view.findViewById(R.id.tvLanguageTitle);
        contry_flag = (ImageView) view.findViewById(R.id.ivChangeLanguage);

        languageTitle.setText(Common.language.getDisplayName());

        int imgLanguage = Common.getFlagLanguage(Common.language.getName());
        contry_flag.setImageResource(imgLanguage);
    }
}