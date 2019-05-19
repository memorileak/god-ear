package com.tungnvan.godear.controllers;

import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.AsyncTask;

import java.io.File;
import java.io.FileInputStream;
import java.util.HashMap;
import java.util.Timer;
import java.util.TimerTask;

public class Player {

    private static class BroadcastTask extends AsyncTask<Runnable, Void, Runnable[]> {

        @Override
        protected Runnable[] doInBackground(Runnable... tasks) {
            return tasks;
        }

        @Override
        protected void onPostExecute(Runnable[] tasks) {
            // This chunk will run on UI thread
            for (Runnable task: tasks) {
                task.run();
            }
        }

    }

    private File file_to_play;
    private MediaPlayer media_player;
    private Timer player_state_timer;
    private Timer player_position_timer;
    private Runnable on_completion;
    private HashMap<String, Runnable> state_observers = new HashMap<>();
    private HashMap<String, Runnable> position_observers = new HashMap<>();

    public Player(String file_path) {
        file_to_play = new File(file_path);
    }

    private void broadcastState() {
        (new Player.BroadcastTask()).execute(state_observers.values().toArray(new Runnable[0]));
    }

    private void broadcastPosition() {
        (new Player.BroadcastTask()).execute(position_observers.values().toArray(new Runnable[0]));
    }

    private void broadcast() {
        broadcastState(); broadcastPosition();
    }

    public void subscribeState(String key, Runnable runnable) {
        state_observers.put(key, runnable);
    }

    public void subscribePosition(String key, Runnable runnable) {
        position_observers.put(key, runnable);
    }

    public void unsubscribeState(String key) {
        state_observers.remove(key);
    }

    public void setOnCompletion(Runnable completion_handler) {
        on_completion = completion_handler;
    }

    public void unsubscribePosition(String key) {
        position_observers.remove(key);
    }

    public void prepare() {
        media_player = new MediaPlayer();
        media_player.setAudioStreamType(AudioManager.STREAM_MUSIC);
        media_player.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                on_completion.run();
            }
        });
        try {
            media_player.setDataSource((new FileInputStream(file_to_play)).getFD());
            media_player.prepare();
            broadcast();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void start() {
        try {
            media_player.start();
            player_state_timer = new Timer();
            player_state_timer.scheduleAtFixedRate(new TimerTask() {
                @Override
                public void run() {
                    broadcastState();
                }
            }, 0, 100);
            player_position_timer = new Timer();
            player_position_timer.scheduleAtFixedRate(new TimerTask() {
                @Override
                public void run() {
                    broadcastPosition();
                }
            }, 25, 50);
            broadcast();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void pause() {
        try {
            media_player.pause();
            player_state_timer.cancel(); player_state_timer.purge();
            player_position_timer.cancel(); player_position_timer.purge();
            broadcast();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void stop() {
        try {
            media_player.stop();
            player_state_timer.cancel(); player_state_timer.purge();
            player_position_timer.cancel(); player_position_timer.purge();
            broadcast();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void release() {
        try {
            media_player.release();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void seekTo(int milisecond) {
        try {
            media_player.seekTo(milisecond);
            broadcast();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public boolean isPlaying() {
        try {
            return media_player.isPlaying();
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public int getDuration() {
        try {
            return media_player.getDuration();
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }

    public int getCurrentPosition() {
        try {
            return media_player.getCurrentPosition();
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }

}
