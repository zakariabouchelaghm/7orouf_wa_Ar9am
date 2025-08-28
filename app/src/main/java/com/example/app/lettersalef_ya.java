package com.example.app;

import android.content.pm.ActivityInfo;
import android.media.MediaPlayer;
import android.os.Bundle;

import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;


public class lettersalef_ya extends Fragment {
    MediaPlayer mp;
    @Override
    public void onPause() {
        super.onPause();
        releasePlayer();
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
    public void play(Integer num){
        releasePlayer();
        String resName = "_" + num;
        int resId = getResources().getIdentifier(resName, "raw", requireContext().getPackageName());
        if (resId != 0) { // make sure resource exists
            mp = MediaPlayer.create(requireContext(), resId);
            if (mp != null) {
                mp.setOnCompletionListener(mediaPlayer -> {
                    releasePlayer();
                });
                try{
                mp.start();}catch(IllegalStateException e){
                    Toast.makeText(getContext(), "MediaPlayer failed to start", Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(getContext(), "MediaPlayer creation failed", Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(getContext(), "Resource not found: " + resName, Toast.LENGTH_SHORT).show();
        }

    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment_lettersalef_ya, container, false);

        int[] ids = {
                R.id.zero, R.id.one, R.id.two, R.id.three,
                R.id.four, R.id.five, R.id.six, R.id.seven,
                R.id.eight, R.id.nine, R.id.ten, R.id.eleven,
                R.id.twelve, R.id.therteen, R.id.fourteen, R.id.fifteen,
                R.id.sixteen, R.id.seventeen, R.id.eighteen, R.id.nineteen,
                R.id.twenty, R.id.twentyone, R.id.twentytwo, R.id.twentythree,
                R.id.twentyfour, R.id.twentyfive, R.id.twentysix, R.id.twentyseven
        };

        for (int i = 0; i < ids.length; i++) {
            final int index = i + 10;  // 1..9
            View v = view.findViewById(ids[i]);
            if (v != null) {
                v.setOnClickListener(view1 -> play(index));

            }
        }



        return view;
    }

}