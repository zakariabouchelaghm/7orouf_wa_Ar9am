package com.example.app;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.activity.OnBackPressedCallback;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import com.google.android.material.bottomnavigation.BottomNavigationView;


public class About extends Fragment {



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_about, container, false);
        ImageButton github = view.findViewById(R.id.Github);
        ImageButton linkedin = view.findViewById(R.id.Linkedin);

        github.setOnClickListener(new View.OnClickListener() {
                                      @Override
                                      public void onClick(View v) {
                                          String url = "https://github.com/zakariabouchelaghm"; // replace with your link
                                          Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                                          startActivity(intent);
                                  }});
        linkedin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String url = "https://www.linkedin.com/in/zakaria-bouchelaghem-77a4a6182/"; // replace with your link
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                startActivity(intent);

            }});
        requireActivity().getOnBackPressedDispatcher().addCallback(
                getViewLifecycleOwner(),
                new OnBackPressedCallback(true) {
                    @Override
                    public void handleOnBackPressed() {
                        // Navigate back to Home fragment
                        Fragment fragment = new Home();
                        requireActivity().getSupportFragmentManager()
                                .beginTransaction()
                                .replace(R.id.frame, fragment)
                                .commit();

                        // Also update navbar highlight
                        BottomNavigationView bottomNavigationView = requireActivity().findViewById(R.id.bottom_nav);
                        bottomNavigationView.setSelectedItemId(R.id.home);
                    }
                }
        );

        return view;
    }
}