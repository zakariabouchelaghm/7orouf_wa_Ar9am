package com.example.app;

import android.content.pm.ActivityInfo;
import android.media.MediaPlayer;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.material.card.MaterialCardView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


public class Listen_choose_numbers extends Fragment {
    @Override
    public void onResume() {
        super.onResume();
        // lock orientation when this fragment is visible
        requireActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
    }

    public void releasePlayer(){
        if (mp != null) {
            try {
                mp.reset();
                mp.release();
            } catch (Exception ignored) {}
            mp = null;
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        releasePlayer();
        requireActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED);
    }
    private MaterialCardView playSoundButton;
    private ImageView option1Button,option2Button,option3Button;
    private List<Question> questions;
    private int currentQuestionIndex=0;
    private MediaPlayer mp;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

       View view =inflater.inflate(R.layout.fragment_listen_choose_numbers, container, false);
       playSoundButton=view.findViewById(R.id.playSoundCard);
       option1Button=view.findViewById(R.id.option1);
       option2Button=view.findViewById(R.id.option2);
       option3Button=view.findViewById(R.id.option3);

       loadQuestions();
       showQuestion();

       playSoundButton.setOnClickListener(v->playSound());
       option1Button.setOnClickListener(v->checkAnswer(0));
       option2Button.setOnClickListener(v->checkAnswer(1));
       option3Button.setOnClickListener(v->checkAnswer(2));


       return view;
    }

    private void loadQuestions() {
        questions = new ArrayList<>();
        questions.add(new Question(R.raw.__5, new int[]{R.drawable.one, R.drawable.nine, R.drawable.five}, R.drawable.five));
        questions.add(new Question(R.raw.__1, new int[]{R.drawable.three, R.drawable.one, R.drawable.two}, R.drawable.one));
        questions.add(new Question(R.raw.__7, new int[]{R.drawable.seven, R.drawable.four, R.drawable.eight}, R.drawable.seven));
        questions.add(new Question(R.raw.__9, new int[]{R.drawable.one, R.drawable.nine, R.drawable.four}, R.drawable.nine));
        questions.add(new Question(R.raw.__3, new int[]{R.drawable.seven, R.drawable.four, R.drawable.three}, R.drawable.three));
        questions.add(new Question(R.raw.__0, new int[]{R.drawable.one, R.drawable.zero, R.drawable.nine}, R.drawable.zero));
        questions.add(new Question(R.raw.__2, new int[]{R.drawable.two, R.drawable.five, R.drawable.three}, R.drawable.two));
        questions.add(new Question(R.raw.__6, new int[]{R.drawable.three, R.drawable.seven, R.drawable.six}, R.drawable.six));
        questions.add(new Question(R.raw.__8, new int[]{R.drawable.eight, R.drawable.one, R.drawable.two}, R.drawable.eight));


        Collections.shuffle(questions);
    }

    private void showQuestion() {
        if(mp!=null){
            mp.release();
        }
        if(currentQuestionIndex<questions.size()){
            Question question=questions.get(currentQuestionIndex);
            option1Button.setImageResource(question.getImageResIds()[0]);
            option2Button.setImageResource(question.getImageResIds()[1]);
            option3Button.setImageResource(question.getImageResIds()[2]);
        }
        else{
            Toast.makeText(getContext(),"انتهى!",Toast.LENGTH_SHORT).show();
            requireActivity()
                    .getSupportFragmentManager()
                    .popBackStack(); // go back to previous fragment


        }
    }

    private void playSound(){
        Question question=questions.get(currentQuestionIndex);
        mp=MediaPlayer.create(getContext(),question.getSoundResId());
        mp.start();

    }

    private void checkAnswer(int index){
        Question question=questions.get(currentQuestionIndex);
        int soundResId;
        if(index==question.getCorrectIndex()){
            soundResId = R.raw.correct;
            playSoundButton.setStrokeColor(0xFF00FF00);
            new Handler(Looper.getMainLooper()).postDelayed(() -> {
                playSoundButton.setStrokeColor(0x00000000); // transparent or default color
            }, 500);
        }
        else{
            soundResId = R.raw.incorrect;
            playSoundButton.setStrokeColor(0xFFFF0000);
            new Handler(Looper.getMainLooper()).postDelayed(() -> {
                playSoundButton.setStrokeColor(0x00000000); // transparent or default color
            }, 500);

        }
        MediaPlayer feedbackPlayer = MediaPlayer.create(requireContext(), soundResId);
        feedbackPlayer.setOnCompletionListener(MediaPlayer::release);
        feedbackPlayer.start();

        currentQuestionIndex++;
        showQuestion();
    }



}