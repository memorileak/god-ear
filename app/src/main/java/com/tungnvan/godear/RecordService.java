package com.tungnvan.godear;

import android.app.IntentService;
import android.app.Notification;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.LocalBroadcastManager;

import com.tungnvan.godear.components.RecordTimer;
import com.tungnvan.godear.components.Recorder;

public class RecordService extends IntentService {

    public static final String CLASS_NAME = "RecordService";
    public static final String BROADCAST_RECORDER = "BROADCAST_RECORDER";
    public static final String BROADCAST_CLOCK = "BROADCAST_CLOCK";
    public static final String IS_RECORDING = "IS_RECORDING";
    public static final String ELAPSED_TIME = "ELAPSED_TIME";

    private String CHANNEL_ID = "1001";
    private int NOTIFICATION_ID = 9009;

    private Recorder recorder;
    private RecordTimer clock;

    public RecordService() {
        super("RecordService");
    }

    @Override
    public void onCreate() {
        recorder = new Recorder();
        clock = new RecordTimer();

        recorder.subscribe(RecordService.CLASS_NAME, new Runnable() {
            @Override
            public void run() {
                broadcastRecorder();
            }
        });

        clock.subscribe(RecordService.CLASS_NAME, new Runnable() {
            @Override
            public void run() {
                broadcastClock();
            }
        });

        LocalBroadcastManager.getInstance(this).registerReceiver(new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                broadcastRecorder();
                broadcastClock();
            }
        }, new IntentFilter(MainActivity.PROBE_SERVICE));

        clock.resetTimer();
//        Intent notificationIntent = new Intent(this, MainActivity.class);
//        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, notificationIntent, 0);
        Notification notification = new NotificationCompat.Builder(this, CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_launcher_foreground)
            .setContentTitle("Recording")
            .setContentText("A service is recording")
            .setPriority(Notification.PRIORITY_DEFAULT)
            .build();
        startForeground(NOTIFICATION_ID, notification);

        super.onCreate();
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        try {
            System.out.println("_________________________________________________________________STARTED___________________________________________________________________");

            recorder.setupRecorder();
            recorder.startRecorder();
            clock.startTimer();

            while (true) ;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onDestroy() {
        recorder.stopRecorder();
        clock.stopTimer();
        clock.resetTimer();

        System.out.println("_________________________________________________________________STOPPED___________________________________________________________________");
        super.onDestroy();
    }

    private void broadcastRecorder() {
        Intent broadcast_intent = new Intent(RecordService.BROADCAST_RECORDER);
        broadcast_intent.putExtra(RecordService.IS_RECORDING, recorder.isRecording());
        LocalBroadcastManager.getInstance(this).sendBroadcast(broadcast_intent);
    }

    private void broadcastClock() {
        Intent broadcast_intent = new Intent(RecordService.BROADCAST_CLOCK);
        broadcast_intent.putExtra(RecordService.ELAPSED_TIME, clock.getElapsedTime());
        LocalBroadcastManager.getInstance(this).sendBroadcast(broadcast_intent);
    }
}
