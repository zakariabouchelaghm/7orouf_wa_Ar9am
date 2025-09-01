package com.example.app;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Question {
    private int soundResId;      // the audio file (e.g., R.raw.cat_sound)
    private int[] images;   // image options (e.g., R.drawable.cat, dog, car)
    private int correctIndex;

    public Question(int soundResId, int[] imageOptions, int  correctImageResId) {
        this.soundResId = soundResId;
        List<Integer> imageList = new ArrayList<>();
        for (int img : imageOptions) {
            imageList.add(img);
        }

        // Shuffle the list
        Collections.shuffle(imageList);

        // Save back into an int[]
        this.images = new int[imageList.size()];
        for (int i = 0; i < imageList.size(); i++) {
            this.images[i] = imageList.get(i);
        }

        // Find where the correct image ended up
        this.correctIndex = imageList.indexOf(correctImageResId);
    }

    public int getSoundResId() { return soundResId; }
    public int[]getImageResIds() { return images; }
    public int getCorrectIndex() { return correctIndex; }
}
