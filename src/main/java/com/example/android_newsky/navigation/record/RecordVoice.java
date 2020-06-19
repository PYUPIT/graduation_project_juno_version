package com.example.android_newsky.navigation.record;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.media.Image;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.android_newsky.R;

import android.content.Intent;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.os.Build;

import android.os.Environment;
import android.util.Log;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import com.example.android_newsky.navigation.RecorderPlayerActivity;

import java.io.File;
import java.io.FileDescriptor;
import java.io.FileInputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;

import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

import me.itangqi.waveloadingview.WaveLoadingView;

public class RecordVoice extends Fragment {

    private MediaPlayer player;
    private MediaRecorder recorder;

    ImageView btnForRecord = null;

    private final String directoryName = Environment.getExternalStorageDirectory().getAbsolutePath() + "/Nokmusae/Recording";

    private String recordingFileName = null;

    private int position;

    private Timer t = null;
    private Timer t2 = null;

    TextView textView;

    private int seconds = 0;
    private int minute = 0;
    private int hour = 0;

    private int temp1 = 0;
    private int temp3 = 0;
    private int temp2 = 0;

    WaveLoadingView waveLoadingView;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        ViewGroup rootView = (ViewGroup)inflater.inflate(R.layout.fragment_menu_record, container, false);

        File recordingDir = new File(directoryName);
        // 녹음된 음성 파일을 저장할 디렉토리 생성
        if(!recordingDir.exists()) {
            recordingDir.mkdirs();
        }

        textView = (TextView)rootView.findViewById(R.id.text_time);

        waveLoadingView = (WaveLoadingView)rootView.findViewById(R.id.waveLoadingView);

        btnForRecord = rootView.findViewById(R.id.record);
        btnForRecord.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ClickRecordBtn();
            }
        });

        Button btnForPlay = rootView.findViewById(R.id.play);
        btnForPlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ClickPlayBtn();
            }
        });

        Button btnForStop = rootView.findViewById(R.id.stop);
        btnForStop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ClickStopBtn();
            }
        });

        return rootView;
    }

    public void ClickPlayBtn()
    {
        FileInputStream fis = null;

        try {
            try {
                KillPlayer();

                player = new MediaPlayer();

                fis = new FileInputStream(directoryName + "/" +recordingFileName + ".mp3");

                FileDescriptor fd = fis.getFD();

                player.setDataSource(fd);
                player.prepare();
                player.start();

                t2 = new Timer();

                seconds = 0;
                minute = 0;
                hour = 0;

                temp2 = 0;
                temp3 = (int) player.getDuration();
                temp3 /= 1000;

                t2.schedule(new TimerTask() {

                    @Override
                    public void run() {
                        textView.post(new Runnable() {

                            public void run() {
                                seconds++;
                                if (seconds == 60) {
                                    seconds = 0;
                                    minute++;
                                }
                                if (minute == 60) {
                                    minute = 0;
                                    hour++;
                                }
                                textView.setText(""
                                        + (hour > 9 ? hour : ("0" + hour)) + " : "
                                        + (minute > 9 ? minute : ("0" + minute))
                                        + " : "
                                        + (seconds > 9 ? seconds : "0" + seconds));

                                temp2 ++;

                                if (temp2 >= temp3) {
                                    t2.cancel();
                                }
                            }
                        });
                    }
                }, 1000, 1000);

                if(temp2 > temp3) {
                    t2.cancel();
                }



            } catch (IOException e) {
                e.printStackTrace();
            }

            Toast.makeText(getContext(), "재생을 시작합니다!", Toast.LENGTH_LONG).show();

        } catch (Exception e) {
            Toast.makeText(getContext(), "재생할 파일이 존재하지 않습니다!", Toast.LENGTH_LONG).show();
            e.printStackTrace();
        }
    }

    public void ClickStopBtn()
    {
        Log.d("RecordVoice", "일시정지 버튼 클릭됨!");

        if (player != null && player.isPlaying())
        {
            position = player.getCurrentPosition();
            player.pause();
        }

        t2.cancel();

        Toast.makeText(getContext(), "재생을 일시정지 합니다!", Toast.LENGTH_LONG).show();
    }

    private int index_rdc = 0;

    public void ClickRecordBtn()
    {
        if(index_rdc == 0) {
            RunTime();
            recordAudio();

            btnForRecord.setImageResource(R.drawable.btn_stop);

            index_rdc = 1;
        }
        else
        {
            if (recorder != null)
            {
                recorder.stop();
                recorder.release();

                recorder = null;

                btnForRecord.setImageResource(R.drawable.btn_record);

                index_rdc = 0;
                t.cancel();

                Toast.makeText(getContext(), "녹 음 중 지", Toast.LENGTH_SHORT).show();
            }
        }
    }


    public void RunTime()
    {
        t = new Timer();

        seconds = 0;
        minute = 0;
        hour = 0;

        temp1 = 0;

        t.schedule(new TimerTask() {

            @Override
            public void run() {
                textView.post(new Runnable() {

                    public void run() {
                        seconds++;
                        if (seconds == 60) {
                            seconds = 0;
                            minute++;
                        }
                        if (minute == 60) {
                            minute = 0;
                            hour++;
                        }
                        textView.setText(""
                                + (hour > 9 ? hour : ("0" + hour)) + " : "
                                + (minute > 9 ? minute : ("0" + minute))
                                + " : "
                                + (seconds > 9 ? seconds : "0" + seconds));

                        waveLoadingView.setProgressValue(temp1);
                        temp1++;
                    }
                });

            }
        }, 1000, 1000);
    }

    private void recordAudio()
    {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyMMdd_HHmmss");

        Date time = new Date();

        String timeForNaming = simpleDateFormat.format(time);

        recordingFileName = "녹음 " + timeForNaming;

        try
        {
            if (recorder != null)
            {
                recorder.stop();
                recorder.release();
                recorder = null;
            }

            try
            {
                recorder = new MediaRecorder();
                recorder.setAudioSource(MediaRecorder.AudioSource.MIC);
                recorder.setOutputFormat(MediaRecorder.OutputFormat.MPEG_4);
                recorder.setAudioEncoder(MediaRecorder.AudioEncoder.DEFAULT);
                recorder.setOutputFile(directoryName + "/" +recordingFileName + ".mp3");
            } catch (Exception e) { e.printStackTrace(); }

            recorder.prepare();
            recorder.start();

            Toast.makeText(getContext(), "녹 음 시 작", Toast.LENGTH_LONG).show();

        } catch (IOException e) { e.printStackTrace(); }
    }

    public void onButton4Clicked(View v) {
        Log.d("RecordActivity", "중지 버튼 클릭됨!");

        if (player != null && player.isPlaying()) {
            player.stop();
        }
        Toast.makeText(getContext(), "재생을 중지합니다!", Toast.LENGTH_LONG).show();
    }

    public void onButton3Clicked(View v)
    {
//        Log.d("RecordActivity", "목록 버튼 클릭됨!");
//
//        if (player != null && !player.isPlaying()) {
//            player.start();
//            player.seekTo(position);
//        }
//
//        Intent intent = new Intent(getActivity(), RecorderPlayerActivity.class);
//        startActivity(intent);
    }

    // 앱이 종료가 되었을 때도
    @Override
    public void onDestroy() {
        super.onDestroy();

        KillPlayer();
    }

    private void KillPlayer() {
        if (player != null) {
            player.release();
            player = null;
        }
    }
}
