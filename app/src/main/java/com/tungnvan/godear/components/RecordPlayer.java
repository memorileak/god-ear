package com.tungnvan.godear.components;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.SeekBar;
import android.widget.TextView;

import com.tungnvan.godear.R;
import com.tungnvan.godear.controllers.Player;
import com.tungnvan.godear.utils.TimeUtils;

import java.io.File;

public class RecordPlayer {

    private static final String CLASS_NAME = "RECORD_PLAYER";

    private Context own_context;
    private AlertDialog.Builder dialog_builder;
    private AlertDialog dialog;
    private View record_player_body;
    private SeekBar record_player_seekbar;
    private TextView current_time;
    private TextView end_time;
    private File file_to_play;
    private Player player;

    public RecordPlayer(Context context, String file_path) {
        own_context = context;
        file_to_play = new File(file_path);
        preparePlayer(context, file_path);
        record_player_body = LayoutInflater.from(own_context).inflate(R.layout.record_player_body, null);
        record_player_seekbar = (SeekBar) record_player_body.findViewById(R.id.record_player_seekbar);
        current_time = (TextView) record_player_body.findViewById(R.id.record_player_current_time);
        end_time = (TextView) record_player_body.findViewById(R.id.record_player_end_time);
        setInitialValues();
        dialog_builder = new AlertDialog.Builder(own_context);
        dialog = dialog_builder
                .setTitle(file_to_play.getName())
                .setView(record_player_body)
                .create();
        dialog.setCanceledOnTouchOutside(false);
    }

    private void preparePlayer(Context context, String file_path) {
        player = new Player(context, file_path);
        player.subscribeState(RecordPlayer.CLASS_NAME, new Runnable() {
            @Override
            public void run() {
                updateDialogUIByPlayerState();
            }
        });
        player.subscribePosition(RecordPlayer.CLASS_NAME, new Runnable() {
            @Override
            public void run() {
                updateSeekbarByPlayerPosition();
            }
        });
        player.setOnCompletion(new Runnable() {
            @Override
            public void run() {
                dialog.dismiss();
            }
        });
        player.prepare();
    }

    private void setInitialValues() {
        current_time.setText(TimeUtils.toHMSString(0));
        end_time.setText(TimeUtils.toHMSString(player.getDuration() / 1000));
        record_player_seekbar.setMax(player.getDuration());
        record_player_seekbar.setProgress(player.getCurrentPosition());
        record_player_seekbar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (fromUser) player.seekTo(progress);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
    }

    private void updateDialogUIByPlayerState() {
        current_time.setText(TimeUtils.toHMSString(player.getCurrentPosition() / 1000));
        record_player_seekbar.setProgress(player.getCurrentPosition());
    }

    private void updateSeekbarByPlayerPosition() {
        record_player_seekbar.setProgress(player.getCurrentPosition());
    }

    public void showDialog() {
        if (dialog != null) {
            dialog.show();
            player.start();
            dialog.setOnDismissListener(new AlertDialog.OnDismissListener() {
                @Override
                public void onDismiss(DialogInterface dialog) {
                    player.unsubscribeState(RecordPlayer.CLASS_NAME);
                    player.unsubscribePosition(RecordPlayer.CLASS_NAME);
                    player.stop();
                    player.release();
                }
            });
        }
    }
}
