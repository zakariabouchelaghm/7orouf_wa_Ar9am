package com.example.app;

import android.content.pm.ActivityInfo;
import android.media.MediaPlayer;
import android.os.Bundle;

import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.material.card.MaterialCardView;


public class numbers_write extends Fragment {

    MediaPlayer mp;

    final boolean[] isDrawing = {false};
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
        // unlock / restore when you leave the fragment
        requireActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED);
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_numbers, container, false);
        DrawingView drawingView = view.findViewById(R.id.drawingView);
        Button clearButton = view.findViewById(R.id.clear);
        clearButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawingView.clearCanvas();
            }
        });
        drawingView.setStrokeWidth(90);


        Button sendButton = view.findViewById(R.id.send);
        MaterialCardView MaterialCardView=view.findViewById(R.id.card);
        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!isDrawing[0]) return;
                drawingView.sendDrawingToServer(predicted -> {
                    // Here you have the predicted integer

                    String resName = "_" + predicted;
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
                        drawingView.clearCanvas();
                });
            }
        });

        Button startButton = view.findViewById(R.id.start);
        startButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!isDrawing[0]) {

                    drawingView.setDrawingEnabled(true);
                    startButton.setText("توقف");
                    isDrawing[0] = true;
                } else {
                    // Stop drawing
                    drawingView.setDrawingEnabled(false);
                    drawingView.clearCanvas();
                    startButton.setText("بدأ");
                    isDrawing[0] = false;
                    if (mp != null) {
                        mp.stop();
                        mp.release();
                        mp = null;
                    }
                }
            }
        });

        return view;
    }




}