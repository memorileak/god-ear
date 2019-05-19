package com.tungnvan.godear.components;

import android.content.Context;
import android.media.MediaMetadataRetriever;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.lucasurbas.listitemview.ListItemView;
import com.tungnvan.godear.R;
import com.tungnvan.godear.utils.TimeUtils;

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
    public RecordListAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        View list_record_view = inflater.inflate(R.layout.record, parent, false);
        return new ViewHolder(list_record_view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecordListAdapter.ViewHolder viewHolder, int i) {
        final File record = list_records.get(i);
        MediaMetadataRetriever meta_record_retriever = new MediaMetadataRetriever();
        meta_record_retriever.setDataSource(record.getAbsolutePath());
        int record_duration = Integer.parseInt(meta_record_retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION));
        ListItemView record_view = viewHolder.getRecordView();
        record_view.setTitle(record.getName());
        record_view.setSubtitle(TimeUtils.toHMSString(record_duration / 1000));
        record_view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                (new RecordPlayer(v.getContext(), record.getAbsolutePath())).showDialog();
            }
        });
    }

    @Override
    public int getItemCount() {
        return list_records.size();
    }
}
