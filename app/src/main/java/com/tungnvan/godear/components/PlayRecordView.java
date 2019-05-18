package com.tungnvan.godear.components;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.TextView;
import android.support.v7.widget.Toolbar;

import com.tungnvan.godear.R;
import com.tungnvan.godear.utils.FileUtils;
import com.tungnvan.godear.utils.RecordNameUtils;
import com.tungnvan.godear.utils.TimeUtils;

import java.io.File;
import java.io.IOException;

public class PlayRecordView extends AppCompatActivity implements View.OnClickListener {
    private int position;
    private TextView record_duration;
    private TextView record_time;
    private Button play_pause_button;
    private Button forward_button;
    private Button backward_button;
    //private TextView record_name;
    private SeekBar record_seek;
    private Toolbar record_toolbar;

    //private Button rename_button;
    //private Button delete_button;
    private String recordname;
    private String recordduration;
    private AlertDialog.Builder dialog_builder;
    private AlertDialog dialog;

    public Handler threadHandler = new Handler();
    public MediaPlayer mediaPlayer;
    String dir_path = Environment.getExternalStorageDirectory().getAbsolutePath() + "/GodEar/";
    File record = new File(dir_path);

    @Override
    public void onDestroy() {
        super.onDestroy();
        mediaPlayer.stop();
        finish();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play_record);

        play_pause_button = findViewById(R.id.play_pause_button);
        play_pause_button.setOnClickListener(this);
        //rename_button = findViewById(R.id.rename_button);
        //rename_button.setOnClickListener(this);
        //delete_button = findViewById(R.id.delete_button);
        //delete_button.setOnClickListener(this);
        forward_button = findViewById(R.id.next_button);
        forward_button.setOnClickListener(this);
        backward_button = findViewById(R.id.back_button);
        backward_button.setOnClickListener(this);

        record_toolbar = findViewById(R.id.record_play_toolbar);
        setSupportActionBar(record_toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        //record_toolbar.setTitle(recordname);

        record_seek = findViewById(R.id.record_seek);
        record_seek.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if(fromUser){
                    mediaPlayer.seekTo(progress);
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
        record_time = findViewById(R.id.record_time);
        record_duration = findViewById(R.id.record_duration);
        //record_name = findViewById(R.id.record_title);

        mediaPlayer = new MediaPlayer();
        Intent intent = getIntent();
        position = intent.getIntExtra("position_id", 0);

        playRecord(position);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.record_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.rename:
                renameButtonClicked();
                return true;
            case R.id.delete:
                deleteButtonClicked();
            default:
                return super.onOptionsItemSelected(item);
        }
    }


    public void playRecord(int position) {
        File dir_folder = new File(dir_path);
        if (dir_folder.isDirectory()) {
            File[] record_list = dir_folder.listFiles();

            int positionCompare = 0;

            for (File file : record_list) {
                if (positionCompare != position) {
                    positionCompare++;
                    continue;
                } else {
                    record = file;
                    break;
                }
            }

            recordname = record.getName();
            //record_name.setText(recordname);
            record_toolbar.setTitle(recordname);

            try {
                this.mediaPlayer.setDataSource(dir_path + recordname);
                this.mediaPlayer.prepare();
            } catch (IOException e) {
                e.printStackTrace();
            }

            int duration = this.mediaPlayer.getDuration();
            recordduration = TimeUtils.millisecondsToString(duration);
            int currentPosition = mediaPlayer.getCurrentPosition();

            if (currentPosition == 0) {
                record_seek.setMax(duration);
            } else if (currentPosition == duration) {
                mediaPlayer.reset();
                play_pause_button.setBackgroundResource(R.drawable.play_button_shape);
            }

            mediaPlayer.start();
            record_duration.setText(recordduration);

            UpdateSeekBarThread updateSeekBarThread = new UpdateSeekBarThread();
            threadHandler.postDelayed(updateSeekBarThread, 50);
        }
    }

