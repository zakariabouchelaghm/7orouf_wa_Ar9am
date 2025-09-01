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


public class listen_choose_letters extends Fragment {
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
        View view=inflater.inflate(R.layout.fragment_listen_choose_letters, container, false);
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
        questions.add(new Question(R.raw._12, new int[]{R.drawable.djim_1, R.drawable.ta_1, R.drawable.noun_1}, R.drawable.ta_1));
        questions.add(new Question(R.raw._16, new int[]{R.drawable.kha_1, R.drawable.yea_1, R.drawable.alef_1}, R.drawable.kha_1));
        questions.add(new Question(R.raw._20, new int[]{R.drawable.sin, R.drawable.tha_1, R.drawable.za_1}, R.drawable.za_1));
        questions.add(new Question(R.raw._28, new int[]{R.drawable.ghayn_1, R.drawable.ain_1, R.drawable.fa_1}, R.drawable.ghayn_1));
        questions.add(new Question(R.raw._10, new int[]{R.drawable.alef_1, R.drawable.da_1, R.drawable.wew_1}, R.drawable.alef_1));
        questions.add(new Question(R.raw._37, new int[]{R.drawable.yea_1, R.drawable.chin_1, R.drawable.h7a_1}, R.drawable.yea_1));
        questions.add(new Question(R.raw._34, new int[]{R.drawable.noun_1, R.drawable.tha_1, R.drawable.taa_1}, R.drawable.noun_1));
        questions.add(new Question(R.raw._11, new int[]{R.drawable.ba_1, R.drawable.thaa_1, R.drawable.ghayn_1}, R.drawable.ba_1));
        questions.add(new Question(R.raw._13, new int[]{R.drawable.tha_1, R.drawable.da_1, R.drawable.thal_1}, R.drawable.tha_1));
        questions.add(new Question(R.raw._16, new int[]{R.drawable.kha_1, R.drawable.ba_1, R.drawable.yea_1}, R.drawable.kha_1));
        questions.add(new Question(R.raw._14, new int[]{R.drawable.djim_1, R.drawable.sad_1, R.drawable.kaf_1}, R.drawable.djim_1));
        questions.add(new Question(R.raw._25, new int[]{R.drawable.taa_1, R.drawable.alef_1, R.drawable.ba_1}, R.drawable.taa_1));

        questions.add(new Question(R.raw._17, new int[]{R.drawable.da_1, R.drawable.ta_1, R.drawable.fa_1}, R.drawable.da_1));
        questions.add(new Question(R.raw._18, new int[]{R.drawable.thal_1, R.drawable.da_1, R.drawable.ha_1}, R.drawable.thal_1));
        questions.add(new Question(R.raw._36, new int[]{R.drawable.wew_1, R.drawable.thaa_1, R.drawable.kaf_1}, R.drawable.wew_1));


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