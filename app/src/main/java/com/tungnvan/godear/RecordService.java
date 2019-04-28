package com.tungnvan.godear;

import android.app.IntentService;
import android.app.Notification;
import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.support.v4.content.LocalBroadcastManager;

import com.tungnvan.godear.components.RecordTimer;
import com.tungnvan.godear.components.Recorder;
import com.tungnvan.godear.utils.TimeUtils;

public class RecordService extends IntentService {

    public static final String CLASS_NAME = "RecordService";
    public static final String BROADCAST_RECORDER = "BROADCAST_RECORDER";
    public static final String BROADCAST_CLOCK = "BROADCAST_CLOCK";
    public static final String IS_RECORDING = "IS_RECORDING";
    public static final String ELAPSED_TIME = "ELAPSED_TIME";

    private String CHANNEL_ID = "9001";
    private int NOTIFICATION_ID = 9009;
    private NotificationCompat.Builder notification_builder;
    private PendingIntent pending_main_activity_intent;
    private Recorder recorder;
    private RecordTimer clock;

    public RecordService() {
        super("RecordService");
    }

    private Notification buildForegroundNotification(String content) {
        pending_main_activity_intent = pending_main_activity_intent == null
            ? TaskStackBuilder.create(this)
                .addNextIntentWithParentStack(new Intent(this, MainActivity.class))
                .getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT)
            : pending_main_activity_intent;
        notification_builder = notification_builder == null
            ? new NotificationCompat.Builder(this, CHANNEL_ID)
                .setSmallIcon(R.drawable.ic_god_ear)
                .setContentTitle("God Ear is listening")
                .setPriority(Notification.PRIORITY_DEFAULT)
                .setContentIntent(pending_main_activity_intent)
            : notification_builder;
        return notification_builder.setContentText(content).build();
    }

    private void notifyNotification(Notification notification) {
        NotificationManagerCompat.from(this).notify(NOTIFICATION_ID, notification);
    }

    private void startForegroundWithNotification(Notification notification) {
        startForeground(NOTIFICATION_ID, notification);
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
                if (recorder.isRecording()) {
                    notifyNotification(buildForegroundNotification(TimeUtils.toHMSString(clock.getElapsedTime())));
                }
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
        startForegroundWithNotification(buildForegroundNotification(TimeUtils.toHMSString(clock.getElapsedTime())));
        super.onCreate();
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        try {
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
        super.onDestroy();
    }
}
