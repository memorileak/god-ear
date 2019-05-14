package com.tungnvan.godear.components;

import android.app.Activity;

import java.util.HashMap;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

public class RecordTimer extends Activity {

    private static RecordTimer instance = null;

    private int elapsed_time = 0;
    private Timer elapsed_counter;
    private HashMap<String, Runnable> observers = new HashMap<>();

    public class CountingElapsed extends TimerTask {
        @Override
        public void run() {
            tickTimer();
        }
    }

    public static RecordTimer getInstance() {
        if (instance == null) instance = new RecordTimer();
        return instance;
    }

    private RecordTimer() {}

    private void tickTimer() {
        ++elapsed_time;
        broadcast();
    }

    public void subscribe(String key, Runnable runnable) {
        observers.put(key, runnable);
    }

    public void unsubscribe(String key) {
        observers.remove(key);
    }

    public void broadcast() {
        for (Map.Entry<String, Runnable> entry: observers.entrySet()) {
            entry.getValue().run();
        }
    }

    public void resetTimer() {
        elapsed_time = 0;
        broadcast();
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
