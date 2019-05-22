package com.tungnvan.godear.controllers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.v4.content.LocalBroadcastManager;

import com.tungnvan.godear.components.RecordService;
import com.tungnvan.godear.commons.PhonecallReceiver;

import java.util.Date;

public class CallReceiver extends PhonecallReceiver {

    private BroadcastReceiver broadcast_receiver = null;

    private void startRecording(Context ctx, String number, boolean is_incoming_call) {
        Intent record_service_intent = new Intent(ctx, RecordService.class);
        record_service_intent.putExtra(RecordService.RECORD_TYPE, is_incoming_call ? RecordService.RECORD_TYPES[1] : RecordService.RECORD_TYPES[2]);
        record_service_intent.putExtra(RecordService.CALL_NUMBER, number);
        broadcast_receiver = broadcast_receiver == null
            ? new BroadcastReceiver() {
                @Override
                public void onReceive(Context context, Intent intent) {
                    RecorderStateHolder.getInstance().setRecordingState(intent.getBooleanExtra(RecordService.IS_RECORDING, false));
                    RecorderStateHolder.getInstance().setRecordFilePath(intent.getStringExtra(RecordService.FILE_PATH));
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

    private void stopRecording(Context ctx) {
        Intent record_service_intent = new Intent(ctx, RecordService.class);
        try {
            ctx.stopService(record_service_intent);
            LocalBroadcastManager.getInstance(ctx).unregisterReceiver(broadcast_receiver);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onIncomingCallStarted(Context ctx, String number, Date start) {
        if ((new SettingsManager(ctx)).getAutoHearIncomingCall()) {
            startRecording(ctx, number, true);
        }
    }

    @Override
    protected void onIncomingCallEnded(Context ctx, String number, Date start, Date end) {
        if ((new SettingsManager(ctx)).getAutoHearIncomingCall()) {
            stopRecording(ctx);
        }
    }

    @Override
    protected void onOutgoingCallStarted(Context ctx, String number, Date start) {
        if ((new SettingsManager(ctx)).getAutoHearOutgoingCall()) {
            startRecording(ctx, number, false);
        }
    }

    @Override
    protected void onOutgoingCallEnded(Context ctx, String number, Date start, Date end) {
        if ((new SettingsManager(ctx)).getAutoHearOutgoingCall()) {
            stopRecording(ctx);
        }
    }

    @Override
    protected void onMissedCall(Context ctx, String number, Date start) {
        SettingsManager settings_manager = new SettingsManager(ctx);
        if (settings_manager.getAutoHearIncomingCall() || settings_manager.getAutoHearOutgoingCall()) {
            stopRecording(ctx);
        }
    }
}
