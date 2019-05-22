package com.tungnvan.godear.components;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;

import com.tungnvan.godear.R;
import com.tungnvan.godear.constants.GlobalConstants;
import com.tungnvan.godear.utils.FileUtils;

import java.io.File;
import java.util.ArrayList;

public class RecordListActivity extends AppCompatActivity {

    private ArrayList<File> record_files;
    private Toolbar list_records_toolbar;
    private RecyclerView record_list;
    private RecordListAdapter record_list_adapter;

    private void drawListRecord() {
        record_files = FileUtils.listAllRecordFilesNonrecursively(GlobalConstants.RECORD_DIRECTORY);
        record_list = (RecyclerView) findViewById(R.id.record_list);
        record_list_adapter = new RecordListAdapter(record_files);
        record_list_adapter.setOnUpdateList(new Runnable() {
            @Override
            public void run() {
                drawListRecord();
            }
        });
        record_list.setAdapter(record_list_adapter);
        record_list.setLayoutManager(new LinearLayoutManager(this));
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);

        list_records_toolbar = (Toolbar) findViewById(R.id.list_records_toolbar);
        setSupportActionBar(list_records_toolbar);
        setTitle(R.string.listText);

        drawListRecord();
    }
}
