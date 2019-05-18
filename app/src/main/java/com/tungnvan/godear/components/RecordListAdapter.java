package com.tungnvan.godear.components;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.lucasurbas.listitemview.ListItemView;
import com.tungnvan.godear.R;

import java.io.File;
import java.util.ArrayList;

public class RecordListAdapter extends RecyclerView.Adapter<RecordListAdapter.ViewHolder> {

    public class ViewHolder extends RecyclerView.ViewHolder {

        private ListItemView record_view;

        public ViewHolder(View view) {
            super(view);
            record_view = (ListItemView) view.findViewById(R.id.record_view);
        }

        public ListItemView getRecordView() {
            return record_view;
        }

    }

    private ArrayList<File> list_records;

    public RecordListAdapter(ArrayList<File> record_list) {
        list_records = record_list;
    }

    @NonNull
    @Override
    public RecordListAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        Context context = viewGroup.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        View list_record_view = inflater.inflate(R.layout.record, viewGroup, false);
        return new ViewHolder(list_record_view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecordListAdapter.ViewHolder viewHolder, int i) {
        final File record = list_records.get(i);
        ListItemView record_view = viewHolder.getRecordView();
        record_view.setTitle(record.getName());
        record_view.setSubtitle("00:mm:ss");
        record_view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.v("THIS", record.getAbsolutePath());
            }
        });
    }

    @Override
    public int getItemCount() {
        return list_records.size();
    }
}
