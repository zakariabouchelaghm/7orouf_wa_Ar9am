package com.example.app;

import android.os.Bundle;

import androidx.activity.OnBackPressedCallback;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.material.bottomnavigation.BottomNavigationView;


public class Activities extends Fragment {
    CardView listen;
    CardView write;
    CardView learn;
    final PopupWrite_and_listen popupFragment1= new PopupWrite_and_listen();
    final PopupFragmentLearn popupFragment2= new PopupFragmentLearn();
    final PopupFragmentListen_and_write popupFragment3= new PopupFragmentListen_and_write();
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view=inflater.inflate(R.layout.fragment_activities, container, false);

        listen= view.findViewById(R.id.listen_and_write);
        listen.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                popupFragment3.show(getParentFragmentManager(), "myPopup");

            }
        });
        write= view.findViewById(R.id.write_and_listen);
        write.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popupFragment1.show(getParentFragmentManager(), "myPopup");
            }
        });
        learn= view.findViewById(R.id.learn);
        learn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                popupFragment2.show(getParentFragmentManager(), "myPopup");
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
