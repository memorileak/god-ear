package com.tungnvan.godear;

import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;
import android.content.Intent;

import com.tungnvan.godear.activities.settings.SettingsActivity;
import com.tungnvan.godear.utils.Record;
import com.tungnvan.godear.utils.TimeUtils;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;



public class ListActivity extends AppCompatActivity {
    RecyclerView recordView;
    TextView recordText;
    RecyclerViewAdapter recordAdapter;
    List<Record> data;



    private MediaPlayer mediaPlayer;




    @Override
    public void onCreate(Bundle savedInstanceState)    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);
        updateList();
    }

    @Override
    public void onResume()
    {
        super.onResume();
        //mediaPlayer.stop();
        updateList();
    }

    public void updateList(){
        recordView = (RecyclerView) findViewById(R.id.recordList);
        recordText = findViewById(R.id.recordText);


        data = new ArrayList<>();
        int recordCount = 0;
        //lấy danh sách bản ghi từ thư mục lưu trữ
        String dir_path = Environment.getExternalStorageDirectory().getAbsolutePath() + "/GodEar/";
        File dir_folder = new File(dir_path);
        if (dir_folder.isDirectory()) {
            File[] record_list = dir_folder.listFiles();
            for (File file : record_list) {
                this.mediaPlayer = new MediaPlayer();
                String recordname = file.getName();
                recordCount +=1;
                //Uri uri = Uri.fromFile(file);
                try {
                    this.mediaPlayer.setDataSource(dir_path + recordname);
                    this.mediaPlayer.prepare();
                } catch (IOException e) {
                    e.printStackTrace();
                }

                int duration = this.mediaPlayer.getDuration();
                String recordmaxtime = TimeUtils.millisecondsToString(duration);
                Record record = new Record(recordCount, recordname,recordmaxtime);
                data.add(record);
            }
        }


        recordText.setText("Record List ( " + recordCount +")");

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

            @Override
            public void onPlayClick(int position) {
                Intent intent = new Intent(ListActivity.this, PlayRecordView.class);

                intent.putExtra("position_id", position);
                startActivity (intent);


            }
        });
    }










}
