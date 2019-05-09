package com.tungnvan.godear.components;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.v4.content.LocalBroadcastManager;

import com.tungnvan.godear.RecordService;
import com.tungnvan.godear.commons.PhonecallReceiver;
import com.tungnvan.godear.utils.FileUtils;
import com.tungnvan.godear.utils.RecordNameUtils;

import java.util.Date;

public class CallReceiver extends PhonecallReceiver {

    private BroadcastReceiver broadcast_receiver = null;

    private void startRecording(Context ctx, String number) {
        Intent record_service_intent = new Intent(ctx, RecordService.class);
        broadcast_receiver = broadcast_receiver == null
            ? new BroadcastReceiver() {
                @Override
                public void onReceive(Context context, Intent intent) {
                    RecorderStateHolder.setRecordingState(intent.getBooleanExtra(RecordService.IS_RECORDING, false));
                    RecorderStateHolder.setRecordFilePath(intent.getStringExtra(RecordService.FILE_PATH));
                }
            }
            : broadcast_receiver;
        try {
            ctx.stopService(record_service_intent);
            LocalBroadcastManager.getInstance(ctx).registerReceiver(broadcast_receiver, new IntentFilter(RecordService.BROADCAST_RECORDER));
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            ctx.startService(record_service_intent);
        }
    }

    private void stopRecording(Context ctx, String number) {
        Intent record_service_intent = new Intent(ctx, RecordService.class);
        try {
            ctx.stopService(record_service_intent);
            FileUtils.renameFile(
                RecorderStateHolder.getRecordFilePath(),
                RecordNameUtils.produceFilePathFromName(FileUtils.generateFileNameByTime("Called by " + number + " at "))
            );
            LocalBroadcastManager.getInstance(ctx).unregisterReceiver(broadcast_receiver);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onIncomingCallStarted(Context ctx, String number, Date start) {
        startRecording(ctx, number);
    }

    @Override
    protected void onIncomingCallEnded(Context ctx, String number, Date start, Date end) {
        stopRecording(ctx, number);
    }

    @Override
    protected void onOutgoingCallStarted(Context ctx, String number, Date start) {
        startRecording(ctx, number);
    }

    @Override
    protected void onOutgoingCallEnded(Context ctx, String number, Date start, Date end) {
        stopRecording(ctx, number);
    }

    @Override
    protected void onMissedCall(Context ctx, String number, Date start) {
        stopRecording(ctx, number);
    }
}
