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


public class letters_write extends Fragment {

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

    @Override
    public void onPause() {
        super.onPause();
        // unlock / restore when you leave the fragment
        if (mp != null) {
            if (mp.isPlaying()) {
                mp.stop();
            }
            mp.release();
            mp = null;
        }
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
        drawingView.setStrokeWidth(70);
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
                    int resId = getResources().getIdentifier(pred, "raw", requireContext().getPackageName());
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
                        Toast.makeText(getContext(), "Resource not found: " + pred, Toast.LENGTH_SHORT).show();
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
                    // Start drawing
                    drawingView.setDrawingEnabled(true);
                    startButton.setText("توقف");
                    isDrawing[0] = true;
                } else {
                    // Stop drawing
                    drawingView.setDrawingEnabled(false);
                    startButton.setText("بدأ");
                    drawingView.clearCanvas();
                    isDrawing[0] = false;
                    if (mp != null) {
                        try {
                            if (mp.isPlaying()) {
                                mp.stop();
                            }
                            mp.release();
                            mp = null;
                        } catch (IllegalStateException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        });

        return view;
    }



}