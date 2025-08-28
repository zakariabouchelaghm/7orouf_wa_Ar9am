package com.example.app;

import android.content.pm.ActivityInfo;
import android.os.Bundle;

import androidx.cardview.widget.CardView;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


public class PopupFragmentListen_and_write extends DialogFragment {


    CardView numbers,letters;

    @Override
    public void onResume() {
        super.onResume();
        // lock to portrait when this fragment is visible
        requireActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
    }

    @Override
    public void onPause() {
        super.onPause();
        // restore default (system/manifest setting) when leaving this fragment
        requireActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED);
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view=inflater.inflate(R.layout.fragment_popup_listen_and_write, container, false);
        numbers= view.findViewById(R.id.numbers);
        letters= view.findViewById(R.id.letters);
        Fragment fragment_numbers = new numbers();
        numbers.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Fragment fragment= new numbers();
                requireActivity().getSupportFragmentManager().beginTransaction().replace(R.id.frame,fragment_numbers).addToBackStack(null).commit();
                dismiss();
            }
        });
        Fragment fragment_letters = new letters();
        letters.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Fragment fragment= new numbers();
                requireActivity().getSupportFragmentManager().beginTransaction().replace(R.id.frame,fragment_letters).addToBackStack(null).commit();
                dismiss();
            }
        });
        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        if (getDialog() != null && getDialog().getWindow() != null) {
            // Make window background transparent
            getDialog().getWindow().setBackgroundDrawableResource(android.R.color.transparent);

            // Set size of the popup
            getDialog().getWindow().setLayout(
                    ViewGroup.LayoutParams.WRAP_CONTENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT
            );
        }
    }

}
