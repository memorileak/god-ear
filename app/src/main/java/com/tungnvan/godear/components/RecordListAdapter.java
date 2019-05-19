package com.tungnvan.godear.components;

import android.content.Context;
import android.media.MediaMetadataRetriever;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.lucasurbas.listitemview.ListItemView;
import com.tungnvan.godear.R;
import com.tungnvan.godear.commons.ConfirmDialog;
import com.tungnvan.godear.utils.FileUtils;
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
    private Runnable on_update;

    public RecordListAdapter(ArrayList<File> record_list) {
        list_records = record_list;
    }

    private void handleRenameRecord() {}

    private void handleDeleteRecord(final Context context, final String file_path) {
        new ConfirmDialog(context, "Delete this file?")
            .setOnGranted(new Runnable() {
                @Override
                public void run() {
                    try {
                        FileUtils.deleteFile(file_path);
                        Toast.makeText(context, "File has been deleted!", Toast.LENGTH_SHORT).show();
                        if (on_update != null) on_update.run();
                    } catch (Exception e) {
                        e.printStackTrace();
                        Toast.makeText(context, e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
            })
            .setOnDenied(new Runnable() {
                @Override
                public void run() {}
            })
            .showDialog();
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
        final ListItemView record_view = viewHolder.getRecordView();
        record_view.setTitle(record.getName());
        record_view.setSubtitle(TimeUtils.toHMSString(record_duration / 1000));
        record_view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                (new RecordPlayer(v.getContext(), record.getAbsolutePath())).showDialog();
            }
        });
        record_view.setOnMenuItemClickListener(new ListItemView.OnMenuItemClickListener() {
            @Override
            public void onActionMenuItemSelected(final MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.record_rename_item:
                        handleRenameRecord();
                        break;
                    case R.id.record_delete_item:
                        handleDeleteRecord(record_view.getContext(), record.getAbsolutePath());
                        break;
                    default:
                        break;
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return list_records.size();
    }

    public void setOnUpdateList(Runnable updateHandler) {
        on_update = updateHandler;
    }
}
