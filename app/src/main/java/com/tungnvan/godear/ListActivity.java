package com.tungnvan.godear;

import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.tungnvan.godear.utils.Record;
import com.tungnvan.godear.utils.TimeUtils;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;



public class ListActivity extends AppCompatActivity {
    RecyclerView recordView;
    RecyclerViewAdapter recordAdapter;
    List<Record> data;
    private Button play_button;
    private SeekBar record_seek;
    private TextView record_time;
    private TextView record_max_time;

    private TextView record_name;
    private MediaPlayer mediaPlayer;

    @Override
    public void onCreate(Bundle savedInstanceState)    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);
        recordView = (RecyclerView) findViewById(R.id.recordList);
        play_button = (Button) findViewById(R.id.play_button);
        record_seek = (SeekBar) findViewById(R.id.record_seek);
        record_time = (TextView) findViewById(R.id.record_time);
        record_max_time = (TextView) findViewById(R.id.record_max_time);
        record_name = (TextView) findViewById(R.id.record_name);


        data = new ArrayList<>();
        int ID = 0;
        //lấy danh sách bản ghi từ thư mục lưu trữ
        String dir_path = Environment.getExternalStorageDirectory().getAbsolutePath() + "/GodEar/";
            File dir_folder = new File(dir_path);
            if (dir_folder.isDirectory()) {
                File[] record_list = dir_folder.listFiles();
                for (File file : record_list) {
                    this.mediaPlayer = new MediaPlayer();
                    String recordname = file.getName();
                    ID +=1;
                    //Uri uri = Uri.fromFile(file);
                    try {
                        this.mediaPlayer.setDataSource(dir_path + recordname);
                        this.mediaPlayer.prepare();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                    int duration = this.mediaPlayer.getDuration();
                    String recordmaxtime = TimeUtils.millisecondsToString(duration);
                    Record record = new Record(ID, recordname,recordmaxtime);
                    data.add(record);
                }
            }

        recordAdapter = new RecyclerViewAdapter(data);


        LinearLayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);

        recordView.setLayoutManager(layoutManager);
        recordView.setAdapter(recordAdapter);
        recordAdapter.setOnItemClickedListener(new RecyclerViewAdapter.OnItemClickedListener() {
            @Override
            public void onItemClick(String username) {
                Toast.makeText(ListActivity.this, username, Toast.LENGTH_SHORT).show();
            }
        });
    }









}
