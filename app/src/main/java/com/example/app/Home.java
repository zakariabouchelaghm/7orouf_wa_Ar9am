package com.example.app;

import android.os.Bundle;

import androidx.activity.OnBackPressedCallback;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import org.imaginativeworld.whynotimagecarousel.ImageCarousel;
import org.imaginativeworld.whynotimagecarousel.model.CarouselItem;

import java.util.ArrayList;
import java.util.List;


public class Home extends Fragment {

    Button start;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view= inflater.inflate(R.layout.fragment_home, container, false);
        ImageCarousel imagecarousel = view.findViewById(R.id.carousel);
        imagecarousel.registerLifecycle(getLifecycle());
        List<CarouselItem> list=new ArrayList<>();
        list.add(new CarouselItem(R.drawable.cartoonairobotscene));
        list.add(new CarouselItem(R.drawable.cartoonairobotscene1));
        list.add(new CarouselItem(R.drawable.cartoonairobotscene2));
        imagecarousel.setData(list);
        imagecarousel.setAutoPlay(true);
        imagecarousel.setAutoPlayDelay(2000); // delay between slides in ms (3 seconds)

        start=view.findViewById(R.id.Start);
        start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Fragment fragment= new Activities();
                requireActivity().getSupportFragmentManager().beginTransaction().replace(R.id.frame,fragment).addToBackStack(null).commit();
                BottomNavigationView bottomNavigationView = requireActivity().findViewById(R.id.bottom_nav);
                bottomNavigationView.setSelectedItemId(R.id.menu);
            }
        });
        requireActivity().getOnBackPressedDispatcher().addCallback(
                getViewLifecycleOwner(),
                new OnBackPressedCallback(true) {
                    @Override
                    public void handleOnBackPressed() {
                        requireActivity().finish();

                    }
                }
        );
        return view;
        }

    }