    public void playPauseButtonClicked(View view) {
        if (mediaPlayer.isPlaying()) {
            mediaPlayer.pause();
            play_pause_button.setBackgroundResource(R.drawable.play_button_shape);
        } else {
            play_pause_button.setBackgroundResource(R.drawable.pause_button_shape);
            mediaPlayer.start();
        }
    }

    public void forwardButtonClicked(View view) {
        int currentPosition = this.mediaPlayer.getCurrentPosition();
        int duration = this.mediaPlayer.getDuration();
//         5 giây.
        int ADD_TIME = 5000;

        if (currentPosition + ADD_TIME < duration) {
            this.mediaPlayer.seekTo(currentPosition + ADD_TIME);
        }
    }

    public void backwardButtonClicked(View view) {
        int currentPosition = this.mediaPlayer.getCurrentPosition();
//        5 giây.
        int SUBTRACT_TIME = 5000;

        if (currentPosition - SUBTRACT_TIME > 0) {
            this.mediaPlayer.seekTo(currentPosition - SUBTRACT_TIME);
        }
    }


    private void showKeyboard() {
        InputMethodManager imm = (InputMethodManager) this.getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);
    }

    private void hideKeyboard() {
        InputMethodManager imm = (InputMethodManager) this.getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.toggleSoftInput(InputMethodManager.HIDE_IMPLICIT_ONLY, 0);
    }

    public void renameButtonClicked() {
        View record_rename_form;
        final EditText record_name_input;
        dialog_builder = new AlertDialog.Builder(this);
        record_rename_form = LayoutInflater.from(this).inflate(R.layout.rename_record_form, null);
        record_name_input = record_rename_form.findViewById(R.id.record_name);
        record_name_input.setText(RecordNameUtils.produceFileNameFromPath(recordname));
        record_name_input.setSelectAllOnFocus(true);
        dialog = dialog_builder
                .setTitle(R.string.rename_record_dialog_title)
                .setView(record_rename_form)
                .setPositiveButton(R.string.rename_record_dialog_positive_button, null)
                .setNegativeButton(R.string.rename_record_dialog_negative_button, null)
                .create();
        dialog.setCanceledOnTouchOutside(true);
        if (dialog != null) {
            dialog.show();
            showKeyboard();
            dialog.getButton(Dialog.BUTTON_POSITIVE).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    try {
                        if (FileUtils.renameFile(dir_path + recordname, dir_path + record_name_input.getText().toString())) {
                            dialog.dismiss();
                            hideKeyboard();
                            recordname = record_name_input.getText().toString();
                            //record_name.setText(recordname);
                            record_toolbar.setTitle(recordname);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });
            dialog.getButton(Dialog.BUTTON_NEGATIVE).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.dismiss();
                    hideKeyboard();
                }
            });
        }
    }

    public void deleteButtonClicked() {
        AlertDialog.Builder delete_dialog = new AlertDialog.Builder(this);
        delete_dialog.setMessage("Do you want to delete this record?");
        delete_dialog.setPositiveButton("Delete", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                try {
                    FileUtils.deleteFile(dir_path + recordname);
                    mediaPlayer.stop();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                dialog.cancel();
                finish();
            }
        });
        delete_dialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                dialog.cancel();
            }
        });

        AlertDialog ad = delete_dialog.create();
        ad.show();
    }

    @Override
    public void onClick(View v) {
        if (v == play_pause_button) {
            playPauseButtonClicked(v);
        }
//        if (v == rename_button) {
//            renameButtonClicked(v);
//        }
//        if (v == delete_button) {
//            deleteButtonClicked(v);
//        }
        if (v == forward_button) {
            forwardButtonClicked(v);
        }
        if (v == backward_button) {
            backwardButtonClicked(v);
        }
    }

    // Thread sử dụng để Update trạng thái cho SeekBar.
    public class UpdateSeekBarThread implements Runnable {

        public void run() {
            int currentPosition = mediaPlayer.getCurrentPosition();
            String currentPositionStr = TimeUtils.millisecondsToString(currentPosition);
            record_time.setText(currentPositionStr);
            record_seek.setProgress(currentPosition);

            // Ngừng thread 50 mili giây.
            threadHandler.postDelayed(this, 50);
        }
    }
}
