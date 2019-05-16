package com.tungnvan.godear;

import android.content.Context;
import android.media.MediaPlayer;
import android.os.Environment;
import android.os.Handler;
import android.support.constraint.ConstraintLayout;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.tungnvan.godear.utils.Record;
import com.tungnvan.godear.utils.TimeUtils;
import com.tungnvan.godear.R.drawable;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.RecyclerViewHolder>{

    private Context mContext;
    private List<Record> data = new ArrayList<>();

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
    public void onBindViewHolder(final RecyclerViewHolder holder, int position) {
          holder.itemview.setTag(position);
          holder.recordname.setText(data.get(position).getRecord_name());
          holder.recordduration.setText(data.get(position).getRecord_max_time());



            }



    @Override
    public int getItemCount() {
        return data.size();
    }




    public class RecyclerViewHolder extends RecyclerView.ViewHolder {

          private View itemview;
          public TextView recordname;
          public TextView recordduration;

          public Button playbutton;





        public RecyclerViewHolder(View itemView) {
            super(itemView);
              itemview = itemView;
            recordname = itemView.findViewById(R.id.record_name);
            recordduration = itemView.findViewById(R.id.record_max_time);
 //             recordtime = itemView.findViewById(R.id.record_time);
              playbutton = itemView.findViewById(R.id.play_button);

  //            mediaPlayer = new MediaPlayer();

            //Xử lý khi nút play được bấm
            playbutton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(onItemClickedListener != null) {
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
        void onItemClick(String username);
        void onPlayClick(int position);
    }

    private OnItemClickedListener onItemClickedListener;

    public void setOnItemClickedListener(OnItemClickedListener onItemClickedListener) {
        this.onItemClickedListener = onItemClickedListener;
    }
}






