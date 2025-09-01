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

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;


public class letters extends Fragment {

    // ... add the rest as you need

    MediaPlayer mp;
    private String currentLetter;
    private int currentNumber;
    final boolean[] isDrawing = {false};
    Map<String, String> dict = new HashMap<>();


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
//        if (mp != null) {
//            if (mp.isPlaying()) {
//                mp.stop();
//            }
//            mp.release();
//            mp = null;
//        }
        // unlock / restore when you leave the fragment
        requireActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED);
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_letters, container, false);

        DrawingViewLetter drawingView = view.findViewById(R.id.drawingView);
        Button clearButton = view.findViewById(R.id.clear);
        clearButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawingView.clearCanvas();
            }
        });

        //drawingView.setStrokeWidth(70);
        dict.put("_10", "أ");
        dict.put("_11", "ب");
        dict.put("_12", "ت");
        dict.put("_13", "ث");
        dict.put("_14", "ج");
        dict.put("_15", "ح");
        dict.put("_16", "خ");
        dict.put("_17", "د");
        dict.put("_18", "ذ");
        dict.put("_19", "ر");
        dict.put("_20", "ز");
        dict.put("_21", "س");
        dict.put("_22", "ش");
        dict.put("_23", "ص");
        dict.put("_24", "ض");
        dict.put("_25", "ط");
        dict.put("_26", "ظ");
        dict.put("_27", "ع");
        dict.put("_28", "غ");
        dict.put("_29", "ف");
        dict.put("_30", "ق");
        dict.put("_31", "ك");
        dict.put("_32", "ل");
        dict.put("_33", "م");
        dict.put("_34", "ن");
        dict.put("_35", "ه");
        dict.put("_36", "و");
        dict.put("_37", "ي");
        Button sendButton = view.findViewById(R.id.send);
        MaterialCardView MaterialCardView=view.findViewById(R.id.card);
        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!isDrawing[0]) return;
                drawingView.sendDrawingToServer(predicted -> {
                    // Here you have the predicted string
                    String pred = "_" + predicted;
                    String currentLetter_pred=dict.get(pred);
                    if(currentLetter.contains(currentLetter_pred)){
                        mp = MediaPlayer.create(requireContext(),R.raw.correct);
                        mp.start();
                        generateNewLetter();
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
                    generateNewLetter();
                    drawingView.setDrawingEnabled(true);
                    startButton.setText("توقف");
                    isDrawing[0] = true;
                } else {
                    // Stop drawing
                    drawingView.setDrawingEnabled(false);
                    drawingView.clearCanvas();
                    startButton.setText("بدأ");
                    isDrawing[0] = false;
                    releasePlayer();
                }
            }
        });

        return view;
    }
    private String generateNewLetter() {

        currentNumber = 10 + (int)(Math.random() * 28);
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
        currentLetter=dict.get(resName);
        return currentLetter;
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