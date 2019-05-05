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
          holder.recordname.setText(data.get(position).getRecord_name());
          holder.recordduration.setText(data.get(position).getRecord_max_time());
          holder.recordseek.setEnabled(false);


            }



    @Override
    public int getItemCount() {
        return data.size();
    }

    public MediaPlayer mediaPlayer;
    public Handler threadHandler = new Handler();

    public class RecyclerViewHolder extends RecyclerView.ViewHolder {

        private View itemview;
        public TextView recordname;
        public TextView recordduration;
        public TextView recordtime;
        public Button playbutton;
        public SeekBar recordseek;




        public RecyclerViewHolder(View itemView) {
            super(itemView);
            itemview = itemView;
            recordname = itemView.findViewById(R.id.record_name);
            recordduration = itemView.findViewById(R.id.record_max_time);
            recordtime = itemView.findViewById(R.id.record_time);
            playbutton = itemView.findViewById(R.id.play_button);
            recordseek = itemView.findViewById(R.id.record_seek);
            mediaPlayer = new MediaPlayer();

            //Xử lý khi nút Chi tiết được bấm
            playbutton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    recordseek.setEnabled(true);
                    if(mediaPlayer.isPlaying()){
                        mediaPlayer.pause();


                    }

                    else {

                        if (mediaPlayer.getCurrentPosition() != 0) {
                            mediaPlayer.start();
                        } else {

                            String dir_path = Environment.getExternalStorageDirectory().getAbsolutePath() + "/GodEar/";
                            String record_path = dir_path + recordname.getText().toString();
                            //final File file = new File(record_path);
                            //Uri uri = Uri.fromFile(file);
                            mediaPlayer = new MediaPlayer();
                            try {
                                mediaPlayer.setDataSource(record_path);
                                mediaPlayer.prepare();

                            } catch (IOException e) {
                                e.printStackTrace();
                            }


                            // Khoảng thời gian của bài hát (Tính theo mili giây).
                            int duration = mediaPlayer.getDuration();

                            int currentPosition = mediaPlayer.getCurrentPosition();
                            if (currentPosition == 0) {
                                recordseek.setMax(duration);
                            } else if (currentPosition == duration) {

                                mediaPlayer.reset();
                            }
                            mediaPlayer.start();

                            // Tạo một thread để update trạng thái của SeekBar.
                            UpdateSeekBarThread updateSeekBarThread = new UpdateSeekBarThread();
                            threadHandler.postDelayed(updateSeekBarThread, 50);
                        }
                    }
                }
            });
        }

        // Thread sử dụng để Update trạng thái cho SeekBar.
        public class UpdateSeekBarThread implements Runnable {


            public void run() {
                if (mediaPlayer.isPlaying()) {
                    playbutton.setBackgroundResource(R.drawable.pause_button_shape);
                } else {
                    playbutton.setBackgroundResource(R.drawable.play_button_shape);
                }
                    int currentPosition = mediaPlayer.getCurrentPosition();
                    String currentPositionStr = TimeUtils.millisecondsToString(currentPosition);
                    recordtime.setText(currentPositionStr + " - ");
                    recordseek.setProgress(currentPosition);

                    // Ngừng thread 50 mili giây.
                    threadHandler.postDelayed(this, 50);
                }

        }
    }







    public interface OnItemClickedListener {
        void onItemClick(String username);
    }

    private OnItemClickedListener onItemClickedListener;

    public void setOnItemClickedListener(OnItemClickedListener onItemClickedListener) {
        this.onItemClickedListener = onItemClickedListener;
    }
}






