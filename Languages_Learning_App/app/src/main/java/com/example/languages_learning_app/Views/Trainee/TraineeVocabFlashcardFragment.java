package com.example.languages_learning_app.Views.Trainee;

import android.app.ActionBar;
import android.app.Dialog;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.example.languages_learning_app.Common.Common;
import com.example.languages_learning_app.DTO.Lesson;
import com.example.languages_learning_app.DTO.Vocabulary;
import com.example.languages_learning_app.R;
import com.squareup.picasso.Picasso;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Random;

public class TraineeVocabFlashcardFragment extends Fragment {

    private static final String SELECTED_LESSON = "Lesson";
    private static final String LEARNED_VOCABS = "LearnedList";
    private static final String LIST_VOCABS = "ListVocabs";

    private Lesson lesson;
    private List<Vocabulary> vocabularies;
    private ArrayList<String> learnedList;

    Button btnShowFlashcard, btnRemembered, btnResult;
    Dialog dialog;
    ImageView imgVocab, imgLeft, imgRight, imgClose, imgHeadphones;
    TextView txtWord, txtPronunciation, txtMeaning;

    private Vocabulary currentVocab;
    private TextToSpeech mTTS;


    public TraineeVocabFlashcardFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param lesson selected lesson
     * @param learnedList list vocabulary id that trainee learned
     * @return A new instance of fragment TraineeVocabFlashcardFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static TraineeVocabFlashcardFragment newInstance(Lesson lesson, ArrayList<String> learnedList, List<Vocabulary> vocabularies) {
        TraineeVocabFlashcardFragment fragment = new TraineeVocabFlashcardFragment();
        Bundle args = new Bundle();
        args.putSerializable(SELECTED_LESSON, lesson);
        args.putStringArrayList(LEARNED_VOCABS, learnedList);
        args.putSerializable(LIST_VOCABS, (Serializable) vocabularies);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            lesson = (Lesson) getArguments().getSerializable(SELECTED_LESSON);
            learnedList = getArguments().getStringArrayList(LEARNED_VOCABS);
            vocabularies = (List<Vocabulary>) getArguments().getSerializable(LIST_VOCABS);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root = inflater.inflate(R.layout.fragment_trainee_vocab_flashcard, container, false);

        setTextToSpeech();
        setDialog();

        btnShowFlashcard = root.findViewById(R.id.btnShowDialog);
        btnShowFlashcard.setOnClickListener((View v) -> {
            currentVocab = getRandomVocab();
            showDialog(currentVocab);
        });

        return root;
    }

    private void setTextToSpeech() {
        Locale locale = Common.getLocale(Common.language.getName());
        mTTS = new TextToSpeech(getContext(), new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                if (status == TextToSpeech.SUCCESS) {
                    int result = mTTS.setLanguage(locale);

                    if (result == TextToSpeech.LANG_MISSING_DATA || result == TextToSpeech.LANG_NOT_SUPPORTED) {
                        Log.e("TTS", "Language not supported");
                    }
                } else {
                    Log.e("TTS", "Initialization failed");
                }
            }
        });
    }

    private void speak(String word) {
        float pitch = 1;
        float speed = 1;
        mTTS.setPitch(pitch);
        mTTS.setSpeechRate(speed);

        mTTS.speak(word, TextToSpeech.QUEUE_FLUSH, null);
    }

    @Override
    public void onDestroy() {
        if (mTTS != null) {
            mTTS.stop();
            mTTS.shutdown();
        }

        super.onDestroy();
    }

    private Vocabulary getRandomVocab(){
        Random random = new Random();
        int index = random.nextInt(vocabularies.size());
        return vocabularies.get(index);
    }

    private void setDialog() {
        // Set some attributes for dialog
        dialog = new Dialog(getContext());
        dialog.setContentView(R.layout.diaglog_flash_card);
        dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        Window window = dialog.getWindow();
        window.setGravity(Gravity.CENTER);
        window.getAttributes().windowAnimations = R.style.DialogAnimationUpDown;
        dialog.setCancelable(false);
        window.setLayout(ActionBar.LayoutParams.WRAP_CONTENT, ActionBar.LayoutParams.WRAP_CONTENT);

        // Mapping
        imgVocab = dialog.findViewById(R.id.imgVocab);
        imgLeft = dialog.findViewById(R.id.imgLeft);
        imgRight = dialog.findViewById(R.id.imgRight);
        imgClose = dialog.findViewById(R.id.imgClose);
        imgHeadphones = dialog.findViewById(R.id.imgHeadphone);
        txtWord = dialog.findViewById(R.id.txtWord);
        txtPronunciation = dialog.findViewById(R.id.txtPronunciation);
        txtMeaning = dialog.findViewById(R.id.txtMeaning);
        btnResult = dialog.findViewById(R.id.btnResult);

        imgClose.setOnClickListener((View v) -> {
            dialog.cancel();
        });

        imgHeadphones.setOnClickListener((View v) -> {
                speak(currentVocab.getWord());
        });

        imgLeft.setOnClickListener((View v) -> {
            dialog.dismiss();
            currentVocab = getRandomVocab();
            showDialog(currentVocab);
        });

        imgRight.setOnClickListener((View v) -> {
            dialog.dismiss();
            currentVocab = getRandomVocab();
            showDialog(currentVocab);
        });

        btnResult.setOnClickListener((View v) -> {
            txtWord.setText(currentVocab.getWord());
            txtPronunciation.setText(currentVocab.getPronunciation());
            txtMeaning.setText(currentVocab.getMeaning());
        });
    }

    private void showDialog(Vocabulary vocabulary) {

        Random random = new Random();
        int mode = random.nextInt(2);

        if(mode == 0) {
            txtWord.setText(vocabulary.getWord());
            txtPronunciation.setText(vocabulary.getPronunciation());
            txtMeaning.setText("???");
        } else if (mode == 1) {
            txtWord.setText("???");
            txtPronunciation.setText("");
            txtMeaning.setText(vocabulary.getMeaning());
        }

        Picasso.get().load(vocabulary.getImageUrl())
                .fit()
                .centerCrop()
                .into(imgVocab);

        dialog.show();
    }
}