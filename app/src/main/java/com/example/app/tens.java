package com.example.app;

import android.media.MediaPlayer;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;


public class tens extends Fragment {

    MediaPlayer mp;
    @Override
    public void onPause() {
        super.onPause();
        // stop and release media player if it's playing
        if (mp != null) {
            if (mp.isPlaying()) {
                mp.stop();
            }
            mp.release();
            mp = null;
        }
    }
    public void play(Integer num){
        if (mp != null) {
            if (mp.isPlaying()) {
                mp.stop();
            }
            mp.release();
            mp = null;
        }
        String resName = "__" + num;
        int resId = getResources().getIdentifier(resName, "raw", requireContext().getPackageName());
        if (resId != 0) { // make sure resource exists
            mp = MediaPlayer.create(requireContext(), resId);
            if (mp != null) {
                mp.setOnCompletionListener(mediaPlayer -> {
                    mediaPlayer.release();
                    mp = null;
                });
                mp.start();
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

        View view= inflater.inflate(R.layout.fragment_tens, container, false);
        int[] ids = {R.id.ten,R.id.twenty,R.id.thirty,
                R.id.fourty,R.id.fifty,R.id.sixty,R.id.seventy,R.id.eighty,R.id.ninety,
                R.id.oneh};

        for (int i = 0; i <ids.length; i++) {
            final int index = (i+1)*10;
            View v = view.findViewById(ids[i]);
            v.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    play(index);
                }
            });
        }
        return view;
    }
}