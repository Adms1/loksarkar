package com.loksarkar.utils;

import android.os.Handler;

import com.loksarkar.listener.OnProgressCompleteListener;
import com.loksarkar.ui.ProcessButton;

import java.util.Random;

public class ProgressGenerator {


    private OnProgressCompleteListener mListener;
    private int mProgress;

    public ProgressGenerator(OnProgressCompleteListener listener) {
        mListener = listener;
    }

    public void start(final ProcessButton button) {
        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                mProgress += 10;
                button.setProgress(mProgress);
                if (mProgress < 100) {
                    handler.postDelayed(this, generateDelay());
                } else {
                    mListener.onComplete();
                }
            }
        }, generateDelay());
    }

    private Random random = new Random();

    private int generateDelay() {
        return random.nextInt(1000);
    }
}
