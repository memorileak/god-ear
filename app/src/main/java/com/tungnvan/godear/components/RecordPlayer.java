package com.tungnvan.godear.components;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;

import com.tungnvan.godear.R;
import com.tungnvan.godear.controllers.Player;
import com.tungnvan.godear.utils.RecordNameUtils;
import com.tungnvan.godear.utils.TimeUtils;

public class RecordPlayer {

    private static final String CLASS_NAME = "RECORD_PLAYER";

    private Context own_context;
    private AlertDialog.Builder dialog_builder;
    private AlertDialog dialog;
    private View record_player_body;
    private Button record_player_control_button;
    private TextView record_player_record_name;
    private SeekBar record_player_seekbar;
    private TextView current_time;
    private TextView end_time;
    private Player player;

    public RecordPlayer(Context context, String file_path) {
        own_context = context;
        preparePlayer(context, file_path);
        record_player_body = LayoutInflater.from(own_context).inflate(R.layout.record_player_body, null);
        record_player_control_button = (Button) record_player_body.findViewById(R.id.record_player_control_button);
        record_player_record_name = (TextView) record_player_body.findViewById(R.id.record_player_record_name);
        record_player_seekbar = (SeekBar) record_player_body.findViewById(R.id.record_player_seekbar);
        current_time = (TextView) record_player_body.findViewById(R.id.record_player_current_time);
        end_time = (TextView) record_player_body.findViewById(R.id.record_player_end_time);
        setInitialValues(context, file_path);
        dialog_builder = new AlertDialog.Builder(own_context);
        dialog = dialog_builder
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

    private void setInitialValues(Context context, String file_path) {
        record_player_control_button.setBackground(context.getResources().getDrawable(R.drawable.player_pause_button));
        record_player_control_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                handleControlButtonClick();
            }
        });
        record_player_record_name.setText(RecordNameUtils.produceFileNameFromPath(file_path));
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
            public void onStartTrackingTouch(SeekBar seekBar) {}

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {}
        });
    }

    private void updateControlButtonUIByPlayerState(boolean is_playing) {
        record_player_control_button.setBackground(
            is_playing
                ? own_context.getResources().getDrawable(R.drawable.player_pause_button)
                : own_context.getResources().getDrawable(R.drawable.player_play_button)
        );
    }

    private void updateDialogUIByPlayerState() {
        updateControlButtonUIByPlayerState(player.isPlaying());
        current_time.setText(TimeUtils.toHMSString(player.getCurrentPosition() / 1000));
        record_player_seekbar.setProgress(player.getCurrentPosition());
    }

    private void updateSeekbarByPlayerPosition() {
        record_player_seekbar.setProgress(player.getCurrentPosition());
    }

    private void handleControlButtonClick() {
        if (player.isPlaying()) {
            player.pause();
        } else {
            player.start();
        }
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
