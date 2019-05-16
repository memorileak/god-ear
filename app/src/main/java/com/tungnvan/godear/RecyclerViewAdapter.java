package com.tungnvan.godear;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.tungnvan.godear.utils.Record;

import java.util.ArrayList;
import java.util.List;


public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.RecyclerViewHolder> {

    private Context mContext;
    private List<Record> data = new ArrayList<>();
    private int lastSelectedPosition = -1;

    public RecyclerViewAdapter(Context mContext, List<Record> data) {
        this.mContext = mContext;
        this.data = data;
    }

    public RecyclerViewAdapter(List<Record> data) {
        this.data = data;
    }

    @Override
    public RecyclerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.record, parent, false);
        return new RecyclerViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final RecyclerViewHolder holder, final int position) {
        holder.itemview.setTag(position);
        holder.recordname.setText(data.get(position).getRecord_name());
        holder.recordduration.setText(data.get(position).getRecord_max_time());
        holder.record.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                  if(lastSelectedPosition > 0){
//                      //data.get(lastSelectedPosition).setIs_selected(false);
//                  }
//                  Intent intent = new Intent(this, PlayRecordView.class);
//                  intent.putExtra("position_id", position);
//                  startActivity (intent);
            }
        });
    }


    @Override
    public int getItemCount() {
        return data.size();
    }


    public class RecyclerViewHolder extends RecyclerView.ViewHolder {

        private View itemview;
        public TextView recordname;
        public TextView recordduration;
        public CardView record;
        public Button playbutton;


        public RecyclerViewHolder(View itemView) {
            super(itemView);
            itemview = itemView;
            recordname = itemView.findViewById(R.id.record_name);
            recordduration = itemView.findViewById(R.id.record_max_time);
            record = itemView.findViewById(R.id.recordCard);
            playbutton = itemView.findViewById(R.id.play_button);

            //            mediaPlayer = new MediaPlayer();

            //Xử lý khi nút play được bấm
            playbutton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (onItemClickedListener != null) {
                        int position = getAdapterPosition();
                        if (position != RecyclerView.NO_POSITION) {
                            onItemClickedListener.onPlayClick(position);
                        }
                    }

                }
            });
        }


    }


    public interface OnItemClickedListener {
        void onItemClick(int position);

        void onPlayClick(int position);
    }

    private OnItemClickedListener onItemClickedListener;

    public void setOnItemClickedListener(OnItemClickedListener onItemClickedListener) {
        this.onItemClickedListener = onItemClickedListener;
    }
}






