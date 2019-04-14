package com.tungnvan.godear.components;

import android.app.Activity;
import android.widget.TextView;

import java.util.Timer;
import java.util.TimerTask;

public class RecordTimer extends Activity {

    private int elapsed_time = 0;
    private Timer elapsed_counter;
    private TextView presentor;

    public RecordTimer(TextView clock_view) {
        presentor = clock_view;
    }

    public class CountingElapsed extends TimerTask {
        @Override
        public void run() {
            tickTimer();
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    presentor.setText(String.format("%03d", elapsed_time));
                }
            });
        }
    }

    private void tickTimer() {
        ++elapsed_time;
    }

    public void resetTimer() {
        elapsed_time = 0;
        presentor.setText(String.format("%03d", elapsed_time));
    }

    public void startTimer() {
        elapsed_counter = new Timer();
        elapsed_counter.scheduleAtFixedRate(new CountingElapsed(), 1000, 1000);
    }

    public void stopTimer() {
        elapsed_counter.cancel();
        elapsed_counter.purge();
    }

    public int getElapsedTime() {
        return elapsed_time;
    }

}
