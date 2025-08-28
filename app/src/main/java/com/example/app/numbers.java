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


public class numbers extends Fragment {

    MediaPlayer mp;
    private int currentNumber;
    final boolean[] isDrawing = {false};
    @Override
    public void onResume() {
        super.onResume();
        // lock orientation when this fragment is visible
        requireActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
    }

    @Override
    public void onPause() {
        super.onPause();
        if (mp != null) {
            if (mp.isPlaying()) {
                mp.stop();
            }
            mp.release();
            mp = null;
        }
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

                    if(predicted==currentNumber){
                        mp = MediaPlayer.create(requireContext(),R.raw.correct);
                        mp.start();
                        generateNewNumber();
                        MaterialCardView.setStrokeColor(0xFF00FF00);
                        new Handler(Looper.getMainLooper()).postDelayed(() -> {
                            MaterialCardView.setStrokeColor(0x00000000); // transparent or default color
                        }, 500);
                        drawingView.clearCanvas();
                    }else{
                        mp = MediaPlayer.create(requireContext(),R.raw.incorrect);
                        mp.start();
                        generateNewNumber_inc();
                        MaterialCardView.setStrokeColor(0xFFFF0000);
                        new Handler(Looper.getMainLooper()).postDelayed(() -> {
                            MaterialCardView.setStrokeColor(0x00000000); // transparent or default color
                        }, 500);
                        drawingView.clearCanvas();
                    }

                    // Do whatever you want with the predicted number
                    // e.g., navigate to another fragment, update UI, etc.
                });
            }
        });

        Button startButton = view.findViewById(R.id.start);
        startButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!isDrawing[0]) {
                    // Start drawing
                    generateNewNumber();
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
    private int generateNewNumber() {

        currentNumber = (int)(Math.random() * 10);// 0-9
        String resName = "_" + currentNumber;
        int resId = getResources().getIdentifier(resName, "raw", requireContext().getPackageName());
        if (resId != 0) { // make sure resource exists
            mp = MediaPlayer.create(requireContext(), resId);
            if (mp != null) {
                mp.setOnCompletionListener(mediaPlayer -> {
                    mediaPlayer.release();
                });
                mp.start();
            } else {
                Toast.makeText(getContext(), "MediaPlayer creation failed", Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(getContext(), "Resource not found: " + resName, Toast.LENGTH_SHORT).show();
        }
        return currentNumber;
    }

    private void generateNewNumber_inc() {


        String resName = "_" + currentNumber;
        int resId = getResources().getIdentifier(resName, "raw", requireContext().getPackageName());
        if (resId != 0) { // make sure resource exists
            mp = MediaPlayer.create(requireContext(), resId);
            if (mp != null) {
                mp.setOnCompletionListener(mediaPlayer -> {
                    mediaPlayer.release();
                });
                mp.start();
            } else {
                Toast.makeText(getContext(), "MediaPlayer creation failed", Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(getContext(), "Resource not found: " + resName, Toast.LENGTH_SHORT).show();
        }

    }

}